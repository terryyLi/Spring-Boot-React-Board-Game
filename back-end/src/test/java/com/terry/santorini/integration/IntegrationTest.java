package com.terry.santorini.integration;

import static org.junit.Assert.assertEquals;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;

public class IntegrationTest {

    protected int fromVec(int x, int y) {
        return Board.parsePosition(x, y);
    }

    protected Game placeAllWorkers(Game game) {
        // both players place workers
         return game.initWorker(fromVec(0,0))
                    .initWorker(fromVec(3,4))
                    .newRound()
                    .initWorker(fromVec(1,0))
                    .initWorker(fromVec(4,4))
                    .newRound();
    }

    protected Game createTowerAt(Game game, int pos, int level) {
        // hack game for testing purposes
        Board board = game.getBoard();
        for (int i = 0; i < level; i++) {
            board = board.buildBlock(pos);
        }
        return game.update(board);
    }

    protected void assertEndGame(Game game, int winnerId) {
        assertEquals(game.getStage(), GameStage.END);
        String playerName = winnerId==0? "A":"B";
        assertEquals(game.getStageText(), 
            "Game Over. Player %s is the winner!"
            .formatted(playerName));
    }
}
