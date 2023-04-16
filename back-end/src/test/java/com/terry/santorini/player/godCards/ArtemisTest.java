// CHECKSTYLE:OFF
package com.terry.santorini.player.godCards;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class ArtemisTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(new String[]{"Artemis", "Human"});
    }

    @Test
    public void testAbilityInitiallyNotActive() {
        GameActions artemis = game.getActions(0);

        assertFalse(artemis.isAbilityActive());
    }

    @Test
    public void testPossibleSecondMove() {
        GameActions artemis = game.initWorker(0)
                                .getActions(0);
        game = artemis.updateFocus(0)   // select position at 0
                    .move(game, 0, 1);

        assertEquals(game.getStage(), GameStage.MOVE);
    }

    @Test
    public void testAbilityActivatedAfterFirstBuild() {
        game = game.initWorker(0)
                    .select(0)
                    .move(1);
        GameActions artemis = game.getActions(0);

        assertTrue(artemis.isAbilityActive());
    }

    @Test
    public void testStartBuildingAfterSecondMove() {
        GameActions artemis = game.initWorker(0)
                                .getActions(0);
        game = artemis.updateFocus(0)   // select position at 0
                    .move(game, 0, 1);
        artemis = game.getActions(0);
        game = artemis.move(game, 0, 2);

        assertEquals(game.getStage(), GameStage.BUILD);
    }

    @Test
    public void testAbilityDeactivatedAfterSecondMove() {
        game = game.initWorker(0)
                    .select(0)
                    .move(1)
                    .move(2);
        GameActions artemis = game.getActions(0);

        assertFalse(artemis.isAbilityActive());
    }

    @Test
    public void testAbilityDeactivatedAfterSkipMove() {
        game = game.initWorker(0)
                    .select(0)
                    .move(1)
                    .useAbility();
        GameActions artemis = game.getActions(0);

        assertFalse(artemis.isAbilityActive());
    }

    @Test
    public void testCanNotMoveBackToInitialSpace() {
        game = game.initWorker(0)
                    .select(0)
                    .move(1);
        GameActions demeter = game.getActions(0); // moved from 0 to 1

        List<Integer> options = demeter.getValidMoves(game);
        assertFalse(options.contains(0));   // can't move back to 0 
        assertEquals(options.size(), 4);
    }

    // immutability tests

    @Test
    public void testImmutableMovementTrackers() {
        GameActions artemis = game.initWorker(0)
                                .select(0)
                                .getActions(0);
        game = artemis.move(game, 0, 1);  // moved from 0 to 1
        // game is updated but artemis is not
        artemis = artemis.updateFocus(1);
        List<Integer> options = artemis.getValidMoves(game);
        // old artemis doesn't know about first move     
        assertTrue(options.contains(0));  
        assertEquals(options.size(), 5); 
    }
}
