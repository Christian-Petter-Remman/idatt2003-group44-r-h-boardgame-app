package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import edu.ntnu.idi.idatt.filehandling.*;
import edu.ntnu.idi.idatt.model.common.BoardGameFactory;
import edu.ntnu.idi.idatt.model.common.BoardGame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnakesAndLaddersFactory extends BoardGameFactory {

  private static final String BOARD_DIRECTORY = "data/custom_boards/snakes_and_ladders/";
  private static final Logger logger = LoggerFactory.getLogger(SnakesAndLaddersFactory.class);

  @Override
  public BoardGame createBoardGame() {
    SnakesAndLadders game = new SnakesAndLadders();
    game.setBoard(createDefaultBoard());
    game.initialize(game.getBoard());
    return game;
  }

  @Override
  public String[] getAvailableConfigurations() {
    File directory = new File(BOARD_DIRECTORY);
    List<String> boardFiles = new ArrayList<>();

    boardFiles.add("default");
    boardFiles.add("easy");
    boardFiles.add("hard");

    if (directory.exists() && directory.isDirectory()) {
      File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
      if (files != null) {
        for (File file : files) {
          String configName = file.getName().replace(".json", "");
          if (!boardFiles.contains(configName)) {
            boardFiles.add(configName);
          }
        }
      }
    }
    return boardFiles.toArray(new String[0]);
  }

  @Override
  public BoardGame createBoardGameFromConfiguration(String configurationName) {
    SnakesAndLadders game = new SnakesAndLadders();
    Board board;

    board = loadBoardFromFile(configurationName + ".json");

    if (board == null) {
      board = switch (configurationName.toLowerCase()) {
        case "default" -> createDefaultBoard();
        case "easy" -> createEasyBoard();
        case "hard" -> createHardBoard();
        default -> {
          logger.warn("Unknown configuration: {}, using default", configurationName);
          yield createDefaultBoard();
        }
      };
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

  private Board createDefaultBoard() {
    logger.debug("Creating default board for Snakes and Ladders");
    Board board = new Board();
    board.initializeEmptyBoard();
    board.addDefaultLaddersAndSnakes();
    return board;
  }

  private Board loadBoardFromFile(String fileName) {
    String filePath = BOARD_DIRECTORY + fileName;
    File file = new File(filePath);

    if (!file.exists()) {
      logger.debug("Board file does not exist: {}", filePath);
      return null;
    }

    try {
      BoardJsonHandler handler = new BoardJsonHandler();
      Board board = handler.loadFromFile(filePath);
      logger.info("Successfully loaded board from: {}", filePath);
      return board;
    } catch (Exception e) {
      logger.error("Failed to load board from file: {}", e.getMessage());
      return null;
    }
  }

  private Board createEasyBoard() {
    logger.info("Creating easy board for Snakes and Ladders");
    Board board = new Board();
    board.initializeEmptyBoard();

    board.addFullLadder(3, 22);
    board.addFullLadder(35, 67);
    board.addFullLadder(60, 82);

    board.addSnake(20, 5);
    board.addSnake(49, 30);
    board.addSnake(98, 81);

    return board;
  }

  private Board createHardBoard() {
    logger.info("Creating hard board configuration");
    Board board = new Board();
    board.initializeEmptyBoard();

    board.addFullLadder(1, 38);
    board.addFullLadder(4, 14);
    board.addFullLadder(28, 84);

    board.addDefaultSnakes();
    board.addSnake(32, 10);
    board.addSnake(48, 26);

    return board;
  }

  private boolean saveBoardToFile(Board board, String fileName) {
    String filePath = BOARD_DIRECTORY + File.separator + fileName;
    try {
      BoardJsonHandler handler = new BoardJsonHandler();
      handler.saveToFile(board, filePath);
      return true;
    } catch (Exception e) {
      logger.error("Failed to save board to file: {}\n Error message: {} ", filePath, e.getMessage());
      return false;
    }
  }
}