package edu.ntnu.idi.idatt.model.snakesladders.tile;

import edu.ntnu.idi.idatt.model.common.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

  @Test
  void testRegularTileHasNoSnakeOrLadder() {
    Tile tile = new Tile(1);
    assertFalse(tile.hasSpecialTile());
    assertEquals(1, tile.getNumberOfTile());
    assertEquals(-1, tile.getDestination(), "Regular tiles should have -1 as destination");
  }
}