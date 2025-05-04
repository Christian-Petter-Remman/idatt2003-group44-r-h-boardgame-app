package edu.ntnu.idi.idatt.controller.snl;

import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.SNLGame;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import javafx.scene.Parent;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
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
    game.addMoveObserver(observer);
    game.addTurnObserver(observer);
    game.addWinnerObserver(observer);
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

  public List<Player> getAllPlayers() {
    return game.getPlayers();
  }

  public SNLGame getGame() {
    return game;
  }

  public void handleRoll() {
    game.playTurn();
  }

  public String getTileColor(int tileNum) {
    return (tileNum % 2 == 0) ? "#f0f0f0" : "#d0d0d0";  // Light and dark colors
  }

  public List<Player> getPlayersAtPosition(int position) {
    List<Player> playersAtPosition = new ArrayList<>();
    for (Player player : game.getPlayers()) {
      if (player.getPosition() == position) {
        playersAtPosition.add(player);
      }
    }
    return playersAtPosition;
  }

  public void initializeGameScreen() {
    notifyPlayerPositionChangedAll();
  }

  public void notifyPlayerPositionChangedAll() {
    for (Player player : game.getPlayers()) {
      int pos = player.getPosition();
      notifyPlayerPositionChanged(player, pos, pos);
    }
  }

  public void notifyPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
    for (GameScreenObserver observer : observers) {
      observer.onPlayerPositionChanged(player, oldPosition, newPosition);
    }
  }

  public void saveGame(File tempFile, String filename) {
    FileManager.saveGameToPermanent(tempFile,"snl",filename);
  }

  @Override
  public void navigateTo(String destination) {

    // Implement navigation handling logic
    switch (destination) {
      case "INTRO_SCREEN":
        NavigationManager.getInstance().navigateTo(NavigationTarget.START_SCREEN);
        break;
      default:
        logger.warn("Unknown destination: {}", destination);
        break;
    }
  }

  @Override
  public void navigateBack() {
    // Implement navigation back logic
  }

  @Override
  public void setRoot(Parent root) {
    this.root = root;
  }

  public Parent getRoot() {
    return root;
  }
}