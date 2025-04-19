package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.LadderTile;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.SnakeTile;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Tile;
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
    //game.addPlayer("Alice", "peach");
    //game.addPlayer("Bob", "bowser");

    FileManager.saveLastGamePlayers(game);

    File file = new File(FileManager.LAST_GAME_PLAYERS_FILE);
    assertTrue(file.exists(), "Last game players file should be saved");
  }

  @Test
  void testSavedPlayersAreCorrectlyStoredAndLoaded() throws Exception {
    // Opprett spill og legg til spillere
    SnakesAndLadders game = new SnakesAndLadders();
    //game.addPlayer("Kari", "peach");
    //game.addPlayer("Per", "bowser");

    // Lagre spillerne
    FileManager.saveLastGamePlayers(game);

    // Lag nytt spill og last spillerne fra filen
    SnakesAndLadders loadedGame = new SnakesAndLadders();
    int loaded = loadedGame.loadPlayersFromCsv(FileManager.LAST_GAME_PLAYERS_FILE);

    assertEquals(2, loaded, "Should load 2 players");
    assertEquals("Kari", loadedGame.getPlayers().get(0).getName());
    assertEquals("Per", loadedGame.getPlayers().get(1).getName());
  }

  @Test
  void testBoardIsSavedAndLoadedCorrectly() throws FileWriteException, FileReadException, JsonParsingException {
    // Lag et brett og sett inn en stige og en slange
    Board board = new Board();
    board.setTile(5, new LadderTile(5, 15));
    board.setTile(10, new SnakeTile(10, 3));

    FileManager.saveDefaultBoard(board);

    Board loaded = new BoardJsonHandler().loadFromFile(FileManager.DEFAULT_BOARD_FILE);

    assertNotNull(loaded, "Loaded board should not be null");

    Tile ladder = loaded.getTile(5);
    Tile snake = loaded.getTile(10);

    assertTrue(ladder.hasSnakeOrLadder(), "Tile 5 should be a ladder");
    assertEquals(15, ladder.getDestination(), "Ladder destination should be 15");

    assertTrue(snake.hasSnakeOrLadder(), "Tile 10 should be a snake");
    assertEquals(3, snake.getDestination(), "Snake destination should be 3");
  }
}