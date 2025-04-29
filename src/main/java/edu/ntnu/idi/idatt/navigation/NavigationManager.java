package edu.ntnu.idi.idatt.navigation;

import edu.ntnu.idi.idatt.controller.common.CharacterSelectionHandler;
import edu.ntnu.idi.idatt.controller.common.GameScreenController;
import edu.ntnu.idi.idatt.controller.common.IntroScreenController;
import edu.ntnu.idi.idatt.controller.common.LoadController;
import edu.ntnu.idi.idatt.controller.snakesandladders.SalRuleSelectionController;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.rule_selection.SalRuleSelectionModel;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.view.common.GameScreenView;
import edu.ntnu.idi.idatt.view.common.IntroScreenView;
import edu.ntnu.idi.idatt.view.common.character_selection_screen.CharacterSelectionScreen;
import edu.ntnu.idi.idatt.view.common.LoadScreenView;


import edu.ntnu.idi.idatt.view.snakesandladders.SalRuleSelectionView;
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
    SAL_RULE_SELECTION,
    SAL_GAME_SCREEN
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
    primaryStage.setFullScreenExitHint("");
    primaryStage.setFullScreen(true);
  }

  // Base navigation methods
  public void navigateTo(NavigationTarget target) {
    switch (target) {
      case INTRO_SCREEN -> showIntroScreen();
      case CHARACTER_SELECTION -> navigateToCharacterSelection();
      case LOAD_SCREEN -> navigateToLoadScreen();
      case SAL_RULE_SELECTION -> navigateToSalRuleSelection();
      case SAL_GAME_SCREEN -> navigateToSalGameScreen();
      // Add other cases as needed
      default -> logger.warn("Unhandled navigation target: {}", target);
    }
  }

  // Dedicated navigation methods
  // Common screens
  public void showIntroScreen() {
    IntroScreenController controller = new IntroScreenController();
    IntroScreenView view = controller.getView();
    setRoot(view.getRoot());
    logger.info("Navigated to Intro Screen");
  }

  public void navigateToCharacterSelection() {
    CharacterSelectionManager model = new CharacterSelectionManager();
    CharacterSelectionScreen view = new CharacterSelectionScreen(model);
    CharacterSelectionHandler handler = new CharacterSelectionHandler(model, view);
    view.setHandler(handler);
    setRoot(view.getView());
    logger.info("Navigated to Character Selection Screen");
  }

  public void navigateToLoadScreen() {
    LoadController controller = new LoadController();
    LoadScreenView view = new LoadScreenView(controller);
    setRoot(view.getRoot());
    logger.info("Navigated to Load Screen");
  }

  // Snakes and Ladders specific screens
  public void navigateToSalRuleSelection() {
    logger.info("This code is loaded");

      try {
        SalRuleSelectionModel model = new SalRuleSelectionModel();
        SalRuleSelectionController controller = new SalRuleSelectionController(model);
        SalRuleSelectionView view = new SalRuleSelectionView(model, controller);
        if (view == null) {
          logger.error("THE RULE SELECTION SCREEN IS NULL");
          return;
        }
        setRoot(view.getRoot());
      } catch (Exception e) {
        logger.error("The RuleSelection module could not be loaded");
      }
    logger.info("Navigated to Rule Selection Screen");
  }

  public void navigateToSalGameScreen() {
    // 1. Create new game model
    SnakesAndLadders game = new SnakesAndLadders();

    // 2. Create controller for game
    GameScreenController controller = new GameScreenController(game, "defaultBoard.json", "saveFile.csv");

    // 3. Create view for game
    GameScreenView view = new GameScreenView(controller);

    // 4. Set root to the new view
    setRoot(view.getRoot());

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
