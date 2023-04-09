package edu.cmu.cs214.backend;

public class Tower {
    private int level;
    private int maxLevel;
    private boolean isOccupied;

    /**
     * Initializes a new {@link Tower} instance with a level of 0
     * and it is not occuplied
     * 
     * @param level the level of tower
     * @param maxLevel max level of tower
     *
     */
    public Tower(int level, int maxLevel) {
        this.level = level;
        this.maxLevel = maxLevel;
        this.isOccupied = false;
    }

    /**
     * check if the grid is occupied by Worker
     *
     * @return true if the position is occupied, false otherwise
     */
    public boolean isOccupiedByWorker() {
        return this.isOccupied;
    }

    /**
     * set the grid occupied if isSetOccupied is true, not occupied otherwise
     *
     * @param isSetOccupied indicating if set occupied or not
     */
    public void setIsOccuplied(boolean isSetOccupied) {
        this.isOccupied = isSetOccupied;
    }

    /**
     * get the level of tower
     *
     * @return the level of tower
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * get the max level of tower
     *
     * @return the max level of tower
     */
    public int getMaxLevel() {
        return this.maxLevel;
    }

    /**
     * check if the tower has dome
     *
     * @return true if the tower has dome, false otherwise
     */
    public boolean hasDome() {
        return this.level == maxLevel;
    }
    
    /**
     * increment the level of tower by 1
     *
     */
    public void incrLevel() {
        if(!this.hasDome()) {
            this.level++;
        }
    }
}
