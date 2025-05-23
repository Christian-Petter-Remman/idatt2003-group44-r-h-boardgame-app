package edu.ntnu.idi.idatt.filehandling;


import edu.ntnu.idi.idatt.model.common.Player;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <h1>FileManager</h1>
 *
 * <p>Utility class for managing file operations such as directory creation, saving game states,
 * deleting files, and exporting game data to CSV.
 */
public class FileManager {

  private static final Logger logger = LoggerFactory.getLogger(FileManager.class);

  public static final String LOGS_DIR = "logs";
  public static final String DATA_DIR = "data";
  public static final String PLAYERS_DIR = "data/players";
  public static final String SAVED_GAMES_DIR = "data/saved_games";
  public static final String CUSTOM_BOARDS_DIR = "data/custom_boards";
  public static final String SNAKES_LADDERS_BOARDS_DIR = "data/custom_boards/snakes_and_ladders";
  public static final String STAR_GAME_DIR = "data/custom_boards/star_game";
  public static final String MEMORYGAME_DIR = "data/memorygame";

  /**
   * <h2>ensureApplicationDirectoriesExist</h2>
   *
   * <p>Creates required application directories if they do not exist, and verifies they are
   * writable.
   *
   *
   * @throws IOException If a directory is missing or inaccessible.
   */
  public static void ensureApplicationDirectoriesExist() throws IOException {
    String[] directories = {
        LOGS_DIR,
        DATA_DIR,
        PLAYERS_DIR,
        SAVED_GAMES_DIR,
        CUSTOM_BOARDS_DIR,
        SNAKES_LADDERS_BOARDS_DIR,
        STAR_GAME_DIR,
        MEMORYGAME_DIR
    };

    for (String dirPath : directories) {
      try {
        Path dir = Paths.get(dirPath);
        if (!Files.exists(dir)) {
          Files.createDirectories(dir);
          logger.info("Directory created at {}", dir.toAbsolutePath());
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

  /**
   * <h2>saveGameToPermanent</h2>
   *
   * <p>Moves a temporary save file to a permanent location.
   *
   * @param tempFile  The temporary file.
   * @param gamePath  Subdirectory path.
   * @param userInput New file name.
   */
  public static void saveGameToPermanent(File tempFile, String gamePath, String userInput) {
    File savedDir = new File("saves/load/" + gamePath + "/");
    if (!savedDir.exists()) {
      boolean ok = savedDir.mkdirs();
      if (!ok) {
        logger.error("Failed to create directory: {}", savedDir.getAbsolutePath());
        return;
      } else {
        logger.info("Directory created at: {}", savedDir.getAbsolutePath());
      }
    }

    File newFile = new File(savedDir, userInput);
    boolean success = tempFile.renameTo(newFile);
    if (success) {
      logger.info("Game permanently saved as: {}", newFile.getName());
    } else {
      logger.error("Failed to rename file {} to {}", tempFile.getAbsolutePath(),
          newFile.getAbsolutePath());
    }
  }


  /**
   * <h2>deletePermanentGame</h2>
   *
   * <p>Deletes a permanent save file.
   *
   * @param tempFile The file to delete.
   */
  public static void deletePermanentGame(File tempFile) {
    boolean success = tempFile.delete();
    if (success) {
      logger.info("Game permanently deleted as: {}", tempFile.getName());
    }
  }

  /**
   * <h2>writeSNLGameStateToCSV</h2>
   *
   * <p>Writes a Snakes and Ladders game state to a CSV file.
   *
   * @param file      Output file.
   * @param players   List of players.
   * @param boardPath Path of the board file used.
   * @param diceCount Number of dice used.
   * @param turnIndex Current turn index.
   */
  public static void writeSNLGameStateToCSV(File file, List<Player> players, String boardPath,
      int diceCount, int turnIndex) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write("Board," + boardPath + "\n");
      writer.write("DiceCount," + diceCount + "\n");
      writer.write("CurrentTurnIndex," + turnIndex + "\n");
      writer.write("Players:\n");
      for (Player p : players) {
        writer.write(String.format("%s,%s,%d\n",
            p.getName(),
            p.getCharacterIcon(),
            p.getPosition()));
      }
      logger.info("Game state written to: {}", file.getAbsolutePath());
    } catch (IOException e) {
      logger.error("Failed to write game state to CSV", e);
    }
  }

  /**
   * <h2>writeStarGameStateToCSV</h2>
   *
   * <p>Writes a Star game state to a CSV file.
   *
   * @param file      Output file.
   * @param players   List of players.
   * @param boardPath Path of the board file used.
   * @param diceCount Number of dice used.
   * @param turnIndex Current turn index.
   */
  public static void writeStarGameStateToCSV(File file, List<Player> players, String boardPath,
      int diceCount, int turnIndex) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
      writer.write("Board," + boardPath + "\n");
      writer.write("DiceCount," + diceCount + "\n");
      writer.write("CurrentTurnIndex," + turnIndex + "\n");
      writer.write("Players:\n");
      for (Player p : players) {
        writer.write(String.format("%s,%s,%d,%d\n",
            p.getName(),
            p.getCharacterIcon(),
            p.getPosition(),
            p.getPoints()));
      }
      logger.info("Game state written to {}", file.getAbsolutePath());
    } catch (IOException e) {
      logger.error("Failed to write game state to CSV", e);
    }
  }

}