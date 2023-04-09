package edu.cmu.cs214.backend;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPlayerTest {
    @Test
    public void moveTooFar() {
        Board board1 = new Board(5, 5, 4);
        Player player1 = new DefaultPlayer(new Worker(new Position(0, 0), 1), new Worker(new Position(1, 0), 2), board1, 1);
        boolean move = player1.move(player1.getWorker1(), new Position(2, 1));
        assertTrue(!move);
        assertEquals(player1.getWorker1().getPosition().getX(), 0);
        assertEquals(player1.getWorker1().getPosition().getY(), 0);
    }

    @Test
    public void diagnalSuccessfulMove() {
        Board board1 = new Board(5, 5, 4);
        Player player1 = new DefaultPlayer(new Worker(new Position(0, 0), 1), new Worker(new Position(1, 0), 2), board1, 1);
        boolean move = player1.move(player1.getWorker1(), new Position(1, 1));
        assertTrue(move);
        assertEquals(player1.getWorker1().getPosition().getX(), 1);
        assertEquals(player1.getWorker1().getPosition().getY(), 1);
    }

    @Test
    public void moveOutOfBound() {
        Board board1 = new Board(5, 5, 4);
        Player player1 = new DefaultPlayer(new Worker(new Position(0, 0), 1), new Worker(new Position(1, 0), 2), board1, 1);
        boolean move = player1.move(player1.getWorker1(), new Position(-1, 0));
        assertTrue(!move);
        assertEquals(player1.getWorker1().getPosition().getX(), 0);
        assertEquals(player1.getWorker1().getPosition().getY(), 0);
    }

    @Test
    public void moveOccupied() {
        Board board1 = new Board(5, 5, 4);
        Player player1 = new DefaultPlayer(new Worker(new Position(0, 0), 1), new Worker(new Position(1, 0), 2), board1, 1);
        boolean move = player1.move(player1.getWorker1(), new Position(1, 0));
        assertTrue(!move);
        assertEquals(player1.getWorker1().getPosition().getX(), 0);
        assertEquals(player1.getWorker1().getPosition().getY(), 0);
    }

    @Test
    public void moveUpFail() {
        Board board1 = new Board(5, 5, 4);
        Player player1 = new DefaultPlayer(new Worker(new Position(0, 0), 1), new Worker(new Position(1, 0), 2), board1, 1);
        player1.getBoard().incrTowerLevel(new Position(1, 0));
        player1.getBoard().incrTowerLevel(new Position(1, 0));
        boolean move = player1.move(player1.getWorker1(), new Position(1, 0));
        assertTrue(!move);
        assertEquals(player1.getWorker1().getPosition().getX(), 0);
        assertEquals(player1.getWorker1().getPosition().getY(), 0);
    }
    
    @Test
    public void moveUpSuccess() {
        Board board1 = new Board(5, 5, 4);
        Player player1 = new DefaultPlayer(new Worker(new Position(0, 0), 1), new Worker(new Position(1, 0), 2), board1, 1);
        player1.getBoard().incrTowerLevel(new Position(1, 0));
        boolean move = player1.move(player1.getWorker1(), new Position(1, 0));
        assertTrue(!move);
        assertEquals(player1.getWorker1().getPosition().getX(), 0);
        assertEquals(player1.getWorker1().getPosition().getY(), 0);
    }

    @Test
    public void moveDownSuccess() {
        Board board1 = new Board(5, 5, 4);
        Player player1 = new DefaultPlayer(new Worker(new Position(0, 0), 1), new Worker(new Position(1, 0), 2), board1, 1);
        player1.getBoard().incrTowerLevel(new Position(0, 0));
        player1.getBoard().incrTowerLevel(new Position(0, 0));
        player1.getBoard().incrTowerLevel(new Position(0, 0));
        boolean move = player1.move(player1.getWorker1(), new Position(1, 1));
        assertTrue(move);
        assertEquals(player1.getWorker1().getPosition().getX(), 1);
        assertEquals(player1.getWorker1().getPosition().getY(), 1);
    }

    @Test
    public void simplyBuild() {
        Board board1 = new Board(5, 5, 4);
        Player player1 = new DefaultPlayer(new Worker(new Position(0, 0), 1), new Worker(new Position(1, 0), 2), board1, 1);
        boolean build1 = player1.build(player1.getWorker1(), new Position(1, 1));
        assertTrue(build1);
        assertEquals(player1.getBoard().getTowerLevel(new Position(1, 1)), 1);
    }

    @Test
    public void buildOutOfBound() {
        Board board1 = new Board(5, 5, 4);
        Player player1 = new DefaultPlayer(new Worker(new Position(0, 0), 1), new Worker(new Position(1, 0), 2), board1, 1);
        boolean build1 = player1.build(player1.getWorker1(), new Position(-1, 1));
        assertTrue(!build1);
    }

    @Test
    public void buildOccupied() {
        Board board1 = new Board(5, 5, 4);
        Player player1 = new DefaultPlayer(new Worker(new Position(0, 0), 1), new Worker(new Position(1, 0), 2), board1, 1);
        boolean build1 = player1.build(player1.getWorker1(), new Position(1, 0));
        assertTrue(!build1);
        assertEquals(player1.getBoard().getTowerLevel(new Position(1, 0)), 0);
    }

    @Test
    public void moveUDefaultPlayerdWin() {
        Board board1 = new Board(5, 5, 4);
        Player player1 = new DefaultPlayer(new Worker(new Position(0, 0), 1), new Worker(new Position(1, 0), 2), board1, 1);
        player1.getBoard().incrTowerLevel(new Position(0, 0));
        player1.getBoard().incrTowerLevel(new Position(0, 0));
        player1.getBoard().incrTowerLevel(new Position(1, 1));
        player1.getBoard().incrTowerLevel(new Position(1, 1));
        player1.getBoard().incrTowerLevel(new Position(1, 1));
        boolean move = player1.move(player1.getWorker1(), new Position(1, 1));
        assertTrue(move);
        assertEquals(player1.getWorker1().getPosition().getX(), 1);
        assertEquals(player1.getWorker1().getPosition().getY(), 1);
        assertTrue(player1.isWin());
    }
}
