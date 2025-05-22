
package edu.ntnu.idi.idatt.controller.common.load;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.filehandling.GameStateCsvLoader;
import edu.ntnu.idi.idatt.model.common.factory.SNLFactory;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.SNLGame;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.snl.SNLGameScreenView;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * <h1>SNLLoadGameController</h1>
 * This controller handles the loading of saved Snakes and Ladders games.
 * It reads saved game files, parses them into game state objects, and launches
 * the game screen accordingly.
 *
 * <p>This class follows the NavigationHandler interface to support screen transitions.</p>
 *
 * @author NTNU
 */
public class SNLLoadGameController implements NavigationHandler {

  private static final Logger logger = LoggerFactory.getLogger(SNLLoadGameController.class);
  private static final String SAVE_DIR = "saves/load/snl";

  /**
   * <h2>getRecentSaveFiles</h2>
   * Retrieves the most recently modified save files in descending order.
   *
   * @param count Number of recent save files to retrieve.
   * @return An array of File objects representing the save files.
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
   * <h2>loadSNLGame</h2>
   * Loads a Snakes and Ladders game from a given CSV file.
   * If successful, it initializes the game and navigates to the game screen.
   *
   * @param file The File object pointing to the saved CSV file.
   */
  public void loadSNLGame(File file) {
    try {
      GameStateCsvLoader.GameState gameState = GameStateCsvLoader.SNLLoad(file.getAbsolutePath());

      String boardPath = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + gameState.getBoardFile();

      SNLBoard board = new SNLFactory().loadBoardFromFile(boardPath);
      SNLGame game = new SNLGame(board, gameState.getPlayers(), gameState.getDiceCount(), gameState.getCurrentTurnIndex());

      SNLGameScreenController controller = new SNLGameScreenController(game, file, boardPath);
      SNLGameScreenView view = new SNLGameScreenView(controller);

      NavigationManager.getInstance().setHandler(controller);
      NavigationManager.getInstance().setRoot(view.getRoot());

      logger.info("Loaded SNL game from save: {}", file.getName());

    } catch (IOException e) {
      logger.error("Failed to load game from file: {}", file.getName(), e);
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Could not load game");
      alert.setContentText("Failed to load: " + file.getName());
      alert.showAndWait();
    }
  }

  /**
   * <h2>navigateTo</h2>
   * Unused navigation method (no implementation).
   *
   * @param destination Unused parameter.
   */
  @Override
  public void navigateTo(String destination) {
  }

  /**
   * <h2>navigateBack</h2>
   * Unused navigation method (no implementation).
   */
  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
  }

  /**
   * <h2>setRoot</h2>
   * Unused root setter method (no implementation).
   *
   * @param root Unused parameter.
   */
  @Override
  public void setRoot(Parent root) {

  }
}
