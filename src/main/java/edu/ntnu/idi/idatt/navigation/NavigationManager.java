package edu.ntnu.idi.idatt.navigation;


import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.controller.common.*;
import edu.ntnu.idi.idatt.controller.common.load.SNLLoadGameController;
import edu.ntnu.idi.idatt.controller.common.load.StarLoadGameController;
import edu.ntnu.idi.idatt.controller.memorygame.MemoryGameController;
import edu.ntnu.idi.idatt.controller.memorygame.MemoryRuleSelectionController;
import edu.ntnu.idi.idatt.controller.paint.PaintCanvasController;
import edu.ntnu.idi.idatt.controller.snl.*;
import edu.ntnu.idi.idatt.controller.star.StarGameController;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.filehandling.GameStateCsvLoader;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.factory.SNLFactory;
import edu.ntnu.idi.idatt.model.common.factory.StarFactory;
import edu.ntnu.idi.idatt.model.common.intro.StartScreenModel;
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

import java.io.File;
import java.util.Objects;

import edu.ntnu.idi.idatt.view.star.StarLoadGameView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NavigationManager {

  private static final Logger logger = LoggerFactory.getLogger(NavigationManager.class);
  private static final NavigationManager instance = new NavigationManager();


  private Stage primaryStage;
  private NavigationHandler currentHandler;
  private CharacterSelectionManager characterSelectionManager;
  private SNLRuleSelectionModel ruleSelectionModel;
  private StarCharSelectionController starCharSelectionController;
  private Scene scene;


  private NavigationManager() {
  }

  public static NavigationManager getInstance() {
    return instance;
  }

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
    if (root == null)
      throw new NullPointerException("Root cannot be null");
    if (scene != null) {
      scene.setRoot(root);
    }
  }

  public void navigateBack() {
   //TODO: Implement navigateBack
  }

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
    new StartScreenModel();
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
    starCharSelectionController = new StarCharSelectionController(characterSelectionManager,view);
    setHandler(starCharSelectionController);
    setRoot(view.getView());
  }


  public void navigateToSNLCharacterSelection() {
    characterSelectionManager = new CharacterSelectionManager();
    CharacterSelectionScreen view = new CharacterSelectionScreen(characterSelectionManager);
    CharacterSelectionController controller = new CharacterSelectionController(
        characterSelectionManager, view);
    setCharacterSelectionManager(characterSelectionManager);
    setHandler(controller);
    setRoot(view.getView());
    logger.info("CharacterSelectionManager initialized: {}",
        characterSelectionManager != null ? "Yes" : "No");
    logger.info("CharacterSelectionScreen initialized: {}", "Yes");
  }

  public void navigateToSNLRuleSelection() {
    try {
      ruleSelectionModel = new SNLRuleSelectionModel();
      SNLRuleSelectionController controller = new SNLRuleSelectionController(ruleSelectionModel,
          characterSelectionManager);
      SNLRuleSelectionView view = new SNLRuleSelectionView(ruleSelectionModel, controller);
      view.initializeUI();
      setRuleSelectionModel(ruleSelectionModel);
      setHandler(controller);
      setRoot(view.getRoot());
      logger.info("Navigated to Rule Selection Screen");
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
    logger.info("Starting Star Game...");
    try {
      String savePath = starCharSelectionController.getSavePath();
      File saveFile = new File(savePath);
      GameStateCsvLoader.GameState gameState = GameStateCsvLoader.StarLoad(savePath);
      String boardpath = "default.json";

      StarFactory factory = new StarFactory();
      StarBoard board = factory.loadBoardFromFile(boardpath);

      StarGame game = new StarGame(board,gameState.getPlayers(), gameState.getCurrentTurnIndex());

      StarGameController controller = new StarGameController(game,saveFile);
      controller.notifyPlayerPositionChangedAll();
      StarGameView view = new StarGameView(controller);
      view.initializeUI();

      setHandler(controller);
      setRoot(view.getRoot());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void navigateToSNLGameScreen() {
    logger.info("Starting Snakes and Ladders game...");
    try {
      String savePath = ruleSelectionModel.getSavePath();
      File saveFile = new File(savePath);
      GameStateCsvLoader.GameState gameState = GameStateCsvLoader.SNLLoad(savePath);
      String boardPath = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + gameState.getBoardFile();
      logger.info("Final board path: {}", boardPath);

      SNLFactory factory = new SNLFactory();
      SNLBoard board = factory.loadBoardFromFile(boardPath);

      SNLGame game = new SNLGame(board, gameState.getPlayers(), gameState.getDiceCount(),
          gameState.getCurrentTurnIndex());
      SNLGameScreenController controller = new SNLGameScreenController(game,saveFile);

      controller.notifyPlayerPositionChangedAll();

      SNLGameScreenView gameScreenView = new SNLGameScreenView(controller);
      gameScreenView.initializeUI();

      setHandler(controller);
      setRoot(gameScreenView.getRoot());

      logger.info("Snakes and Ladders game screen initialized successfully.");
    } catch (Exception e) {
      logger.error("Failed to SNLLoad Snakes and Ladders game from save file", e);

    }
  }

  public void navigateToMemoryRuleScreen() {
    try {
      MemoryRuleSelectionController controller = new MemoryRuleSelectionController();
      MemoryRuleSelectionView view = new MemoryRuleSelectionView(controller);
      view.initializeUI();
      setHandler(controller);
      setRoot(view.getRoot());
      logger.info("Navigated to Memory Game Rule Selection Screen");
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
      logger.info("Navigated to Memory Game Screen");
    } catch (ClassCastException e) {
      showAlert("Error", "Internal navigation error: wrong handler type.");
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
      logger.info("Navigated to Paint Canvas");
    } catch (Exception e) {
      logger.error("Failed to load Paint Canvas", e);
      showAlert("Error", "Failed to load Paint Canvas");
    }
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  public void setLogo(String path) {
    primaryStage.getIcons().add(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream(path))));
  }
}

