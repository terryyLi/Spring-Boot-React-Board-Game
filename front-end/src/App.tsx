import React from 'react'
import './App.css'
import './GodCardSelector.css'
import '@coreui/coreui/dist/css/coreui.min.css'
import { CDropdown, CDropdownItem, CDropdownMenu, CDropdownToggle, CFormCheck } from '@coreui/react'
import { GameState, Field } from './gameTypes'
import GameField from './Field'

// an empty interface is equivalent to `{}`. (@typescript-eslint/no-empty-interface)
class App extends React.Component<Readonly<any>, GameState> {
  private initialized: boolean = false
  private requestLock: boolean = false
  private useAIPlayer: boolean[] = [false, false]

  /**
   * @param props has type Props
   */
  constructor (props: Readonly<any>) {
    super(props)
    /**
     * state has type GameState as specified in the class inheritance.
     */
    this.state = {
      fields: [],
      validOptions: [],
      message: 'placeholder',
      stage: 'new',
      godCardOptions: [],
      cardSelection: []
    }
  }

  newGame = (): void => {
    this.initialized = false
    fetch('/cardList')
      .then(async (response) => {
        return await response.json()
      })
      .then((data) => {
        this.setState({
          godCardOptions: data.cardList,
          cardSelection: [0, 0],
          stage: 'new'
        })
      })
      .catch(() => {})
  }

  startGame = (): void => {
    const selections: number[] = this.state.cardSelection
    const godCard0: string = this.state.godCardOptions[selections[0]]
    const godCard1: string = this.state.godCardOptions[selections[1]]
    const ai0: string = this.useAIPlayer[0].toString()
    const ai1: string = this.useAIPlayer[1].toString()
    this.fetchNewGameState(`/newGame?godCard0=${godCard0}&godCard1=${godCard1}&` +
                            `AI0=${ai0}&AI1=${ai1}`)
      .then(() => {
        this.initialized = true
      })
      .catch(() => {})
  }

  play (fieldId: number): React.MouseEventHandler {
    return (e) => {
      // prevent the default behavior on clicking a link; otherwise, it will jump to a new page.
      e.preventDefault()

      switch (this.state.stage) {
        case 'init':
          void this.fetchNewGameState(`/initWorker?field_id=${fieldId}`); break
        case 'select':
          void this.fetchNewGameState(`/select?field_id=${fieldId}`); break
        case 'move':
          void this.fetchNewGameState(`/move?field_id=${fieldId}`); break
        case 'build':
          void this.fetchNewGameState(`/build?field_id=${fieldId}`); break
      }
    }
  }

  handle (action: string): React.MouseEventHandler {
    return (e) => {
      e.preventDefault()
      switch (action) {
        case 'endTurn':
          void this.fetchNewGameState('/endTurn'); break
        case 'undo':
          void this.fetchNewGameState('/undo'); break
        case 'ability':
          void this.fetchNewGameState('/ability'); break
        case 'start':
          this.startGame(); break
        case 'new':
          this.newGame(); break
        case 'error':
          break
      }
    }
  }

  fetchNewGameState = async (uri: string): Promise<void> => {
    // only allow one request at a time.
    if (this.requestLock) { return }

    this.requestLock = true
    await fetch(uri)
      .then(async (response) => {
        return await response.json()
      })
      .then((data) => {
        this.setState({
          fields: data.fields,
          validOptions: data.validOptions,
          message: data.message,
          stage: data.stage,
          godCard: data.godCard
        })
      })
      .finally(() => {
        this.requestLock = false
      })
  }

  createField (field: Field, index: number): React.ReactNode {
    const gameField: React.ReactNode = <GameField field={field} validOptions={this.state.validOptions} />
    if (this.state.validOptions.includes(index)) {
      return (
        <div key={index}>
          <span onClick={this.play(field.field_id)}>
            {gameField}
          </span>
        </div>
      )
    } else {
      return (
        <div key={index}>
          {gameField}
        </div>
      )
    }
  }

  createEndTurnButton (): React.ReactNode {
    if (this.state.stage === 'endTurn') {
      return (
        <button onClick={this.handle(this.state.stage)}>End Turn</button>
      )
    } else {
      return (
        <button disabled>End Turn</button>
      )
    }
  }

  createAbilityButton (): React.ReactNode {
    let buttonText = this.state.godCard?.ability_desc
    buttonText = buttonText === null ? 'Ability' : buttonText
    const buttonActive = this.state.godCard?.is_ability_active
    if (buttonActive === true) {
      return (
        <button onClick={this.handle('ability')}>{buttonText}</button>
      )
    } else {
      return (
        <button disabled>{buttonText}</button>
      )
    }
  }

  checkAIPlayer = (idx: number): React.MouseEventHandler => {
    return (e) => {
      this.useAIPlayer[idx] = !this.useAIPlayer[idx]
    }
  }

  createGodCardSelector (): React.ReactNode {
    const cardList = this.state.godCardOptions
    const selections = this.state.cardSelection
    const acitve: string = (this.state.stage === 'new') ? 'active' : ''
    return (
      <div className={`overlay ${acitve}`}>
        <div className='overlay-content'>
          <p className='bg-player0'> Player A</p>
          <CDropdown>
            <CDropdownToggle color='secondary'>{cardList[selections[0]]}</CDropdownToggle>
            <CDropdownMenu>
              {cardList.map((godCardName, i) => this.createDropDownItem(0, godCardName, i))}
            </CDropdownMenu>
          </CDropdown>
          <br />
          <CFormCheck inline label='AI' id='AI-switch0' className='AI-switch' onClick={this.checkAIPlayer(0)} />
          <p className='bg-player1'> Player B</p>
          <CDropdown>
            <CDropdownToggle color='secondary'>{cardList[selections[1]]}</CDropdownToggle>
            <CDropdownMenu>
              {cardList.map((godCardName, i) => this.createDropDownItem(1, godCardName, i))}
            </CDropdownMenu>
          </CDropdown>
          <br />
          <CFormCheck inline label='AI' id='AI-switch1' className='AI-switch' onClick={this.checkAIPlayer(1)} />
          <p />
          <button className='submit-btn' onClick={this.handle('start')}>Ready</button>
        </div>
      </div>
    )
  }

  createDropDownItem (menuId: number, godCardName: string, index: number): React.ReactNode {
    const handler = (): void => {
      const selections = this.state.cardSelection
      selections[menuId] = index
      this.setState({
        cardSelection: selections
      })
    }
    return <CDropdownItem key={index} onClick={handler}>{godCardName}</CDropdownItem>
  }

  componentDidMount (): void {
    if (!this.initialized) {
      this.newGame()
    }
  }

  render (): React.ReactNode {
    return (
      <div className='center-container'>
        <div id='message-bar'>
          <p id='message'>{this.state.message}</p>
          <p id='card-desc'>{this.state.godCard?.card_desc}</p>
        </div>
        <div id='board'>
          {this.state.fields.map((field, i) => this.createField(field, i))}
        </div>
        <div id='bottombar'>
          <button onClick={this.handle('new')}>New Game</button>
          {this.createEndTurnButton()}
          <button onClick={this.handle('undo')}>Undo</button>
          {this.createAbilityButton()}
        </div>
        {this.createGodCardSelector()}
      </div>
    )
  }
}

export default App
