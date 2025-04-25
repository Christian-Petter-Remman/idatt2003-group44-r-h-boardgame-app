package edu.ntnu.idi.idatt.mainapp;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.controller.common.IntroScreenController;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.common.IntroScreenView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main extends Application {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  @Override
  public void start(Stage primaryStage) {

    try {
      FileManager.ensureApplicationDirectoriesExist();

      NavigationManager navigationManager = NavigationManager.getInstance();
      navigationManager.initialize(primaryStage);

      primaryStage.setTitle("BoardGame App");
      primaryStage.setFullScreenExitHint("");

      IntroScreenController introController = new IntroScreenController();

      logger.info("Initializing IntroScreenController: {}", introController);

      IntroScreenView introView = new IntroScreenView(introController);

      logger.info("Initializing IntroScreenView: {}", introView);

      navigationManager.setRoot(introView.getRoot());
      primaryStage.show();
      Platform.runLater(() -> primaryStage.setFullScreen(true));

    } catch (Exception e) {
      logger.error("Error during startup: {}", e.getMessage(), e);
      showAlert("Startup Error", "Could not start the game");
    }
  }
}
