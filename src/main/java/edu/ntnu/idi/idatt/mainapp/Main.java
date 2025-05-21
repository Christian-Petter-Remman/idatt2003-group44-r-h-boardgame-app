package edu.ntnu.idi.idatt.mainapp;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import javafx.application.Application;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

/**
 * <h1>Main</h1>
 *
 * Entry point for the JavaFX application. Initializes directory structure, navigation, and main UI.
 * Also handles startup exceptions and cleanup of temporary saved games.
 */
public class Main extends Application {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private final NavigationManager navigationManager = NavigationManager.getInstance();

  /**
   * <h2>start</h2>
   *
   * Launches the JavaFX application. Sets up directories, UI, and navigation.
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
      primaryStage.setResizable(true);
      primaryStage.show();

    } catch (Exception e) {
      logger.error("Error during startup: {}", e.getMessage(), e);
      showAlert("Startup Error", "Could not start the game");
    }
  }

  /**
   * <h2>cleanTempSaves</h2>
   *
   * Deletes all files in the `saves/temp/` directory to ensure no leftover temporary save files exist.
   */
  public static void cleanTempSaves() {
    File tempDir = new File("saves/temp/");
    if (tempDir.exists() && tempDir.isDirectory()) {
      File[] files = tempDir.listFiles();
      if (files != null) {
        Arrays.stream(files).forEach(File::delete);
        logger.info("Cleaned up all temporary saves.");
      }
    }
  }

  /**
   * <h2>main</h2>
   *
   * Main method for launching the application via `Application.launch`.
   *
   * @param args command-line arguments (not used).
   */
  public static void main(String[] args) {
    launch(args);
  }
}