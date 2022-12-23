package edu.cmu.cs214.hw3;

public class Apollo extends AbstractPlayer{

    private Player opponentPlayer;
    private Worker opponentWorker;
    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param worker1 the first worker
     * @param worker2 the second worker
     * @param board the board
     * @param value the value of player
     *
     */
    public Apollo(Worker worker1, Worker worker2, Board board, int value) {
        super(worker1, worker2, board, value);
    }

    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param board the board
     * @param value the value of player
     *
     */
    public Apollo(Board board, int value) {
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
            
            this.store(p2);
        }

        return true;
    }

    /**
     * store the worker / player at p
     *
     * @param p the position at which the opponent info
     * is stored
     */
    public void store(Position p) {
        Board board = this.getBoard();
        this.opponentWorker = board.getWorker(p);
        this.opponentPlayer = board.getPlayer(p);
    }

    /**
     * move a Worker to the destination pNew
     *
     * @param worker the work to be moved
     * @param pNew position of destination
     * 
     * @return true if the move is legal and over, false otherwise
     */
    public boolean move(Worker worker, Position pNew) {
        Board board = this.getBoard();
        Position pCurr = worker.getPosition();
        if(!this.isValidMove(pCurr, pNew)) {
            return false;
        }
        this.setPrevMove(pCurr);
        board.setStateAfterMove(pCurr, pNew);
        worker.setPosition(pNew);
        this.checkWinningCondition(pCurr, pNew);
        this.setRemainingMoveSteps(this.getRemainingMoveSteps() - 1);
        if(this.getRemainingMoveSteps() == 0) {
            worker.setReadyToBuild(true);
            this.setRemainingMoveSteps(this.getMaxMoveSteps());
            this.setPrevMove(new Position(-1, -1));
        }
        if(this.opponentPlayer != null) {
            board.getTower(pCurr).setIsOccuplied(true);
            board.setPlayer(pCurr, this.opponentPlayer);
            board.setWorker(pCurr, this.opponentWorker);
            this.opponentWorker.setPosition(pCurr);
        }
        this.opponentWorker = null;
        this.opponentPlayer = null;
        
        return true;
    }
    
}
