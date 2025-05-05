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
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

public class SNLGameScreenController implements NavigationHandler {
  private static final Logger logger = LoggerFactory.getLogger(SNLGameScreenController.class);

  private final SNLGame game;
  private final List<GameScreenObserver> observers = new ArrayList<>();
  private Parent root;
  private final File csvFile;

  public SNLGameScreenController(SNLGame game, File csvFile) {
    this.game = game;
    this.csvFile = csvFile;
  }

  public void registerObserver(GameScreenObserver observer) {
    observers.add(observer);
    game.addMoveObserver(observer);
    game.addTurnObserver(observer);
    game.addWinnerObserver(observer);
  }

  public File getCsvFile() {
    return csvFile;
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

  /**
   * Returns the image for the current player's token,
   * or null if none.
   */
  public Image getCurrentPlayerImage() {
    Player p = getCurrentPlayer();
    if (p != null && p.getCharacter() != null) {
      String name = p.getCharacter().toLowerCase();
      URL url = getClass().getResource("/player_icons/" + name + ".png");
      if (url != null) {
        return new Image(url.toExternalForm());
      } else {
        logger.warn("No icon found for character '{}'", name);
      }
    }
    return null;

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
    return (tileNum % 2 == 0) ? "#f0f0f0" : "#d0d0d0";
  }

  public List<Player> getPlayersAtPosition(int pos) {
    List<Player> out = new ArrayList<>();
    for (Player p : game.getPlayers()) {
      if (p.getPosition() == pos) out.add(p);
    }
    return out;
  }

  // fire off a full update to position for each player
  public void notifyPlayerPositionChangedAll() {
    for (Player p : game.getPlayers()) {
      for (GameScreenObserver o : observers) {
        o.onPlayerPositionChanged(p, p.getPosition(), p.getPosition());
      }
    }
  }


  @Override public void navigateTo(String destination) {
    if ("INTRO_SCREEN".equals(destination)) {
      NavigationManager.getInstance().navigateTo(NavigationTarget.START_SCREEN);
    } else {
      logger.warn("Unknown destination: {}", destination);

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
  @Override public void navigateBack() {}
  @Override public void setRoot(Parent root) { this.root = root; }
  public Parent getRoot() { return root; }
}
