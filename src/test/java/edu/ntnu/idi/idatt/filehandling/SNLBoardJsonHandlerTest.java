package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.filehandling.handlers.SNLBoardJsonHandler;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SNLBoardJsonHandlerTest {

  private static final String TEST_FILE = "test_board.json";
  private SNLBoardJsonHandler handler;

  @BeforeEach
  void setUp() {
    handler = new SNLBoardJsonHandler();
  }

  @AfterEach
  void tearDown() {
    new File(TEST_FILE).delete();
  }

  @Test
  void testSaveAndLoadBoard() throws Exception {
    SNLBoard originalBoard = new SNLBoard();
    handler.saveToFile(originalBoard, TEST_FILE);

    SNLBoard loadedBoard = handler.loadFromFile(TEST_FILE);

    assertNotNull(loadedBoard);
    assertEquals(100, loadedBoard.getTiles().size(), "SNLBoard should have 100 tiles");
  }

  @Test
  void testLoadFromInvalidFilePath() {
    assertThrows(FileReadException.class, () -> {
      handler.loadBoardFromFile("non_existent_file.json");
    });
  }

  @Test
  void testLoadBoardFromResourceFile() throws Exception {
    var url = getClass().getClassLoader().getResource("BoardFiles/test_board.json");
    assertNotNull(url, "JSON file not found in BoardFiles folder");

    String path = Path.of(url.toURI()).toString();
    SNLBoard loadedBoard = handler.loadFromFile(path);

    assertNotNull(loadedBoard, "SNLBoard should not be null");
    assertTrue(loadedBoard.getTiles().size() > 0, "SNLBoard should contain tiles");
  }
}