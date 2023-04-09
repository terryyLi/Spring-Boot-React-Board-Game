package edu.cmu.cs214.backend;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class GameTest {
    @Test
    public void testDefaultPlayerWithPan() {
        Game game1 = new Game();
        Player player1 = new DefaultPlayer(game1.getBoard(), 1);
        game1.setPlayer1(player1);
        game1.setPlayer2(new Pan(game1.getBoard(), 2));
        game1.setCurrentPlayer(player1);
        Worker worker11 = new Worker(new Position(1, 1), 1);
        Worker worker21 = new Worker(new Position(4, 1), 1);
        game1.getPlayer1().setWorker1(worker11);
        game1.getPlayer1().setWorker2(new Worker(new Position(1, 2),2));
        game1.getPlayer1().setCurrentWorker(worker11);
        game1.getPlayer2().setWorker1(worker21);
        game1.getPlayer2().setWorker2(new Worker(new Position(4, 2),2));
        game1.getPlayer2().setCurrentWorker(worker21);

        Player p1 = game1.getCurrentPlayer();
        assertTrue(p1.getValue() == 1);
        Board board1 = game1.getCurrentPlayer().getBoard();
        boolean move1 = p1.move(p1.getWorker1(), new Position(2, 1));
        assertTrue(move1);
        assertEquals(p1.getWorker1().getPosition().getX(), 2);
        assertEquals(p1.getWorker1().getPosition().getY(), 1);

        boolean build1 = p1.build(p1.getWorker1(), new Position(3, 1));
        assertTrue(build1);
        assertEquals(board1.getTowerLevel(new Position(3, 1)), 1);

        game1.switchTurns();
        p1 = game1.getCurrentPlayer();
        assertTrue(p1.getValue() == 2);

        boolean move2 = p1.move(p1.getWorker1(), new Position(3, 1));
        assertTrue(move2);
        assertEquals(p1.getWorker1().getPosition().getX(), 3);
        assertEquals(p1.getWorker1().getPosition().getY(), 1);

        boolean build2 = p1.build(p1.getWorker1(), new Position(3, 2));
        assertTrue(build2);
        assertTrue(board1.getTowerLevel(new Position(3, 2)) == 1);

        game1.switchTurns();
        p1 = game1.getCurrentPlayer();
        assertTrue(p1.getValue() == 1);

        boolean move3 = p1.move(p1.getWorker2(), new Position(2, 2));
        // can step 1 up
        assertTrue(move3);
        assertEquals(p1.getWorker2().getPosition().getX(), 2);
        assertEquals(p1.getWorker2().getPosition().getY(), 2);
        
        boolean build3 = p1.build(p1.getWorker2(), new Position(3, 2));
        assertTrue(build3);
        assertTrue(board1.getTowerLevel(new Position(3, 2)) == 2);
        
        game1.switchTurns();
        p1 = game1.getCurrentPlayer();
        assertTrue(p1.getValue() == 2);

        boolean move4 = p1.move(p1.getWorker1(), new Position(3, 2));
        // can step 1 up
        assertTrue(move4);
        assertEquals(p1.getWorker1().getPosition().getX(), 3);
        assertEquals(p1.getWorker1().getPosition().getY(), 2);
        boolean build4Fail = p1.build(p1.getWorker1(), new Position(2, 2));
        assertTrue(!build4Fail);
        boolean build4 = p1.build(p1.getWorker1(), new Position(2, 3));
        assertTrue(build4);
        assertTrue(board1.getTowerLevel(new Position(2, 3)) == 1);

        game1.switchTurns();
        p1 = game1.getCurrentPlayer();
        assertTrue(p1.getValue() == 1);

        boolean move5 = p1.move(p1.getWorker2(), new Position(2, 3));
        // can step 1 up
        assertTrue(move5);
        assertEquals(p1.getWorker2().getPosition().getX(), 2);
        assertEquals(p1.getWorker2().getPosition().getY(), 3);
        boolean build5 = p1.build(p1.getWorker2(), new Position(2, 4));
        assertTrue(build5);
        assertTrue(board1.getTowerLevel(new Position(2, 4)) == 1);

        game1.switchTurns();
        p1 = game1.getCurrentPlayer();
        assertTrue(p1.getValue() == 2);

        boolean move6 = p1.move(p1.getWorker1(), new Position(3, 3));
        // can step  horizontally
        assertTrue(move6);
        assertEquals(p1.getWorker1().getPosition().getX(), 3);
        assertEquals(p1.getWorker1().getPosition().getY(), 3);
        assertTrue(p1.getValue() == 2);
        assertTrue(game1.isGameOver());
        assertTrue(game1.getWinner().equals(p1));
    }

    @Test
    public void testBasicMove1() {
        Game game1 = new Game(1, 1, 1, 2, 4, 1, 4, 2, 5, 4);
        Player p1 = game1.getCurrentPlayer();
        boolean move1 = p1.move(p1.getWorker1(), new Position(2, 1));
        assertTrue(move1);
        assertEquals(p1.getWorker1().getPosition().getX(), 2);
        assertEquals(p1.getWorker1().getPosition().getY(), 1);
        boolean move2 = p1.move(p1.getWorker1(), new Position(3, 2));
        assertTrue(move2);
        assertEquals(p1.getWorker1().getPosition().getX(), 3);
        assertEquals(p1.getWorker1().getPosition().getY(), 2);
        // cannot go occupied position 
        boolean move3 = p1.move(p1.getWorker1(), new Position(4, 2));
        assertTrue(!move3);
        assertEquals(p1.getWorker1().getPosition().getX(), 3);
        assertEquals(p1.getWorker1().getPosition().getY(), 2);
        //worker1: (3,2)
    }

    @Test
    public void testBasicMove2() {
        Game game1 = new Game(1, 1, 1, 2, 4, 1, 4, 2, 5, 4);
        game1.switchTurns();
        Player p2 = game1.getCurrentPlayer();
        //cannot go out of bound
        boolean move1 = p2.move(p2.getWorker2(), new Position(5, 2));
        assertTrue(!move1);
        assertEquals(p2.getWorker2().getPosition().getX(), 4);
        assertEquals(p2.getWorker2().getPosition().getY(), 2);
        // cannot go occupied position 
        boolean move2 = p2.move(p2.getWorker2(), new Position(4, 1));
        assertTrue(!move2);
        assertEquals(p2.getWorker2().getPosition().getX(), 4);
        assertEquals(p2.getWorker2().getPosition().getY(), 2);
        // cannot go non-adjacent position
        boolean move3 = p2.move(p2.getWorker2(), new Position(1, 1));
        assertTrue(!move3);
        assertEquals(p2.getWorker2().getPosition().getX(), 4);
        assertEquals(p2.getWorker2().getPosition().getY(), 2);
        boolean move4 = p2.move(p2.getWorker2(), new Position(3, 2));
        assertTrue(move4);
        assertEquals(p2.getWorker2().getPosition().getX(), 3);
        assertEquals(p2.getWorker2().getPosition().getY(), 2);
    }

    @Test
    public void testMoveWithTower() {
        Game game1 = new Game(1, 1, 1, 2, 4, 1, 4, 2, 5, 4);
        Player p1 = game1.getCurrentPlayer();
        Board board1 = game1.getCurrentPlayer().getBoard();
        boolean build1 = p1.build(p1.getWorker1(), new Position(2, 1));
        assertTrue(build1);
        boolean build2 = p1.build(p1.getWorker1(), new Position(2, 1));
        assertTrue(build2);
        assertTrue(board1.getTowerLevel(new Position(2, 1)) == 2);
        boolean move1 = p1.move(p1.getWorker1(), new Position(2, 1));
        // cannot go from level 0 to level 2
        assertTrue(!move1);
        assertEquals(p1.getWorker1().getPosition().getX(), 1);
        assertEquals(p1.getWorker1().getPosition().getY(), 1);
        boolean move2 = p1.move(p1.getWorker1(), new Position(0, 0));
        assertTrue(move2);
        assertEquals(p1.getWorker1().getPosition().getX(), 0);
        assertEquals(p1.getWorker1().getPosition().getY(), 0);
        boolean build3 = p1.build(p1.getWorker1(), new Position(1, 1));
        assertTrue(build3);
        assertTrue(board1.getTowerLevel(new Position(1, 1)) == 1);
        boolean buildFail = p1.build(p1.getWorker1(), new Position(1, 2));
        // cannot build on occupied position
        assertTrue(!buildFail);
        assertTrue(board1.getTowerLevel(new Position(1, 2)) == 0);
        boolean move4 = p1.move(p1.getWorker1(), new Position(1, 1));
        // can step 1 up
        assertTrue(move4);
        assertEquals(p1.getWorker1().getPosition().getX(), 1);
        assertEquals(p1.getWorker1().getPosition().getY(), 1);
        boolean move5 = p1.move(p1.getWorker1(), new Position(2, 1));
        // can step 1 up
        assertTrue(move5);
        assertEquals(p1.getWorker1().getPosition().getX(), 2);
        assertEquals(p1.getWorker1().getPosition().getY(), 1);
        boolean build4 = p1.build(p1.getWorker1(), new Position(2, 2));
        assertTrue(build4);
        boolean build5 = p1.build(p1.getWorker1(), new Position(2, 2));
        assertTrue(build5);
        assertTrue(board1.getTowerLevel(new Position(2, 2)) == 2);
        boolean move6 = p1.move(p1.getWorker1(), new Position(2, 2));
        // can step  horizontally
        assertTrue(move6);
        assertEquals(p1.getWorker1().getPosition().getX(), 2);
        assertEquals(p1.getWorker1().getPosition().getY(), 2);
        boolean build6 = p1.build(p1.getWorker1(), new Position(2, 1));
        assertTrue(build6);
        boolean move7 = p1.move(p1.getWorker1(), new Position(2, 1));
        // can step  horizontally
        assertTrue(move7);
        assertEquals(p1.getWorker1().getPosition().getX(), 2);
        assertEquals(p1.getWorker1().getPosition().getY(), 1);
        assertTrue(game1.isGameOver());
        assertTrue(game1.getWinner().equals(p1));
    }
}
