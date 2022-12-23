package edu.cmu.cs214.hw3;

public class Artemis extends AbstractPlayer {
    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param worker1 the first worker
     * @param worker2 the second worker
     * @param board the board
     * @param value the value of player
     *
     */
    public Artemis(Worker worker1, Worker worker2, Board board, int value) {
        super(worker1, worker2, board, value);
        this.setRemainingMoveSteps(2);
        this.setMaxMoveSteps(2);
    }

    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param board the board
     * @param value the value of player
     *
     */
    public Artemis(Board board, int value) {
        super(board, value);
        this.setRemainingMoveSteps(2);
        this.setMaxMoveSteps(2);
    }

    /**
     * check if p2 is in bound and occupied, and if p1 and p2 
     * are adjacent
     *
     * @param p1 the start position
     * @param p2 the end position
     * @return true if: 
     * 1. p2 is in bound. 
     * 2. p1 and p2 are adjacent
     * 3. p2 is not occupied
     * 4. move up for at most one level
     * 5. next level is not above max level 
     */
    public boolean isValidMove(Position p1, Position p2) {
        Board board = this.getBoard();
        if(!(board.isInBound(p2) && board.isAdjacent(p1, p2)
                && board.isNotOccupied(p2))) {
                    return false;
                }
        int startLevel = board.getTower(p1).getLevel();
        int endLevel = board.getTower(p2).getLevel();
        Position prevMove = this.getPrevMove();
        if(prevMove.equals(p2)) {
            return false;
        }
        return (endLevel - startLevel <= 1) && endLevel != board.getTower(p2).getMaxLevel();
    }
}
