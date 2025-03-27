package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import org.junit.jupiter.api.*;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

  @BeforeEach
  void cleanTestFiles() {
    new File(FileManager.DEFAULT_BOARD_FILE).delete();
    new File(FileManager.DEFAULT_PLAYERS_FILE).delete();
    new File(FileManager.LAST_GAME_PLAYERS_FILE).delete();
  }

  @Test
  void testEnsureApplicationDirectoriesExist() {
    FileManager.ensureApplicationDirectoriesExist();
    assertTrue(new File(FileManager.DATA_DIR).exists());
    assertTrue(new File(FileManager.PLAYERS_DIR).exists());
  }

  @Test
  void testSaveDefaultBoard() throws FileWriteException {
    Board board = new Board();
    FileManager.saveDefaultBoard(board);
    File savedFile = new File(FileManager.DEFAULT_BOARD_FILE);
    assertTrue(savedFile.exists(), "Default board file should be created");
  }

  @Test
  void testLoadOrCreateDefaultPlayersCreatesFile() throws FileReadException, CsvFormatException, FileWriteException {
    SnakesAndLadders game = new SnakesAndLadders();
    int loaded = FileManager.loadOrCreateDefaultPlayers(game);
    assertEquals(2, loaded, "Should create 2 default players when file is missing");

    File file = new File(FileManager.DEFAULT_PLAYERS_FILE);
    assertTrue(file.exists(), "Default players file should be created");
  }

  @Test
  void testSaveLastGamePlayers() throws FileWriteException {
    SnakesAndLadders game = new SnakesAndLadders();
    game.addPlayer("Alice");
    game.addPlayer("Bob");

    FileManager.saveLastGamePlayers(game);

    File file = new File(FileManager.LAST_GAME_PLAYERS_FILE);
    assertTrue(file.exists(), "Last game players file should be saved");
  }

  @Test
  void testSavedPlayersAreCorrectlyStoredAndLoaded() throws Exception {
    // Opprett spill og legg til spillere
    SnakesAndLadders game = new SnakesAndLadders();
    game.addPlayer("Kari");
    game.addPlayer("Per");

    // Lagre spillerne
    FileManager.saveLastGamePlayers(game);

    // Lag nytt spill og last spillerne fra filen
    SnakesAndLadders loadedGame = new SnakesAndLadders();
    int loaded = loadedGame.loadPlayersFromCsv(FileManager.LAST_GAME_PLAYERS_FILE);

    assertEquals(2, loaded, "Should load 2 players");
    assertEquals("Kari", loadedGame.getPlayers().get(0).getName());
    assertEquals("Per", loadedGame.getPlayers().get(1).getName());
  }
}