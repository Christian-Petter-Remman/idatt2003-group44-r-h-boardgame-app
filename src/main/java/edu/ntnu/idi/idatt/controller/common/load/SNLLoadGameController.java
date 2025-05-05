
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

public class SNLLoadGameController implements NavigationHandler {

  private static final Logger logger = LoggerFactory.getLogger(SNLLoadGameController.class);
  private static final String SAVE_DIR = "saves/load/snl";

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

  public void loadSNLGame(File file) {
    try {
      GameStateCsvLoader.GameState gameState = GameStateCsvLoader.SNLLoad(file.getAbsolutePath());

      String boardPath = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + gameState.getBoardFile();

      SNLFactory factory = new SNLFactory();
      SNLBoard board = factory.loadBoardFromFile(boardPath);

      SNLGame game = new SNLGame(board, gameState.getPlayers(), gameState.diceCount, gameState.getCurrentTurnIndex());
      SNLGameScreenController controller = new SNLGameScreenController(game,file);
      SNLGameScreenView view = new SNLGameScreenView(controller);
      view.initializeUI();

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
