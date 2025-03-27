package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class BoardJsonHandlerTest {

  private static final String TEST_FILE = "test_board.json";
  private BoardJsonHandler handler;

  @BeforeEach
  void setUp() {
    handler = new BoardJsonHandler();
  }

  @AfterEach
  void tearDown() {
    new File(TEST_FILE).delete();
  }

  @Test
  void testSaveAndLoadBoard() throws IOException, FileReadException, JsonParsingException {
    Board originalBoard = new Board();
    handler.saveToFile(originalBoard, TEST_FILE);

    Board loadedBoard = handler.loadFromFile(TEST_FILE);

    assertNotNull(loadedBoard);
    assertEquals(100, loadedBoard.getTiles().size(), "Board should have 100 tiles");
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
    Board loadedBoard = handler.loadFromFile(path);

    assertNotNull(loadedBoard, "Board should not be null");
    assertTrue(loadedBoard.getTiles().size() > 0, "Board should contain tiles");
  }
}