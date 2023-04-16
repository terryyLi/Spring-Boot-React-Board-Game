import React from 'react'
import { Field, Worker } from './gameTypes'

interface Props {
  field: Field
  validOptions: number[]
}

class GameField extends React.Component<Props> {
  render (): React.ReactNode {
    const field: Field = this.props.field
    const playable: string = this.props.validOptions.includes(field.field_id) ? 'clickable' : ''
    // determine player color for worker
    let fieldClass: string = ''
    let fieldDisplay: string = ' '
    if (field.hasWorker) {
      const worker: Worker = field.worker
      fieldClass = `bg-player${worker.owner_id}`
      fieldDisplay = worker.owner_name
    } else if (field.isCapped) {
      fieldDisplay = 'O'
    }
    return (
      <div className={`field ${playable}`}>
        <p>
          {'['.repeat(field.level)}
          <span className={`${fieldClass}`}>{fieldDisplay}</span>
          {']'.repeat(field.level)}
        </p>
      </div>
    )
  }
}

export default GameField
