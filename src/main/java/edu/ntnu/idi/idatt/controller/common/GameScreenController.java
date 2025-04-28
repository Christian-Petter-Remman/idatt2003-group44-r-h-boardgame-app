package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.util.AlertUtil;
import edu.ntnu.idi.idatt.view.common.GameScreenView;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class GameScreenController implements NavigationHandler {

  private static final Logger logger = LoggerFactory.getLogger(GameScreenController.class);

  private final SnakesAndLadders game;
  private final String boardFile;
  private final String saveFileName;
  private final List<GameScreenObserver> observers = new ArrayList<>();

  public GameScreenController(SnakesAndLadders game, String boardFile, String saveFileName) {
    this.game = game;
    this.boardFile = boardFile;
    this.saveFileName = saveFileName;
  }

  // observer

  public void registerObserver(GameScreenObserver observer) {
    observers.add(observer);
  }

  // Model Accessors

  public List<Player> getPlayers() {
    return game.getPlayers();
  }

  public Board getBoard() {
    return game.getBoard();
  }

  public Player getCurrentPlayer() {
    return game.getCurrentPlayer();
  }

  public SnakesAndLadders getGame() {
    return game;
  }

  // Game actions

  public void handleRoll() {
    Player currentPlayer = game.getCurrentPlayer();
    int currentPosition = currentPlayer.getPosition();

    int roll = game.rollDice();
    notifyDiceRolled(roll);

    int tentativePosition = currentPosition + roll;
    if (tentativePosition > 100) {
      logger.info("Invalid move: position {} + roll {} exceeds 100", currentPosition, roll);
      return;
    }

    currentPlayer.setPosition(tentativePosition);
    notifyPlayerPositionChanged(currentPlayer, currentPosition, tentativePosition);

    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    pause.setOnFinished(event -> finalizeMove(currentPlayer, tentativePosition));
    pause.play();
  }

  private void finalizeMove(Player currentPlayer, int tentativePosition) {
    int finalPosition = game.getBoard().getFinalPosition(tentativePosition);

    if (finalPosition != tentativePosition) {
      currentPlayer.setPosition(finalPosition);
      notifyPlayerPositionChanged(currentPlayer, tentativePosition, finalPosition);
    }

    if (currentPlayer.hasWon()) {
      notifyGameOver(currentPlayer);
    } else {
      game.advanceTurn();
      notifyPlayerTurnChanged(game.getCurrentPlayer());
    }
  }

  public void saveGame() {
    try {
      List<String> lines = new ArrayList<>();
      lines.add("Board:" + boardFile);

      for (Player player : game.getPlayers()) {
        lines.add(player.getName() + "," + player.getCharacter() + "," + player.getPosition());
      }

      Files.write(Paths.get(saveFileName), lines);
      logger.info("Game saved to {}", saveFileName);
      notifyGameSaved(saveFileName);

    } catch (IOException e) {
      logger.error("Failed to save game: {}", e.getMessage());
      AlertUtil.showAlert("Save Error", "Failed to save game: " + e.getMessage());
    }
  }

  // Notify Observers

  private void notifyPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
    for (GameScreenObserver observer : observers) {
      observer.onPlayerPositionChanged(player, oldPosition, newPosition);
    }
  }

  private void notifyDiceRolled(int result) {
    for (GameScreenObserver observer : observers) {
      observer.onDiceRolled(result);
    }
  }

  private void notifyPlayerTurnChanged(Player currentPlayer) {
    for (GameScreenObserver observer : observers) {
      observer.onPlayerTurnChanged(currentPlayer);
    }
  }

  private void notifyGameOver(Player winner) {
    for (GameScreenObserver observer : observers) {
      observer.onGameOver(winner);
    }
  }

  private void notifyGameSaved(String filePath) {
    for (GameScreenObserver observer : observers) {
      observer.onGameSaved(filePath);
    }
  }

  // Navigation

  @Override
  public void navigateTo(String destination) {
    switch (destination) {
      case "INTRO_SCREEN":
        NavigationManager.getInstance().navigateTo(NavigationManager.NavigationTarget.INTRO_SCREEN);
        break;
      default:
        logger.warn("Unknown destination: {}", destination);
        break;
    }
  }

  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateTo(NavigationManager.NavigationTarget.INTRO_SCREEN);
  }
}
