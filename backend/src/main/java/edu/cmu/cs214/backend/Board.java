package edu.cmu.cs214.backend;


public class Board {
    private Tower[][] towers;
    private Player[][] players;
    private Worker[][] workers;
    private int row;
    private int col;

    /**
     * Initializes a new {@link Board} instance with row and col and
     * contains a grids of dimension row * col containing empty grid
     * @param row the row number of board
     * @param col the col number of board
     * @param maxLevel max level of tower
     *
     */
    public Board(int row, int col, int maxLevel) {
        this.towers = new Tower[row][col];
        this.players = new Player[row][col];
        this.workers = new Worker[row][col];
        this.row = row;
        this.col = col;
        for(int r = 0; r < this.row; r++) {
            for(int c = 0; c < this.col; c++) {
                this.towers[r][c] = new Tower(0, maxLevel);
            }
        }
    }

    /**
     * set the value of the worker at position p
     * @param p the position at which the worker will be set
     * @param wk the worker to be set
     *
     */
    public void setWorker(Position p, Worker wk) {
        int row = p.getX();
        int col = p.getY();
        this.workers[row][col] = wk;
    }

    /**
     * set the value of the player at position p
     * @param p the position at which the player will be set
     * @param pl the player to be set
     */
    public void setPlayer(Position p, Player pl) {
        int row = p.getX();
        int col = p.getY();
        this.players[row][col] = pl;
    }

    /**
     * get the value of the worker at position p
     * @param p the position at which the worker will be got
     * @return the worker at position p
     * 
     */
    public Worker getWorker(Position p) {
        int row = p.getX();
        int col = p.getY();
        return this.workers[row][col];
    }

    /**
     * get the max row of the board
     * @return the max row of the board
     * 
     */
    public int getRow() {
        return this.row;
    }

    /**
     * get the max col of the board
     * @return the max col of the board
     * 
     */
    public int getCol() {
        return this.col;
    }

    /**
     * get the value of the player at position p
     *
     * @param p the position at which the player will be got
     * @return the player at position p
     * 
     */
    public Player getPlayer(Position p) {
        int row = p.getX();
        int col = p.getY();
        return this.players[row][col];
    }

    /**
     * get the Tower at postion p
     *
     * @param p the position
     * 
     * @return the Tower at postion p
     */
    public Tower getTower(Position p) {
        int row = p.getX();
        int col = p.getY();
        return this.towers[row][col];
    }

    /**
     * get the level of tower in position p
     *
     * @param p the position
     * 
     * @return the level of tower in position p
     */
    public int getTowerLevel(Position p) {
        return this.getTower(p).getLevel();
    }

    /**
     * check if a dome can be built at position p
     *
     * @param p the position
     * @return true if a dome can be built, false otherwise
     */
    public boolean canBuild(Position p) {
        return this.getTower(p).getLevel() < this.getTower(p).getMaxLevel();
    }

    /**
     * increment the level of tower on a position p by 1
     *
     * @param p the position
     */
    public void incrTowerLevel(Position p) {
        this.getTower(p).incrLevel();
    }

    /**
     * check if position p is within bound
     *
     * @param p the position
     * @return true if position p is within bound, false otherwise
     */
    public boolean isInBound(Position p) {
        return (0 <= p.getX() && p.getX() < this.row
        && 0 <= p.getY() && p.getY() < this.col);
    }

    /**
     * check if position p2 is adjacent grids of position p1
     *
     * @param p1 position 1
     * @param p2 position 2
     * @return true if position p2 is adjacent grids of 
     * position p1, false otherwise
     */
    public boolean isAdjacent(Position p1, Position p2) {
        return (Math.abs(p1.getX() - p2.getX()) <= 1 
            && Math.abs(p1.getY() - p2.getY()) <= 1);
    }

    /**
     * check if certain position is occuplied by worker
     *
     * @param p the position
     * @return true if the position p is not occuplied, false otherwise
     */
    public boolean isNotOccupied(Position p) {
        return !this.getTower(p).isOccupiedByWorker();
    }

    /**
     * set the start position unoccupied and end position occupied
     *
     * @param p1 the start position
     * @param p2 the end position
     */
    public void setStateAfterMove(Position p1, Position p2) {
        this.getTower(p1).setIsOccuplied(false);
        this.getTower(p2).setIsOccuplied(true);
        this.setPlayer(p2, this.getPlayer(p1));
        this.setWorker(p2, this.getWorker(p1));
        this.setPlayer(p1, null);
        this.setWorker(p1, null);
    } 
}
