package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.filehandling.*;
import edu.ntnu.idi.idatt.model.common.factory.BoardGameFactory;
import edu.ntnu.idi.idatt.model.common.game.BoardGame;
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
    game.initialize();
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

    switch (configurationName.toLowerCase()) {
      case "default":
        board = createDefaultBoard();
        break;

      case "easy":
        board = createEasyBoard();
        break;

      case "hard":
        board = createHardBoard();
        break;

        default:
          board = loadBoardFromFile(configurationName + ".json");
          if (board == null) {
            board = createDefaultBoard();
          }
    }

    game.setBoard(board);
    game.initialize();
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

  private Board createEasyBoard() {
    logger.debug("Creating easy board for Snakes and Ladders");
    Board board = new Board();
    board.addDefaultLadders();

    board.addSnake(20, 5);
    board.addSnake(49,30);
    board.addSnake(77, 60);
    board.addSnake(98,81);

    return board;
  }

  private Board createHardBoard() {
    logger.debug("Creating hard board configuration");
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

  private Board loadBoardFromFile(String fileName) {
    String filePath = BOARD_DIRECTORY + File.separator + fileName;
    try {
      BoardJsonHandler handler = new BoardJsonHandler();
      return handler.loadBoardFromFile(filePath);
    } catch (FileReadException | JsonParsingException e) {
      logger.error("Failed to load board from file: {}\n Error message: {} ", filePath, e.getMessage());
      return null;
    }
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
