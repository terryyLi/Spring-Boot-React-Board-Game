// CHECKSTYLE:OFF
package com.terry.santorini.player.godCards;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.player.GameActions;

public class ApolloTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(new String[]{"Apollo", "Human"});
    }

    private int fromVec(int x, int y) {
        return Board.parsePosition(x, y);
    }

    @Test
    public void testSwapPositionWithOpponent() {
        int pos1 = fromVec(0, 0);
        int pos2 = fromVec(0, 1);
        Board board = game.getBoard();
        board = board.initWorkerFor(0, null, pos1)
                    .initWorkerFor(1, null, pos2);
        game = game.update(board);

        GameActions minotaur = game.getActions(0)
                                .updateFocus(pos1);    // select pos1
        game = minotaur.move(game, 0, pos2);
        board = game.getBoard();

        assertEquals(board.getOccupantId(pos1), 1);
        assertEquals(board.getOccupantId(pos2), 0);
    }

    @Test
    public void testCanNotGoMoreLevelsBySwap () {
        int pos1 = fromVec(0, 0);
        int pos2 = fromVec(1, 1);
        Board board = game.getBoard();
        board = board.initWorkerFor(0, null, pos1)
                    .initWorkerFor(1, null, pos2)
                    .buildBlock(pos2)
                    .buildBlock(pos2); // pos2 is not climbable from pos1
        game = game.update(board);

        GameActions minotaur = game.getActions(0)
                                .updateFocus(pos1);    // select pos1
        
        List<Integer> options = minotaur.getValidMoves(game);
        assertFalse(options.contains(pos2));
        assertEquals(options, Arrays.asList(fromVec(1, 0), fromVec(0, 1)));
    }

    @Test
    public void testCanGoDownMoreLevelsBySwap () {
        int pos1 = fromVec(0, 0);
        int pos2 = fromVec(1, 1);
        Board board = game.getBoard();
        board = board.initWorkerFor(0, null, pos1)
                    .initWorkerFor(1, null, pos2)
                    .buildBlock(pos1)
                    .buildBlock(pos1); // lv2 tower at pos1
        game = game.update(board);

        GameActions minotaur = game.getActions(0)
                                .updateFocus(pos1);    // select pos1
        
        List<Integer> options = minotaur.getValidMoves(game);
        assertTrue(options.contains(pos2));
        assertEquals(options, Arrays.asList(fromVec(1, 0), fromVec(0, 1), pos2));
    }
}