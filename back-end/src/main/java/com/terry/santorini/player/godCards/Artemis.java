package com.terry.santorini.player.godCards;

import java.util.List;
import java.util.stream.Collectors;

import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class Artemis extends GodCard{
    private final boolean movedThisRound;
    private final int initialSpace;

    public Artemis(GameActions god) {
        super(god);
        movedThisRound = false; // whether has performed first move
        initialSpace = -1;      // original location of first move, if moved
    }

    public Artemis(GameActions god, boolean movedThisRound, int initialSpace) {
        super(god);
        this.movedThisRound = movedThisRound;
        this.initialSpace = initialSpace;
    }

    @Override
    public String getGodDescription() {
        return "Artemis: Your Worker may move one additional time, but not back to its initial space";
    }

    @Override
    public String getAbilityDescription() {
        return "Skip Move";
    } 

    @Override
    public GodCard updateFocus(int newFocus) {
        GameActions god = this.getGod().updateFocus(newFocus);
        return new Artemis(god, movedThisRound, initialSpace);
    }

    @Override
    public GodCard updateActivity(boolean active) {
        GameActions god = this.getGod().updateActivity(active);
        return new Artemis(god, movedThisRound, initialSpace);
    }

    /**
     * Reset the focus to unselected state and erase the record about first move
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game afterRoundEnd(Game context, int playerId) {
        GameActions god = this.getGod().updateFocus(-1);
        GameActions newGodCard = new Artemis(god, false, -1);
        return context.update(newGodCard, playerId);
    }

     /**
     * Perform the default move. If hasn't move in this round yet, activate 
     * the ability and allow for a sceond move. Otherwise, deactivate ability 
     * and move to next stage
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param target position of the selected worker
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game move(Game context, int playerId, int target) {
        int source = this.getFocus();
        context = super.move(context, playerId, target);
        GameActions newGodCard;
        if (this.movedThisRound) {
            // end moving if already moved twice 
            newGodCard = this.updateActivity(false);
            context = context.update(GameStage.BUILD);
        } 
        else {
            // allow second move
            newGodCard = this.getGod().updateActivity(true);
            newGodCard = new Artemis(newGodCard, true, source);
            context = context.update(GameStage.MOVE);
        }
        newGodCard = newGodCard.updateFocus(target);
        return context.update(newGodCard, playerId);
    }

    /**
     * Get default movable option set. Artemis can not move back to where he started.
     * If has previously moved in this round, remove the initial position from the list
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    @Override
    public List<Integer> getValidMoves(Game context) {
        List<Integer> validOptions = super.getValidMoves(context);

        if (this.initialSpace == -1) {
            return validOptions;
        }
        else {  // prevent moving to the initial space
            return validOptions.stream()
                .filter(option -> option != initialSpace)
                .collect(Collectors.toList());
        }
    }

    /**
     * Artemis can choose to skip the second move. Deactivate the ability and 
     * advance to next game stage
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game ability(Game context, int playerId) {
        context = super.ability(context, playerId);
        // skip second move, go to build stage 
        GameActions newGodCard = this.updateActivity(false);
        return context.update(newGodCard, playerId)
                    .update(GameStage.BUILD);
    }
}

