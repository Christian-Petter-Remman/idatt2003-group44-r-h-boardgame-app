package edu.ntnu.idi.idatt.model.common.factory;

import edu.ntnu.idi.idatt.filehandling.handlers.SNLBoardJsonHandler;
import edu.ntnu.idi.idatt.model.common.BoardGameFactory;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import java.io.File;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SNLFactory extends BoardGameFactory {

  private static final String BOARD_DIRECTORY = "data/custom_boards/snakes_and_ladders/";
  private static final Logger logger = LoggerFactory.getLogger(SNLFactory.class);
  private final SNLBoardJsonHandler boardJsonHandler = new SNLBoardJsonHandler();


  public SNLBoard loadBoardFromFile(String fileName) {

    File file = new File(fileName);

    if (!file.exists()) {
      logger.debug("SNLBoard file does not exist: {}", fileName);
      return null;
    }

    try {
      SNLBoard board = boardJsonHandler.loadBoardFromFile(fileName);
      logger.info("Successfully loaded board from: {}", fileName);
      return board;
    } catch (Exception e) {
    logger.error("Failed to SNLLoad board from file: {}", e.getMessage(), e);
    return null;
  }
    }


  private boolean saveBoardToFile(SNLBoard board, String fileName) {
    String filePath = BOARD_DIRECTORY + File.separator + fileName;
    try {
      boardJsonHandler.saveToFile(board, filePath);
      return true;
    } catch (Exception e) {
      logger.error("Failed to save board to file: {}\n Error message: {} ", filePath, e.getMessage());
      return false;
    }
  }
}
