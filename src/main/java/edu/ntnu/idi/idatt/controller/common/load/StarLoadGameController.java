
package edu.ntnu.idi.idatt.controller.common.load;

import static edu.ntnu.idi.idatt.controller.common.load.SNLLoadGameController.getFiles;

import edu.ntnu.idi.idatt.controller.star.StarGameController;
import edu.ntnu.idi.idatt.filehandling.GameStateCsvLoader;
import edu.ntnu.idi.idatt.filehandling.GameStateCsvLoader.GameState;
import edu.ntnu.idi.idatt.model.common.factory.StarFactory;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import edu.ntnu.idi.idatt.model.stargame.StarGame;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.star.StarGameView;
import java.io.File;
import java.io.IOException;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <h1>StarLoadGameController</h1>
 * Responsible for loading saved Star Game files and initializing the corresponding view and
 * controller. Handles reading from the save directory, parsing CSV files, and transitioning the
 * UI.
 *
 * @author Oliver, Christian
 */
public class StarLoadGameController implements NavigationHandler {

  private static final Logger logger = LoggerFactory.getLogger(StarLoadGameController.class);
  private static final String SAVE_DIR = "saves/load/star";

  /**
   * <h2>getRecentSaveFiles</h2>
   * Retrieves a list of the most recently modified Star Game save files from the designated save
   * directory.
   *
   * @param count the maximum number of recent files to return
   * @return an array of the most recent save files
   */
  public File[] getRecentSaveFiles(int count) {
    return getFiles(count, SAVE_DIR, logger);
  }

  /**
   * <h2>loadStarGame</h2>
   * Loads a saved Star Game from a given CSV file, reconstructs the board and players, and
   * initializes the game controller and view.
   *
   * @param file the CSV file to load the game from
   */
  public void loadStarGame(File file) {
    try {
      GameState gameState = GameStateCsvLoader.starLoad(file.getAbsolutePath());
      StarBoard board = new StarFactory().loadBoardFromFile(gameState.getBoardFile());
      StarGame game = new StarGame(board, gameState.getPlayers(), gameState.getCurrentTurnIndex());
      StarGameController controller = new StarGameController(game, file);
      StarGameView view = new StarGameView(controller);
      view.initializeUI();

      NavigationManager.getInstance().setHandler(controller);
      NavigationManager.getInstance().setRoot(view.getRoot());

      logger.info("Loaded Star Game from save: {}", file.getName());

    } catch (IOException e) {
      logger.error("Failed to load game from file: {}", file.getName(), e);
      showErrorAlert(file.getName());
    }
  }

  /**
   * <h2>showErrorAlert</h2>
   * Displays an alert if the game fails to load.
   *
   * @param filename the name of the file that failed to load
   */
  private void showErrorAlert(String filename) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Could not load game");
    alert.setContentText("Failed to load: " + filename);
    alert.showAndWait();
  }

  /**
   * <h2>navigateTo</h2>
   * Navigation to a specific destination.
   *
   * @param destination the destination to navigate to
   */
  @Override
  public void navigateTo(String destination) {
  }

  /**
   * <h2>navigateBack</h2>
   * Handles the back navigation in the application.
   */
  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
  }

  /**
   * <h2>setRoot</h2>
   * Root assignment for the navigation handler.
   *
   * @param root the parent node
   */
  @Override
  public void setRoot(Parent root) {

  }
}
