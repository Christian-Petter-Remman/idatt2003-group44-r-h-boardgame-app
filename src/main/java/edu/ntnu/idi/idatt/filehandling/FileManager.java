package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.model.common.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;

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

  public static void saveGameToPermanent(File tempFile, String gamePath, String userInput) {
    File savedDir = new File("saves/load/" + gamePath+"/");
    if (!savedDir.exists()) savedDir.mkdirs();

    File newFile = new File("saves/load/"+gamePath+"/" + userInput);
    boolean success = tempFile.renameTo(newFile);
    if (success) {
      logger.info("Game permanently saved as: {}", newFile.getName());
    }
  }

  public static void deletePermanentGame(File tempFile) {
    boolean success = tempFile.delete();
    if (success) {
      logger.info("Game permanently deleted as: {}", tempFile.getName());
    }
  }

  public static void writeSNLGameStateToCSV(File file, List<Player> players, String boardPath, int diceCount, int turnIndex) {
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
      logger.info("Game state written to {}", file.getAbsolutePath());
    } catch (IOException e) {
      logger.error("Failed to write game state to CSV", e);
    }
  }

  public static void writeStarGameStateToCSV(File file, List<Player> players, String boardPath, int diceCount, int turnIndex) {
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
                p.getPoints())
        );
      }
      logger.info("Game state written to {}", file.getAbsolutePath());
    } catch (IOException e) {
      logger.error("Failed to write game state to CSV", e);
    }
  }


  public static void cleanupIfTemporary(String tempFilePath) {
    if (tempFilePath != null) {
      File file = new File(tempFilePath);
      if (file.exists()) {
        file.delete();
        logger.info("Deleted temporary save file: {}", tempFilePath);
      }
    }
  }

}
