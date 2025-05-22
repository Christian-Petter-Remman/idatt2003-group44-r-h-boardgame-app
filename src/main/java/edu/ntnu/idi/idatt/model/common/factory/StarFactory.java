package edu.ntnu.idi.idatt.model.common.factory;

import edu.ntnu.idi.idatt.filehandling.handlers.StarBoardJsonHandler;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * <h1>StarFactory</h1>
 *
 * Factory class responsible for loading {@link StarBoard} instances from JSON files
 * using the {@link StarBoardJsonHandler}.
 */
public class StarFactory {

  private static final String BOARD_DIRECTORY = "data/custom_boards/star_game/";
  private static final Logger logger = LoggerFactory.getLogger(StarFactory.class);
  private final StarBoardJsonHandler boardJsonHandler = new StarBoardJsonHandler();

  /**
   * <h2>loadBoardFromFile</h2>
   *
   * Loads a Star board from a specified JSON file name.
   *
   * @param fileName The name of the JSON file located in the star game board directory.
   * @return A {@link StarBoard} object if loading succeeds, or {@code null} if the file doesn't exist or an error occurs.
   */
  public StarBoard loadBoardFromFile(String fileName) {
    String filePath = BOARD_DIRECTORY + fileName;
    File file = new File(filePath);

    if (!file.exists()) {
      logger.info("StarBoard file does not exist: {}", filePath);
      return null;
    }

    try {
      StarBoard board = boardJsonHandler.loadBoardFromFile(filePath);
      logger.info("Successfully loaded board from: {}", filePath);
      return board;
    } catch (Exception e) {
      logger.error("Failed to load board from file: {}", e.getMessage(), e);
      return null;
    }
  }
}