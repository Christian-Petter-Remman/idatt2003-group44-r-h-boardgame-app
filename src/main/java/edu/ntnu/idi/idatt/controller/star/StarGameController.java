package edu.ntnu.idi.idatt.controller.star;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.Tile;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.stargame.*;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StarGameController implements NavigationHandler {


  private static final Logger logger = LoggerFactory.getLogger(StarGameController.class);

  private final StarGame game;
  private final List<GameScreenObserver> observers = new ArrayList<>();
  private Parent root;

  public StarGameController(StarGame game) {
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

  public StarGame getGame() {
    return game;
  }

  public void handleRoll() {
    game.playTurn();
  }


  public String getTileColor(int tileNum) {
    Tile tile = getBoard().getTile(tileNum);


    for (Jail jail : ((StarBoard) getBoard()).getJailTiles()) {
      if (tileNum == jail.getStart() + 1) {
        return "red";
      }
    }

    if (tile.hasAttribute(Bridge.class)) {
      return "yellow";
    } else if (tile.hasAttribute(Tunnel.class)) {
      return "purple";
    } else if (tile.hasAttribute(Path.class)) {
      return "blue";
    }

    if (tileNum == 1000) {
      return "black";
    }

    return (tileNum % 2 == 0) ? "#f0f0f0" : "#d0d0d0";
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

  @Override
  public void navigateTo(String destination) {

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
  }

  @Override
  public void setRoot(Parent root) {
    this.root = root;
  }

  public Parent getRoot() {
    return root;
  }
}