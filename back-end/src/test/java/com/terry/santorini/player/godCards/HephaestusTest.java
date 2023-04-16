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

public class HephaestusTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(new String[]{"Hephaestus", "Human"});
    }

    @Test
    public void testPossibleSecondBuild() {
        game = game.build(0);

        assertEquals(game.getStage(), GameStage.BUILD);
    }

    @Test
    public void testAbilityActiveAfterFirstBuild() {
        game = game.build(0);

        GameActions hephaestus = game.getActions(0);
        assertTrue(hephaestus.isAbilityActive());
    }

    @Test
    public void testSceondBuildHasOnlyOneOption() {
        game = game.build(0);

        GameActions hephaestus = game.getActions(0);
        List<Integer> options = hephaestus.getValidBuilds(game);
        
        assertEquals(options, Arrays.asList(0));
    }

    @Test
    public void testAbilityDeactivateAfterSecondBuild() {
        game = game.build(0)
                .build(0);

        GameActions hephaestus = game.getActions(0);
        assertFalse(hephaestus.isAbilityActive());
    }

    @Test
    public void testAbilityDeactivateAfterSkipBuild() {
        game = game.build(0)
                .useAbility();

        GameActions hephaestus = game.getActions(0);
        assertFalse(hephaestus.isAbilityActive());
    }

    @Test
    public void testNoSecondBuildIfFullBuild() {
        Board board = game.getBoard()
                        .buildBlock(0)
                        .buildBlock(0);
        game = game.update(board)
                    .build(0); // full build at 0

        assertEquals(game.getStage(), GameStage.ENDTURN);
    }

    // immutability tests

    @Test
    public void testImmutableBuildTrackers() {
        GameActions hephaestus = game.getActions(0)
                                    .updateFocus(1); // near the build
        game = hephaestus.build(game, 0, 0);
        // game is updated but god card is not
        
        List<Integer> options = hephaestus.getValidBuilds(game);
        assertEquals(options.size(), 5); 
    }
}
