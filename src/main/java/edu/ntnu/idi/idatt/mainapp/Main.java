package edu.ntnu.idi.idatt.mainapp;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import javafx.application.Application;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;


public class Main extends Application {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private final NavigationManager navigationManager = NavigationManager.getInstance();

  public void start(Stage primaryStage) {
    try {
      FileManager.ensureApplicationDirectoriesExist();
      cleanTempSaves();

      NavigationManager.getInstance().initialize(primaryStage);
      NavigationManager.getInstance().navigateTo(NavigationTarget.START_SCREEN);
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

  public static void cleanTempSaves() {
    File tempDir = new File("saves/temp/");
    if (tempDir.exists()) {
      for (File file : tempDir.listFiles()) {
        file.delete();
      }
      logger.info("Cleaned up all temporary saves.");
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
