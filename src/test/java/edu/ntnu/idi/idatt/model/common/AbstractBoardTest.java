package edu.ntnu.idi.idatt.model.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractBoardTest {

  private AbstractBoard board;
  private final int BOARD_SIZE = 100;

  private static class TestBoard extends AbstractBoard {

    private final int size;

    public TestBoard(int size) {
      super(size);
      this.size = size;
    }

    @Override
    public int getSize() {
      return size;
    }
  }

  @BeforeEach
  void setUp() {
    board = new TestBoard(BOARD_SIZE);
  }

  @Test
  void constructor_createsCorrectNumberOfTiles() {
    for (int i = 0; i < BOARD_SIZE; i++) {
      Tile tile = board.getTile(i);
      assertNotNull(tile);
      assertEquals(i, tile.getNumber());
    }
  }

  @Test
  void getTile_validTileNumber_returnsTile() {
    Tile tile = board.getTile(5);
    assertNotNull(tile);
    assertEquals(5, tile.getNumber());
  }

  @Test
  void getTile_invalidTileNumber_throwsException() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> board.getTile(BOARD_SIZE + 1));
    assertTrue(exception.getMessage().contains("not found"));
  }

  @Test
  void addTile_overwritesExistingTile() {
    Tile newTile = new Tile(2);
    board.addTile(newTile);
    Tile fetchedTile = board.getTile(2);
    assertEquals(newTile, fetchedTile);
  }

  @Test
  void initializeBoard_overwritesAllTilesCorrectly() {
    board.initializeBoard();
    for (int i = 0; i < BOARD_SIZE; i++) {
      Tile tile = board.getTile(i);
      assertNotNull(tile);
      assertEquals(i, tile.getNumber());
    }
  }
}