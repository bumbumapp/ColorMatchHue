package com.bumbumapps.hue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Regression: touching/dropping between tiles gave position -1 and crashed with ArrayIndexOutOfBounds. */
public class TileAdapterPositionTest {
    @Test
    public void invalidGridPositionIsRejected() {
        assertFalse(TileAdapter.isValidPosition(-1, 16));
        assertFalse(TileAdapter.isValidPosition(16, 16));
    }

    @Test
    public void tilePositionsAreAccepted() {
        assertTrue(TileAdapter.isValidPosition(0, 16));
        assertTrue(TileAdapter.isValidPosition(15, 16));
    }
}
