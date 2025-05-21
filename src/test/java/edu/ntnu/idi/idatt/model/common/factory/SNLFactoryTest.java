package edu.ntnu.idi.idatt.model.common.factory;

import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNLFactoryTest {

  @Test
  void testLoadBoardFromNonexistentFileReturnsNull() {
    SNLFactory factory = new SNLFactory();
    SNLBoard board = factory.loadBoardFromFile("nonexistentfile.json");
    assertNull(board);
  }
}
