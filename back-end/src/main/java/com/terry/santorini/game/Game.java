package com.terry.santorini.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import com.terry.santorini.board.Board;
import com.terry.santorini.player.AIPlayer;
import com.terry.santorini.player.GameActions;
import com.terry.santorini.player.Player;
import com.terry.santorini.player.godCards.GodCardFactory;

public final class Game {
    // game components
    private final Player[] players;
    private final Board board;
    // game state
    private final int round;
    private final GameStage stage;
    private final Deque<Game> history;
    // game constant
    private static final int MAX_NUM_WORKER = 2;
    private static final int MAX_NUM_PLAYER = 2;


    public Game() {
        this(new String[]{"Human", "Human"});
    }

    public Game(String[] godCardNames) {
        this.players = new Player[MAX_NUM_PLAYER];
        this.players[0] = new Player("A", GodCardFactory.getCard(godCardNames[0]));
        this.players[1] = new Player("B", GodCardFactory.getCard(godCardNames[1]));

        this.board = new Board();

        this.round = 0;
        this.stage = GameStage.INIT;
        this.history = new ArrayDeque<Game>();
    }

    public Game(Player[] players, Board board, int round, GameStage stage, Deque<Game> history) {
        this.players = players;
        this.board = board;
        this.round = round;
        this.stage = stage;
        this.history = history;
    }


    public Board getBoard() {
        return this.board;
    }

    public GameStage getStage() {
        return this.stage;
    }

    public int getMaxNumWorker() {
        return MAX_NUM_WORKER;
    }

    public Game setUpAI(int playerId) {
        Player[] newPlayers = this.getPlayers();
        newPlayers[playerId] = new AIPlayer(newPlayers[playerId]);
        return new Game(newPlayers, board, round, stage, history);
    }

    /**
     * Update the game state for a brand new round and switch player
     * 
     * @return updated {@link Game}
     */
    public Game newRound() {
        GameStage newStage;
        int playerId = this.getCurrentPlayerId();
        GameActions godCard = this.getActions(playerId);
        // not enought workers. still in INIT stage
        if (board.getNumWorkers() < MAX_NUM_WORKER * MAX_NUM_PLAYER) {
            newStage = GameStage.INIT;
        }
        else {
            newStage = GameStage.SELECT;
        }
        return godCard.afterRoundEnd(this, playerId)
                    .update(newStage)
                    .nextRound();
    }

    /**
     * Place a worker on the game board for the current player
     * 
     * @param pos position to place worker on
     * 
     * @return updated {@link Game}
     */
    public Game initWorker(int pos) {
        int playerId = this.getCurrentPlayerId();
        GameActions godCard = this.getActions(playerId);

        return godCard.placeWorker(this.push(), playerId, pos);
                
    }

    /**
     * Select a worker that belongs to the current player and update the game state
     * 
     * @param pos position of the selected worker
     * 
     * @return updated {@link Game}
     */
    public Game select(int pos) {
        int playerId = this.getCurrentPlayerId();
        GameActions godCard = this.getActions(playerId);

        return godCard.select(this.push(), playerId, pos);
    }

    /**
     * Move a worker to the designated position, assuming the movement is valid
     * This method assumes the destination position is chosen from the valid option set
     * 
     * @param newPos desitination position of the selected worker
     * 
     * @return updated {@link Game}
     */
    public Game move(int newPos) {
        int playerId = this.getCurrentPlayerId();
        GameActions godCard = this.getActions(playerId);

        return godCard.move(this.push(), playerId, newPos);
    }

    /**
     * Perform action build at the designated position, assuming the position is valid
     * This method assumes the destination position is chosen from the valid option set. 
     * 
     * @param pos target position
     * 
     * @return updated {@link Game}
     */
    public Game build(int pos) {
        int playerId = this.getCurrentPlayerId();
        GameActions godCard = this.getActions(playerId);

        return godCard.build(this.push(), playerId, pos);
    }

    /**
     * Perform special action defined by different {@link GameActions}
     * 
     * @return updated {@link Game}
     */
    public Game useAbility() {
        int playerId = this.getCurrentPlayerId();
        GameActions godCard = this.getActions(playerId);

        return godCard.ability(this.push(), playerId);
    }

    /**
     * Restore all game states back to the previous step
     * 
     * @return updated {@link Game}
     */
    public Game undo() {
        if (history.size() < 1) {
            return this;
        }
        return history.peekLast();
    }

    /**
     * Retrieve all possible options based on current {@link GameStage},
     * ensuring every option is valid.
     * 
     * @return list of valid options
     */
    public List<Integer> getValidOptions() {
        if(stage.equals(GameStage.INIT)){
            return this.getValidWorkerPlacement();
        }
        else if (stage.equals(GameStage.SELECT)) {
            return this.getValidSelections();
        } 
        else if (stage.equals(GameStage.MOVE)) {
            int playerId = this.getCurrentPlayerId();
            int opponentId = this.getOpponentId();
            int focus = this.getActions(playerId).getFocus();
            GameActions oppGodCard = this.getActions(opponentId);
            return oppGodCard.banOpponentMoves(this, this.getValidMoves(), focus);
        }
        else if (stage.equals(GameStage.BUILD)) {
            return this.getValidBuilds();
        }
        return new ArrayList<>();
    }

    /**
     * Retrieve a message indicating the task that the player 
     * needs to perform during that stage.
     * 
     * @return a string message
     */
    public String getStageText() {
        int playerId = this.getCurrentPlayerId();
        String playerName = this.players[playerId].getName();
        return this.stage.getDescription(playerName);
    }


    /**
     * Get the player id/index for the current player
     * 
     * @return index to current player
     */
    public int getCurrentPlayerId() {
        return round % MAX_NUM_PLAYER;
    }

    /**
     * Get the player id/index for the current player's opponent
     * 
     * @return index to opponent player
     */
    public int getOpponentId() {
        return (round+1) % MAX_NUM_PLAYER;
    }

    /**
     * Update the state of a specific player's god card
     * 
     * @param newGodCard object used to update the player
     * @param playerId  index of the player to be updated
     * 
     * @return updated {@link Game}
     */
    public Game update(GameActions newGodCard, int playerId) {
        Player[] newPlayers = this.getPlayers();
        newPlayers[playerId] = newPlayers[playerId].update(newGodCard);
        return new Game(newPlayers, board, round, stage, history);
    }

    /**
     * Update the state of the gameboard
     * 
     * @param newBoard object used to update the board
     * 
     * @return updated {@link Game}
     */
    public Game update(Board newBoard) {
        return new Game(players, newBoard, round, stage, history);
    }

    /**
     * Update the stage of the game
     * This has no effect if the game is in END stage
     * 
     * @param newStage object used to update the stage
     * 
     * @return updated {@link Game}
     */
    public Game update(GameStage newStage) {
        newStage = stage == GameStage.END? stage : newStage;
        return new Game(players, board, round, newStage, history);
    }

    /**
     * Advance the game to next round
     * This has no effect if the game is in END stage
     * 
     * @return updated {@link Game}
     */
    public Game nextRound() {
        return new Game(players, board, round+1, stage, history); 
    }

    /**
     * Advance the game to winner's round
     * 
     * @param winnerId player id of the winner
     *
     * @return updated {@link Game}
     */
    public Game updateWinner(int winnerId) {
        if (winnerId == this.getCurrentPlayerId()) {
            return this.update(GameStage.END);
        }
        return this.nextRound()
                .update(GameStage.END);
    }

    @Override
    public String toString() {
        int playerId = this.getCurrentPlayerId();
        GameActions godCard = this.getActions(playerId);
        return  """
            {
                "fields": %s,
                "validOptions": %s,
                "message": "%s",
                "stage": "%s",
                "godCard": %s
            }
            """.formatted(board.toString(), 
                        this.getValidOptionString(), 
                        this.getStageText(), 
                        stage,
                        godCard.toString()
                ); 
    }

    // Private Methods

    private Game push() {
        Deque<Game> newHistory = new ArrayDeque<>(this.history);
        newHistory.addLast(this);

        return new Game(players, board, round, stage, newHistory);
    }

    public GameActions getActions(int playerId) {
        return this.players[playerId].getActions();
    }

    public Player[] getPlayers() {
        return Arrays.copyOf(this.players, MAX_NUM_PLAYER);
    }


    private List<Integer> getValidWorkerPlacement() {
        int playerId = this.getCurrentPlayerId();
        GameActions godCard = this.getActions(playerId);
        return godCard.getValidWorkerPlacement(this);
    }

    private List<Integer> getValidSelections() {
        int playerId = this.getCurrentPlayerId();
        GameActions godCard = this.getActions(playerId);
        return godCard.getValidSelections(this);
    }

    private List<Integer> getValidMoves() {
        int playerId = this.getCurrentPlayerId();
        GameActions godCard = this.getActions(playerId);
        return godCard.getValidMoves(this);
    }

    private List<Integer> getValidBuilds() {
        int playerId = this.getCurrentPlayerId();
        GameActions godCard = this.getActions(playerId);
        return godCard.getValidBuilds(this);
    }

    private String getValidOptionString() {
        int playerId = this.getCurrentPlayerId();
        if (players[playerId] instanceof AIPlayer) {
            // Make AI options invisible to front-end 
            return "[]";
        }
        return this.getValidOptions().toString();
    }
}
