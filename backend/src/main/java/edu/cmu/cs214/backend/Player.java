package edu.cmu.cs214.backend;

public interface Player {
    /**
     * set max build steps to be n
     * @param n the max build steps to be set
     *
     */
    void setMaxBuildSteps(int n);

    /**
     * set remaining build steps to be n
     * @param n the remaining build steps to be set
     *
     */
    void setRemainingBuildSteps(int n);

    /**
     * get remaining build steps
     * @return the remaining build steps
     *
     */
    int getRemainingBuildSteps();

    /**
     * get max build steps
     * @return the max build steps
     *
     */
    int getMaxBuildSteps();

    /**
     * set max move steps to be n
     * @param n the max move steps to be set
     *
     */
    void setMaxMoveSteps(int n);

    /**
     * set remaining move steps to be n
     * @param n the remaining move steps to be set
     *
     */
    void setRemainingMoveSteps(int n);

    /**
     * get remaining move steps
     * @return the remaining move steps
     *
     */
    int getRemainingMoveSteps();

    /**
     * get max move steps
     * @return the max move steps
     *
     */
    int getMaxMoveSteps();

    /**
     * set worker1 to be worker1
     * @param worker1 the first worker
     *
     */
    void setWorker1(Worker worker1);

    /**
     * set worker2 to be worker2
     * @param worker2 the second worker
     *
     */
    void setWorker2(Worker worker2);

    /**
     * set the value of the Player
     * @param currWorker the value of worker to be set
     */
    void setCurrentWorker(Worker currWorker);

    /**
     * get the value of the Player
     *
     * @return the value of the Player
     */
    Worker getCurrentWorker();

    /**
     * get the position of previous build
     *
     * @return the position of previous build
     */
    Position getPrevBuild();

    /**
     * get the position of previous move
     *
     * @return the position of previous move
     */
    Position getPrevMove();

    /**
     * set the position of previous move
     * @param p the position to be set as prev move
     *
     */
    void setPrevMove(Position p);


    /**
     * get the value of the Player
     *
     * @return the value of the Player
     */
    int getValue();

    /**
     * get the first worker
     *
     * @return the first worker
     */
    Worker getWorker1();

    /**
     * get the second worker
     *
     * @return the second worker
     */
    Worker getWorker2();

    /**
     * get the board
     *
     * @return the board
     */
    Board getBoard();

    /**
     * check if the player wins
     *
     * @return true if it wins, false otherwise
     */
    boolean isWin();

    /**
     * set the player winner
     *
     */
    void setIsWin();

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
    boolean isValidMove(Position p1, Position p2);

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
    boolean isValidBuild(Position p1, Position p2);

    /**
     * addition conditions if the move from p1 to p2 makes the player the winner
     * if yes, set isWin to true
     * 
     * @param p1 the start position
     * @param p2 the end position
     *
     */
    void otherWinningConditions(Position p1, Position p2);

    /**
     * check if the move from p1 to p2 makes the player the winner
     * if yes, set isWin to true
     * 
     * @param p1 the start position
     * @param p2 the end position
     * 
     */
    void checkWinningCondition(Position p1, Position p2);


    /**
     * build a dome to the destination
     *
     * @param worker the work to perform build
     * @param pNew position of destination
     * @return true if the build is legal and over, false otherwise
     */
    boolean build(Worker worker, Position pNew);
    

    /**
     * move a Worker to the destination pNew
     *
     * @param worker the work to be moved
     * @param pNew position of destination
     * 
     * @return true if the move is legal and over, false otherwise
     */
    boolean move(Worker worker, Position pNew);
}
