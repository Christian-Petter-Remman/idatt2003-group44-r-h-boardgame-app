package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class FileManager {

  private static final Logger logger = LoggerFactory.getLogger(FileManager.class);

  public static final String LOGS_DIR = "logs";
  public static final String DATA_DIR = "data";
  public static final String PLAYERS_DIR = "data/players";
  public static final String SAVED_GAMES_DIR = "data/saved_games";
  public static final String CUSTOM_BOARDS_DIR = "data/custom_boards";
  public static final String SNAKES_LADDERS_BOARDS_DIR = "data/custom_boards/snakes_and_ladders";

  public static final String DEFAULT_PLAYERS_FILE = "data/players/default_players.csv";
  public static final String LAST_GAME_PLAYERS_FILE = "data/players/last_game_players.csv";
  public static final String DEFAULT_BOARD_FILE = "data/custom_boards/default_board.json";


  public static void ensureApplicationDirectoriesExist() throws IOException {
    String[] directories = {
        LOGS_DIR,
        DATA_DIR,
        PLAYERS_DIR,
        SAVED_GAMES_DIR,
        CUSTOM_BOARDS_DIR,
        SNAKES_LADDERS_BOARDS_DIR
    };

    for (String dirPath : directories) {
      try {
        Path dir = Paths.get(dirPath);
        if (!Files.exists(dir)) {
          Files.createDirectories(dir);
          logger.info("Directory created at: {}", dir.toAbsolutePath());
        } else if (!Files.isDirectory(dir)) {
          logger.error("Path exists but is not a directory: {}", dir.toAbsolutePath());
          throw new IOException("Path exists but is not a directory: " + dir.toAbsolutePath());
        } else {
          logger.debug("Directory already exists: {}", dir.toAbsolutePath());
        }

        if (!Files.isWritable(dir)) {
          logger.error("Directory exists but is not writable: {}", dir.toAbsolutePath());
          throw new IOException("Directory exists but is not writable: " + dir.toAbsolutePath());
        }
      } catch (SecurityException e) {
        logger.error("Security exception when accessing directory {}: {}", dirPath, e.getMessage());
        throw new IOException("Security exception when accessing directory: " + dirPath, e);
      } catch (IOException e) {
        logger.error("Failed to create directory {}: {}", dirPath, e.getMessage());
        throw new IOException("Failed to create directory: " + dirPath, e);
      }
    }

    logger.info("All application directories verified successfully");
  }

  public static void saveDefaultBoard(Board board) throws FileWriteException {
    if (board == null) {
      logger.error("Cannot save null board");
      throw new IllegalArgumentException("Board cannot be null");
    }

    try {
      File boardsDir = new File(CUSTOM_BOARDS_DIR);
      if (!boardsDir.exists()) {
        boolean created = boardsDir.mkdirs();
        if (!created) {
          logger.error("Failed to create boards directory: {}", boardsDir.getAbsolutePath());
          throw new FileWriteException(
              "Could not create boards directory: " + boardsDir.getAbsolutePath());
        }
      }

      if (!board.saveToJson(DEFAULT_BOARD_FILE)) {
        logger.error("Failed to save default board to JSON: {}", DEFAULT_BOARD_FILE);
        throw new FileWriteException("Could not save default board to file: " + DEFAULT_BOARD_FILE);
      }

      File boardFile = new File(DEFAULT_BOARD_FILE);
      if (!boardFile.exists() || boardFile.length() == 0) {
        logger.error("Board file was not created properly: {}", DEFAULT_BOARD_FILE);
        throw new FileWriteException("Board file was not created properly: " + DEFAULT_BOARD_FILE);
      }

      logger.info("Successfully saved default board to JSON: {}", DEFAULT_BOARD_FILE);
    } catch (SecurityException e) {
      logger.error("Security exception when saving board: {}", e.getMessage());
      throw new FileWriteException("Security exception when saving board", e);
    } catch (Exception e) {
      logger.error("Unexpected error saving board: {}", e.getMessage());
      throw new FileWriteException("Unexpected error saving board", e);
    }
  }

  public static int loadOrCreateDefaultPlayers(SnakesAndLadders game) {

    if (game == null) {
      logger.error("Cannot load players into null game");
      throw new IllegalArgumentException("Game cannot be null");
    }

    File playersFile = new File(DEFAULT_PLAYERS_FILE);
    int playersLoaded = 0;

    try {
      File parentDir = playersFile.getParentFile();
      if (!parentDir.exists()) {
        boolean created = parentDir.mkdirs();
        if (!created) {
          logger.error("Failed to create players directory: {}", parentDir.getAbsolutePath());
          throw new FileWriteException(
              "Could not create players directory: " + parentDir.getAbsolutePath());
        }
      }

      if (playersFile.exists()) {
        if (!playersFile.canRead()) {
          logger.error("Players file exists but cannot be read: {}", playersFile.getAbsolutePath());
          throw new FileReadException(
              "Players file exists but cannot be read: " + playersFile.getAbsolutePath());
        }

        try {
          playersLoaded = game.loadPlayersFromCsv(playersFile.getPath());
          logger.info("Loaded {} players from CSV: {}", playersLoaded, playersFile.getPath());
        } catch (Exception e) {
          logger.error("Error loading players from CSV: {}", e.getMessage());
          throw new FileReadException("Error loading players from CSV", e);
        }
      }

      if (playersLoaded == 0) {
        logger.info("No players loaded from CSV. Creating default players");

        try {
          game.addPlayer("Player 1", "bowser");
          game.addPlayer("Player 2", "peach");

          if (!playersFile.getParentFile().exists()) {
            boolean created = playersFile.getParentFile().mkdirs();
            if (!created) {
              logger.error("Failed to create directory for players file: {}",
                  playersFile.getParentFile());
              throw new FileWriteException("Failed to create directory for players file");
            }
          }

          boolean saved = game.savePlayersToCsv(playersFile.getPath());
          if (!saved) {
            logger.error("Failed to save default players to CSV");
            throw new FileWriteException("Failed to save default players to CSV");
          }

          logger.info("Saved default players to CSV: {}", playersFile.getPath());
          return 2;
        } catch (Exception e) {
          logger.error("Error creating default players: {}", e.getMessage());
          throw new FileWriteException("Error creating default players", e);
        }
      }

      return playersLoaded;
    } catch (SecurityException e) {
      logger.error("Security exception when handling player file: {}", e.getMessage());
      try {
        throw new FileReadException("Security exception when handling player file", e);
      } catch (FileReadException ex) {
        throw new RuntimeException(ex);
      }
    } catch (FileWriteException | FileReadException e) {
      throw new RuntimeException(e);
    }
  }

  public static void saveLastGamePlayers(SnakesAndLadders game) throws FileWriteException {
    if (game == null) {
      logger.error("Cannot save players from null game");
      throw new IllegalArgumentException("Game cannot be null");
    }

    try {
      File playersFile = new File(LAST_GAME_PLAYERS_FILE);
      File parentDir = playersFile.getParentFile();
      if (!parentDir.exists()) {
        boolean created = parentDir.mkdirs();
        if (!created) {
          logger.error("Failed to create directory for last game players: {}",
              parentDir.getAbsolutePath());
          throw new FileWriteException(
              "Could not create directory for last game players: " + parentDir.getAbsolutePath());
        }
      }

      boolean success = game.savePlayersToCsv(LAST_GAME_PLAYERS_FILE);
      if (!success) {
        logger.error("Failed to save last game players to CSV: {}", LAST_GAME_PLAYERS_FILE);
        throw new FileWriteException(
            "Could not save last game players to: " + LAST_GAME_PLAYERS_FILE);
      }

      File savedFile = new File(LAST_GAME_PLAYERS_FILE);
      if (!savedFile.exists() || savedFile.length() == 0) {
        logger.error("Last game players file was not created properly: {}", LAST_GAME_PLAYERS_FILE);
        throw new FileWriteException(
            "Last game players file was not created properly: " + LAST_GAME_PLAYERS_FILE);
      }

      logger.info("Successfully saved last game players to CSV: {}", LAST_GAME_PLAYERS_FILE);
    } catch (SecurityException e) {
      logger.error("Security exception when saving last game players: {}", e.getMessage());
      throw new FileWriteException("Security exception when saving last game players", e);
    } catch (Exception e) {
      logger.error("Unexpected error saving last game players: {}", e.getMessage());
      throw new FileWriteException("Unexpected error saving last game players", e);
    }
  }
}
