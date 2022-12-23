package edu.cmu.cs214.hw3;

public class Minotaur extends AbstractPlayer{
    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param worker1 the first worker
     * @param worker2 the second worker
     * @param board the board
     * @param value the value of player
     *
     */
    public Minotaur(Worker worker1, Worker worker2, Board board, int value) {
        super(worker1, worker2, board, value);
    }

    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param board the board
     * @param value the value of player
     *
     */
    public Minotaur(Board board, int value) {
        super(board, value);
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
     * 6. if p2 is occupied by opponent, push it out
     */
    @Override
    public boolean isValidMove(Position p1, Position p2) {
        Board board = this.getBoard();
        if(!(board.isInBound(p2) && board.isAdjacent(p1, p2))) {
            return false;
        }
        int startLevel = board.getTower(p1).getLevel();
        int endLevel = board.getTower(p2).getLevel();
        if(!((endLevel - startLevel <= 1) && endLevel != board.getTower(p2).getMaxLevel())) {
            return false;
        }

        if(!board.isNotOccupied(p2)) {
            if(board.getPlayer(p2).getValue() == this.getValue()) {
                return false;
            }
            
            Worker opponent = board.getWorker(p2);
            int newX = (p2.getX() - p1.getX()) + p2.getX();
            int newY = (p2.getY() - p1.getY()) + p2.getY();
            Position p3 = new Position(newX, newY);
            if(!this.push(opponent, p3)) {
                return false;
            }
        }

        return true;
    }

    /**
     * push a Worker to the destination pNew
     *
     * @param worker the work to be moved
     * @param pNew position of destination
     * 
     * @return true if the push is legal, false otherwise
     */
    public boolean push(Worker worker, Position pNew) {
        Board board = this.getBoard();
        Position pCurr = worker.getPosition();
        if(!super.isValidMove(pCurr, pNew)) {
            return false;
        }
        board.setStateAfterMove(pCurr, pNew);
        worker.setPosition(pNew);
        
        return true;
    }
}
