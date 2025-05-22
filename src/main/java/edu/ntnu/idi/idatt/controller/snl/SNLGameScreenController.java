package edu.ntnu.idi.idatt.controller.snl;

import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.SNLGame;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <h1>SNLGameScreenController</h1>
 * Controller for Snakes and Ladders game screen. Manages player state, game actions, observers, and
 * navigation.
 *
 * @author Oliver, Christian
 */
public class SNLGameScreenController implements NavigationHandler {

  private static final Logger logger = LoggerFactory.getLogger(SNLGameScreenController.class);

  private final SNLGame game;
  private final List<GameScreenObserver> observers = new ArrayList<>();
  private final File csvFile;
  private final String boardPath;
  private Parent root;

  /**
   * <h2>Constructor</h2>
   * Initializes controller with game instance, save file, and board path.
   *
   * @param game      the SNL game instance
   * @param csvFile   the file containing game state
   * @param boardPath the path to the board file
   */
  public SNLGameScreenController(SNLGame game, File csvFile, String boardPath) {
    this.game = game;
    this.csvFile = csvFile;
    this.boardPath = boardPath;
  }

  /**
   * <h2>Registers an observer for game screen events.</h2>
   *
   * @param observer the observer to register
   */
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

  public int getCurrentPlayerIndex() {
    return game.getCurrentPlayerIndex();
  }

  public int getDiceCount() {
    return game.getDiceCount();
  }

  public String getBoardPath() {
    return boardPath;
  }

  /**
   * <h2>Returns shortened file name from full board path.</h2>
   *
   * @param boardPath full path to the board file
   * @return file name only
   */
  public String getShortenBoardPath(String boardPath) {
    return Paths.get(boardPath).getFileName().toString();
  }


  public SNLGame getGame() {
    return game;
  }

  /**
   * <h2>Executes a player turn by rolling the dice.</h2>
   */
  public void handleRoll() {
    game.playTurn();
  }

  /**
   * <h2>Returns tile color for a given tile number.</h2>
   *
   * @param tileNum the tile number
   * @return color in hex format
   */
  public String getTileColor(int tileNum) {
    return (tileNum % 2 == 0) ? "#f0f0f0" : "#d0d0d0";
  }

  /**
   * <h2>Finds players located at a specific position.</h2>
   *
   * @param position the tile position
   * @return list of players at that position
   */
  public List<Player> getPlayersAtPosition(int position) {
    return game.getPlayers().stream()
        .filter(player -> player.getPosition() == position)
        .collect(Collectors.toList());
  }

  /**
   * <h2>Sends player position updates to all observers.</h2>
   */
  public void notifyPlayerPositionChangedAll() {
    game.getPlayers()
        .forEach(p -> notifyPlayerPositionChanged(p, p.getPosition(), p.getPosition()));
  }

  /**
   * <h2>Notifies observers of a single player's position update.</h2>
   *
   * @param player      the player
   * @param oldPosition the old position
   * @param newPosition the new position
   */
  public void notifyPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
    observers.forEach(o -> o.onPlayerPositionChanged(player, oldPosition, newPosition));
  }

  /**
   * <h2>Saves the game to the permanent directory.</h2>
   *
   * @param tempFile the temporary game file
   * @param filename the filename to save as
   */
  public void saveGame(File tempFile, String filename) {
    FileManager.saveGameToPermanent(tempFile, "snl", filename);
  }

  /**
   * <h2>Deletes a saved game file from permanent storage.</h2>
   *
   * @param tempFile the file to delete
   */
  public void deleteGame(File tempFile) {
    FileManager.deletePermanentGame(tempFile);
  }

  @Override
  public void navigateTo(String destination) {

    if (destination.equals("INTRO_SCREEN")) {
      NavigationManager.getInstance().navigateTo(NavigationTarget.START_SCREEN);
    } else {
      logger.warn("Unknown destination: {}", destination);
    }
  }

  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
  }

  @Override
  public void setRoot(Parent root) {
    this.root = root;
  }

  public Parent getRoot() {
    return root;
  }
}