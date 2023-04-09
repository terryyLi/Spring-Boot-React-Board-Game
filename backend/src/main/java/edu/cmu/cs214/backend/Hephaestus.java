package edu.cmu.cs214.backend;

public class Hephaestus extends AbstractPlayer{
     /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param worker1 the first worker
     * @param worker2 the second worker
     * @param board the board
     * @param value the value of player
     *
     */
    public Hephaestus(Worker worker1, Worker worker2, Board board, int value) {
        super(worker1, worker2, board, value);
        this.setRemainingBuildSteps(2);
        this.setMaxBuildSteps(2);
    }

    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param board the board
     * @param value the value of player
     *
     */
    public Hephaestus(Board board, int value) {
        super(board, value);
        this.setRemainingBuildSteps(2);
        this.setMaxBuildSteps(2);
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
     * 4. tower level at p2 is not max level
     */
    @Override
    public boolean isValidBuild(Position p1, Position p2) {
        if(this.getRemainingBuildSteps() == 1) {
            return this.getPrevBuild().equals(p2);
        }
        Board board = this.getBoard();
        if(!(board.isInBound(p2) && board.isAdjacent(p1, p2)
                && board.isNotOccupied(p2))) {
                    return false;
                }
        Position prevBuild = this.getPrevBuild();
        if(prevBuild.equals(p2)) {
            return false;
        }
        return board.getTower(p2).getLevel() < board.getTower(p2).getMaxLevel();
    }
    
}
