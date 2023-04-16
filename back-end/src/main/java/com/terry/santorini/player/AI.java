package com.terry.santorini.player;

import java.util.List;
import java.util.Random;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.godCards.GodCard;

public class AI extends GodCard {
    private static final int DEPTH = 4;
    private static final int TOWER_SCORE = 5;
    private static final int WIN_SCORE = 50;

    public AI(GameActions basicActions) {
        super(basicActions);
    }

    @Override
    public String getGodDescription() {
        return super.getGodDescription() + " (AI)";
    }

    @Override
    public String getAbilityDescription() {
        return "AUTO";
    }

    @Override
    public boolean isAbilityActive() {
        return true;
    }

    @Override
    public GodCard updateFocus(int newFocus) {
        return new AI(this.getGod().updateFocus(newFocus));
    }

    @Override
    public GodCard updateActivity(boolean active) {
        return new AI(this.getGod().updateActivity(active));
    }

    @Override
    public Game afterRoundEnd(Game context, int playerId) {
        return this.getGod().afterRoundEnd(context, playerId);
    }

    @Override
    public Game placeWorker(Game context, int playerId, int pos) {
        return this.getGod().placeWorker(context, playerId, pos);
    }

    @Override
    public Game select(Game context, int playerId, int pos) {
        return this.getGod().select(context, playerId, pos);
    }

    @Override
    public Game move(Game context, int playerId, int target) {
        return this.getGod().move(context, playerId, target);
    }

    @Override
    public Game build(Game context, int playerId, int target) {
        return this.getGod().build(context, playerId, target);
    }

    @Override
    public Game ability(Game context, int playerId) {
        if (context.getStage().equals(GameStage.END)){
            // do nothing after game end
            return context.undo();
        } 
        else if (context.getStage().equals(GameStage.ENDTURN)) {
            // move to next round
            return context.undo().newRound();
        }
        else if (super.isAbilityActive()) {
            // trigger god card ability 
            Random rand = new Random();
            if (rand.nextDouble() < 1/2.0) {
                context = super.ability(context, playerId);
            }
        }

        int bestChoice = this.minimax(context, playerId, DEPTH)[1];
        context = this.play(context, playerId, bestChoice);
        return context;
    }

    private int[] minimax(Game context, int playerId, int depth) {
        List<Integer> options = context.getValidOptions();
        int currentPlayerId = context.getCurrentPlayerId();
        boolean isCurrentPlayer = playerId == currentPlayerId;
        int bestScore = isCurrentPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestChoice = -1;

        if (options.isEmpty() || depth == 0) {
            // Gameover or depth reached, evaluate score
            bestScore = evaluate(context, playerId);
        } 
        else if (context.getStage().equals(GameStage.INIT)) {
            // choose random space to place worker
            Random rand = new Random();
            bestChoice = options.get(rand.nextInt(options.size())); 
        }
        else {
            for (int pos : options) {
                // Try this move for the current "player"
                context = this.simulate(context, pos);
                if (isCurrentPlayer) {  // me (AI) is maximizing player
                    currentScore = minimax(context, playerId, depth-1)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestChoice = pos;
                    }
                } else {  // opp is minimizing
                    currentScore = minimax(context, playerId, depth-1)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestChoice = pos;
                    }
                }
                context = context.undo();
            }
        }

        return new int[] {bestScore, bestChoice};
    }

    private int evaluate(Game context, int playerId) {
        int score = 0;
        Board board = context.getBoard();
        // add large score to the winner
        if (context.getStage().equals(GameStage.END)) {
            score += (context.getCurrentPlayerId() == playerId) ? WIN_SCORE : -WIN_SCORE;
        }
        // evaluate board
        for (int origin = 0; origin < board.size(); origin++) {
            if (!board.isOccupied(origin)) {
                continue;
            }
            // maximizing score if current player's worker
            int multiplier = board.getOccupantId(origin) == playerId? 1 : -1;
            for (int neigh = 0; neigh < board.size(); neigh++) {
                if (!board.isAdjacent(origin, neigh)) {
                    continue;
                }
                else if (board.isCapped(neigh)) {
                    score -= multiplier;
                }
                else if (origin == neigh) {
                    score += board.getLevel(neigh) * TOWER_SCORE * multiplier;
                }
                else if (board.isClimbable(origin, neigh) && !board.isOccupied(neigh)){
                    score += board.getLevel(neigh) * multiplier;
                }
            }
        }

        return score;
    }

    private Game play(Game context, int playerId, int pos) {
        GameActions actions = context.getActions(playerId);
        GameStage stage = context.getStage();
        if(stage.equals(GameStage.INIT)){
            context = actions.placeWorker(context, playerId, pos);
        }
        else if (stage.equals(GameStage.SELECT)) {
            context = actions.select(context, playerId, pos);
        } 
        else if (stage.equals(GameStage.MOVE)) {
            context = actions.move(context, playerId, pos);
        }
        else if (stage.equals(GameStage.BUILD)) {
            context = actions.build(context, playerId, pos);
        }

        return context;
    }

    private Game simulate(Game context, int pos) {
        GameStage stage = context.getStage();
        if(stage.equals(GameStage.INIT)){
            context = context.initWorker(pos);
        }
        else if (stage.equals(GameStage.SELECT)) {
            context = context.select(pos);
        } 
        else if (stage.equals(GameStage.MOVE)) {
            context = context.move(pos);
        }
        else if (stage.equals(GameStage.BUILD)) {
            context = context.build(pos);
        }
        // auto end turn
        stage = context.getStage();
        if(stage.equals(GameStage.ENDTURN)){
            context = context.newRound();
        } 

        return context;
    }
    
}
