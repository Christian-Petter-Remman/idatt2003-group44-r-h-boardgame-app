package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.SNLGame;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.util.AlertUtil;
import javafx.scene.Parent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SNLGameScreenController implements NavigationHandler {

  private static final Logger logger = LoggerFactory.getLogger(SNLGameScreenController.class);

  private final SNLGame game;
  private final List<GameScreenObserver> observers = new ArrayList<>();
  private Parent root;

  public SNLGameScreenController(SNLGame game) {
    this.game = game;
  }

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

  // NavigationHandler methods
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

  @Override
  public void setRoot(Parent root) {
    this.root = root;
  }

  public Parent getRoot() {
    return root;
  }
}