// CHECKSTYLE:OFF
package com.terry.santorini.board;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FieldTest {
    private Field field;

    @Before
    public void setUp() {
        field = new Field(0);
    }

    @Test
    public void testFieldInitializedWithZeroLevel() {
        assertEquals(field.getLevel(), 0);
    }

    @Test
    public void testBuildBlockOnEmptyFieldAddOneLevel() {
        field = field.buildBlock();
        assertEquals(field.getLevel(), 1);
    }

    @Test
    public void testFieldIsNotCappedByFourBuilds() {
        field = field.buildBlock()
                    .buildBlock()
                    .buildBlock()
                    .buildBlock();

        assertFalse(field.isCapped());
    }

    @Test
    public void testFieldCanHaveAtMostThreeLevels() {
        field = field.buildBlock()
                    .buildBlock()
                    .buildBlock()
                    .buildBlock()
                    .buildBlock();
                    
        assertEquals(field.getLevel(), 3);
    }

    @Test
    public void testCappedAfterBuildDome() {
        field = field.buildDome();
        assertTrue(field.isCapped());
    }

    @Test
    public void testOccupiedAfterWorkerEnter() {
        field = field.enter(new Worker(0, null));
        assertTrue(field.isOccupied());
    }

    @Test
    public void testNotOccupiedAfterWorkerLeave() {
        field = field.enter(new Worker(0, null))
                    .leave();

        assertFalse(field.isOccupied());
    }

    @Test
    public void testFullBuildAfterThreeBuilds() {
        field = field.buildBlock()
                    .buildBlock()
                    .buildBlock();

        assertTrue(field.isFullBuild());
    }

    // Immutablility Testing
    
    @Test 
    public void testImmutableByBuildBlock() {
        field.buildBlock();

        assertEquals(field.getLevel(), 0);
    }

    @Test 
    public void testImmutableByBuildDome() {
        field.buildDome();

        assertFalse(field.isCapped());
    }

    @Test 
    public void testImmutableByWorkerEnter() {
        field.enter(new Worker(0, null));

        assertFalse(field.isOccupied());
    }
}