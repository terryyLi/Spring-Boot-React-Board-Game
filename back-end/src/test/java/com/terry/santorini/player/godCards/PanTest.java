// CHECKSTYLE:OFF
package com.terry.santorini.player.godCards;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;
import com.terry.santorini.player.GameActions;

public class PanTest {
    private Game game;

    @Before
    public void setUp() {
        game = new Game(new String[]{"Pan", "Human"});
    }

    @Test
    public void testWinByReachFullTower() {
        game = game.build(0)
                    .build(0)
                    .build(0);
        GameActions pan = game.getActions(0)
                            .updateFocus(1);
        // move from 1 to 0
        game = pan.move(game, 0, 0);
        
        assertEquals(game.getStage(), GameStage.END);
    }

    @Test
    public void testWinByMoveDownTwoLevels() {
        game = game.build(0)
                    .build(0);
        GameActions pan = game.getActions(0)
                            .updateFocus(0);
        // move from 0 to 1
        game = pan.move(game, 0, 1);
        
        assertEquals(game.getStage(), GameStage.END);
    }
}
