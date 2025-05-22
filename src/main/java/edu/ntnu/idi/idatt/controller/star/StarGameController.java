package edu.ntnu.idi.idatt.controller.star;

import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.Tile;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.stargame.Bridge;
import edu.ntnu.idi.idatt.model.stargame.Jail;
import edu.ntnu.idi.idatt.model.stargame.Path;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import edu.ntnu.idi.idatt.model.stargame.StarGame;
import edu.ntnu.idi.idatt.model.stargame.Tunnel;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <h1>StarGameController</h1>
 * Controller class for managing the StarGame logic and navigation. It acts as a bridge between the
 * game model, the observers, and the navigation system.
 *
 * @author Oliver, Christian
 */
public class StarGameController implements NavigationHandler {

  private static final Logger logger = LoggerFactory.getLogger(StarGameController.class);

  private final StarGame game;
  private final List<GameScreenObserver> observers = new ArrayList<>();
  private final File csvFile;
  private Parent root;

  /**
   * <h2>Constructor.</h2>
   *
   * @param game    the StarGame instance to control.
   * @param csvFile the file where the game state is stored.
   */
  public StarGameController(StarGame game, File csvFile) {
    this.game = game;
    this.csvFile = csvFile;
  }

  /**
   * <h2>Registers an observer for game screen updates.</h2>
   *
   * @param observer the observer to be registered
   */
  public void registerObserver(GameScreenObserver observer) {
    observers.add(observer);
    game.addMoveObserver(observer);
    game.addTurnObserver(observer);
    game.addWinnerObserver(observer);
  }

  /**
   * <h2>Returns the CSV file for the current game session.</h2>
   *
   * @return CSV file
   */
  public File getCsvFile() {
    return csvFile;
  }

  /**
   * <h2>Gets the list of players in the game.</h2>
   *
   * @return list of players
   */
  public List<Player> getPlayers() {
    return game.getPlayers();
  }

  /**
   * <h2>Returns the game board.</h2>
   *
   * @return the current game board
   */
  public AbstractBoard getBoard() {
    return game.getBoard();
  }

  /**
   * <h2>Returns the current dice count.</h2>
   *
   * @return number of dice
   */
  public int getDiceCount() {
    return game.getDiceCount();
  }

  /**
   * <h2>Gets the player whose turn it is currently.</h2>
   *
   * @return current player
   */
  public Player getCurrentPlayer() {
    return game.getCurrentPlayer();
  }

  /**
   * <h2>Returns the StarGame instance.</h2>
   *
   * @return game object
   */
  public StarGame getGame() {
    return game;
  }

  /**
   * <h2>Triggers a player turn.</h2>
   */
  public void handleRoll() {
    game.playTurn();
  }

  /**
   * <h2>Gets the index of the current player's turn.</h2>
   *
   * @return index of current player
   */
  public int getCurrentTurn() {
    return game.getCurrentPlayerIndex();
  }

  /**
   * <h2>Gets the color to display for a given tile.</h2>
   *
   * @param tileNum the tile number
   * @return color string
   */
  public String getTileColor(int tileNum) {
    Tile tile = getBoard().getTile(tileNum);

    for (Jail jail : ((StarBoard) getBoard()).getJailTiles()) {
      if (tileNum == jail.getStart() + 1) {
        return "red";
      }
    }

    if (tile.hasAttribute(Bridge.class)) {
      return "yellow";
    }
    if (tile.hasAttribute(Tunnel.class)) {
      return "purple";
    }
    if (tile.hasAttribute(Path.class)) {
      return "blue";
    }
    if (tileNum == 1000) {
      return "black";
    }

    return (tileNum % 2 == 0) ? "#f0f0f0" : "#d0d0d0";
  }

  /**
   * <h2>Gets all players currently located at a given tile.</h2>
   *
   * @param position tile index
   * @return list of players on the tile
   */
  public List<Player> getPlayersAtPosition(int position) {
    return game.getPlayers().stream()
        .filter(p -> p.getPosition() == position)
        .collect(Collectors.toList());
  }

  /**
   * <h2>Notifies all observers of each player's current position.</h2>
   */
  public void notifyPlayerPositionChangedAll() {
    game.getPlayers().forEach(player -> {
      int pos = player.getPosition();
      notifyPlayerPositionChanged(player, pos, pos);
    });
  }

  /**
   * <h2>Notifies observers of a specific player's movement.</h2>
   *
   * @param player      the player that moved
   * @param oldPosition the previous position
   * @param newPosition the new position
   */
  public void notifyPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
    observers.forEach(o -> o.onPlayerPositionChanged(player, oldPosition, newPosition));
  }

  /**
   * <h2>Saves the game permanently.</h2>
   *
   * @param tempFile temporary save file
   * @param filename final filename
   */
  public void saveGame(File tempFile, String filename) {
    FileManager.saveGameToPermanent(tempFile, "star", filename);
  }

  /**
   * <h2>Deletes the given game file.</h2>
   *
   * @param tempFile file to delete
   */
  public void deleteGame(File tempFile) {
    FileManager.deletePermanentGame(tempFile);
  }

  /**
   * <h2>Navigates to a specified destination.</h2>
   *
   * @param destination navigation target
   */
  @Override
  public void navigateTo(String destination) {
    if (destination.equals("INTRO_SCREEN")) {
      NavigationManager.getInstance().navigateTo(NavigationTarget.START_SCREEN);
    } else {
      logger.warn("Unknown destination: {}", destination);
    }
  }

  /**
   * <h2>Handles back navigation.</h2>
   */
  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
  }

  /**
   * <h2>Sets the root node of the scene.</h2>
   *
   * @param root the root node
   */
  @Override
  public void setRoot(Parent root) {
    this.root = root;
  }

  /**
   * <h2>Returns the root node.</h2>
   *
   * @return root node
   */
  public Parent getRoot() {
    return root;
  }
}