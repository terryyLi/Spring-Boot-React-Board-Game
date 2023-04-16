// CHECKSTYLE:OFF
package com.terry.santorini.player.godCards;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class PrometheusTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(new String[]{"Prometheus", "Human"});
    }

    @Test
    public void testAbilityActiveAfterSelect() {
        game = game.initWorker(0)
            .select(0);

        GameActions prometheus = game.getActions(0);
        assertTrue(prometheus.isAbilityActive());
    }

    @Test
    public void testAbilityDeactiveAfterMove() {
        game = game.initWorker(0)
            .select(0)
            .move(1);

        GameActions prometheus = game.getActions(0);
        assertFalse(prometheus.isAbilityActive());    
    }

    @Test
    public void testCanBuildAfterUseAbility() {
        game = game.initWorker(0)
            .select(0)
            .useAbility();

        assertEquals(game.getStage(), GameStage.BUILD);
    }

    @Test
    public void testNormallyCanMoveUp() {
        game = game.build(1);
        game = game.initWorker(0)
                    .select(0);

        GameActions prometheus = game.getActions(0);
        List<Integer> options = prometheus.getValidMoves(game);

        assertTrue(options.contains(1));  
    }

    @Test
    public void testCanNotMoveUpAfterAbilityAndBuild() {
        game = game.build(1);
        game = game.initWorker(0)
                    .select(0)
                    .useAbility()
                    .build(24);

        GameActions prometheus = game.getActions(0);
        List<Integer> options = prometheus.getValidMoves(game);

        assertFalse(options.contains(1)); 
        assertEquals(options.size(), 2);  
    }

    @Test
    public void testShouldMoveAfterAbilityAndBuild() {
        game = game.initWorker(0)
                    .select(0)
                    .useAbility()
                    .build(1);
        assertEquals(game.getStage(), GameStage.MOVE);
    }

    // immutability
    @Test
    public void testImmutableBuildTrackers() {
        game = game.build(0);
        game = game.initWorker(1)
                    .select(1)
                    .useAbility();
        GameActions prometheus = game.getActions(0)
                            .updateFocus(1); // near the build
        game = prometheus.build(game, 0, 24);
        // game is updated but god card is not
        // prometheus = game.getActions(0);
        List<Integer> options = prometheus.getValidMoves(game);
        assertTrue(options.contains(0));  
        assertEquals(options.size(), 5); 
    }

}
