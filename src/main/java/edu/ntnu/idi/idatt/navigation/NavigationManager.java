package edu.ntnu.idi.idatt.navigation;

import edu.ntnu.idi.idatt.controller.common.CharacterSelectionController;
import edu.ntnu.idi.idatt.model.common.screens.CharacterSelectionModel;
import edu.ntnu.idi.idatt.view.common.IntroScreenView;
import edu.ntnu.idi.idatt.view.common.CharacterSelectionView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavigationManager {
  private static final Logger logger = LoggerFactory.getLogger(NavigationManager.class);
  private static NavigationManager instance;
  private Stage primaryStage;
  private final Stack<Parent> navigationStack = new Stack<>();

  public enum NavigationTarget {
    INTRO_SCREEN,
    LOAD_SCREEN,
    CHARACTER_SELECTION,
    RULE_SELECTION,
    GAME_SCREEN
  }

  private NavigationManager() {}

  public static synchronized NavigationManager getInstance() {
    if (instance == null) {
      instance = new NavigationManager();
    }
    return instance;
  }

  public void initialize(Stage primaryStage) {
    this.primaryStage = primaryStage;
    primaryStage.setTitle("Board Game App");
  }

  // Base navigation methods
  public void navigateTo(NavigationTarget target) {
    switch (target) {
      case INTRO_SCREEN -> showIntroScreen();
      case CHARACTER_SELECTION -> navigateToCharacterSelection();
      case LOAD_SCREEN -> {
        logger.info("Navigated to Load Screen");
      }
      case RULE_SELECTION -> navigateToRuleSelection();
      case GAME_SCREEN -> {
        logger.info("Navigated to Game Screen");
      }
      // Add other cases as needed
      default -> logger.warn("Unhandled navigation target: {}", target);
    }
  }

  // Dedicated navigation methods
  public void showIntroScreen() {
    IntroScreenView view = new IntroScreenView();
    setRoot(view.getRoot());
    logger.info("Navigated to Intro Screen");
  }

  public void navigateToCharacterSelection() {
    CharacterSelectionModel model = new CharacterSelectionModel();
    CharacterSelectionController controller = new CharacterSelectionController(model);
    CharacterSelectionView view = new CharacterSelectionView(controller, model);
    setRoot(view.getRoot());
    logger.info("Navigated to Character Selection Screen");
  }

  public void navigateToRuleSelection() {
    // Implementation for rule selection
    logger.info("Navigated to Rule Selection Screen");
  }

  // Core navigation functionality
  public void setRoot(Parent root) { // TODO: Consider changing access modifier to private
    if (primaryStage.getScene() == null) {
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
    } else {
      navigationStack.push(primaryStage.getScene().getRoot());
      primaryStage.getScene().setRoot(root);
    }
    primaryStage.show();
  }

  public void navigateBack() {
    if (!navigationStack.isEmpty()) {
      Parent previous = navigationStack.pop();
      primaryStage.getScene().setRoot(previous);
      logger.info("Navigated back to previous screen");
    } else {
      logger.warn("Navigation stack empty - can't go back");
    }
  }
}
