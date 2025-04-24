package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SNLBoard;
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
}
