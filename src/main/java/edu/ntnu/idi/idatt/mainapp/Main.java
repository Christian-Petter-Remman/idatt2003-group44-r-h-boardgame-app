package edu.ntnu.idi.idatt.mainapp;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import java.io.File;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>Main</h1>
 *
 * <p>Entry point for the JavaFX application. Initializes directory structure, navigation, and main
 * UI.
 * Also handles startup exceptions and cleanup of temporary saved games.
 */
public class Main extends Application {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private final NavigationManager navigationManager = NavigationManager.getInstance();

  /**
   * <h2>start</h2>
   *
   * <p>Launches the JavaFX application. Sets up directories, UI, and navigation.
   *
   * @param primaryStage the primary stage provided by JavaFX.
   */
  @Override
  public void start(Stage primaryStage) {
    try {
      FileManager.ensureApplicationDirectoriesExist();
      cleanTempSaves();

      navigationManager.initialize(primaryStage);
      navigationManager.navigateTo(NavigationTarget.START_SCREEN);
      navigationManager.setLogo("/images/logo.png");

      primaryStage.setFullScreen(true);
      primaryStage.setFullScreenExitHint("");
      primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
      primaryStage.setResizable(false);

      primaryStage.show();

    } catch (Exception e) {
      logger.error("Error during startup: {}", e.getMessage(), e);
      showAlert("Startup Error", "Could not start the game");
    }
  }

  /**
   * <h2>cleanTempSaves</h2>
   *
   * <p>Deletes all files in the `saves/temp/` directory to ensure no leftover temporary save files
   * exist.
   */
  public static void cleanTempSaves() {
    File tempDir = new File("saves/temp/");
    if (tempDir.exists() && tempDir.isDirectory()) {
      File[] files = tempDir.listFiles();
      if (files != null) {
        int deleted = 0;
        int failed = 0;
        for (File file : files) {
          boolean success = file.delete();
          if (success) {
            deleted++;
          } else {
            failed++;
            logger.warn("Failed to delete temporary save file: {}", file.getAbsolutePath());
          }
        }
        logger.info("Cleaned up temporary saves. Deleted: {}, Failed: {}", deleted, failed);
      }
    }
  }


  /**
   * <h2>main</h2>
   *
   * <p>Main method for launching the application via `Application.launch`.
   *
   * @param args command-line arguments (not used).
   */
  public static void main(String[] args) {
    launch(args);
  }
}
