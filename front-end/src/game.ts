interface GameState {
  cells: Cell[];
  winner: number | null; // winner maybe null
  playerTurn: number;
  workerTurn: number | null;
  isReadyToBuild: boolean;
  isInitializable: boolean;
}

interface Cell {
  text: string;
  selectable: boolean;
  movable: boolean;
  buildable: boolean;
  initializable: boolean;
  x: number;
  y: number;
}

export type { GameState, Cell }