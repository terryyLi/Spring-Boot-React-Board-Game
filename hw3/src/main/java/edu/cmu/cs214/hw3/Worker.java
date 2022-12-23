package edu.cmu.cs214.hw3;

public class Worker{
    private Position position;
    private int value;
    private boolean readyToBuild;

    /**
     * Initializes a new {@link Worker} instance
     * which contains its position
     * 
     * @param position the position of the worker
     * @param value the value of worker
     *
     */
    public Worker(Position position, int value) {
        this.position = position;
        this.value = value;
        this.readyToBuild = false;
    }

    /**
     * set the value of is ready to build
     * @param isReady if the worker is ready to build
     */
    public void setReadyToBuild(boolean isReady) {
        this.readyToBuild = isReady;
    }

    /**
     * get the value of is ready to build
     * 
     * @return the value of is ready to build
     *
     */
    public boolean getReadyToBuild() {
        return this.readyToBuild;
    }

    /**
     * get the value of the Worker
     *
     * @return the value of the Worker
     */
    public int getValue() {
        return this.value;
    }

    /**
     * get the position of the Worker
     *
     * @return the position of the Worker
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * set the position of the Worker to be p
     *
     * @param p position
     * 
     */
    public void setPosition(Position p) {
        this.position = p;
    }
}
