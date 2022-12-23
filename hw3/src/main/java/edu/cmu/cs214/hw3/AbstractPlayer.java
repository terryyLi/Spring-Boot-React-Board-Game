package edu.cmu.cs214.hw3;

public abstract class AbstractPlayer implements Player{
    private Worker worker1;
    private Worker worker2;
    private Worker currentWorker;
    private Board board;
    private boolean isWin;
    private int value;
    private int remainingBuildSteps;
    private int remainingMoveSteps;
    private int maxBuildSteps;
    private int maxMoveSteps;
    private Position prevBuild;
    private Position prevMove;

    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param worker1 the first worker
     * @param worker2 the second worker
     * @param board the board
     * @param value the value of player
     *
     */
    public AbstractPlayer(Worker worker1, Worker worker2, Board board, int value) {
        this.board = board;
        this.setWorker1(worker1);
        this.setWorker2(worker2);
        this.isWin = false;
        this.board.getTower(this.worker1.getPosition()).setIsOccuplied(true);
        this.board.getTower(this.worker2.getPosition()).setIsOccuplied(true);
        this.value = value;
        this.remainingMoveSteps = 1;
        this.remainingBuildSteps = 1;
        this.maxMoveSteps = 1;
        this.maxBuildSteps = 1;
        this.prevBuild = new Position(-1, -1);
        this.prevMove = new Position(-1, -1); 
    }

    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param board the board
     * @param value the value of player
     *
     */
    public AbstractPlayer(Board board, int value) {
        this.board = board;
        this.isWin = false;
        this.value = value;
        this.remainingMoveSteps = 1;
        this.remainingBuildSteps = 1;
        this.maxMoveSteps = 1;
        this.maxBuildSteps = 1;
        this.prevBuild = new Position(-1, -1);
        this.prevMove = new Position(-1, -1); 
    }

    /**
     * set max build steps to be n
     * @param n the max build steps to be set
     *
     */
    public void setMaxBuildSteps(int n) {
        this.maxBuildSteps = n;
    }

    /**
     * set remaining build steps to be n
     * @param n the remaining build steps to be set
     *
     */
    public void setRemainingBuildSteps(int n) {
        this.remainingBuildSteps = n;
    }

    /**
     * get remaining build steps
     * @return the remaining build steps
     *
     */
    public int getRemainingBuildSteps() {
        return this.remainingBuildSteps;
    }

    /**
     * get max build steps
     * @return the max build steps
     *
     */
    public int getMaxBuildSteps() {
        return this.maxBuildSteps;
    }

    /**
     * set max move steps to be n
     * @param n the max move steps to be set
     *
     */
    public void setMaxMoveSteps(int n) {
        this.maxMoveSteps = n;
    }

    /**
     * set remaining move steps to be n
     * @param n the remaining move steps to be set
     *
     */
    public void setRemainingMoveSteps(int n) {
        this.remainingMoveSteps = n;
    }

    /**
     * get remaining move steps
     * @return the remaining move steps
     *
     */
    public int getRemainingMoveSteps() {
        return this.remainingMoveSteps;
    }

    /**
     * get max move steps
     * @return the max move steps
     *
     */
    public int getMaxMoveSteps() {
        return this.maxMoveSteps;
    }

    /**
     * set worker1 to be worker1
     * @param worker1 the first worker
     *
     */
    public void setWorker1(Worker worker1) {
        Position p = worker1.getPosition();
        if(this.board.getTower(p).isOccupiedByWorker()) {
            return;
        }
        this.worker1 = worker1;
        this.board.getTower(p).setIsOccuplied(true);
        this.board.setPlayer(p, this);
        this.board.setWorker(p, worker1);
    }

    /**
     * set worker2 to be worker2
     * @param worker2 the second worker
     *
     */
    public void setWorker2(Worker worker2) {
        Position p = worker2.getPosition();
        if(this.board.getTower(p).isOccupiedByWorker()) {
            return;
        }
        this.worker2 = worker2;
        this.board.getTower(p).setIsOccuplied(true);
        this.board.setPlayer(p, this);
        this.board.setWorker(p, worker2);
    }

    /**
     * set the value of the Player
     * @param currWorker the value of worker to be set
     */
    public void setCurrentWorker(Worker currWorker) {
        this.currentWorker = currWorker;
    }

    /**
     * get the value of the Player
     *
     * @return the value of the Player
     */
    public Worker getCurrentWorker() {
        return this.currentWorker;
    }

    /**
     * get the position of previous build
     *
     * @return the position of previous build
     */
    public Position getPrevBuild() {
        return this.prevBuild;
    }

    /**
     * get the position of previous move
     *
     * @return the position of previous move
     */
    public Position getPrevMove() {
        return this.prevMove;
    }

    /**
     * set the position of previous move
     * @param p the position to be set to prev move
     *
     */
    public void setPrevMove(Position p) {
        this.prevMove = p;
    }

    /**
     * get the value of the Player
     *
     * @return the value of the Player
     */
    public int getValue() {
        return this.value;
    }

    /**
     * get the first worker
     *
     * @return the first worker
     */
    public Worker getWorker1() {
        return this.worker1;
    }

    /**
     * get the second worker
     *
     * @return the second worker
     */
    public Worker getWorker2() {
        return this.worker2;
    }

    /**
     * get the board
     *
     * @return the board
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * check if the player wins
     *
     * @return true if it wins, false otherwise
     */
    public boolean isWin() {
        return this.isWin;
    }

    /**
     * set the player winner
     *
     */
    public void setIsWin() {
        this.isWin = true;
    }

    /**
     * check if the move from p1 to p2 is valid
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
        if(!(this.board.isInBound(p2) && this.board.isAdjacent(p1, p2)
                && this.board.isNotOccupied(p2))) {
                    return false;
                }
        int startLevel = this.board.getTower(p1).getLevel();
        int endLevel = this.board.getTower(p2).getLevel();
        return (endLevel - startLevel <= 1) && endLevel != this.board.getTower(p2).getMaxLevel();
    }

    /**
     * check if the build from p1 to p2 is valid
     *
     * @param p1 the start position
     * @param p2 the end position
     * @return true if: 
     * 1. p2 is in bound. 
     * 2. p1 and p2 are adjacent
     * 3. p2 is not occupied
     * 4. tower level at p2 is not max level
     */
    public boolean isValidBuild(Position p1, Position p2) {
        if(!(this.board.isInBound(p2) && this.board.isAdjacent(p1, p2)
                && this.board.isNotOccupied(p2))) {
                    return false;
                }
        return this.board.getTower(p2).getLevel() < this.board.getTower(p2).getMaxLevel();
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
        return;
    }

    /**
     * check if the move from p1 to p2 makes the player the winner
     * if yes, set isWin to true
     * 
     * @param p1 the start position
     * @param p2 the end position
     * 
     */
    public void checkWinningCondition(Position p1, Position p2) {
        if(this.board.getTowerLevel(p2) == this.board.getTower(p2).getMaxLevel() - 1) {
            this.setIsWin();
        }
        this.otherWinningConditions(p1, p2);
    }


    /**
     * build a dome to the destination
     *
     * @param worker the work to perform build
     * @param pNew position of destination
     * @return true if the build is legal and over, false otherwise
     */
    public boolean build(Worker worker, Position pNew) {
        if(this.remainingBuildSteps == 0) {
            worker.setReadyToBuild(false);
            this.remainingBuildSteps = this.maxBuildSteps;
            this.prevBuild = new Position(-1, -1);
            return true;
        }
        Position pCurr = worker.getPosition();
        if(!this.isValidBuild(pCurr, pNew)) {
            return false;
        }
        this.prevBuild = pNew;
        this.board.incrTowerLevel(pNew);
        this.remainingBuildSteps = this.remainingBuildSteps - 1;
        if(this.remainingBuildSteps == 0) {
            worker.setReadyToBuild(false);
            this.remainingBuildSteps = this.maxBuildSteps;
            this.prevBuild = new Position(-1, -1);
        }
        
        return true;
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
        if(this.remainingMoveSteps == 0) {
            worker.setReadyToBuild(true);
            this.remainingMoveSteps = this.maxMoveSteps;
            this.prevMove = new Position(-1, -1);
            return true;
        }
        Position pCurr = worker.getPosition();
        if(!this.isValidMove(pCurr, pNew)) {
            return false;
        }
        this.prevMove = pCurr;
        this.board.setStateAfterMove(pCurr, pNew);
        worker.setPosition(pNew);
        this.checkWinningCondition(pCurr, pNew);
        this.remainingMoveSteps = this.remainingMoveSteps - 1;
        if(this.remainingMoveSteps == 0) {
            worker.setReadyToBuild(true);
            this.remainingMoveSteps = this.maxMoveSteps;
            this.prevMove = new Position(-1, -1);
        }
        
        return true;
    }
}
