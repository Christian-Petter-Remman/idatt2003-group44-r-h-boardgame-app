package edu.ntnu.idi.idatt.model.common.factory;

import edu.ntnu.idi.idatt.filehandling.handlers.StarBoardJsonHandler;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class StarFactory {

  private static final String BOARD_DIRECTORY = "data/custom_boards/star_game/";
  private static final Logger logger = LoggerFactory.getLogger(StarFactory.class);
  private final StarBoardJsonHandler boardJsonHandler = new StarBoardJsonHandler();


  public StarBoard loadBoardFromFile(String fileName) {
    String filePath = BOARD_DIRECTORY + fileName;
    File file = new File(filePath);

    if (!file.exists()) {
      logger.info("STARBoard file does not exist: {}", filePath);
      return null;
    }

    try {
      StarBoard board = boardJsonHandler.loadBoardFromFile(filePath);
      logger.info("Successfully loaded board from: {}", filePath);
      return board;
    } catch (Exception e) {
      logger.error("Failed to Load board from file: {}", e.getMessage());
      return null;
    }
  }

}
