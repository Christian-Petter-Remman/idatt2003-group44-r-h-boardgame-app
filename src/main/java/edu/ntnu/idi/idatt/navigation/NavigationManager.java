package edu.ntnu.idi.idatt.navigation;

import edu.ntnu.idi.idatt.controller.common.CharacterSelectionController;
import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.controller.common.IntroScreenController;
import edu.ntnu.idi.idatt.controller.snl.SNLLoadController;
import edu.ntnu.idi.idatt.controller.snl.SNLRuleSelectionController;
import edu.ntnu.idi.idatt.model.paint.PaintModel;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.SNLGame;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.view.common.game.GameScreenView;
import edu.ntnu.idi.idatt.view.common.intro.IntroScreenView;
import edu.ntnu.idi.idatt.view.common.character.CharacterSelectionScreen;
import edu.ntnu.idi.idatt.view.common.game.LoadScreenView;


import edu.ntnu.idi.idatt.view.paint.PaintCanvasView;
import edu.ntnu.idi.idatt.view.snl.SNLRuleSelectionView;
import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
    SAL_GAME_SCREEN,

    PAINT_CANVAS_SCREEN
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

      case PAINT_CANVAS_SCREEN -> navigateToPaintCanvas();

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
    CharacterSelectionController handler = new CharacterSelectionController(model, view);
    view.setHandler(handler);
    setRoot(view.getView());
    logger.info("Navigated to Character Selection Screen");
  }

  public void navigateToLoadScreen() {
    SNLLoadController controller = new SNLLoadController();
    LoadScreenView view = new LoadScreenView(controller);
    setRoot(view.getRoot());
    logger.info("Navigated to Load Screen");
  }

  // Snakes and Ladders specific screens
  public void navigateToSalRuleSelection() {
    logger.info("This code is loaded");

      try {
        SNLRuleSelectionModel model = new SNLRuleSelectionModel();
        SNLRuleSelectionController controller = new SNLRuleSelectionController(model);
        SNLRuleSelectionView view = new SNLRuleSelectionView(model, controller);
        setRoot(view.getRoot());
      } catch (Exception e) {
        logger.error("The RuleSelection module could not be loaded");
      }
    logger.info("Navigated to Rule Selection Screen");
  }

  public void navigateToSalGameScreen() {
    // 1. Create new game model
    SNLGame game = new SNLGame(new SNLBoard(100));

    // 2. Create controller for game
    SNLGameScreenController controller = new SNLGameScreenController(game, "defaultBoard.json", "saveFile.csv");

    // 3. Create view for game
    GameScreenView view = new GameScreenView(controller);

    // 4. Set root to the new view
    setRoot(view.getRoot());

  }

  public void navigateToPaintCanvas() {
    PaintModel model = new PaintModel();
    PaintCanvasView view = new PaintCanvasView(model, primaryStage);
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

  public void setLogo(String path) {
    primaryStage.getIcons().add(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream(path))));
  }
}
