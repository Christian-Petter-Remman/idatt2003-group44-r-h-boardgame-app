package edu.ntnu.idi.idatt.model.common.factory;

import edu.ntnu.idi.idatt.filehandling.handlers.StarBoardJsonHandler;
import edu.ntnu.idi.idatt.model.common.BoardGameFactory;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class StarFactory extends BoardGameFactory {

  private static final String BOARD_DIRECTORY = "data/custom_boards/star_game/";
  private static final Logger logger = LoggerFactory.getLogger(StarFactory.class);
  private final StarBoardJsonHandler boardJsonHandler = new StarBoardJsonHandler();


  public String[] getAvailableConfigurations() {
    return new String[]{"default", "easy", "hard"};
  }

//  public <T extends BoardGame> T createBoardGameFromConfiguration(String configurationName, Class<T> gameClass) {
//    StarBoard board;
//
//    if ("random".equalsIgnoreCase(configurationName)) {
//      board = new StarBoard(130);
//      board.initializeBoard();
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
//      gameInstance.initializeBoard(board);
//      return gameInstance;
//    } catch (Exception e) {
//      logger.error("Could not instantiate game of type " + gameClass.getSimpleName(), e);
//      throw new RuntimeException("Failed to create game instance", e);
//    }
//  }

//  @Override
//  public boolean saveBoardGameConfiguration(BoardGame game, String configurationName) {
//    if (!(game instanceof StarGame starGame)) {
//      return false;
//    }
//
//    StarBoard board = starGame.getBoard();
//    return saveBoardToFile(board, configurationName);
//  }

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

  private boolean saveBoardToFile(StarBoard board, String fileName) {
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
