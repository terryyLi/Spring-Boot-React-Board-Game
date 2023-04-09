package edu.cmu.cs214.backend;

public class Pan extends AbstractPlayer{
     /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param worker1 the first worker
     * @param worker2 the second worker
     * @param board the board
     * @param value the value of player
     *
     */
    public Pan(Worker worker1, Worker worker2, Board board, int value) {
        super(worker1, worker2, board, value);
    }

    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param board the board
     * @param value the value of player
     *
     */
    public Pan(Board board, int value) {
        super(board, value);
    }

    /**
     * addition conditions if the move from p1 to p2 makes the player the winner
     * if yes, set isWin to true
     * 
     * @param p1 the start position
     * @param p2 the end position
     *
     */
    public void otherWinningConditions(Position p1, Position p2) {
        Board board = this.getBoard();
        if(board.getTowerLevel(p1) - board.getTowerLevel(p2) >= 2) {
            this.setIsWin();
        }
        return;
    }
}
