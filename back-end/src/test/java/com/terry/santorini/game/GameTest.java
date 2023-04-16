// CHECKSTYLE:OFF
package com.terry.santorini.game;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.terry.santorini.board.Board;

public class GameTest {
    private Game game;

    @Before
    public void setUp() {
        // default matchup, no god cards
        game = new Game(new String[]{"Human", "Human"});
    }

    private Game placeAllWorkers(Game game) {
        return game.initWorker(0)
                    .initWorker(1)  // first player places two workers
                    .newRound()         // pass to next player
                    .initWorker(2)  
                    .initWorker(3)  // second player places two workers
                    .newRound();
    }

    private Game buildTestBoard(Game game) {
        Board board = game.getBoard();
        board = board.initWorkerFor(0, null, vec(1,1))
                    .initWorkerFor(0, null, vec(1,0)) // occupy by same player
                    .initWorkerFor(1, null, vec(1,2)) // occupy by opponent
                    .buildBlock(vec(0,1))
                    .buildBlock(vec(0,1))  // unclimbable 2-level tower
                    .buildDome(vec(0, 2)); // dome

        return game.update(board);
    }

    private int vec(int x, int y) {
        return Board.parsePosition(x, y);
    }

    @Test
    public void testFirstPlayerCanPlaceWorker() {
        assertEquals(game.getStage(), GameStage.INIT);
    }

    @Test
    public void testEndOfTurnAfterPlacingTwoWorkers() {
        game = game.initWorker(0)
                .initWorker(1);
        assertEquals(game.getStage(), GameStage.ENDTURN);
    }

    @Test
    public void testSecondPlayerCanPlaceWorker() {
        game = game.initWorker(0)
                .initWorker(1)
                .newRound();
        assertEquals(game.getStage(), GameStage.INIT);
    }

    @Test
    public void testShouldSelectAfterAllWorkersPlaced() {
        game = placeAllWorkers(game);
        assertEquals(game.getStage(), GameStage.SELECT);
    }

    @Test
    public void testShouldMoveAfterSelect() {
        game = placeAllWorkers(game)
                .select(1);
        assertEquals(game.getStage(), GameStage.MOVE);
    }

    @Test
    public void testShouldBuildAfterMove() {
        game = placeAllWorkers(game)
                .select(1)
                .move(2);
        assertEquals(game.getStage(), GameStage.BUILD);
    }

    @Test
    public void testEndOfTurnAfterBuild() {
        game = placeAllWorkers(game)
                .select(1)
                .move(2)
                .build(3);
        assertEquals(game.getStage(), GameStage.ENDTURN);
    }

    @Test 
    public void testEndAfterMoveToFullTower() {
        game = placeAllWorkers(game);
        // build a full tower
        Board board = game.getBoard()
                        .buildBlock(1)
                        .buildBlock(1)
                        .buildBlock(1);
        game = game.update(board);
        // move a worker to the full tower and win
        game = game.select(0)
                    .move(1);

        assertEquals(game.getStage(), GameStage.END);
    }

    @Test
    public void testCanNotUpdateStageAfterEnd() {
        game = game.update(GameStage.END)
                    .update(GameStage.MOVE);

        assertEquals(game.getStage(), GameStage.END);          
    }

    @Test 
    public void testCanUndoAllWayBackToBeginning() {
        game = placeAllWorkers(game)
                .select(1)
                .move(2)
                .build(3);
        game = game.undo();
        assertEquals(game.getStage(), GameStage.BUILD);    
        game = game.undo();
        assertEquals(game.getStage(), GameStage.MOVE);    
        game = game.undo();
        assertEquals(game.getStage(), GameStage.SELECT);    
        game = game.undo();
        assertEquals(game.getStage(), GameStage.INIT);   
        game = game.undo();
        assertEquals(game.getStage(), GameStage.INIT);       
        game = game.undo();
        assertEquals(game.getStage(), GameStage.INIT);       
        game = game.undo();
        assertEquals(game.getStage(), GameStage.INIT);             
    }

    @Test 
    public void testCanOnlySelectOwnedWorkers() {
        Board board = game.getBoard();
        board = board.initWorkerFor(0, null, 2)
                    .initWorkerFor(0, null, 10)
                    .initWorkerFor(1, null, 0)
                    .initWorkerFor(1, null, 1);
        game = game.update(board)
                .update(GameStage.SELECT);

        assertEquals(game.getValidOptions(), Arrays.asList(2,10));
    }

    @Test 
    public void testCanOnlyMoveToAdjacentUnoccupiedClimbableSpaces() {
        game = buildTestBoard(game)
                .select(vec(1,1));

        assertEquals(game.getValidOptions(), 
            Arrays.asList(
                vec(0,0), vec(2,0), vec(2,1), vec(2,2)
            )
        );
    }
    @Test 
    public void testCanOnlyBuildToAdjacentUnoccupiedSpaces() {
        game = buildTestBoard(game)
                .select(vec(1,1))
                .move(vec(1,1));

        assertEquals(game.getValidOptions(), 
            Arrays.asList(
                vec(0,0), vec(2,0), vec(0, 1), vec(2,1), vec(2,2)
            )
        );
    }

    // Immutability tests

    @Test
    public void testImmutableByUpdatedBoard() {
        Board board = game.getBoard().buildBlock(0);
        game.update(board);

        assertEquals(board.getLevel(0), 1);
        assertEquals(game.getBoard().getLevel(0), 0);
    }

    @Test 
    public void testImmutableByUpdatedStage() {
        game.update(GameStage.END);

        assertNotEquals(game.getStage(), GameStage.END);
    }

    @Test 
    public void testImmutableByNextRound() {
        game.nextRound();

        assertNotEquals(game.getCurrentPlayerId(), 1);
    }

    @Test 
    public void testImmutableByUpdateWinner() {
        game.updateWinner(0);

        assertNotEquals(game.getStage(), GameStage.END);
    }
}
