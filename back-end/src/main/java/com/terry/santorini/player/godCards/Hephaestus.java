package com.terry.santorini.player.godCards;

import java.util.Arrays;
import java.util.List;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class Hephaestus extends GodCard {
    private final boolean builtThisRound;
    private final int builtAt;

    public Hephaestus(GameActions god) {
        super(god);
        builtThisRound = false;
        builtAt = -1;
    }

    public Hephaestus(GameActions god, boolean builtThisRound, int builtAt) {
        super(god);
        this.builtThisRound = builtThisRound;   // whether has performed first build
        this.builtAt = builtAt;                 // location of first build, if any
    }

    @Override
    public String getGodDescription() {
        return "Hephaestus: Your Worker may build one additional block (not dome) on top of your first block";
    }

    @Override
    public String getAbilityDescription() {
        return "Skip Build";
    } 

    @Override
    public GodCard updateFocus(int newFocus) {
        GameActions god = this.getGod().updateFocus(newFocus);
        return new Hephaestus(god, builtThisRound, builtAt);
    }

    @Override
    public GodCard updateActivity(boolean active) {
        GameActions god = this.getGod().updateActivity(active);
        return new Hephaestus(god, builtThisRound, builtAt);
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
        newGodCard = new Hephaestus(newGodCard, false, -1);
        return context.update(newGodCard, playerId);
    }

    /**
     * Perform the default build. If already built in this round or can not build more blocks 
     * onto the same tower, deactivate ability and move to next stage.
     * Otherwise, activate the ability and allow for a sceond build. 
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

        Board board = context.getBoard();
        if (this.builtThisRound || board.isFullBuild(target)) {
            // End building if already built twice OR can not build more blocks
            GameActions newGodCard = this.updateActivity(false);
            return context.update(newGodCard, playerId)
                    .update(GameStage.ENDTURN);
        } 
        else {
            // enter second build
            GameActions newGodCard = this.getGod().updateActivity(true);
            newGodCard = new Hephaestus(newGodCard, true, target);
            return context.update(newGodCard, playerId)
                    .update(GameStage.BUILD);
        }
    }

    /**
     * Get default buildable option set. Hephaestus can build twice on the same place.
     * If has previously built in this round, it should be the only valid option
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
        else {  // can only build on the same space
            return Arrays.asList(builtAt);
        }
    }

    /**
     * Hephaestus can choose to skip the second build. Deactivate the ability and 
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

