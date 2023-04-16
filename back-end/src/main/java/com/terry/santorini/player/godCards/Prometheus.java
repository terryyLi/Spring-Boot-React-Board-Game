package com.terry.santorini.player.godCards;

import java.util.List;
import java.util.stream.Collectors;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class Prometheus extends GodCard {
    private final boolean buildBeforeMove;
    private final boolean builtThisRound;

    public Prometheus(GameActions god) {
        this(god, false, false);
    }

    public Prometheus(GameActions god, boolean buildBeforeMove, boolean builtThisRound) {
        super(god);
        this.buildBeforeMove = buildBeforeMove;
        this.builtThisRound = builtThisRound;
    }

    @Override
    public String getGodDescription() {
        return "Prometheus: If your Worker does not move up, it may build both before and after moving";
    }

    @Override
    public String getAbilityDescription() {
        return "Build Before Move";
    }

    @Override
    public GodCard updateFocus(int newFocus) {
        GameActions god = this.getGod().updateFocus(newFocus);
        return new Prometheus(god, buildBeforeMove, builtThisRound);
    }

    @Override
    public GodCard updateActivity(boolean active) {
        GameActions god = this.getGod().updateActivity(active);
        return new Prometheus(god, buildBeforeMove, builtThisRound);
    }

    /**
     * Reset the focus to unselected state and erase the special build record
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game afterRoundEnd(Game context, int playerId) {
        GameActions god = this.getGod().updateFocus(-1);
        GameActions newGodCard = new Prometheus(god, false, false);
        return context.update(newGodCard, playerId);
    }

    /**
     * Perform the default select. Activate the ability while entering the MOVE stage
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param pos position of the selected worker
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game select(Game context, int playerId, int pos) {
        context = super.select(context, playerId, pos);
        // activate ability after worker selection
        GameActions newActions = this.updateActivity(true)
                                .updateFocus(pos);

        return context.update(newActions, playerId);
    }

    /**
     * Perform the default move. Deactivate the ability while leaving the MOVE stage
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param target position of the selected worker
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game move(Game context, int playerId, int target) {
        context = super.move(context, playerId, target);
        // deactivate ability after worker move
        GameActions newActions = this.updateActivity(false)
                                .updateFocus(target);

        return context.update(newActions, playerId);
    }

    /**
     * Perform the default build. If this build is performed before move, go to MOVE stage
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param target position of the selected worker
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game build(Game context, int playerId, int target) {
        context = super.build(context, playerId, target);

        if (buildBeforeMove && !builtThisRound) {
            // going to build before move but not done yet
            GameActions newGodCard = new Prometheus(this.getGod(), true, true);
            return context.update(newGodCard, playerId)
                        .update(GameStage.MOVE); 
        }
        return context;
    }

    /**
     * Get default movable option set. Prometheus can not move up if he built beforehand.
     * If has previously built in this round, remove upward movements from the list
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    @Override
    public List<Integer> getValidMoves(Game context) {
        List<Integer> validOptions = super.getValidMoves(context);
        if (builtThisRound) {
            // can not move up if build beforehand
            Board board = context.getBoard();
            int source = this.getFocus();
            int fromLevel = board.getLevel(source);
            validOptions =  validOptions.stream()
                .filter(option -> board.getLevel(option) - fromLevel <= 0)
                .collect(Collectors.toList()); 
        }
        return validOptions;
    }

    /**
     * Prometheus can choose to build before move. Deactivate the ability and 
     * go to BUILD stage
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game ability(Game context, int playerId) {
        context = super.ability(context, playerId);

        // switch to build stage
        GameActions newGodCard = this.getGod().updateActivity(false);
        newGodCard = new Prometheus(newGodCard, true, false);
        return context.update(newGodCard, playerId)
                    .update(GameStage.BUILD);
    }

    
}

