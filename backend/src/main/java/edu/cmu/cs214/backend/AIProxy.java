package edu.cmu.cs214.backend;
import java.util.concurrent.ThreadLocalRandom;


public class AIProxy {
    private Player aiPlayer;

    /*
     * initialize AIProxy
     */
    public AIProxy() {
    }

    /**
     * set the ai player
     *
     * @param player the player to be set to ai player
     * 
     */
    public void setPlayer(Player player) {
        this.aiPlayer = player;
    }

    /**
     * generate a random valid position to move
     *
     * @return the randomly generated number
     * 
     */
    private Position generateRandomValidMovePosition() {
        Board board = this.aiPlayer.getBoard();
        Position currP = this.aiPlayer.getCurrentWorker().getPosition();
        int maxX = board.getCol();
        int maxY = board.getRow();
        int randomX = ThreadLocalRandom.current().nextInt(maxX);
        int randomY = ThreadLocalRandom.current().nextInt(maxY);
        while(!this.aiPlayer.isValidMove(currP, new Position(randomX, randomY))) {
            randomX = ThreadLocalRandom.current().nextInt(maxX);
            randomY = ThreadLocalRandom.current().nextInt(maxY);
        }

        return new Position(randomX, randomY);
    }

    /**
     * generate a random valid position to build
     *
     * @return the randomly generated number
     * 
     */
    private Position generateRandomValidBuildPosition() {
        Board board = this.aiPlayer.getBoard();
        Position currP = this.aiPlayer.getCurrentWorker().getPosition();
        int maxX = board.getCol();
        int maxY = board.getRow();
        int randomX = ThreadLocalRandom.current().nextInt(maxX);
        int randomY = ThreadLocalRandom.current().nextInt(maxY);
        while(!this.aiPlayer.isValidBuild(currP, new Position(randomX, randomY))) {
            randomX = ThreadLocalRandom.current().nextInt(maxX);
            randomY = ThreadLocalRandom.current().nextInt(maxY);
        }

        return new Position(randomX, randomY);
    }

    /*
     * auto make sequence of actions of the aiPlayer.
     */
    public void automate() {
        this.setCurrentWorker();
        Player currPlayer = this.aiPlayer;
        int upperBound = 0;
        int lowerBound = 0;
        upperBound++;
        upperBound++;
        int randomX = ThreadLocalRandom.current().nextInt(lowerBound, upperBound);

        if(currPlayer.getMaxMoveSteps() > 1) {
            while(currPlayer.getRemainingMoveSteps() > 1) {
                this.move();
            }
            if(randomX == 0) {
                currPlayer.setRemainingMoveSteps(0);
            }
        }
        this.move();

        randomX = ThreadLocalRandom.current().nextInt(lowerBound, upperBound);

        if(currPlayer.getMaxBuildSteps() > 1) {
            while(currPlayer.getRemainingBuildSteps() > 1) {
                this.build();
            }
            if(randomX == 0) {
                currPlayer.setRemainingBuildSteps(0);
            }
        }
        this.build();
    }

    /*
     * auto move the aiPlayer.
     */
    public void move() {
        Position randomP = this.generateRandomValidMovePosition();
        Worker currWorker = this.aiPlayer.getCurrentWorker();
        this.aiPlayer.move(currWorker, randomP);
    }

    /*
     * auto build the aiPlayer.
     */
    public void build() {
        Position randomP = this.generateRandomValidBuildPosition();
        Worker currWorker = this.aiPlayer.getCurrentWorker();
        this.aiPlayer.build(currWorker, randomP);
    }

    /*
     * auto initialize workers for AI player
     */
    public void initialize() {
        Board board = this.aiPlayer.getBoard();
        int maxX = board.getCol();
        int maxY = board.getRow();
        int randomX1 = ThreadLocalRandom.current().nextInt(maxX);
        int randomY1 = ThreadLocalRandom.current().nextInt(maxY);
        while(!board.isNotOccupied(new Position(randomX1, randomY1))) {
            randomX1 = ThreadLocalRandom.current().nextInt(maxX);
            randomY1 = ThreadLocalRandom.current().nextInt(maxY);
        }
        this.aiPlayer.setWorker1(new Worker(new Position(randomX1, randomY1), 1));
        int randomX2 = ThreadLocalRandom.current().nextInt(maxX);
        int randomY2 = ThreadLocalRandom.current().nextInt(maxY);
        while(!board.isNotOccupied(new Position(randomX2, randomY2))) {
            randomX2 = ThreadLocalRandom.current().nextInt(maxX);
            randomY2 = ThreadLocalRandom.current().nextInt(maxY);
        }
        this.aiPlayer.setWorker2(new Worker(new Position(randomX2, randomY2), 2));
    }

    /*
     * auto initialize workers for AI player
     */
    public void setCurrentWorker() {
        int upperBound = 0;
        int lowerBound = 0;
        upperBound++;
        upperBound++;
        int randomX = ThreadLocalRandom.current().nextInt(lowerBound, upperBound);
        if(randomX == 0) {
            this.aiPlayer.setCurrentWorker(this.aiPlayer.getWorker1());
        } else {
            this.aiPlayer.setCurrentWorker(this.aiPlayer.getWorker2());
        }
    }

}
