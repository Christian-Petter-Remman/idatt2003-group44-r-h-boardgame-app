package edu.ntnu.idi.idatt.controller.snakesandladders;

import edu.ntnu.idi.idatt.exceptions.InvalidGameConfigurationException;
import edu.ntnu.idi.idatt.filehandling.BoardJsonHandler;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.SalRuleSelectionViewObserver;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.util.AlertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SalRuleSelectionController {
  private static final Logger logger = LoggerFactory.getLogger(SalRuleSelectionController.class);

  private final List<SalRuleSelectionViewObserver> observers = new ArrayList<>();
  private final SnakesAndLaddersFactory factory;
  private final BoardJsonHandler boardJsonHandler;
  private NavigationHandler navigationHandler;

  private String selectedDifficulty = "default";
  private int diceCount = 1;
  private int ladderCount = 8;
  private int snakeCount = 8;
  private int selectedRandomBoard = -1;
  private String baseName;
  private List<Player> players;

  public SalRuleSelectionController(SnakesAndLaddersFactory factory) {
    this.factory = factory;
    this.boardJsonHandler = new BoardJsonHandler();
  }

  public void setNavigationHandler(NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  public void registerViewObserver(SalRuleSelectionViewObserver observer) {
    observers.add(observer);
  }

  public void initializeDefaultSettings() {
    setDifficulty("default");
    setDiceCount(1);
  }

  public void setDifficulty(String difficulty) {
    this.selectedDifficulty = difficulty;

    switch (difficulty.toLowerCase()) {
      case "easy":
        ladderCount = 10;
        snakeCount = 4;
        break;
      case "hard":
        ladderCount = 5;
        snakeCount = 10;
        break;
      case "random":
        selectRandomBoard();
        return;
      default:
        ladderCount = 8;
        snakeCount = 8;
        break;
    }

    notifyDifficultyChanged(difficulty);
    notifyLadderCountChanged(ladderCount);
    notifySnakeCountChanged(snakeCount);
  }

  public void selectRandomBoard() {
    selectedRandomBoard = new Random().nextInt(8) + 1;
    selectedDifficulty = "random";

    try {
      String boardPath = "data/custom_boards/snakes_and_ladders/random" + selectedRandomBoard + ".json";
      Board board = boardJsonHandler.loadBoardFromFile(boardPath);

      ladderCount = board.getLadders().size();
      snakeCount = board.getSnakes().size();

      logger.info("Selected random board {}: {} ladders, {} snakes",
          selectedRandomBoard, ladderCount, snakeCount);

      notifyDifficultyChanged("random");
      notifyRandomBoardSelected(selectedRandomBoard);
      notifyLadderCountChanged(ladderCount);
      notifySnakeCountChanged(snakeCount);
    } catch (Exception e) {
      logger.error("Failed to load random board: {}", e.getMessage());
      AlertUtil.showAlert("Error", "Failed to load a random board. Default settings will be used.");

      ladderCount = 8;
      snakeCount = 8;
      selectedDifficulty = "default";

      notifyDifficultyChanged("default");
      notifyLadderCountChanged(ladderCount);
      notifySnakeCountChanged(snakeCount);
    }
  }

  public void setDiceCount(int count) {
    if (count <= 0) {
      logger.warn("Invalid dice count: {}, defaulting to 1", count);
      count = 1;
    }
    this.diceCount = count;
    notifyDiceCountChanged(count);
  }

  public void setBaseName(String baseName) {
    this.baseName = baseName;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public int getLadderCount() {
    return ladderCount;
  }

  public int getSnakeCount() {
    return snakeCount;
  }

  public int getSelectedRandomBoard() {
    return selectedRandomBoard;
  }

  public String getBoardFile() {
    if ("random".equals(selectedDifficulty)) {
      return "data/custom_boards/snakes_and_ladders/random" + selectedRandomBoard + ".json";
    } else {
      return "data/custom_boards/snakes_and_ladders/" + selectedDifficulty + ".json";
    }
  }

  public String getCsvFileName() {
    return "data/user-data/player-files/" + baseName + ".csv";
  }

  public SnakesAndLadders createGame() {
    try {
      String boardFile = getBoardFile();
      String csvPath = getCsvFileName();

      SnakesAndLadders game = boardJsonHandler.loadGameFromFile(boardFile, SnakesAndLadders::new);

      int playersLoaded = game.loadPlayersFromCsv(csvPath);
      logger.info("Loaded {} players from {}", playersLoaded, csvPath);

      game.setDice(new Dice(diceCount));
      return game;
    } catch (Exception e) {
      logger.error("Error creating game: {}", e.getMessage());
      throw new RuntimeException("Failed to create game", e);
    }
  }

  public void startGame() {
    try {
      validateGameSettings();
      navigationHandler.navigateTo("GAME_SCREEN");
    } catch (NumberFormatException e) {
      logger.error("Error with starting game: {}", e.getMessage());
      AlertUtil.showAlert("Invalid Input", "Please enter a valid number for dice.");
    } catch (Exception e) {
      logger.error("Error starting game: {}", e.getMessage());
      AlertUtil.showAlert("Game Error", "An error occurred while starting the game: " + e.getMessage());
    }
  }

  private void validateGameSettings() {
    if (diceCount <= 0) {
      throw new InvalidGameConfigurationException("Dice count must be larger than 0");
    }

    if (players == null || players.isEmpty()) {
      throw new InvalidGameConfigurationException("No players specified");
    }
  }

  // Observer notification methods
  private void notifyDifficultyChanged(String difficulty) {
    for (SalRuleSelectionViewObserver observer : observers) {
      observer.onDifficultyChanged(difficulty);
    }
  }

  private void notifyLadderCountChanged(int count) {
    for (SalRuleSelectionViewObserver observer : observers) {
      observer.onLadderCountChanged(count);
    }
  }

  private void notifySnakeCountChanged(int count) {
    for (SalRuleSelectionViewObserver observer : observers) {
      observer.onSnakeCountChanged(count);
    }
  }

  private void notifyRandomBoardSelected(int boardNumber) {
    for (SalRuleSelectionViewObserver observer : observers) {
      observer.onRandomBoardSelected(boardNumber);
    }
  }

  private void notifyDiceCountChanged(int count) {
    for (SalRuleSelectionViewObserver observer : observers) {
      observer.onDiceCountChanged(count);
    }
  }
}
