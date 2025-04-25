package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.util.AlertUtil;
import edu.ntnu.idi.idatt.view.common.GameScreenView;
import edu.ntnu.idi.idatt.view.common.IntroScreenView;
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
  private final String csvFileName;
  private final List<GameScreenObserver> observers = new ArrayList<>();

  public GameScreenController(SnakesAndLadders game, String boardFile, String csvFileName) {
    this.game = game;
    this.boardFile = boardFile;
    this.csvFileName = csvFileName;
  }

  public void registerObserver(GameScreenObserver observer) {
    observers.add(observer);
  }

  public void displayGameScreen(GameScreenView view) {
    NavigationManager.getInstance().setRoot(view.getRoot());
  }

  public List<Player> getPlayers() {
    return game.getPlayers();
  }

  public Board getBoard() {
    return game.getBoard();
  }

  public Player getCurrentPlayer() {
    return game.getCurrentPlayer();
  }

  public List<String> getCharacterNames() {
    return game.getCharacterNames();
  }

  public SnakesAndLadders getGame() {
    return game;
  }

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
    pause.setOnFinished(event -> {
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
    });
    pause.play();
  }

  public void saveGame() {
    try {
      List<String> lines = new ArrayList<>();
      lines.add("Board:" + boardFile);

      for (Player player : game.getPlayers()) {
        lines.add(player.getName() + "," + player.getCharacter() + "," + player.getPosition());
      }

      Files.write(Paths.get(csvFileName), lines);
      logger.info("Game saved to {}", csvFileName);
      notifyGameSaved(csvFileName);
    } catch (IOException e) {
      logger.error("Failed to save game: {}", e.getMessage());
      AlertUtil.showAlert("Save Error", "Failed to save game: " + e.getMessage());
    }
  }

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

  @Override
  public void navigateTo(String destination) {
    switch (destination) {
      case "INTRO_SCREEN":
        logger.info("Navigated to Intro Screen");
        break;

      default:
        logger.warn("Unknown destination: {}", destination);
        break;
    }
  }

  @Override
  public void navigateBack() {
    navigateTo("INTRO_SCREEN");
  }
}
