// CHECKSTYLE:OFF
package com.terry.santorini.integration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;

public class BaseIntegrationTest extends IntegrationTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game();
    }

    @Test
    public void test2RoundsGamePlay() {
        game = placeAllWorkers(game);
        
        // p1 play a complete round
        game = game.select(fromVec(0,0))
                .move(fromVec(0,1))
                .build(fromVec(0,0))
                .newRound();
        // p2 player a complete round
        game = game.select(fromVec(1,0))
                .move(fromVec(1,1))
                .build(fromVec(1,0))
                .newRound();

        Board board = game.getBoard();
        assertEquals(board.getOccupantId(fromVec(0, 1)), 0);
        assertEquals(board.getOccupantId(fromVec(1, 1)), 1);
        assertEquals(board.getLevel(fromVec(1, 0)), 1);
        assertEquals(board.getLevel(fromVec(0, 0)), 1);
    }

    @Test
    public void testWinGame(){
        // speed up the game by adding a few towers using hack
        game = createTowerAt(game, fromVec(0, 0), 3);
        game = createTowerAt(game, fromVec(0,1), 2);    // p1 at lv2
        game = createTowerAt(game, fromVec(1,1), 2);    // p2 at lv2

        // p1 win by move
        game = game.select(fromVec(0, 1))
                .move(fromVec(0, 0));

        assertEndGame(game, 0);

        // undo and p2 win by move
        game = game.undo()
                .select(fromVec(0, 1))
                .move(fromVec(0, 2))
                .build(fromVec(0, 3))
                .newRound();
        game = game.select(fromVec(1, 1))
                .move(fromVec(0, 0));

        assertEndGame(game, 1);
    }
}