package edu.cmu.cs214.hw3;

public class DefaultPlayer extends AbstractPlayer{
    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param worker1 the first worker
     * @param worker2 the second worker
     * @param board the board
     * @param value the value of player
     *
     */
    public DefaultPlayer(Worker worker1, Worker worker2, Board board, int value) {
        super(worker1, worker2, board, value);
    }

    /**
     * Initializes a new {@link Player} instance which 
     * contains two workers and the board it stands on
     * @param board the board
     * @param value the value of player
     *
     */
    public DefaultPlayer(Board board, int value) {
        super(board, value);
    }
}

