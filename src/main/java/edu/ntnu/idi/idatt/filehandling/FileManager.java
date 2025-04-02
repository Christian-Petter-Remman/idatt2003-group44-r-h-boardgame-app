package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileManager {
  private static final Logger logger = LoggerFactory.getLogger(FileManager.class);

  public static final String LOGS_DIR = "logs";
  public static final String DATA_DIR = "data";
  public static final String PLAYERS_DIR = "data/players";
  public static final String SAVED_GAMES_DIR = "data/saved_games";
  public static final String CUSTOM_BOARDS_DIR = "data/custom_boards";

  public static final String DEFAULT_PLAYERS_FILE = "data/players/default_players.csv";
  public static final String LAST_GAME_PLAYERS_FILE = "data/players/last_game_players.csv";
  public static final String DEFAULT_BOARD_FILE = "data/custom_boards/default_board.json";

  public static void ensureApplicationDirectoriesExist() {
    String[] directories = {
            LOGS_DIR,
            DATA_DIR,
            PLAYERS_DIR,
            SAVED_GAMES_DIR,
            CUSTOM_BOARDS_DIR
    };

    for (String dir : directories) {
      File directory = new File(dir);
      if (!directory.exists()) {
        boolean created = directory.mkdirs();
        if (created) {
          logger.info("Directory created at: {}", directory.getAbsolutePath());
        } else {
          logger.warn("Failed to create directory: {}", directory.getAbsolutePath());
        }
      }
    }
  }

  public static void saveDefaultBoard(Board board) throws FileWriteException {
    File boardsDir = new File(CUSTOM_BOARDS_DIR);
    if (!boardsDir.exists()) {
      boardsDir.mkdirs();
    }

    if (!board.saveToJson(DEFAULT_BOARD_FILE)) {
      logger.error("Failed to save default board to JSON: {}", DEFAULT_BOARD_FILE);
      throw new FileWriteException("Could not save default board to file: " + DEFAULT_BOARD_FILE);
    }

    logger.info("Saved default board to JSON: {}", DEFAULT_BOARD_FILE);
  }

  public static int loadOrCreateDefaultPlayers(SnakesAndLadders game)
          throws FileReadException, CsvFormatException, FileWriteException {

    File playersFile = new File(DEFAULT_PLAYERS_FILE);
    int playersLoaded = 0;

    if (playersFile.exists()) {
      playersLoaded = game.loadPlayersFromCsv(playersFile.getPath());
      logger.info("Loaded {} players from CSV: {}", playersLoaded, playersFile.getPath());
    }

    if (playersLoaded == 0) {
      logger.info("No players loaded from CSV. Creating default players");

      game.addPlayer("Player 1","bowser");
      game.addPlayer("Player 2","peach");

      boolean saved = game.savePlayersToCsv(playersFile.getPath());
      if (!saved) {
        throw new FileWriteException("Failed to save default players to CSV");
      }

      logger.info("Saved default players to CSV: {}", playersFile.getPath());
      return 2;
    }

    return playersLoaded;
  }

  public static void saveLastGamePlayers(SnakesAndLadders game) throws FileWriteException {
    boolean success = game.savePlayersToCsv(LAST_GAME_PLAYERS_FILE);
    if (!success) {
      logger.error("Failed to save last game players to CSV: {}", LAST_GAME_PLAYERS_FILE);
      throw new FileWriteException("Could not save last game players to: " + LAST_GAME_PLAYERS_FILE);
    }

    logger.info("Saved last game players to CSV: {}", LAST_GAME_PLAYERS_FILE);
  }
}