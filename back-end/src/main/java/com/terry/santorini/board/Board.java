package com.terry.santorini.board;

import java.util.Arrays;

public class Board {
    private static final int STD_GRID_SIZE = 5;
    private static final int SIZE = STD_GRID_SIZE * STD_GRID_SIZE;
    private final Field[] grid;
    

    public Board() {
        // Initialize empty fields
        grid = new Field[SIZE];
        for (int i = 0; i < SIZE; i++){
            grid[i] = new Field(i);
        }
    }

    public Board(Field[] grid) {
        this.grid = grid;
    }

    public int size() {
        return SIZE;
    }

    /**
     * Construct a new {@link Worker} and place it onto the board at given position
     * 
     * @param playerId id of the player who performed this operation
     * @param playerName name of the player who performed this operation
     * @param pos the position of selected field
     * 
     * @return updated {@link Board}
     */
    public Board initWorkerFor(int playerId, String playerName, int pos) {
        Field[] newGrid = getGrid();
        newGrid[pos] = grid[pos].enter(new Worker(playerId, playerName));
        return new Board(newGrid);
    }

    /**
     * Vacate a {@link Field} and place the vacated {@link Worker} 
     * onto a different position
     * 
     * @param from the position to be vacated
     * @param to the position to be moved to
     * 
     * @return updated {@link Board}
     */
    public Board move(int from, int to) {
        Field[] newGrid = getGrid();
        Worker worker = newGrid[from].getOccupant();
        newGrid[from] = grid[from].leave();
        newGrid[to] = grid[to].enter(worker);
        return new Board(newGrid);
    }

    /**
     * Swap the occupant {@link Worker} between two {@link Field}
     * 
     * @param from the first position 
     * @param to the second position
     * 
     * @return updated {@link Board}
     */
    public Board swap(int from, int to) {
        Field[] newGrid = getGrid();
        Worker workerFrom = newGrid[from].getOccupant();
        Worker workerTo = newGrid[to].getOccupant();
        newGrid[to] = grid[to].enter(workerFrom);
        newGrid[from] = grid[from].enter(workerTo);
        return new Board(newGrid);
    }

    /**
     * Build a block onto the selected {@link Field}
     * 
     * @param fieldId the position of selected field
     * 
     * @return updated {@link Board}
     */
    public Board buildBlock(int fieldId) {
        Field[] newGrid = getGrid();
        newGrid[fieldId] = this.grid[fieldId].buildBlock();
        return new Board(newGrid);
    }

    /**
     * Build a dome onto the selected {@link Field}
     * 
     * @param fieldId the position of selected field
     * 
     * @return updated {@link Board}
     */
    public Board buildDome(int fieldId) {
        Field[] newGrid = getGrid();
        newGrid[fieldId] = this.grid[fieldId].buildDome();
        return new Board(newGrid);
    }

    /**
     * Check if two given positions are 8-directionally adjacent to 
     * each other in the 2D coordinate system
     * 
     * @param source the position where the operation is from
     * @param destination the position where the operation is targeted
     * 
     * @return true if two positions are adjacent
     */
    public boolean isAdjacent(int source, int destination) {
        int srcX = source % STD_GRID_SIZE;
        int srcY = Math.floorDiv(source, STD_GRID_SIZE);
        int destX = destination % STD_GRID_SIZE;
        int destY = Math.floorDiv(destination, STD_GRID_SIZE); 

        return Math.abs(srcX - destX) <= 1 
            && Math.abs(srcY - destY) <= 1;
    }

    /**
     * Check if it is allowed to climb from source to destination
     * 
     * @param source the position where the operation is from
     * @param destination the position where the operation is targeted
     * 
     * @return true if it is climbable
     */
    public boolean isClimbable(int source, int destination) {
        int srcLevel = this.grid[source].getLevel();
        int dstLevel = this.grid[destination].getLevel();
        return (dstLevel - srcLevel) <= 1;
    }

    /**
     * Check if the given position is capped
     * 
     * @param fieldId the position of selected field
     * 
     * @return true if it is capped
     */
    public boolean isCapped(int fieldId) {
        return this.grid[fieldId].isCapped();
    }

    /**
     * Check if the given position has a full tower
     * 
     * @param fieldId the position of selected field
     * 
     * @return true if contains a full tower
     */
    public boolean isFullBuild(int fieldId) {
        return this.grid[fieldId].isFullBuild();
    }

    /**
     * Check if the given position is occupied
     * 
     * @param fieldId the position of selected field
     * 
     * @return true if it is occupied
     */
    public boolean isOccupied(int fieldId) {
        return this.grid[fieldId].isOccupied();
    }

    /**
     * Retrieve the player Id whose worker occupied the given position
     * 
     * @param fieldId the position of selected field
     * 
     * @return id of the player; -1 if the position is not occupied
     */
    public int getOccupantId(int fieldId) {
        Worker worker =  this.grid[fieldId].getOccupant();
        return worker == null ? -1 : worker.getOwnerId();
    }

    /**
     * Retrieve the tower level of the given position
     * 
     * @param fieldId the position of selected field
     * 
     * @return level of the given position
     */
    public int getLevel(int fieldId) {
        return this.grid[fieldId].getLevel();
    }

    /**
     * Parse the coordinate to the field id
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     * 
     * @return fieldId; -1 if given coordinate is out of bound
     */
    public static int parsePosition(int x, int y) {
        if ( x < 0 || x >= STD_GRID_SIZE ||
             y < 0 || y >= STD_GRID_SIZE) {
            return -1;
        }
        return y * STD_GRID_SIZE + x;
    }

    /**
     * Retrieve the total number of workers residing on the board
     * 
     * @return number of workers
     */
    public int getNumWorkers() {
        int nWorkers = 0;
        for (int i = 0; i < this.grid.length; i++) {
            nWorkers += this.grid[i].isOccupied() ? 1 : 0;
        }
        return nWorkers;
    }

    /**
     * Retrieve the number of workers residing on the board 
     * that belongs to the specific player
     * 
     * @param playerId id of the player
     * 
     * @return number of workers
     */
    public int getNumWorkers(int playerId) {
        int nWorkers = 0;
        for (int i = 0; i < this.grid.length; i++) {
            nWorkers += this.getOccupantId(i) == playerId ? 1 : 0;
        }
        return nWorkers;
    }

    /**
     * Infer the next position on the same direction given two positions
     * E.g. 5 and 6 will return 7, because (1, 0) -> (1, 1) -> (1, 2)
     * E.g. 6 and 5 will return -1, because (1, 1) -> (1, 0) -> (1, -1) which is not on board
     * 
     * @param curPos the position where the operation is from
     * @param newPos the position where the operation is targeted
     * 
     * @return fieldId; -1 if the inferred position is out of bound
     */
    public int inferNextPosition(int curPos, int newPos) {
        
        int curX = curPos % STD_GRID_SIZE;
        int curY = Math.floorDiv(curPos, STD_GRID_SIZE);
        int newX = newPos % STD_GRID_SIZE;
        int newY = Math.floorDiv(newPos, STD_GRID_SIZE);
        
        int dx = newX - curX;
        int dy = newY - curY;

        int inferred = Board.parsePosition(newX + dx, newY + dy);

        return inferred;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.grid);
    } 

    private Field[] getGrid() {
        return Arrays.copyOf(this.grid, this.grid.length);
    }
}

