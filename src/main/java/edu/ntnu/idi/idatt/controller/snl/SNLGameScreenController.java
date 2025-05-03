package edu.ntnu.idi.idatt.controller.snl;

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
import java.util.List;
import java.util.stream.Collectors;

public class SNLGameScreenController implements NavigationHandler {

  private static final Logger logger = LoggerFactory.getLogger(SNLGameScreenController.class);
  private final SNLGame game;
  private final String savePath;

  public SNLGameScreenController(SNLGame game, String savePath) {
    this.game = game;
    this.savePath = savePath;
  }

  public void registerObserver(GameScreenObserver observer) {
    game.addMoveObserver(observer);
    game.addTurnObserver(observer);
    game.addWinnerObserver(observer);
    game.addSaveObserver(observer);
  }

  public void notifyPlayerPositionChangedAll() {
    Player current = game.getCurrentPlayer();
    game.notifyMoveObservers(current, 0);
  }

  public AbstractBoard getBoard() {
    return game.getBoard();
  }

  public List<Player> getPlayersAtPosition(int position) {
    return game.getPlayers().stream()
        .filter(p -> p.getPosition() == position)
        .collect(Collectors.toList());
  }

  public Image getCurrentPlayerImage() {
    Player p = game.getCurrentPlayer();
    if (p != null && p.getCharacter() != null) {
      String name = p.getCharacter().toLowerCase();
      URL url = getClass().getResource("/player_icons/" + name + ".png");
      if (url != null) {
        return new Image(url.toExternalForm());
      }
      logger.warn("No image found for character: {}", name);
    }
    return null;
  }

  public void handleRoll() {
    game.playTurn();
  }

  public String getTileColor(int tileNum) {
    return (tileNum % 2 == 0) ? "#f0f0f0" : "#d0d0d0";
  }

  public void saveGame() {
    game.saveGame(savePath);
  }

  @Override
  public void navigateTo(String destination) {
    NavigationManager.getInstance().navigateTo(NavigationTarget.valueOf(destination));
  }

  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
  }

  @Override
  public void setRoot(Parent root) {
    NavigationManager.getInstance().setRoot(root);
  }

  public SNLGame getGame() {
    return game;
  }
}
