package edu.ntnu.idi.idatt.controller.common.load;

import edu.ntnu.idi.idatt.filehandling.GameStateCsvLoader;
import edu.ntnu.idi.idatt.model.common.factory.StarFactory;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.star.StarGameView;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import edu.ntnu.idi.idatt.model.stargame.StarGame;
import edu.ntnu.idi.idatt.controller.star.StarGameController;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * <h1>StarLoadGameController</h1>
 * Responsible for loading saved Star Game files and initializing the corresponding view and controller.
 * Handles reading from the save directory, parsing CSV files, and transitioning the UI.
 *
 * @author Your Name
 */
public class StarLoadGameController implements NavigationHandler {

  private static final Logger logger = LoggerFactory.getLogger(StarLoadGameController.class);
  private static final String SAVE_DIR = "saves/load/star";

  /**
   * <h2>getRecentSaveFiles</h2>
   * Retrieves a list of the most recently modified Star Game save files from the designated save directory.
   *
   * @param count the maximum number of recent files to return
   * @return an array of the most recent save files
   */
  public File[] getRecentSaveFiles(int count) {
    File saveFolder = new File(SAVE_DIR);
    if (!saveFolder.exists() || !saveFolder.isDirectory()) {
      logger.warn("Save directory not found: {}", SAVE_DIR);
      return new File[0];
    }

    File[] csvFiles = saveFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
    if (csvFiles == null) return new File[0];

    return Arrays.stream(csvFiles)
            .sorted(Comparator.comparingLong(File::lastModified).reversed())
            .limit(count)
            .toArray(File[]::new);
  }

  /**
   * <h2>loadStarGame</h2>
   * Loads a saved Star Game from a given CSV file, reconstructs the board and players,
   * and initializes the game controller and view.
   *
   * @param file the CSV file to load the game from
   */
  public void loadStarGame(File file) {
    try {
      GameStateCsvLoader.GameState gameState = GameStateCsvLoader.StarLoad(file.getAbsolutePath());
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
   * Navigation not implemented for this controller.
   *
   * @param destination unused
   */
  @Override
  public void navigateTo(String destination) {
    // Intentionally left blank
  }

  /**
   * <h2>navigateBack</h2>
   * Navigation back not implemented for this controller.
   */
  @Override
  public void navigateBack() {
    // Intentionally left blank
  }

  /**
   * <h2>setRoot</h2>
   * Root assignment not used in this controller as NavigationManager handles it.
   *
   * @param root the parent node
   */
  @Override
  public void setRoot(Parent root) {
    // Intentionally left blank
  }
}