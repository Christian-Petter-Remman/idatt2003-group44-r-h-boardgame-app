package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import edu.ntnu.idi.idatt.filehandling.*;
import edu.ntnu.idi.idatt.model.common.BoardGameFactory;
import edu.ntnu.idi.idatt.model.common.BoardGame;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnakesAndLaddersFactory extends BoardGameFactory {

  private static final String BOARD_DIRECTORY = "data/custom_boards/snakes_and_ladders/";
  private static final Logger logger = LoggerFactory.getLogger(SnakesAndLaddersFactory.class);
  private final BoardJsonHandler boardJsonHandler = new BoardJsonHandler();

  @Override
  public BoardGame createBoardGame() {
    SnakesAndLadders game = new SnakesAndLadders();
    game.setBoard(loadBoardFromFile("default.json"));
    game.initialize(game.getBoard());
    return game;
  }

  @Override
  public String[] getAvailableConfigurations() {
    return new String[]{"default", "easy", "hard"};
  }

  @Override
  public BoardGame createBoardGameFromConfiguration(String configurationName) {
    SnakesAndLadders game = new SnakesAndLadders();
    Board board;

    if ("random".equalsIgnoreCase(configurationName)) {
      board = new Board();
      board.initializeEmptyBoard();
      logger.info("Created empty board for random configuration");
    } else {
      board = loadBoardFromFile(configurationName + ".json");

      if (board == null) {
        logger.warn("Failed to load board for configuration: {}, using default", configurationName);
        board = loadBoardFromFile("default.json");

        if (board == null) {
          logger.error("Failed to load default board, creating basic board");
          board = new Board();
          board.initializeEmptyBoard();
          board.addDefaultLaddersAndSnakes();
        }
      }
    }

    game.setBoard(board);
    game.initialize(game.getBoard());
    return game;
  }

  @Override
  public boolean saveBoardGameConfiguration(BoardGame game, String configurationName) {
    if (!(game instanceof SnakesAndLadders snakesAndLadders)) {
      return false;
    }

    Board board = snakesAndLadders.getBoard();
    return saveBoardToFile(board, configurationName);
  }

  private Board loadBoardFromFile(String fileName) {
    String filePath = BOARD_DIRECTORY + fileName;
    File file = new File(filePath);

    if (!file.exists()) {
      logger.debug("Board file does not exist: {}", filePath);
      return null;
    }

    try {
      Board board = boardJsonHandler.loadFromFile(filePath);
      logger.info("Successfully loaded board from: {}", filePath);
      return board;
    } catch (Exception e) {
      logger.error("Failed to load board from file: {}", e.getMessage());
      return null;
    }
  }

  private boolean saveBoardToFile(Board board, String fileName) {
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
