package edu.ntnu.idi.idatt.model.common.factory;

import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StarFactoryTest {

  @Test
  void testLoadBoardFromNonexistentFileReturnsNull() {
    StarFactory factory = new StarFactory();
    StarBoard board = factory.loadBoardFromFile("definitely_missing_board.json");
    assertNull(board);
  }
}
