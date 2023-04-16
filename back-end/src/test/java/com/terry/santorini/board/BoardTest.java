// CHECKSTYLE:OFF
package com.terry.santorini.board;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class BoardTest {
    private Board board;

    @Before
    public void setUp() {
        board = new Board();
    }


    @Test
    public void testboardAlwaysHaveSize25() {
        assertEquals(board.size(), 25);
    }

    @Test 
    public void testBuildBlockIsAppliedToCorrectTarget() {
        board = board.buildBlock(6);
        assertEquals(board.getLevel(6), 1);
    }

    @Test 
    public void testBuildDomeIsAppliedToCorrectTarget() {
        board = board.buildDome(6);
        assertTrue(board.isCapped(6));
    }

    @Test
    public void testEmptyBoardHasNoWorker() {
        assertEquals(board.getNumWorkers(), 0);
    }

    @Test
    public void testCanInitWorker() {
        board = board.initWorkerFor(0, null, 0);
        assertEquals(board.getNumWorkers(), 1);
    }

    @Test
    public void testCanGetNumWorkerBasedOnPlayerId() {
        board = board.initWorkerFor(0, null, 0);

        assertEquals(board.getNumWorkers(0), 1);
        assertEquals(board.getNumWorkers(1), 0);
    }

    @Test
    public void testCanMoveWorkerOnBoard() {
        board = board.initWorkerFor(0, null, 0)
                    .move(0, 1);

        assertFalse(board.isOccupied(0));
        assertTrue(board.isOccupied(1));
    }

    @Test
    public void testCanSwapWorkersOnBoard() {
        board = board.initWorkerFor(0, null, 0)
                    .initWorkerFor(1, null, 1)
                    .swap(0, 1);

        assertEquals(board.getOccupantId(0), 1);
        assertEquals(board.getOccupantId(1), 0);
    }

    @Test
    public void test1_0AND1_1AreAdjacent() {
        int x = Board.parsePosition(1, 0);
        int y = Board.parsePosition(1, 1);

        assertTrue(board.isAdjacent(x, y));
    }
    @Test
    public void test1_0AND1_2AreNotAdjacent() {
        int x = Board.parsePosition(1, 0);
        int y = Board.parsePosition(1, 2);

        assertFalse(board.isAdjacent(x, y));
    }

    @Test
    public void test1_0AND2_1AreAdjacent() {
        int x = Board.parsePosition(1, 0);
        int y = Board.parsePosition(2, 1);

        assertTrue(board.isAdjacent(x, y));
    }

    @Test
    public void testClimbableFromL0ToL1(){
        board = board.buildBlock(3);

        assertTrue(board.isClimbable(2, 3));
    }

    @Test
    public void testNotClimbableFromL0ToL2(){
        board = board.buildBlock(3)
                    .buildBlock(3);

        assertFalse(board.isClimbable(2, 3));
    }

    @Test
    public void testCanInferPositionIfStayOnBoard(){
        int x = Board.parsePosition(1, 0);
        int y = Board.parsePosition(1, 1);
        int z = Board.parsePosition(1, 2);

        assertEquals(board.inferNextPosition(x, y), z);
    }

    @Test
    public void testCanNotInferPositionIfOutOfBoard(){
        int x = Board.parsePosition(1, 1);
        int y = Board.parsePosition(1, 0);

        assertEquals(board.inferNextPosition(x, y), -1);
    }

    // Immutablility Testing
    
    @Test 
    public void testImmutableByMove() {
        board = board.initWorkerFor(0, null, 0);
        Board newBoard = board.move(0, 1);

        assertTrue(board.isOccupied(0));
        assertTrue(newBoard.isOccupied(1));
    }

    @Test 
    public void testImmutableBySwap() {
        board = board.initWorkerFor(0, null, 0)
                .initWorkerFor(1, null, 1);
        board.swap(0, 1);
    
        assertEquals(board.getOccupantId(0), 0);
        assertEquals(board.getOccupantId(1), 1);
    }

    @Test 
    public void testImmutableByBuildBlock() {
        board = board.initWorkerFor(0, null, 0);
        board.buildBlock(1);

        assertEquals(board.getLevel(1), 0);
    }

    @Test 
    public void testImmutableByBuildDome() {
        board = board.initWorkerFor(0, null, 0);
        board.buildDome(1);

        assertFalse(board.isCapped(1));
    }
}