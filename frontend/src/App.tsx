import React from 'react';
import './App.css'; // import the css file to enable your styles.
import { GameState, Cell } from './game';
import BoardCell from './Cell';

/**
 * Define the type of the props field for a React component
 */
interface Props { }

/**
 * Using generics to specify the type of props and state.
 * props and state is a special field in a React component.
 * React will keep track of the value of props and state.
 * Any time there's a change to their values, React will
 * automatically update (not fully re-render) the HTML needed.
 * 
 * props and state are similar in the sense that they manage
 * the data of this component. A change to their values will
 * cause the view (HTML) to change accordingly.
 * 
 * Usually, props is passed and changed by the parent component;
 * state is the internal value of the component and managed by
 * the component itself.
 */
class App extends React.Component<Props, GameState> {
  private initialized: boolean = false;

  /**
   * @param props has type Props
   */
  constructor(props: Props) {
    super(props)
    /**
     * state has type GameState as specified in the class inheritance.
     */
    this.state = { cells: [], playerTurn: 1, workerTurn: 1, isReadyToBuild: false, isInitializable: true, winner: null}
  }

  /**
   * Use arrow function, i.e., () => {} to create an async function,
   * otherwise, 'this' would become undefined in runtime. This is
   * just an issue of Javascript.
   */
  newGame = async () => {
    const response = await fetch('/newgame');
    const json = await response.json();
    this.setState(json);
  }

  /**
   * Use arrow function, i.e., () => {} to create an async function,
   * otherwise, 'this' would become undefined in runtime. This is
   * just an issue of Javascript.
   */
   aimode = async () => {
    const response = await fetch('/aimode');
    const json = await response.json();
    this.setState(json);
  }

  /**
   * play will generate an anonymous function that the component
   * can bind with.
   * @param x 
   * @param y 
   * @returns 
   */
  play(x: number, y: number): React.MouseEventHandler {
    return async (e) => {
      // prevent the default behavior on clicking a link; otherwise, it will jump to a new page.
      e.preventDefault();
      const response = await fetch(`/play?x=${x}&y=${y}`)
      const json = await response.json();
      this.setState(json);
    }
  }

  /**
   * play will generate an anonymous function that the component
   * can bind with.
   * @param x 
   * @param y 
   * @returns 
   */
   initialize(x: number, y: number): React.MouseEventHandler {
    return async (e) => {
      // prevent the default behavior on clicking a link; otherwise, it will jump to a new page.
      e.preventDefault();
      const response = await fetch(`/initialize?x=${x}&y=${y}`)
      const json = await response.json();
      this.setState(json);
    }
  }

  /**
   * select will generate an anonymous function that the component
   * can bind with.
   * @param x 
   * @param y 
   * @returns 
   */
  select(x: number, y: number): React.MouseEventHandler {
    return async (e) => {
      // prevent the default behavior on clicking a link; otherwise, it will jump to a new page.
      e.preventDefault();
      const response = await fetch(`/select?x=${x}&y=${y}`)
      const json = await response.json();
      this.setState(json);
    }
  }

  /**
   * select will generate an anonymous function that the component
   * can bind with.
   * @returns 
   */
   defaultPlayer = async () => {
    const response = await fetch('/defaultPlayer');
    const json = await response.json();
    this.setState(json);
  }

  /**
   * select will generate an anonymous function that the component
   * can bind with.
   * @returns 
   */
   demeter = async () => {
    const response = await fetch('/demeter');
    const json = await response.json();
    this.setState(json);
  }

  /**
   * select will generate an anonymous function that the component
   * can bind with.
   * @returns 
   */
   minotaur = async () => {
    const response = await fetch('/minotaur');
    const json = await response.json();
    this.setState(json);
  }

  /**
   * select will generate an anonymous function that the component
   * can bind with.
   * @returns 
   */
   pan = async () => {
    const response = await fetch('/pan');
    const json = await response.json();
    this.setState(json);
  }

  /**
   * select will generate an anonymous function that the component
   * can bind with.
   * @returns 
   */
   artemis = async () => {
    const response = await fetch('/artemis');
    const json = await response.json();
    this.setState(json);
  }

  /**
   * select will generate an anonymous function that the component
   * can bind with.
   * @returns 
   */
   apollo = async () => {
    const response = await fetch('/apollo');
    const json = await response.json();
    this.setState(json);
  }

  /**
   * select will generate an anonymous function that the component
   * can bind with.
   * @returns 
   */
   hephaestus = async () => {
    const response = await fetch('/hephaestus');
    const json = await response.json();
    this.setState(json);
  }
  

  undo = async () => {
    const response = await fetch('/undo');
    const json = await response.json();
    this.setState(json);
  }

  createCell(cell: Cell, index: number): React.ReactNode {
    if(cell.initializable) {
      return (
        <div key={index}>
          <a href='/' onClick={this.initialize(cell.x, cell.y)}>
            <BoardCell cell={cell}></BoardCell>
          </a>
        </div>
      )
    }
    if (cell.selectable) {
      /**
       * key is used for React when given a list of items. It
       * helps React to keep track of the list items and decide
       * which list item need to be updated.
       * @see https://reactjs.org/docs/lists-and-keys.html#keys
       */
      return (
        <div key={index}>
          <a href='/' onClick={this.select(cell.x, cell.y)}>
            <BoardCell cell={cell}></BoardCell>
          </a>
        </div>
      )
    }
    return (
      <div key={index}>
        <a href='/' onClick={this.play(cell.x, cell.y)}>
          <BoardCell cell={cell}></BoardCell>
        </a>
      </div>
    )
  }

  createInstruction(): React.ReactNode {
    if(this.state.isInitializable) {
      return `Please select your player type (god card or default) and then select worker positions.
              p.s. if you want to play with AI, click AIMode below and choose your player first and then the AI.`
    }
    if (this.state.winner !== null) {
      return `Player ${this.state.winner} wins! Press New Game to restart!`
    } else if (this.state.winner === null && this.state.workerTurn === null) {
      return `It is Player ${this.state.playerTurn}'s turn. Please select your worker:`
    } else if (!this.state.isReadyToBuild){
      return `It is Player ${this.state.playerTurn}'s turn to move Worker ${this.state.workerTurn}. Please move. 
              p.s. if you have super power to move again but wish not to do so, click on yourself to skip.`
    } else {
      return `It is Player ${this.state.playerTurn}'s turn to build using Worker ${this.state.workerTurn}. Please build.
              p.s. if you have super power to build again but wish not to do so, click on yourself to skip.`
    }
  }

  /**
   * This function will call after the HTML is rendered.
   * We update the initial state by creating a new game.
   * @see https://reactjs.org/docs/react-component.html#componentdidmount
   */
  componentDidMount(): void {
    /**
     * setState in DidMount() will cause it to render twice which may cause
     * this function to be invoked twice. Use initialized to avoid that.
     */
    if (!this.initialized) {
      this.newGame();
      this.initialized = true;
    }
  }

  /**
   * The only method you must define in a React.Component subclass.
   * @returns the React element via JSX.
   * @see https://reactjs.org/docs/react-component.html
   */
  render(): React.ReactNode {
    /**
     * We use JSX to define the template. An advantage of JSX is that you
     * can treat HTML elements as code.
     * @see https://reactjs.org/docs/introducing-jsx.html
     */
    return (
      <div>
        <div id="instructions">{this.createInstruction()}</div>
        <div id="board">
          {this.state.cells.map((cell, i) => this.createCell(cell, i))}
        </div>
        <div id="bottombar">
          <button onClick={/* get the function, not call the function */this.newGame}>New Game</button>
          {/* Exercise: implement Undo function */}
          <button onClick={this.aimode}>AIMode</button>
          <button onClick={/* get the function, not call the function */this.defaultPlayer}>Default Player</button>
          <button onClick={/* get the function, not call the function */this.demeter}>Demeter</button>
          <button onClick={/* get the function, not call the function */this.minotaur}>Minotaur</button>
          <button onClick={/* get the function, not call the function */this.pan}>Pan</button>
          <button onClick={/* get the function, not call the function */this.artemis}>Artemis</button>
          <button onClick={/* get the function, not call the function */this.apollo}>Apollo</button>
          <button onClick={/* get the function, not call the function */this.hephaestus}>Hephaestus</button>
          
        </div>
      </div>
    );
  }
}

export default App;
