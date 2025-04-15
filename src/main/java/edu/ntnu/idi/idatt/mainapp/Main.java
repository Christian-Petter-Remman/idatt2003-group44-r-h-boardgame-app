package edu.ntnu.idi.idatt.mainapp;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.view.common.IntroScreenView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main extends Application {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  @Override
  public void start(Stage primaryStage) {
    try {
      FileManager.ensureApplicationDirectoriesExist();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    try {
      IntroScreenView intro = new IntroScreenView(primaryStage);
      intro.prepareScene();

      primaryStage.setTitle("BoardGame App");
      primaryStage.setFullScreenExitHint("");
      primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

      primaryStage.show();

      Platform.runLater(() -> primaryStage.setFullScreen(true));

    } catch (Exception e) {
      logger.error("Error during startup: {}", e.getMessage());
      showAlert("Startup Error", "Could not start the game");
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}