package com.terry.santorini.player.godCards;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class Atlas extends GodCard{
    // choice of build type; Default false (building block)
    private final boolean buildingDome;

    public Atlas(GameActions god) {
        super(god);
        buildingDome = false;
    }

    public Atlas(GameActions god, boolean buildingDome) {
        super(god);
        this.buildingDome = buildingDome;
    }

    @Override
    public String getGodDescription() {
        return "Atlas: Your Worker may build a dome at any level";
    }

    @Override
    public String getAbilityDescription() {
        if (buildingDome) {
            return "Building Dome";
        }
        return "Build Dome";
    }

    @Override
    public GodCard updateFocus(int newFocus) {
        return new Atlas(this.getGod().updateFocus(newFocus));
    }

    @Override
    public GodCard updateActivity(boolean active) {
        return new Atlas(this.getGod().updateActivity(active));
    }

    /**
     * Reset the focus to unselected state and reset the choice of build type 
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game afterRoundEnd(Game context, int playerId) {
        GameActions god = this.getGod().updateFocus(-1);
        GameActions newGodCard = new Atlas(god, false);
        return context.update(newGodCard, playerId);
    }
    
    /**
     * Perform the default move. Activate the ability while entering the BUILD stage
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
        // activate ability after move stage
        GameActions newActions = this.updateActivity(true)
                                .updateFocus(target);

        return context.update(newActions, playerId);
    }

    /**
     * Perform the desired build. Regardless of the build, deactivate ability afterward 
     * as levaing BUILD stage
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param target position of the selected worker
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game build(Game context, int playerId, int target) {
        if (this.buildingDome){
            context = this.buildDome(context, playerId, target);
        }
        else {
            context = super.build(context, playerId, target);
        }
        // deactivate ability after build stage
        GameActions newActions = this.updateActivity(false);
        return context.update(newActions, playerId); 
    }

    /**
     * Atlas can choose to build dome instead of block. 
     * Enable the dome building mode and deactive the ability afterward. 
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game ability(Game context, int playerId) {
        context = super.ability(context, playerId);
        // change build action from building block to building dome
        GameActions newActions = this.updateActivity(false);
        newActions = new Atlas(newActions, true);

        return context.update(newActions, playerId);
    }

    private Game buildDome(Game context, int playerId, int target) {
        Board board = context.getBoard();
        
        board = board.buildDome(target);
        return context.update(board)
                    .update(GameStage.ENDTURN);
    }
}

