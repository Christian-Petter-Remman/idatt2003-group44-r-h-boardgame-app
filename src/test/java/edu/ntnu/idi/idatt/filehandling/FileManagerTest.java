package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SNLBoard;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Tile;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

  @BeforeEach
  void cleanTestFiles() {
    new File(FileManager.DEFAULT_BOARD_FILE).delete();
    new File(FileManager.DEFAULT_PLAYERS_FILE).delete();
    new File(FileManager.LAST_GAME_PLAYERS_FILE).delete();
  }

  @Test
  void testEnsureApplicationDirectoriesExist() throws IOException {
    FileManager.ensureApplicationDirectoriesExist();
    assertTrue(new File(FileManager.DATA_DIR).exists());
    assertTrue(new File(FileManager.PLAYERS_DIR).exists());
  }

}