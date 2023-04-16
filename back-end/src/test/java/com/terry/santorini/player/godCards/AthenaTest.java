// CHECKSTYLE:OFF
package com.terry.santorini.player.godCards;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.game.Game;
import com.terry.santorini.player.GameActions;

public class AthenaTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(new String[]{"Athena", "Human"});
    } 

    @Test
    public void testOpponentCanNotMoveUpAfterAthenaMovedUp() {
        game = game.initWorker(0)
                .select(0)          // pick worker at 0
                .build(1)
                .build(2);

        GameActions athena = game.getActions(0);
        game = athena.move(game, 0, 1); // move up from 0 to 1
        
        game = game.newRound()
                .initWorker(3)
                .select(3);          // opponent pick worker at 3
        GameActions opponent = game.getActions(1);
        
        List<Integer> options = opponent.getValidMoves(game);
        assertTrue(options.contains(2));   // pos 2 was originally climbable
        options = game.getActions(0)
                        .banOpponentMoves(game, options, 3);

        assertFalse(options.contains(2));   // cannot move up to 2
        assertEquals(options.size(), 4);
    }

    @Test
    public void testAthenaHasNoEffectIfNotMovedUp() {
        game = game.initWorker(0)
                .select(0)          // pick worker at 0
                .build(2);

        GameActions athena = game.getActions(0);
        game = athena.move(game, 0, 1); // move from 0 to 1, but not moving up
        
        game = game.newRound()
                .initWorker(3)
                .select(3);          // opponent pick worker at 3
        GameActions opponent = game.getActions(1);
        
        List<Integer> options = opponent.getValidMoves(game);

        assertTrue(options.contains(2));   // can move up to 2
        assertEquals(options.size(), 5);
    }

    // immutability tests

    @Test
    public void testImmutableMovementTrackers() {
        game = game.initWorker(0)
                .select(0)          // pick worker at 0
                .build(1)
                .build(2);

        GameActions athena = game.getActions(0);
        athena.move(game, 0, 1); // move up from 0 to 1 but didn't update
        
        game = game.newRound()
                .initWorker(3)
                .select(3);          // opponent pick worker at 3
        GameActions opponent = game.getActions(1);
        
        List<Integer> options = opponent.getValidMoves(game);

        assertTrue(options.contains(2));   // can move up to 2
        assertEquals(options.size(), 5);
    }
}
