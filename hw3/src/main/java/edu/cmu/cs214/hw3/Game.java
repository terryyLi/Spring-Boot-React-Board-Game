package edu.cmu.cs214.hw3;

public class Game {
    private Player player1;
    private Player player2;
    private Player currPlayer;
    private Player winner;
    private boolean isGameOver;
    private Board board;
    private boolean isinitializable;
    private static final int BOARDSIZE = 5;
    private static final int MAXLEVEL = 4;
    private boolean isAIMode;

    /**
     * Initializes a new {@link Game} instance
     */
    public Game() {
        this.board = new Board(BOARDSIZE, BOARDSIZE, MAXLEVEL);
        this.isGameOver = false;
        // this.player1 = new DefaultPlayer(this.board, 1);
        // this.player2 = new DefaultPlayer(this.board, 2);
        // this.currPlayer = this.player1;
        this.isinitializable = true;
        this.isAIMode = false;
    }

    /**
     * Initializes a new {@link Game} instance
     * @param r11 row of worker1 of player1
     * @param c11 col of worker1 of player1
     * @param r12 row of worker2 of player1
     * @param c12 col of worker2 of player1
     * @param r21 row of worker1 of player2
     * @param c21 col of worker1 of player2
     * @param r22 row of worker2 of player2
     * @param c22 col of worker2 of player2
     * @param boardSize row / col numbers of board
     * @param maxLevel max level of tower
     * 
     */
    public Game(int r11, int c11, int r12, int c12,
                int r21, int c21, int r22,int c22, int boardSize, int maxLevel) {
        Board board = new Board(boardSize, boardSize, maxLevel);
        Worker worker11 = new Worker(new Position(r11, c11), 1);
        Worker worker12 = new Worker(new Position(r12, c12), 2);
        Worker worker21 = new Worker(new Position(r21, c21), 1);
        Worker worker22 = new Worker(new Position(r22, c22), 2);
        this.player1 = new DefaultPlayer(worker11, worker12, board, 1);
        this.player2 = new DefaultPlayer(worker21, worker22, board, 2);
        board.setPlayer(new Position(r11, c11), this.player1);
        board.setPlayer(new Position(r12, c12), this.player1);
        board.setPlayer(new Position(r21, c21), this.player2);
        board.setPlayer(new Position(r22, c22), this.player2);
        board.setWorker(new Position(r11, c11), worker11);
        board.setWorker(new Position(r12, c12), worker12);
        board.setWorker(new Position(r21, c21), worker21);
        board.setWorker(new Position(r22, c22), worker22);
        this.currPlayer = this.player1;
        this.isGameOver = false;
        this.isinitializable = false;
        this.isAIMode = false;
    }

    /**
     * 
     * @return if it's AI mode
     */
    public boolean isAIMode() {
        return this.isAIMode;
    }

    /**
     * 
     * set AI mode to be true
     */
    public void setAIMode() {
        this.isAIMode = true;
    }

    /**
     * 
     * @return the board dimension
     */
    public int getBoardSize() {
        return BOARDSIZE;
    }

    /**
     * 
     * @return the board 
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * 
     * @return the max level of tower
     */
    public int getMaxLevel() {
        return MAXLEVEL;
    }

    /**
     * 
     * @return if the game is initializable
     */
    public boolean isInitializable() {
        return this.isinitializable;
    }

    /**
     * 
     * set the game to be not initializable
     */
    public void setNotInitializable() {
        this.isinitializable = false;
    }

    /**
     * get the current player
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return this.currPlayer;
    }

    /**
     * set the current player
     *
     * @param p the player to be set to current player
     * 
     */
    public void setCurrentPlayer(Player p) {
        this.currPlayer = p;
    }

    /**
     * set the current player
     *
     */
    public void alterCurrentPlayer() {
        this.currPlayer = this.currPlayer == this.player1 ? this.player2 : this.player1;
    }

    /**
     * set the current player's current worker to null
     *
     */
    public void resetCurrentPlayer() {
        this.currPlayer.setCurrentWorker(null);
    }


    /**
     * get the player 1
     *
     * @return the player 1
     */
    public Player getPlayer1() {
        return this.player1;
    }

    /**
     * set the player 1
     *
     * @param ply1 the player 1
     */
    public void setPlayer1(Player ply1) {
        this.player1 = ply1;
    }

    /**
     * set the player 2
     *
     * @param ply2 the player 2
     */
    public void setPlayer2(Player ply2) {
        this.player2 = ply2;
    }

    /**
     * get the player 2
     *
     * @return the player 2
     */
    public Player getPlayer2() {
        return this.player2;
    }

    /**
     * switch turns and set the new current player
     *
     */
    public void switchTurns() {
        if(this.currPlayer.equals(this.player1)) {
            this.currPlayer = this.player2;
        } else {
            this.currPlayer = this.player1;
        }
    }

    /**
     * check if the game is over
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        if(this.player1.isWin()) {
            this.winner = this.player1;
            this.isGameOver = true;
        }
        if(this.player2.isWin()) {
            this.winner = this.player2;
            this.isGameOver = true;
        }
        return this.isGameOver;
    }

    /**
     * get the winner player
     *
     * @return the winner player
     */
    public Player getWinner() {
        if(this.player1 != null && this.player1.isWin()) {
            this.winner = this.player1;
        }
        if(this.player2 != null && this.player2.isWin()) {
            this.winner = this.player2;
        }
        return this.winner;
    }
}
