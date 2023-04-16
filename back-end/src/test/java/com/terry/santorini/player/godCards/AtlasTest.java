// CHECKSTYLE:OFF
package com.terry.santorini.player.godCards;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.game.Game;
import com.terry.santorini.player.GameActions;

public class AtlasTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(new String[]{"Atlas", "Human"});
    }

    @Test
    public void testBuildBlockAsUsual() {
        GameActions atlas = game.getActions(0)
                                .updateFocus(0);  // select at pos 0
        game = atlas.build(game, 0, 1);

        assertEquals(game.getBoard().getLevel(1), 1);
    }

    @Test
    public void testBuildDomeAtGroundLevel() {
        GameActions atlas = game.getActions(0)
                                .updateFocus(0);  // select at pos 0
        // use ability
        game = atlas.ability(game, 0);
        // build
        atlas = game.getActions(0);
        game = atlas.build(game, 0, 1);

        assertEquals(game.getBoard().getLevel(1), 0);
        assertTrue(game.getBoard().isCapped(1));
    }

    @Test
    public void testBuildDomeAt2LevelTower() {
        game = game.build(1)
                .build(1)
                .select(0)  // select at pos 0
                .useAbility();  // use ability
        GameActions atlas = game.getActions(0);
        game = atlas.build(game, 0, 1);

        assertEquals(game.getBoard().getLevel(1), 2);
        assertTrue(game.getBoard().isCapped(1));
    }

    @Test
    public void testBuildDomeAtFullTowerWithAbilityOn() {
        game = game.build(1)
                .build(1)
                .build(1)
                .select(0)  // select at pos 0
                .useAbility();  // use ability
        GameActions atlas = game.getActions(0);
        game = atlas.build(game, 0, 1);

        assertTrue(game.getBoard().isFullBuild(1));
        assertTrue(game.getBoard().isCapped(1));
    }

    @Test
    public void testBuildDomeAtFullTowerWithAbilityOff() {
        game = game.build(1)
                .build(1)
                .build(1)
                .select(0);  // select at pos 0

        GameActions atlas = game.getActions(0);
        game = atlas.build(game, 0, 1);

        assertTrue(game.getBoard().isFullBuild(1));
        assertTrue(game.getBoard().isCapped(1));
    }

    @Test
    public void testAbilityActiveAfterMove() {
        game = game.initWorker(0)
                .select(0)
                .move(1);

        GameActions atlas = game.getActions(0);
        assertTrue(atlas.isAbilityActive());
    }

    @Test
    public void testAbilityDeactiveAfterUse() {
        game = game.initWorker(0)
                .select(0)
                .move(1)
                .useAbility();

        GameActions atlas = game.getActions(0);
        assertFalse(atlas.isAbilityActive());
    }

    @Test
    public void testAbilityDeactiveAfterBuild() {
        game = game.initWorker(0)
                .select(0)
                .move(1)
                .build(2);

        GameActions atlas = game.getActions(0);
        assertFalse(atlas.isAbilityActive());
    }

    // immutability tests
    @Test
    public void testImmutableBuildChoice() {
        GameActions atlas = game.getActions(0)
                                .updateFocus(0);  // select at pos 0
        // use ability. game updated but not the god card
        game = atlas.ability(game, 0);
        // build
        game = atlas.build(game, 0, 1);
        // built a block, not dome
        assertEquals(game.getBoard().getLevel(1), 1);
        assertFalse(game.getBoard().isCapped(1));
    }
}
