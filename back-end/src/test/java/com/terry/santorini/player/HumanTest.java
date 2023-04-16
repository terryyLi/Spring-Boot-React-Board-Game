// CHECKSTYLE:OFF
package com.terry.santorini.player;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.game.Game;
import com.terry.santorini.board.Board;

public class HumanTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(new String[]{"Human", "Human"});
    }

    private int fromVec(int x, int y) {
        return Board.parsePosition(x, y);
    }

    @Test 
    public void testInitiallyCanPlaceWorkerAnyWhere() {
        GameActions human = game.getActions(0);
        List<Integer> options = human.getValidWorkerPlacement(game);

        assertEquals(options.size(), game.getBoard().size());
    }

    @Test
    public void testCanNotPlaceWorkerAtOccupiedSpaces() {
        game = game.initWorker(0);

        GameActions human = game.getActions(0);
        List<Integer> options = human.getValidWorkerPlacement(game);

        assertEquals(options.size(), game.getBoard().size()-1);
    }

    @Test
    public void testCanOnlySelectOwnWorkersPosition() {
        game = game.initWorker(0)
                    .nextRound()
                    .initWorker(1); 

        GameActions human = game.getActions(1);
        List<Integer> options = human.getValidSelections(game);

        assertEquals(options, Arrays.asList(1));
    }

    @Test
    public void testCanNotMoveIfNotAdjacent() {
        GameActions human = game.getActions(0)
                            .updateFocus(fromVec(0,0));

        List<Integer> options = human.getValidMoves(game);
        
        assertFalse(options.contains(fromVec(1, 2)));
    }

    @Test
    public void testCanNotMoveIfOccupied() {
        int occupying = fromVec(1,1);
        game = game.initWorker(occupying);
        GameActions human = game.getActions(0)
                            .updateFocus(fromVec(0,0));

        List<Integer> options = human.getValidMoves(game);
        
        assertFalse(options.contains(occupying));
    }

    @Test
    public void testCanNotMoveIfCapped() {
        int cappedTower = fromVec(1,1);
        Board board = game.getBoard()
                        .buildDome(cappedTower);

        game = game.update(board);
        GameActions human = game.getActions(0)
                            .updateFocus(fromVec(0,0));

        List<Integer> options = human.getValidMoves(game);
        
        assertFalse(options.contains(cappedTower));
    }

    @Test
    public void testCanNotMoveIfUnclimbable() {
        int lv2Tower = fromVec(1,1);
        Board board = game.getBoard()
                        .buildBlock(lv2Tower)
                        .buildBlock(lv2Tower);

        game = game.update(board);
        GameActions human = game.getActions(0)
                            .updateFocus(fromVec(0,0));

        List<Integer> options = human.getValidMoves(game);
        
        assertFalse(options.contains(lv2Tower));
    }

    @Test
    public void testCanBuildEvenIfUnclimbable() {
        int lv2Tower = fromVec(1,1);
        Board board = game.getBoard()
                        .buildBlock(lv2Tower)
                        .buildBlock(lv2Tower);

        game = game.update(board);
        GameActions human = game.getActions(0)
                            .updateFocus(fromVec(0,0));

        List<Integer> options = human.getValidBuilds(game);
        
        assertTrue(options.contains(lv2Tower));
    }

    // immutable tests

    @Test
    public void testImmutableFocus() {
        GameActions human = game.getActions(0);
        human.updateFocus(fromVec(0,0));

        assertEquals(human.getFocus(), -1);
    }

    @Test
    public void testImmutableAbilityActive() {
        GameActions human = game.getActions(0);
        human.updateActivity(true);

        assertEquals(human.isAbilityActive(), false);
    }

}
