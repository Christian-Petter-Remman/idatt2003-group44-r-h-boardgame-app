//package edu.ntnu.idi.idatt.model.common.factory;
//
//import edu.ntnu.idi.idatt.filehandling.StarBoardJsonHandler;
//import edu.ntnu.idi.idatt.model.common.BoardGame;
//import edu.ntnu.idi.idatt.model.common.BoardGameFactory;
//import edu.ntnu.idi.idatt.model.stargame.StarBoard;
//import edu.ntnu.idi.idatt.model.stargame.StarGame;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//
//public class StarFactory extends BoardGameFactory {
//
//  private static final String BOARD_DIRECTORY = "data/custom_boards/stargame/";
//  private static final Logger logger = LoggerFactory.getLogger(StarFactory.class);
//  private final StarBoardJsonHandler boardJsonHandler = new StarBoardJsonHandler();
//
//
//  @Override
//  public String[] getAvailableConfigurations() {
//    return new String[]{"default", "easy", "hard"};
//  }
//
//  @Override
//  public <T extends BoardGame> T createBoardGameFromConfiguration(String configurationName, Class<T> gameClass) {
//    StarBoard board;
//
//    if ("random".equalsIgnoreCase(configurationName)) {
//      board = new StarBoard();
//      board.initializeEmptyBoard();
//      logger.info("Created empty board for random configuration");
//    } else {
//      board = loadBoardFromFile(configurationName + ".json");
//
//      if (board == null) {
//        logger.warn("Failed to load board for configuration: {}, using default", configurationName);
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
//    if (!(game instanceof StarGame starGame)) {
//      return false;
//    }
//
//    StarBoard board = starGame.getBoard();
//    return saveBoardToFile(board, configurationName);
//  }
//
//  private StarBoard loadBoardFromFile(String fileName) {
//    String filePath = BOARD_DIRECTORY + fileName;
//    File file = new File(filePath);
//
//    if (!file.exists()) {
//      logger.debug("SNLBoard file does not exist: {}", filePath);
//      return null;
//    }
//
//    try {
//      StarBoard board = boardJsonHandler.loadFromFile(filePath);
//      logger.info("Successfully loaded board from: {}", filePath);
//      return board;
//    } catch (Exception e) {
//      logger.error("Failed to load board from file: {}", e.getMessage());
//      return null;
//    }
//  }
//
//  private boolean saveBoardToFile(StarBoard board, String fileName) {
//    String filePath = BOARD_DIRECTORY + File.separator + fileName;
//    try {
//      boardJsonHandler.saveToFile(board, filePath);
//      return true;
//    } catch (Exception e) {
//      logger.error("Failed to save board to file: {}\n Error message: {} ", filePath, e.getMessage());
//      return false;
//    }
//  }
//}
