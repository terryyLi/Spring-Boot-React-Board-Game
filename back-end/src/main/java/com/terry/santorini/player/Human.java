package com.terry.santorini.player;

import java.util.ArrayList;
import java.util.List;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;

public class Human implements GameActions {
    private boolean abilityActive;  // active state of ability
    private int focus;              // selected space on board

    public Human() {
        abilityActive = false;
        focus = -1;
    }

    public Human(boolean abilityActive, int focus) {
        this.abilityActive = abilityActive;
        this.focus = focus;
    }

    public String getGodDescription() {
        return "Human: You do not possess any god power";
    }


    public String getAbilityDescription() {
        return "No Ability";
    }

    public int getFocus() {
        return focus;
    }

    public boolean isAbilityActive() {
        return abilityActive;
    }

    public GameActions updateActivity(boolean active) {
        return new Human(active, focus);
    }

    public GameActions updateFocus(int newFocus) {
        return new Human(abilityActive, newFocus);
    }

    /**
     * Reset the focus to unselected state.
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    public Game afterRoundEnd(Game context, int playerId) {
        GameActions newGodCard = this.updateFocus(-1);
        return context.update(newGodCard, playerId);
    }

    /**
     * Place a worker on an unoccupied space on the board
     * update the game to MOVE stage.
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param pos position of the selected worker
     * 
     * @return updated {@link Game}
     */
    public Game placeWorker(Game context, int playerId, int pos) {
        GameStage newStage;
        String playerName = context.getPlayers()[playerId]
                                .getName();
        Board newBoard = context.getBoard()
                            .initWorkerFor(playerId, playerName, pos);
        // switch player if current player has setup all workers
        if (newBoard.getNumWorkers(playerId) >= context.getMaxNumWorker()){
            newStage = GameStage.ENDTURN;
        }
        else {
            newStage = GameStage.INIT;
        }
        return context.update(newBoard).update(newStage);
    }

    /**
     * Select a worker that belongs to the current player and 
     * update the game to MOVE stage.
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param pos position of the selected worker
     * 
     * @return updated {@link Game}
     */
    public Game select(Game context, int playerId, int pos) {
        GameActions newGodCard = this.updateFocus(pos);
        return context.update(newGodCard, playerId)
                    .update(GameStage.MOVE);
    }
    
    /**
     * Move the selected worker to the designated position, assuming the movement is valid
     * This method assumes the destination position is chosen from the valid option set
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param target position of the selected worker
     * 
     * @return updated {@link Game}
     */
    public Game move(Game context, int playerId, int target) {
        Board board = context.getBoard();
        board = board.move(focus, target);
        GameActions newGodCard = this.updateFocus(target); 

        context = context.update(newGodCard, playerId)
                    .update(board)
                    .update(GameStage.BUILD);

        // check win condition
        return this.checkWinByMove(context, playerId, focus, target);
    }

    /**
     * Perform action build at the designated position, assuming the position is valid
     * This method assumes the destination position is chosen from the valid option set. 
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param target position of the selected worker
     * 
     * @return updated {@link Game}
     */
    public Game build(Game context, int playerId, int target) {
        Board board = context.getBoard();
        if (board.isFullBuild(target)) {
            board = board.buildDome(target);
        }
        else {
            board = board.buildBlock(target);
        }
        return context.update(board)
                    .update(GameStage.ENDTURN);
    }

    /**
     * Human has no ability. This has no effect.
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    public Game ability(Game context,int playerId) {
        return context;
    }

    /**
     * Check winning condition for the movement performed by current player
     * A human wins if a worker moves to a full-build tower
     * 
     * @param context game context
     * @param playerId the player who is checking the winning condition
     * @param from location where the worker was located before move
     * @param to location where the worker is located after move
     * 
     * @return true if the game is over and current player wins
     */
    public Game checkWinByMove(Game context, int playerId, int from, int to) {
        Board board = context.getBoard();
        if (board.isFullBuild(to)) {
            return context.update(GameStage.END)
                        .updateWinner(playerId);
        }
        else {
            return context;
        }
    }

    /**
     * Get all available spaces on the board that can place a worker on.  
     * A space is available only if it is not occupied by other worker yet.
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    public List<Integer> getValidWorkerPlacement(Game context) {
        List<Integer> validOptions = new ArrayList<Integer>();
        Board board = context.getBoard();
        for (int fieldId = 0; fieldId < board.size(); fieldId++) {
            if (!board.isOccupied(fieldId)) {
                validOptions.add(fieldId);
            }
        }
        return validOptions;
    }

    /**
     * Get all selectable spaces on the board where the current player's workers are located.
     * A space is selecteable only if it is occupied by current player.
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    public List<Integer> getValidSelections(Game context) {
        List<Integer> validOptions = new ArrayList<Integer>();
        int playerId = context.getCurrentPlayerId();
        Board board = context.getBoard();
        for (int fieldId = 0; fieldId < board.size(); fieldId++) {
            if (board.getOccupantId(fieldId) == playerId) {
                validOptions.add(fieldId);
            }
        }
        return validOptions;
    }

    /**
     * Get all movable spaces on the board.
     * A space is movable from the initial position only if it is not occupied by any worker,
     * adjacent to the initial position, not capped by a dome, and not two levels taller
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    public List<Integer> getValidMoves(Game context) {
        List<Integer> validOptions = new ArrayList<Integer>();
        Board board = context.getBoard();
        for (int newPos = 0; newPos < board.size(); newPos++) {
            if (newPos != focus &&
                board.isAdjacent(focus, newPos) &&
                board.isClimbable(focus, newPos) &&
                !board.isCapped(newPos) &&
                !board.isOccupied(newPos)
            ) {
                validOptions.add(newPos);
            }

                
        }
        return validOptions;
    }

    /**
     * Get all buildable spaces on the board.
     * A space is buildable from the worker's position only if it is not occupied by any worker,
     * adjacent to the worker's position, and not capped by a dome
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    public List<Integer> getValidBuilds(Game context) {
        List<Integer> validOptions = new ArrayList<Integer>();
        Board board = context.getBoard();
        for (int newPos = 0; newPos < board.size(); newPos++) {
            if (newPos != focus &&
                board.isAdjacent(focus, newPos) &&
                !board.isCapped(newPos) &&
                !board.isOccupied(newPos)
            ) {
                validOptions.add(newPos);
            }  
        }
        return validOptions;
    }

    /**
     * Human cannot ban opponent's movement, so this method has no effect.
     * 
     * @param context the game context
     * @param validOptions the valid moves options
     * @param source the initial position
     * 
     * @return original valid options
     */
    public List<Integer> banOpponentMoves(Game context, List<Integer> validOptions, int source) {
        return validOptions;
    }

    @Override
    public String toString() {
        return  """
                {
                    "card_desc": "%s",
                    "ability_desc": "%s",
                    "is_ability_active": %b
                }
                """.formatted(this.getGodDescription(),
                     this.getAbilityDescription(), 
                     this.isAbilityActive());
    }


}

