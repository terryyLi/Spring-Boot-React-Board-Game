package com.terry.santorini.player;

import java.util.List;

import com.terry.santorini.game.Game;

public interface GameActions {
    /**
     * @return longer description of the god power
     */
    String getGodDescription();

    /**
     * @return shorter description of the god's special action
     */
    String getAbilityDescription();

    /**
     * @return whether the god's ability is ready to use
     */
    boolean isAbilityActive();

    /**
     * @return the selected space 
     */
    int getFocus();

    /**
     * In response to a selection or a movement, update the current focus
     * 
     * @param newFocus new selected space
     * 
     * @return updated {@link GameActions}
     */
    GameActions updateFocus(int newFocus);

    /**
     * Enable/Disable the ability
     * 
     * @param active true for activate; false for deactivate
     * 
     * @return updated {@link GameActions}
     */
    GameActions updateActivity(boolean active);

    /**
     * Stuff to perform at the end of each round.
     * E.g. reset internal state of god cards
     * Should be called at the end of each round.
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    Game afterRoundEnd(Game context, int playerId);

    /**
     * Place a worker on an unoccupied space on the board
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param pos position of the selected worker
     * 
     * @return updated {@link Game}
     */
    Game placeWorker(Game context, int playerId, int pos);

    /**
     * Select a worker that belongs to the current player and update the game state
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param pos position of the selected worker
     * 
     * @return updated {@link Game}
     */
    Game select(Game context, int playerId, int pos);
    
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
    Game move(Game context, int playerId, int target); 

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
    Game build(Game context, int playerId, int target);

    /**
     * Perform special action.
     * E.g. update god card states
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    Game ability(Game context, int playerId);

    /**
     * Check winning condition for the movement performed by current player
     * 
     * @param context game context
     * @param playerId the player who is checking the winning condition
     * @param from location where the worker was located before move
     * @param to location where the worker is located after move
     * 
     * @return true if the game is over and current player wins
     */
    Game checkWinByMove(Game context, int playerId, int from, int to);

    /**
     * Get all available spaces on the board that can place a worker on.  
     * Should be called at the INIT stage of the game.
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    List<Integer> getValidWorkerPlacement(Game context);

    /**
     * Get all selectable spaces on the board where the current player's workers are located.
     * Should be called at the SELECT stage of the game.
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    List<Integer> getValidSelections(Game context);

    /**
     * Get all movable spaces on the board.
     * Should be called at the MOVE stage of the game.
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    List<Integer> getValidMoves(Game context);

    /**
     * Get all buildable spaces on the board.
     * Should be called at the BUILD stage of the game.
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    List<Integer> getValidBuilds(Game context);

    /**
     * Eliminate some forbidden movement from the opponent's valid option set.
     * Some implementations of the {@link GameActions} may have this ability.
     * Should be called at the MOVE stage of the game.
     * 
     * @param context the game context
     * @param validOptions the valid moves options
     * @param source the initial position
     * 
     * @return list of valid options
     */
    List<Integer> banOpponentMoves(Game context, List<Integer> validOptions, int source);
}

