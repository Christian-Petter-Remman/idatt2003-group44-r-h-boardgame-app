package edu.ntnu.idi.idatt.controller.snl;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.SNLGame;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.util.AlertUtil;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class SNLGameScreenController implements NavigationHandler {

  private static final Logger logger = LoggerFactory.getLogger(SNLGameScreenController.class);

  private final SNLGame game;
  private final List<GameScreenObserver> observers = new ArrayList<>();

  public SNLGameScreenController(SNLGame game) {
    this.game = game;
  }

  // observer

  public void registerObserver(GameScreenObserver observer) {
    observers.add(observer);
  }


  public List<Player> getPlayers() {
    return game.getPlayers();
  }

  public AbstractBoard getBoard() {
    return game.getBoard();
  }

  public Player getCurrentPlayer() {
    return game.getCurrentPlayer();
  }

  public SNLGame getGame() {
    return game;
  }


  public void handleRoll() {
    game.playTurn();
  }


//  public void saveGame() {
//    try {
//      List<String> lines = new ArrayList<>();
//      lines.add("Board:" + boardFile);
//
//      for (Player player : game.getPlayers()) {
//        lines.add(player.getName() + "," + player.getCharacter() + "," + player.getPosition());
//      }
//
//      Files.write(Paths.get(saveFileName), lines);
//      logger.info("Game saved to {}", saveFileName);
//      notifyGameSaved(saveFileName);
//
//    } catch (IOException e) {
//      logger.error("Failed to save game: {}", e.getMessage());
//      AlertUtil.showAlert("Save Error", "Failed to save game: " + e.getMessage());
//    }
//  }

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
