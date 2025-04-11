package edu.ntnu.idi.idatt.controller.snakesandladders;

import edu.ntnu.idi.idatt.model.model_observers.DifficultyObserver;
import edu.ntnu.idi.idatt.exceptions.*;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.Player;
import java.util.ArrayList;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SnakesAndLaddersRuleSelectionController {
  private static final Logger logger = LoggerFactory.getLogger(SnakesAndLaddersRuleSelectionController.class);

  private final List<DifficultyObserver> observers = new ArrayList<>();
  private final SnakesAndLaddersFactory factory;
  private SnakesAndLadders currentGame;
  private String selectedDifficulty = "default";

  public SnakesAndLaddersRuleSelectionController(SnakesAndLaddersFactory factory) {
    this.factory = factory;
  }

  public void addObserver(DifficultyObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(DifficultyObserver observer) {
    observers.remove(observer);
  }

  public void notifyObservers(String difficulty) {
    for (DifficultyObserver observer : observers) {
      observer.onDifficultyChanged(difficulty);
    }
  }

  public void setDifficulty(String difficulty) {
    this.selectedDifficulty = difficulty;
    notifyObservers(difficulty);
  }

  public SnakesAndLadders startGame(String difficulty, int diceCount, int ladderCount,
      int penaltyCount, List<Player> players)
      throws InvalidGameConfigurationException {
    validateDifficulty(difficulty);
    validateInput(diceCount, ladderCount, penaltyCount);
    try {
      currentGame = (SnakesAndLadders) factory.createBoardGameFromConfiguration(difficulty);
      currentGame.setDice(new Dice(diceCount));

      if (!difficulty.equals("default")) {
        adjustBoard(currentGame.getBoard(), ladderCount, penaltyCount);
      }

      addPlayers(players);
      currentGame.initialize(currentGame.getBoard());

      logger.info("Game created with difficulty: {}, dice: {}, ladders: {}, penalties: {}",
          difficulty, diceCount, ladderCount, penaltyCount);
      return currentGame;

    } catch (IllegalArgumentException e) {
      logger.error("Failed to create game: {}", e.getMessage());
      throw new InvalidGameConfigurationException("Invalid game configuration: " + e.getMessage());
    }
  }

  public void validateInput(int diceCount, int ladderCount, int penaltyCount) {
    if (diceCount <= 0) {
      throw new InvalidGameConfigurationException("Dice count must be larger than 0");
    }
    if (ladderCount < 0 || penaltyCount < 0) {
      throw new InvalidGameConfigurationException("Ladders and penalties cannot be negative");
    }
  }

  private void validateDifficulty(String difficulty) throws InvalidGameConfigurationException {
    if (!Arrays.asList(factory.getAvailableConfigurations()).contains(difficulty)) {
      throw new InvalidGameConfigurationException("Invalid difficulty level: " + difficulty);
    }
  }

  private void adjustBoard(Board board, int ladderCount, int penaltyCount) {
    board.initializeEmptyBoard();
    addRandomLadders(board, ladderCount);
    addRandomSnakes(board, penaltyCount);
  }

  private void addRandomLadders(Board board, int count) {
    for (int i = 0; i < count; i++) {
      try {
        board.addRandomLadder();
      } catch (IllegalStateException e) {
        logger.warn("Could not add more ladders: {}", e.getMessage());
        break;
      }
    }
    logger.info("Added {} ladders", count);
  }

  private void addRandomSnakes(Board board, int count) {
    for (int i = 0; i < count; i++) {
      try {
        board.addRandomSnake();
      } catch (IllegalStateException e) {
        logger.warn("Could not add more snakes: {}", e.getMessage());
        break;
      }
    }
    logger.info("Added {} snakes", count);
  }

  private void addPlayers(List<Player> players) {
    if (players == null || players.isEmpty()) {
      throw new IllegalArgumentException("At least one player is required");
    }
    players.forEach(currentGame::addPlayer);
  }

  public SnakesAndLadders getCurrentGame() {
    return currentGame;
  }

  public String getSelectedDifficulty() {
    return selectedDifficulty;
  }

  public void setSelectedDifficulty(String selectedDifficulty) {
    this.selectedDifficulty = selectedDifficulty;
  }
}
