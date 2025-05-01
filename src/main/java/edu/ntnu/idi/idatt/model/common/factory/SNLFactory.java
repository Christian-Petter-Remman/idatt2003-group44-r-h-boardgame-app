package edu.ntnu.idi.idatt.model.common.factory;

import edu.ntnu.idi.idatt.filehandling.*;
import edu.ntnu.idi.idatt.model.common.BoardGameFactory;
import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.SNLGame;
import java.io.File;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SNLFactory extends BoardGameFactory {

  private static final String BOARD_DIRECTORY = "data/custom_boards/snakes_and_ladders/";
  private static final Logger logger = LoggerFactory.getLogger(SNLFactory.class);
  private final SNLBoardJsonHandler boardJsonHandler = new SNLBoardJsonHandler();




//  public String[] getAvailableConfigurations() {
//    return new String[]{"default", "easy", "hard"};
//  }
////
//  @Override
//  public <T extends BoardGame> T createBoardGameFromConfiguration(String configurationName, Class<T> gameClass) {
//    SNLBoard board;
//
//    if ("random".equalsIgnoreCase(configurationName)) {
//      board = new SNLBoard(100);
//      board.initializeEmptyBoard();
//      logger.info("Created empty board for random configuration");
//    } else {
//      board = loadBoardFromFile(configurationName + ".json");
//
//      if (board == null) {
//        logger.warn("Failed to SNLLoad board for configuration: {}, using default", configurationName);
//        board = loadBoardFromFile("default.json");
//      }
//    }
//
//    try {
//      T gameInstance = gameClass.getDeclaredConstructor().newInstance();
//      gameInstance.setBoard(board);
//      gameInstance.initialize(board);
//      return gameInstance;
//    } catch (Exception e) {
//      logger.error("Could not instantiate game of type " + gameClass.getSimpleName(), e);
//      throw new RuntimeException("Failed to create game instance", e);
//    }
//  }
//
//  @Override
//  public boolean saveBoardGameConfiguration(BoardGame game, String configurationName) {
//    if (!(game instanceof SNLGame SNLGame)) {
//      return false;
//    }
//
//    SNLBoard board = SNLGame.getBoard();
//    return saveBoardToFile(board, configurationName);
//  }

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
