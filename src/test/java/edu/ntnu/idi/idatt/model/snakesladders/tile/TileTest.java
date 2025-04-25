package edu.ntnu.idi.idatt.model.snakesladders.tile;

import edu.ntnu.idi.idatt.model.tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

  @Test
  void testRegularTileHasNoSnakeOrLadder() {
    Tile tile = new Tile(1);
    assertFalse(tile.hasSnakeOrLadder());
    assertEquals(1, tile.getNumberOfTile());
    assertEquals(-1, tile.getDestination(), "Regular tiles should have -1 as destination");
  }

  @Test
  void testLadderTileBehavior() {
    LadderTile ladder = new LadderTile(4, 22);
    assertTrue(ladder.hasSnakeOrLadder());
    assertEquals(4, ladder.getNumberOfTile());
    assertEquals(22, ladder.getDestination());
  }

  @Test
  void testSnakeTileBehavior() {
    SnakeTile snake = new SnakeTile(17, 7);
    assertTrue(snake.hasSnakeOrLadder());
    assertEquals(17, snake.getNumberOfTile());
    assertEquals(7, snake.getDestination());
  }
}