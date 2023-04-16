// CHECKSTYLE:OFF
package com.terry.santorini.player.godCards;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class MinotaurTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(new String[]{"Minotaur", "Human"});
    }

    private int fromVec(int x, int y) {
        return Board.parsePosition(x, y);
    }

    @Test
    public void testPushOpponentVertically() {
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

        assertEquals(board.getOccupantId(pos1), -1);
        assertEquals(board.getOccupantId(pos2), 0);
        assertEquals(board.getOccupantId(fromVec(0, 2)), 1); // player 1 is pushed down
    }

    @Test
    public void testPushOpponentDiagonally () {
        int pos1 = fromVec(0, 0);
        int pos2 = fromVec(1, 1);
        Board board = game.getBoard();
        board = board.initWorkerFor(0, null, pos1)
                    .initWorkerFor(1, null, pos2);
        game = game.update(board);

        GameActions minotaur = game.getActions(0)
                                .updateFocus(pos1);    // select pos1
        game = minotaur.move(game, 0, pos2);
        board = game.getBoard();

        assertEquals(board.getOccupantId(pos1), -1);
        assertEquals(board.getOccupantId(pos2), 0);
        assertEquals(board.getOccupantId(fromVec(2, 2)), 1); // player 1 is pushed bottom-right
    }

    @Test
    public void testCanNotPushOpponentOutOfBoard () {
        int pos1 = fromVec(1, 1);
        int pos2 = fromVec(0, 0);
        Board board = game.getBoard();
        board = board.initWorkerFor(0, null, pos1)
                    .initWorkerFor(1, null, pos2);
        game = game.update(board);

        GameActions minotaur = game.getActions(0)
                                .updateFocus(pos1);    // select pos1
        
        List<Integer> options = minotaur.getValidMoves(game);
        assertFalse(options.contains(pos2));
        assertEquals(options.size(), 7);
    }

    @Test
    public void testCanNotPushOpponentToOccupiedSpace () {
        int pos1 = fromVec(0, 0);
        int pos2 = fromVec(1, 1);
        int pos3 = fromVec(2, 2);
        Board board = game.getBoard();
        board = board.initWorkerFor(0, null, pos1)
                    .initWorkerFor(1, null, pos2)
                    .initWorkerFor(1, null, pos3);
        game = game.update(board);

        GameActions minotaur = game.getActions(0)
                                .updateFocus(pos1);    // select pos1
        
        List<Integer> options = minotaur.getValidMoves(game);
        assertFalse(options.contains(pos2));
        assertEquals(options, Arrays.asList(fromVec(1, 0), fromVec(0, 1)));
    }

    @Test
    public void testCanNotPushOpponentToCappedSpace () {
        int pos1 = fromVec(0, 0);
        int pos2 = fromVec(1, 1);
        int pos3 = fromVec(2, 2);
        Board board = game.getBoard();
        board = board.initWorkerFor(0, null, pos1)
                    .initWorkerFor(1, null, pos2)
                    .buildDome(pos3);
        game = game.update(board);

        GameActions minotaur = game.getActions(0)
                                .updateFocus(pos1);    // select pos1
        
        List<Integer> options = minotaur.getValidMoves(game);
        assertFalse(options.contains(pos2));
        assertEquals(options, Arrays.asList(fromVec(1, 0), fromVec(0, 1)));
    }

    @Test
    public void testCanPushOpponentToFullTowerAndNotLose() {
        int pos1 = fromVec(0, 0);
        int pos2 = fromVec(1, 0);
        int pos3 = fromVec(2, 0);
        Board board = game.getBoard();
        board = board.initWorkerFor(0, null, pos1)
                    .initWorkerFor(1, null, pos2)
                    .buildBlock(pos3)
                    .buildBlock(pos3)
                    .buildBlock(pos3); // full tower at pos3
        game = game.update(board);

        GameActions minotaur = game.getActions(0)
                                .updateFocus(pos1);    // select pos1
        game = minotaur.move(game, 0, pos2);

        assertNotEquals(game.getStage(), GameStage.END);
        assertNotEquals(game.getCurrentPlayerId(), 1); // player 1 not win by forced to lv3 tower
    }

    @Test
    public void testCanPushPanDown2LevelsAndNotLose() {
        GameActions human = game.getActions(1);
        game = game.update(new Pan(human), 1);

        int pos1 = fromVec(0, 0);
        int pos2 = fromVec(1, 0);
        Board board = game.getBoard();
        board = board.initWorkerFor(0, null, pos1)
                    .initWorkerFor(1, null, pos2)
                    .buildBlock(pos2)
                    .buildBlock(pos2); // Lv2 tower at pos2
        game = game.update(board);

        GameActions minotaur = game.getActions(0)
                                .updateFocus(pos1);    // select pos1
        game = minotaur.move(game, 0, pos2);

        assertNotEquals(game.getStage(), GameStage.END);
        assertNotEquals(game.getCurrentPlayerId(), 1); // player 1 not win by forced down 2 levels

    }
}