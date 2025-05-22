package edu.ntnu.idi.idatt.navigation;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.controller.common.*;
import edu.ntnu.idi.idatt.controller.common.load.SNLLoadGameController;
import edu.ntnu.idi.idatt.controller.common.load.StarLoadGameController;
import edu.ntnu.idi.idatt.controller.memorygame.MemoryGameController;
import edu.ntnu.idi.idatt.controller.memorygame.MemoryRuleSelectionController;
import edu.ntnu.idi.idatt.controller.snl.*;
import edu.ntnu.idi.idatt.controller.star.StarGameController;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.filehandling.GameStateCsvLoader;
import edu.ntnu.idi.idatt.filehandling.GameStateCsvLoader.GameState;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.factory.SNLFactory;
import edu.ntnu.idi.idatt.model.common.factory.StarFactory;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.model.paint.PaintModel;
import edu.ntnu.idi.idatt.model.snl.*;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import edu.ntnu.idi.idatt.model.stargame.StarGame;
import edu.ntnu.idi.idatt.view.common.character.CharacterSelectionScreen;
import edu.ntnu.idi.idatt.view.common.character.StarCharSelectionScreen;
import edu.ntnu.idi.idatt.view.common.intro.StartScreenView;
import edu.ntnu.idi.idatt.view.memorygame.MemoryRuleSelectionView;
import edu.ntnu.idi.idatt.view.paint.PaintCanvasView;
import edu.ntnu.idi.idatt.view.snl.SNLGameScreenView;
import edu.ntnu.idi.idatt.view.snl.SNLLoadGameView;
import edu.ntnu.idi.idatt.view.snl.SNLRuleSelectionView;
import edu.ntnu.idi.idatt.view.star.StarGameView;
import edu.ntnu.idi.idatt.view.star.StarLoadGameView;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

/**
 * <h1>NavigationManager</h1>
 *
 * Singleton responsible for handling all view transitions across the application.
 * Provides centralized navigation logic and maintains a history stack.
 */
public class NavigationManager {

  private static final Logger logger = LoggerFactory.getLogger(NavigationManager.class);
  private static final NavigationManager instance = new NavigationManager();

  private Stage primaryStage;
  private Scene scene;
  private NavigationHandler currentHandler;
  private CharacterSelectionManager characterSelectionManager;
  private SNLRuleSelectionModel ruleSelectionModel;
  private StarCharSelectionController starCharSelectionController;

  private final Deque<Parent> navigationStack = new LinkedList<>();

  private NavigationManager() {}

  /**
   * <h2>getInstance</h2>
   * @return Singleton instance of NavigationManager.
   */
  public static NavigationManager getInstance() {
    return instance;
  }

  /**
   * <h2>initialize</h2>
   * Initializes the primary stage and default scene.
   *
   * @param stage Main application stage.
   */
  public void initialize(Stage stage) {
    this.primaryStage = stage;
    this.scene = new Scene(new StackPane());
    primaryStage.setScene(scene);
    primaryStage.setFullScreen(true);
    primaryStage.setFullScreenExitHint("");
    primaryStage.setFullScreenExitKeyCombination(null);
    primaryStage.setResizable(false);
  }

  public void setHandler(NavigationHandler handler) {
    this.currentHandler = handler;
  }

  public void setCharacterSelectionManager(CharacterSelectionManager manager) {
    this.characterSelectionManager = manager;
  }

  public void setRuleSelectionModel(SNLRuleSelectionModel model) {
    this.ruleSelectionModel = model;
  }

  public void setRoot(Parent root) {
    if (root == null) throw new NullPointerException("Root cannot be null");
    if (scene != null) {
      if (scene.getRoot() != null && scene.getRoot() != root) {
        navigationStack.push(scene.getRoot());
      }
      scene.setRoot(root);
    }
  }

  /**
   * <h2>navigateBack</h2>
   * Navigates back to the previous screen in the stack.
   */
  public void navigateBack() {
    if (!navigationStack.isEmpty()) {
      scene.setRoot(navigationStack.pop());
    } else {
      logger.info("Navigation stack is empty – can't go back further.");
      navigateToStartScreen();
    }
  }

  /**
   * <h2>navigateTo</h2>
   * Navigates to a view based on the specified enum target.
   *
   * @param target The destination screen.
   */
  public void navigateTo(NavigationTarget target) {
    switch (target) {
      case START_SCREEN -> navigateToStartScreen();
      case INTRO_SCREEN -> navigateToIntroScreen();
      case CHARACTER_SELECTION -> navigateToSNLCharacterSelection();
      case SAL_RULE_SELECTION -> navigateToSNLRuleSelection();
      case SAL_GAME_SCREEN -> navigateToSNLGameScreen();
      case STAR_CHARACTER_SELECTION -> navigateToStarCharacterSelection();
      case STAR_INTRO -> navigateToStarIntroScreen();
      case STAR_GAME -> navigateToStarGameScreen();
      case STAR_LOAD_SCREEN -> navigateToStarLoadScreen();
      case SNL_LOAD_SCREEN -> navigateToSnlLoadScreen();
      case MEMORY_RULE_SCREEN -> navigateToMemoryRuleScreen();
      case MEMORY_GAME_SCREEN -> navigateToMemoryGame();
      case PAINT_CANVAS -> navigateToPaintCanvas();
    }
  }

  public void navigateToStartScreen() {
    StartScreenView view = new StartScreenView();
    setRoot(view.getRoot());
  }

  public void navigateToIntroScreen() {
    IntroScreenController controller = new IntroScreenController();
    controller.getView().initializeUI();
    setHandler(controller);
    setRoot(controller.getView().getRoot());
  }

  public void navigateToStarIntroScreen() {
    StarIntroScreenController controller = new StarIntroScreenController();
    controller.getView().initializeUI();
    setHandler(controller);
    setRoot(controller.getView().getRoot());
  }

  public void navigateToStarCharacterSelection() {
    characterSelectionManager = new CharacterSelectionManager();
    StarCharSelectionScreen view = new StarCharSelectionScreen(characterSelectionManager);
    starCharSelectionController = new StarCharSelectionController(characterSelectionManager, view);
    setHandler(starCharSelectionController);
    setRoot(view.getView());
  }

  public void navigateToSNLCharacterSelection() {
    characterSelectionManager = new CharacterSelectionManager();
    CharacterSelectionScreen view = new CharacterSelectionScreen(characterSelectionManager);
    CharacterSelectionController controller = new CharacterSelectionController(characterSelectionManager, view);
    setCharacterSelectionManager(characterSelectionManager);
    setHandler(controller);
    setRoot(view.getView());
    logger.info("Character selection initialized");
  }

  public void navigateToSNLRuleSelection() {
    try {
      ruleSelectionModel = new SNLRuleSelectionModel();
      SNLRuleSelectionController controller = new SNLRuleSelectionController(ruleSelectionModel, characterSelectionManager);
      SNLRuleSelectionView view = new SNLRuleSelectionView(ruleSelectionModel, controller);
      view.initializeUI();
      setRuleSelectionModel(ruleSelectionModel);
      setHandler(controller);
      setRoot(view.getRoot());
    } catch (Exception e) {
      logger.error("The RuleSelection module could not be loaded", e);
    }
  }

  public void navigateToSnlLoadScreen() {
    SNLLoadGameController controller = new SNLLoadGameController();
    SNLLoadGameView view = new SNLLoadGameView(controller);
    setHandler(controller);
    setRoot(view.getRoot());
  }

  public void navigateToStarLoadScreen() {
    StarLoadGameController controller = new StarLoadGameController();
    StarLoadGameView view = new StarLoadGameView(controller);
    setHandler(controller);
    setRoot(view.getRoot());
  }

  public void navigateToStarGameScreen() {
    try {
      String savePath = starCharSelectionController.getSavePath();
      File saveFile = new File(savePath);
      GameState gameState = GameStateCsvLoader.starLoad(savePath);
      StarBoard board = new StarFactory().loadBoardFromFile("default.json");
      StarGame game = new StarGame(board, gameState.getPlayers(), gameState.getCurrentTurnIndex());
      StarGameController controller = new StarGameController(game, saveFile);
      StarGameView view = new StarGameView(controller);
      controller.notifyPlayerPositionChangedAll();
      view.initializeUI();
      setHandler(controller);
      setRoot(view.getRoot());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void navigateToSNLGameScreen() {
    try {
      String savePath = ruleSelectionModel.getSavePath();
      File saveFile = new File(savePath);
      GameState gameState = GameStateCsvLoader.snlLoad(savePath);
      String boardPath = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + gameState.getBoardFile();
      SNLBoard board = new SNLFactory().loadBoardFromFile(boardPath);
      SNLGame game = new SNLGame(board, gameState.getPlayers(), gameState.getDiceCount(), gameState.getCurrentTurnIndex());
      SNLGameScreenController controller = new SNLGameScreenController(game, saveFile, boardPath);
      controller.notifyPlayerPositionChangedAll();
      SNLGameScreenView view = new SNLGameScreenView(controller);
      setHandler(controller);
      setRoot(view.getRoot());
    } catch (Exception e) {
      logger.error("Failed to load Snakes and Ladders game from save file", e);
    }
  }

  public void navigateToMemoryRuleScreen() {
    try {
      MemoryRuleSelectionController controller = new MemoryRuleSelectionController();
      MemoryRuleSelectionView view = new MemoryRuleSelectionView(controller);
      view.initializeUI();
      setHandler(controller);
      setRoot(view.getRoot());
    } catch (Exception e) {
      logger.error("Failed to load Memory Rule Selection", e);
      showAlert("Error", "Failed to load Memory Rule Selection");
    }
  }

  public void navigateToMemoryGame() {
    try {
      MemoryRuleSelectionController ruleCtrl = (MemoryRuleSelectionController) currentHandler;
      MemoryGameSettings settings = ruleCtrl.getSettings();
      if (settings == null) {
        showAlert("Error", "Please complete the rule selection first.");
        return;
      }
      MemoryGameController gameCtrl = new MemoryGameController(settings);
      setHandler(gameCtrl);
      setRoot(gameCtrl.getView().getRoot());
    } catch (Exception e) {
      logger.error("Failed to load Memory Game", e);
      showAlert("Error", "Failed to load Memory Game");
    }
  }

  public void navigateToPaintCanvas() {
    try {
      PaintModel model = new PaintModel();
      PaintCanvasView view = new PaintCanvasView(model);
      setRoot(view.getRoot());
    } catch (Exception e) {
      logger.error("Failed to load Paint Canvas", e);
      showAlert("Error", "Failed to load Paint Canvas");
    }
  }

  /**
   * <h2>getPrimaryStage</h2>
   * @return The primary application stage.
   */
  public Stage getPrimaryStage() {
    return primaryStage;
  }

  /**
   * <h2>setLogo</h2>
   * Sets the application window icon.
   *
   * @param path Path to the image resource.
   */
  public void setLogo(String path) {
    primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(path))));
  }
}