package edu.ntnu.idi.idatt.mainapp;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main extends Application {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  private final NavigationManager navigationManager = NavigationManager.getInstance();

  @Override
  public void start(Stage primaryStage) {

    try {
      FileManager.ensureApplicationDirectoriesExist();

      NavigationManager.getInstance().initialize(primaryStage);

      NavigationManager.getInstance().navigateTo(NavigationManager.NavigationTarget.INTRO_SCREEN);
      primaryStage.show();


      primaryStage.show();
    } catch (Exception e) {
      logger.error("Error during startup: {}", e.getMessage(), e);
      showAlert("Startup Error", "Could not start the game");
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
