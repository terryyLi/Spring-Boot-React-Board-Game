// CHECKSTYLE:OFF
package com.terry.santorini.player.godCards;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class HermesTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(new String[]{"Hermes", "Human"});
    }

    @Test
    public void testAbilityActiveAfterSelect() {
        game = game.initWorker(0)
                .select(0);

        GameActions hermes = game.getActions(0);
        assertTrue(hermes.isAbilityActive());
    }

    @Test
    public void testSelectBuilderAfterMove() {
        game = game.initWorker(0)
                .select(0)
                .useAbility();  // skip move

        assertEquals(game.getStage(), GameStage.SELECT);
    }

    @Test
    public void testCanMoveFreelyIfNotMovedVertically() {
        game = game.initWorker(0)
                .select(0)
                .move(1)   // move to 1
                .move(0);  // move back to 0

        assertEquals(game.getStage(), GameStage.MOVE);
    }

    @Test
    public void testEndMoveIfMovedVertically() {
        game = game.initWorker(0)
                .select(0)
                .build(2)
                .move(1)   // move to 1
                .move(2);  // move up to 2; end moving

        assertEquals(game.getStage(), GameStage.SELECT);
    }

    @Test
    public void testBuildAfterMoveAndSelect() {
        game = game.initWorker(0)
                .select(0)
                .build(1)
                .move(1)    // move up to 1; end moving
                .select(1);    // select builder at 1

        assertEquals(game.getStage(), GameStage.BUILD);
    }

    @Test
    public void testSwitchWorkerAfterFirstMove() {
        game = game.initWorker(0)
                .initWorker(10)
                .select(0)      // select worker at 0
                .move(1)     // move to 1
                .useAbility();      // switch worker

        GameActions hermes = game.getActions(0);
        assertEquals(hermes.getFocus(), 10);
    }

    // immutability tests

    @Test
    public void testImmutableMoveTrakcer() {
        game = game.initWorker(0)
                .select(0);
        GameActions hermes = game.getActions(0);

        game = hermes.move(game, 0, 1);
        game = hermes.ability(game, 0);
        // the ability used should be "switch worker" after first move
        // but the god card was not updated, so the ability was instead "skip move"

        assertNotEquals(game.getStage(), GameStage.MOVE);
    }
}
