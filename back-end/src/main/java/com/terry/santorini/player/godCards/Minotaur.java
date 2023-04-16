package com.terry.santorini.player.godCards;

import java.util.ArrayList;
import java.util.List;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class Minotaur extends GodCard {

    public Minotaur(GameActions god) {
        super(god);
    }

    @Override
    public String getGodDescription() {
        return "Minotaur: Your Worker may move into an opponent Worker's space, if their Worker can be " + 
                "forced one space straight backwards to an unoccupied space at any level";
    }

    @Override
    public GodCard updateFocus(int newFocus) {
        return new Minotaur(this.getGod().updateFocus(newFocus));
    }

    @Override
    public GodCard updateActivity(boolean active) {
        return new Minotaur(this.getGod().updateActivity(active));
    }

    /**
     * Move the selected worker to the designated position. If a opponent's worker is
     * occupying the destination, force it to move by knocking it back.
     * This method assumes both movements are possible and valid.
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * @param target position of the selected worker
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game move(Game context, int playerId, int target) {
        Board board = context.getBoard();
        int opponentId = context.getOpponentId();
        int source = super.getFocus();
        // if moved into opponent's space, knock it back
        if (board.getOccupantId(target) == opponentId){
            int knockBackTo = board.inferNextPosition(source, target);
            board = board.move(target, knockBackTo);
        }

        board = board.move(this.getFocus(), target);
        GameActions newGodCard = this.updateFocus(target); 

        context = context.update(newGodCard, playerId)
                    .update(board)
                    .update(GameStage.BUILD);

        // check win condition
        return this.checkWinByMove(context, playerId, this.getFocus(), target);
    }

    /**
     * Get all default movable options. Minotaur may enter an opponent's space, 
     * if the opponent's worker can be pushed to an unoccupied and uncapped space, 
     * add that position to the list
     * 
     * @param context the game context
     * 
     * @return list of valid options
     */
    @Override
    public List<Integer> getValidMoves(Game context) {
        List<Integer> validOptions = new ArrayList<Integer>();
        Board board = context.getBoard();
        int curPos = super.getFocus();
        int opponentId = context.getOpponentId();
        
        for (int newPos = 0; newPos < board.size(); newPos++) {
            
            if (newPos != curPos &&
                board.isAdjacent(curPos, newPos) &&
                board.isClimbable(curPos, newPos) &&
                !board.isCapped(newPos) &&
                (!board.isOccupied(newPos) || 
                canKnockBackOpponentWorker(board, opponentId, curPos, newPos))
            ) {
                validOptions.add(newPos);
            }     
        }
        return validOptions;
    }

    private boolean canKnockBackOpponentWorker(Board board, int opponentId, int curPos, int newPos) {
        if (board.getOccupantId(newPos) != opponentId) {
            return false;
        }
        int knockBackTo = board.inferNextPosition(curPos, newPos);
        return knockBackTo >= 0 && 
            !board.isCapped(knockBackTo) && 
            !board.isOccupied(knockBackTo);
    }
}

