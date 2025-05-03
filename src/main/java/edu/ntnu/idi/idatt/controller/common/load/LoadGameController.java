
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

public class LoadGameController implements NavigationHandler {

  private static final Logger logger = LoggerFactory.getLogger(LoadGameController.class);
  private static final String SAVE_DIR = "data/saved_games/star";

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

  public void loadStarGame(File file) {
    try {
      GameStateCsvLoader.GameState gameState = GameStateCsvLoader.StarLoad(file.getName());
      StarFactory factory = new StarFactory();
      StarBoard board = factory.loadBoardFromFile(gameState.getBoardFile());
      StarGame game = new StarGame(board,gameState.getPlayers(), gameState.getCurrentTurnIndex());
      StarGameController controller = new StarGameController(game);
      StarGameView view = new StarGameView(controller);
      view.initializeUI();
      NavigationManager.getInstance().setHandler(controller);
      NavigationManager.getInstance().setRoot(view.getRoot());
    } catch (IOException e) {
      logger.error("Failed to load game from file: {}", file.getName(), e);
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Could not load game");
      alert.setContentText("Failed to load: " + file.getName());
      alert.showAndWait();
    }
  }

  @Override
  public void navigateTo(String destination) {
  }

  @Override
  public void navigateBack() {

  }

  @Override
  public void setRoot(Parent root) {

  }
}
