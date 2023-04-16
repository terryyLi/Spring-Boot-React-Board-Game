package com.terry.santorini.player.godCards;

import java.util.List;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public abstract class GodCard implements GameActions {
    private final GameActions basicActions;

    public GodCard(GameActions basicActions) {
        this.basicActions = basicActions;
    }

    protected GameActions getGod() {
        return basicActions;
    }

    @Override
    public abstract GodCard updateFocus(int newFocus);
    
    @Override
    public abstract GodCard updateActivity(boolean active);

    @Override
    public String getGodDescription() {
        return basicActions.getGodDescription();
    }

    @Override
    public String getAbilityDescription() {
        return basicActions.getAbilityDescription();
    }

    @Override
    public boolean isAbilityActive() {
        return basicActions.isAbilityActive();
    }

    @Override
    public int getFocus() {
        return basicActions.getFocus();
    }

    @Override
    public Game afterRoundEnd(Game context, int playerId) {
        GameActions newGodCard = this.updateFocus(-1);
        return context.update(newGodCard, playerId);
    }

    @Override
    public Game placeWorker(Game context, int playerId, int pos) {
        return basicActions.placeWorker(context, playerId, pos);
    }
    
    @Override
    public Game select(Game context, int playerId, int pos) {
        GameActions newGodCard = this.updateFocus(pos);
        return context.update(newGodCard, playerId)
                    .update(GameStage.MOVE);
    }
    
    @Override
    public Game move(Game context, int playerId, int target) {
        Board board = context.getBoard();
        board = board.move(this.getFocus(), target);
        GameActions newGodCard = this.updateFocus(target); 

        context = context.update(newGodCard, playerId)
                    .update(board)
                    .update(GameStage.BUILD);

        // check win condition
        return this.checkWinByMove(context, playerId, this.getFocus(), target);
    }

    @Override
    public Game build(Game context, int playerId, int target) {
        return basicActions.build(context, playerId, target);
    }

    @Override
    public Game ability(Game context, int playerId) {
        return basicActions.ability(context, playerId);
    }

    @Override
    public Game checkWinByMove(Game context, int playerId, int from, int to) {
        return basicActions.checkWinByMove(context, playerId, from, to);
    }

    @Override
    public List<Integer> getValidWorkerPlacement(Game context) {
        return basicActions.getValidWorkerPlacement(context);
    }

    @Override
    public List<Integer> getValidSelections(Game context) {
        return basicActions.getValidSelections(context);
    }

    @Override
    public List<Integer> getValidMoves(Game context) {
        return basicActions.getValidMoves(context);
    }

    @Override
    public List<Integer> getValidBuilds(Game context) {
        return basicActions.getValidBuilds(context);
    }

    @Override
    public List<Integer> banOpponentMoves(Game context, List<Integer> validOptions, int source) {
        return basicActions.banOpponentMoves(context, validOptions, source);
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

