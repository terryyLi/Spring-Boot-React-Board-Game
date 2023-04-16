// CHECKSTYLE:OFF
package com.terry.santorini.integration;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.terry.santorini.board.Board;
import com.terry.santorini.game.Game;
import com.terry.santorini.game.GameStage;

public class GodCardIntegrationTest extends IntegrationTest {
    
    @Test
    public void testMatchUpMinotaurAndPan() {
        Game game = new Game(new String[]{"Minotaur", "Pan"});

        game = placeAllWorkers(game);
        game = createTowerAt(game, fromVec(0, 0), 1);
        game = createTowerAt(game, fromVec(1, 0), 2);
        
        // try to push pan off level-2 tower
        game = game.select(fromVec(0, 0));
        List<Integer> validMoves = game.getValidOptions();
        assertTrue(validMoves.contains(fromVec(1, 0)));

        game = game.move(fromVec(1,0));
        Board board = game.getBoard();
        assertEquals(board.getOccupantId(fromVec(2, 0)), 1);    // pan is pushed off tower
        assertNotEquals(game.getStage(), GameStage.END);                   // game not over
    }

    @Test
    public void testMatchUpAthenaAndMinotaur() {
        Game game = new Game(new String[]{"Athena", "Minotaur"});

        game = placeAllWorkers(game);
        game = createTowerAt(game, fromVec(1, 1), 1);
        
        // athena move up and trigger ability
        game = game.select(fromVec(0, 0))
                .move(fromVec(1, 1))
                .build(0)
                .newRound();
        // Minotaur couldn't push because banned from moving up
        game = game.select(fromVec(1, 0));

        List<Integer> validMoves = game.getValidOptions();
        assertFalse(validMoves.contains(fromVec(1, 1)));
    }

    @Test
    public void testMatchUpAthenaAndArtemis() {
        Game game = new Game(new String[]{"Athena", "Artemis"});

        game = placeAllWorkers(game);
        game = createTowerAt(game, fromVec(0, 1), 1);
        game = createTowerAt(game, fromVec(1, 1), 1);
        
        // athena move up and trigger ability
        game = game.select(fromVec(0, 0))
                .move(fromVec(0, 1))
                .build(0)
                .newRound();
        // Artemis couldn't move up
        game = game.select(fromVec(1, 0));
        List<Integer> validMoves = game.getValidOptions();
        assertEquals(validMoves, Arrays.asList(fromVec(2, 0),fromVec(2, 1)));
        // Artemis second move also couldn't move up
        game = game.move(fromVec(2, 0));
        validMoves = game.getValidOptions();
        assertFalse(validMoves.contains(fromVec(1, 1)));
    }

    @Test
    public void testMatchUpAthenaAndApollo() {
        Game game = new Game(new String[]{"Athena", "Apollo"});

        game = placeAllWorkers(game);
        game = createTowerAt(game, fromVec(1, 1), 1);
        
        // athena move up and trigger ability
        game = game.select(fromVec(0, 0))
                .move(fromVec(1, 1))
                .build(0)
                .newRound();
        // Apollo couldn't swap because banned from moving up
        game = game.select(fromVec(1, 0));

        List<Integer> validMoves = game.getValidOptions();
        assertFalse(validMoves.contains(fromVec(1, 1)));
    }

    @Test
    public void testMatchUpApolloAndPan() {
        Game game = new Game(new String[]{"Apollo", "Pan"});

        game = placeAllWorkers(game);
        game = createTowerAt(game, fromVec(1, 0), 2);
        
        // can not swap with pan at level-2 tower, not climbable
        game = game.select(fromVec(0, 0));
        List<Integer> validMoves = game.getValidOptions();
        assertFalse(validMoves.contains(fromVec(1, 0)));
    }

    @Test
    public void testMatchUpAthenaAndPrometheus() {
        Game game = new Game(new String[]{"Athena", "Prometheus"});

        game = placeAllWorkers(game);
        game = createTowerAt(game, fromVec(0, 1), 1);
        game = createTowerAt(game, fromVec(1, 1), 1);
        
        // athena move up and trigger ability
        game = game.select(fromVec(0, 0))
                .move(fromVec(0, 1))
                .build(0)
                .newRound();
        // Prometheus couldn't move up, even without the first build
        game = game.select(fromVec(1, 0));
        List<Integer> validMoves = game.getValidOptions();
        assertEquals(validMoves, Arrays.asList(fromVec(2, 0),fromVec(2, 1)));
        // Prometheus can still build on those spaces
        game = game.useAbility();
        List<Integer> validBuilds = game.getValidOptions();
        assertEquals(validBuilds, Arrays.asList(
            fromVec(0,0), fromVec(2, 0), fromVec(1, 1), fromVec(2, 1)
        ));
    }
    
}
