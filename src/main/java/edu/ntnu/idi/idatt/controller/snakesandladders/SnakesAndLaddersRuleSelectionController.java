package edu.ntnu.idi.idatt.controller.snakesandladders;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.model_observers.DifficultyObserver;
import edu.ntnu.idi.idatt.exceptions.*;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.filehandling.BoardJsonHandler;
import edu.ntnu.idi.idatt.filehandling.FileManager;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SnakesAndLaddersRuleSelectionController {
  private static final Logger logger = LoggerFactory.getLogger(SnakesAndLaddersRuleSelectionController.class);

  private final List<DifficultyObserver> observers = new ArrayList<>();
  private final SnakesAndLaddersFactory factory;
  private SnakesAndLadders currentGame;
  private int currentLadderCount = 8;
  private int currentSnakeCount = 8;
  private int selectedRandomBoard = -1;
  private final BoardJsonHandler boardJsonHandler;

  public SnakesAndLaddersRuleSelectionController(SnakesAndLaddersFactory factory) {
    this.factory = factory;
    this.boardJsonHandler = new BoardJsonHandler();
  }

  public void addObserver(DifficultyObserver observer) {
    observers.add(observer);
  }

  public void notifyObservers(String difficulty) {
    for (DifficultyObserver observer : observers) {
      observer.onDifficultyChanged(difficulty);
    }
  }

  public String setDifficulty(String difficulty) {
    String boardName = difficulty;

    switch (difficulty.toLowerCase()) {
      case "easy":
        currentLadderCount = 10;
        currentSnakeCount = 4;
        break;
      case "hard":
        currentLadderCount = 5;
        currentSnakeCount = 10;
        break;
      case "random":
        boardName = selectRandomBoard();
        break;
      default:
        currentLadderCount = 8;
        currentSnakeCount = 8;
        break;
    }
    notifyObservers(difficulty);
    return boardName;
  }

  private String selectRandomBoard() {
    selectedRandomBoard = new Random().nextInt(8) + 1;
    String boardName = "random"+ selectedRandomBoard;


    try {
      String boardPath = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/random" + selectedRandomBoard + ".json";
      Board board = boardJsonHandler.loadBoardFromFile(boardPath);

      currentLadderCount = board.getLadders().size();
      currentSnakeCount = board.getSnakes().size();

      logger.info("Selected random board {}: {} ladders, {} snakes",
          selectedRandomBoard, currentLadderCount, currentSnakeCount);
    } catch (Exception e) {
      logger.error("Failed to load random board: {}", e.getMessage());
      showAlert("Error", "Failed to load a random board. Default settings will be used.");

      currentLadderCount = 8;
      currentSnakeCount = 8;

      logger.info("Falling back to default difficulty due to random board loading failure");
      notifyObservers("default");
    }
    return boardName;
  }

  private String selectBoardFile(String difficulty) {
    String fileName;
    String fullPath;

    fileName = difficulty + ".json";
    fullPath = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + fileName;
    return fullPath;
  }

  private String selectRandomBoardFile(String difficulty) {
    Random rand = new Random();
    String fileName;
    String fullPath;
    int randomBoard = rand.nextInt(8) + 1;
    fileName = difficulty + randomBoard + ".json";
    fullPath = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + fileName;
    return fullPath;
  }

  public String GetBoardFile(String difficulty) {
    validateDifficulty(difficulty);
    String result;

    if (difficulty.equals("random")) {
      result = selectRandomBoardFile(difficulty);
    }
    else {
      result = selectBoardFile(difficulty);
    }
    return result;
  }


  public String createGameFile(String difficulty, String baseName) throws InvalidGameConfigurationException {
    validateDifficulty(difficulty);

    try {
      String filename;
      String fullPath;

      if ("random".equalsIgnoreCase(difficulty)) {
        currentGame = factory.createBoardGameFromConfiguration("random", SnakesAndLadders.class);
        filename =  baseName+ ".json";
        fullPath = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + filename;

        boardJsonHandler.saveToFile(currentGame, fullPath);
        logger.info("Random game loaded from predefined board: {}", filename);
      } else {
        currentGame = factory.createBoardGameFromConfiguration(difficulty, SnakesAndLadders.class);

        filename = baseName + ".json";
        fullPath = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + filename;

        boardJsonHandler.saveToFile(currentGame, fullPath);

        logger.info("Game created and saved to {} with difficulty: {}, ladders: {}, penalties: {}",
                fullPath, difficulty, currentLadderCount, currentSnakeCount);
      }

      return fullPath;

    } catch (IllegalArgumentException e) {
      logger.error("Failed to create game: {}", e.getMessage());
      throw new InvalidGameConfigurationException("Invalid game configuration: " + e.getMessage());
    } catch (IOException e) {
      logger.error("IO Error while saving game: {}", e.getMessage());
      throw new RuntimeException("Failed to save game file", e);
    }
  }

  public void validateDice(int diceCount) {
    if (diceCount <= 0) {
      throw new InvalidGameConfigurationException("Dice count must be larger than 0");
    }
  }

  private void validateDifficulty(String difficulty) throws InvalidGameConfigurationException {
    List<String> validDifficulties = new ArrayList<>(Arrays.asList(factory.getAvailableConfigurations()));
    validDifficulties.add("random1");
    validDifficulties.add("random2");
    validDifficulties.add("random3");
    validDifficulties.add("random4");
    validDifficulties.add("random5");
    validDifficulties.add("random6");
    validDifficulties.add("random7");
    validDifficulties.add("random8");


    if (!validDifficulties.contains(difficulty)) {
      throw new InvalidGameConfigurationException("Invalid difficulty level: " + difficulty);
    }
  }

  private void addPlayers(List<Player> players) {
    if (players == null || players.isEmpty()) {
      throw new IllegalArgumentException("At least one player is required");
    }
    players.forEach(currentGame::addPlayer);
  }


  public int getCurrentLadderCount() {
    return currentLadderCount;
  }

  public int getCurrentSnakeCount() {
    return currentSnakeCount;
  }

  public int getSelectedRandomBoard() {
    return selectedRandomBoard;
  }
}
