package edu.cmu.cs214.backend;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * get the row of the position
     *
     * @return the row of the position
     */
    public int getX() {
        return this.x;
    }

    /**
     * get the col of the position
     *
     * @return the col of the position
     */
    public int getY() {
        return this.y;
    }

    /**
     * compare two positions
     *
     * @param p the position to be compared
     * 
     * @return true if two positions have same row and col
     * false otherwise
     */
    public boolean equals(Position p) {
        return this.x == p.getX() && this.y == p.getY();
    }
}
