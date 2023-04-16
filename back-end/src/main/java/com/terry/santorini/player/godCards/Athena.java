package com.terry.santorini.player.godCards;

import java.util.List;
import java.util.stream.Collectors;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.player.GameActions;

public class Athena extends GodCard {
    private final boolean movedUpThisRound; // whether Athena moved up in this round
    private final boolean movedUpLastRound; // whether Athena moved up in last round

    public Athena(GameActions god) {
        super(god);
        movedUpThisRound = false;
        movedUpLastRound = false;
    }

    public Athena(GameActions god, boolean movedUpThisRound, boolean movedUpLastRound) {
        super(god);
        this.movedUpThisRound = movedUpThisRound;
        this.movedUpLastRound = movedUpLastRound;
    }

    @Override
    public String getGodDescription() {
        return "Athena: During opponent's turn: If one of your Workers moved "+
            "up on your last turn, opponent Workers cannot move up this turn";
    }

    @Override
    public GodCard updateFocus(int newFocus) {
        GameActions god = this.getGod().updateFocus(newFocus);
        return new Athena(god, movedUpThisRound, movedUpLastRound);
    }

    @Override
    public GodCard updateActivity(boolean active) {
        GameActions god = this.getGod().updateActivity(active);
        return new Athena(god, movedUpThisRound, movedUpLastRound);
    }

    /**
     * Reset the focus to unselected state and pass on the move-up state to next round,
     * so that this information is perserved in the opponent's turn
     * 
     * @param context the game context
     * @param playerId the id of the player who is performing the action
     * 
     * @return updated {@link Game}
     */
    @Override
    public Game afterRoundEnd(Game context, int playerId) {
        GameActions newGodCard = this.getGod().updateFocus(-1);
        newGodCard = new Athena(newGodCard, false, movedUpThisRound);
        return context.update(newGodCard, playerId);
    }

    /**
     * Perform the default move. Update internal states if moved up
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
        Board board = context.getBoard();

        context = super.move(context, playerId, target);
        if (board.getLevel(target) - board.getLevel(source) > 0) {
            // if moved up this round, ban the opponent from moving up in the next round
            GameActions newGodCard = this.getGod().updateFocus(target);
            newGodCard = new Athena(newGodCard, true, movedUpLastRound);
            context = context.update(newGodCard, playerId);
        }
        return context;
    }

    /**
     * Athena can ban opponent's upward movements if she moved up last round
     * 
     * @param context the game context
     * @param validOptions the valid moves options
     * @param source the initial position
     * 
     * @return original valid options
     */
    @Override
    public List<Integer> banOpponentMoves(Game context, List<Integer> validOptions, int source) {
        if (movedUpLastRound) {
            // removing upward movements
            Board board = context.getBoard();
            int fromLevel = board.getLevel(source);
            validOptions = validOptions.stream()
                .filter(option -> board.getLevel(option) - fromLevel <= 0)
                .collect(Collectors.toList()); 
        }
        return validOptions;
        
    }

}
