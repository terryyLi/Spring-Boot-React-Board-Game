import React from 'react';
import { Cell } from './game';

interface Props {
  cell: Cell
}

class BoardCell extends React.Component<Props> {
  render(): React.ReactNode {
    const selectable = this.props.cell.selectable ? 'selectable' : '';
    const movable = this.props.cell.movable ? 'movable' : '';
    const buildable = this.props.cell.buildable ? 'buildable' : '';
    return (
      <div className={`cell ${selectable} ${movable} ${buildable}`}>{this.props.cell.text}</div>
    )
  }
}

export default BoardCell;