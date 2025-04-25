package edu.ntnu.idi.idatt.navigation;

import edu.ntnu.idi.idatt.controller.common.IntroScreenController;
import edu.ntnu.idi.idatt.view.common.IntroScreenView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public class NavigationManager {
  private static final Logger logger = LoggerFactory.getLogger(NavigationManager.class);
  private static NavigationManager navigator;
  private Stage primaryStage;
  private final Stack<Parent> navigationStack = new Stack<>();

  private NavigationManager() {}

  public static NavigationManager getInstance() {
    if (navigator == null) {
      navigator = new NavigationManager();
    }
    return navigator;
  }

  public void initialize(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void setRoot(Parent root) {
    if (primaryStage.getScene() == null) {
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      logger.info("Created new scene with root");
    } else {
      navigationStack.push(primaryStage.getScene().getRoot());
      primaryStage.getScene().setRoot(root);
      logger.info("Changed scene root and saved previous to stack");
    }
    primaryStage.show();
  }

  public void navigateBack() {
    if (!navigationStack.isEmpty()) {
      Parent previousRoot = navigationStack.pop();
      primaryStage.getScene().setRoot(previousRoot);
      logger.info("Navigated back to previous screen");
    } else {
      logger.warn("Cannot navigate back - navigation stack is empty - Returning to main menu");
      IntroScreenController introController = new IntroScreenController();
      IntroScreenView introScreenView = new IntroScreenView(introController);
      primaryStage.getScene().setRoot(introScreenView.getRoot());
      logger.info("Navigated back to Main Screen");
    }
  }
}
