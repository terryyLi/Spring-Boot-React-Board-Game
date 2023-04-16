// CHECKSTYLE:OFF
package com.terry.santorini.player.godCards;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class DemeterTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(new String[]{"Demeter", "Human"});
    }

    @Test
    public void testPossibleSecondBuildAfterBuildBlock() {
        GameActions demeter = game.getActions(0);
        game = demeter.build(game, 0, 0);

        assertEquals(game.getStage(), GameStage.BUILD);
    }

    @Test
    public void testAbilityActivatedAfterFirstBuild() {
        game = game.build(0);
        GameActions demeter = game.getActions(0);

        assertTrue(demeter.isAbilityActive());
    }

    @Test
    public void testEndOfTurnAfterSecondBuild() {
        GameActions demeter = game.getActions(0);
        game = demeter.build(game, 0, 0);
        demeter = game.getActions(0);
        game = demeter.build(game, 0, 1);
        
        assertEquals(game.getStage(), GameStage.ENDTURN);
    }

    @Test
    public void testAbilityDeactivatedAfterSecondBuild() {
        game = game.build(0)
                    .build(1);
        GameActions demeter = game.getActions(0);

        assertFalse(demeter.isAbilityActive());
    }

    @Test
    public void testAbilityDeactivatedAfterSkipBuild() {
        game = game.build(0)
                    .useAbility();
        GameActions demeter = game.getActions(0);

        assertFalse(demeter.isAbilityActive());
    }

    @Test
    public void testCanNotBuildOnSamePlace() {
        game = game.build(0);
        GameActions demeter = game.getActions(0)
                                .updateFocus(1); // near the build
        
        List<Integer> options = demeter.getValidBuilds(game);
        assertFalse(options.contains(0));   // can't build at 0 again
        assertEquals(options.size(), 4);
    }

    // immutability tests

    @Test
    public void testImmutableByUpdateAbilityStatus() {
        GameActions demeter = game.getActions(0);
        demeter.updateActivity(true);

        assertFalse(demeter.isAbilityActive());
    }

    @Test
    public void testImmutableBuildTrackers() {
        GameActions demeter = game.getActions(0)
                            .updateFocus(1); // near the build
        game = demeter.build(game, 0, 0);
        // game is updated but demeter is not
        
        List<Integer> options = demeter.getValidBuilds(game);
        assertTrue(options.contains(0));  
        assertEquals(options.size(), 5); 
    }
}
