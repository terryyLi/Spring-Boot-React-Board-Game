package com.terry.santorini.player.godCards;

import java.util.List;
import java.util.stream.Collectors;

import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class Demeter extends GodCard{
    private final boolean builtThisRound;   // whether has performed first build
    private final int builtAt;              // location of first build, if any

    public Demeter(GameActions god) {
        super(god);
        builtThisRound = false;
        builtAt = -1;
    }

    public Demeter(GameActions god, boolean builtThisRound, int builtAt) {
        super(god);
        this.builtThisRound = builtThisRound;
        this.builtAt = builtAt;
    }

    @Override
    public String getGodDescription() {
        return "Demeter: Your Worker may build one additional time, but not on the same space";
    }

    @Override
    public String getAbilityDescription() {
        return "Skip Build";
    } 

    @Override
    public GodCard updateFocus(int newFocus) {
        GameActions god = this.getGod().updateFocus(newFocus);
        return new Demeter(god, builtThisRound, builtAt);
    }

    @Override
    public GodCard updateActivity(boolean active) {
        GameActions god = this.getGod().updateActivity(active);
        return new Demeter(god, builtThisRound, builtAt);
    }

    /**
     * Reset the focus to unselected state and erase the record about first build
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game afterRoundEnd(Game context, int playerId) {
        GameActions newGodCard = this.getGod().updateFocus(-1);
        newGodCard = new Demeter(newGodCard, false, -1);
        return context.update(newGodCard, playerId);
    }

    /**
     * Perform the default build. If hasn't built in this round yet, activate 
     * the ability and allow for a sceond build. Otherwise, deactivate ability 
     * and move to next stage
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
        if (this.builtThisRound) {
            // End building if already built twice
            GameActions newGodCard = this.updateActivity(false);
            return context.update(newGodCard, playerId)
                    .update(GameStage.ENDTURN);
        } 
        else {
            // allow second build
            GameActions newGodCard = this.getGod().updateActivity(true);
            newGodCard = new Demeter(newGodCard, true, target);
            return context.update(newGodCard, playerId)
                    .update(GameStage.BUILD);
        }
    }

    /**
     * Get default buildable option set. Demeter can not build twice on the same place.
     * If has previously built in this round, remove the first built position from the list
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    @Override
    public List<Integer> getValidBuilds(Game context) {
        List<Integer> validOptions = super.getValidBuilds(context);

        if (this.builtAt == -1) {
            return validOptions;
        }
        else {  // prevent building on the same space
            return validOptions.stream()
                .filter(option -> option != builtAt)
                .collect(Collectors.toList());
        }
    }

    /**
     * Demeter can choose to skip the second build. Deactivate the ability and 
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
        // skip second build
        GameActions newGodCard = this.updateActivity(false);
        return context.update(newGodCard, playerId)
                    .update(GameStage.ENDTURN);
    }
}

