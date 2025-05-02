package edu.ntnu.idi.idatt.navigation;


import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.controller.common.*;
import edu.ntnu.idi.idatt.controller.memorygame.MemoryGameController;
import edu.ntnu.idi.idatt.controller.memorygame.MemoryRuleSelectionController;
import edu.ntnu.idi.idatt.controller.snl.*;
import edu.ntnu.idi.idatt.controller.star.StarGameController;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.filehandling.GameStateCsvLoader;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.factory.SNLFactory;
import edu.ntnu.idi.idatt.model.common.factory.StarFactory;
import edu.ntnu.idi.idatt.model.common.intro.StartScreenModel;
import edu.ntnu.idi.idatt.model.common.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.model.snl.*;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import edu.ntnu.idi.idatt.model.stargame.StarGame;
import edu.ntnu.idi.idatt.model.stargame.StarPlayer;
import edu.ntnu.idi.idatt.view.common.character.CharacterSelectionScreen;

import edu.ntnu.idi.idatt.view.common.character.StarCharSelectionScreen;
import edu.ntnu.idi.idatt.view.common.intro.StartScreenView;
import edu.ntnu.idi.idatt.view.memorygame.MemoryRuleSelectionView;
import edu.ntnu.idi.idatt.view.snl.SNLGameScreenView;

import edu.ntnu.idi.idatt.view.snl.SNLRuleSelectionView;
import edu.ntnu.idi.idatt.view.star.StarGameView;
import java.util.Objects;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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


  private NavigationManager() {
  }

  public static NavigationManager getInstance() {
    return instance;
  }

  public void initialize(Stage stage) {
    this.primaryStage = stage;
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
    if (primaryStage != null) {
      primaryStage.setScene(new Scene(root));
    } else if (currentHandler != null) {
      currentHandler.setRoot(root);
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

      case MEMORY_RULE_SCREEN -> navigateToMemoryRuleScreen();
      case MEMORY_GAME_SCREEN -> navigateToMemoryGame();

    }
  }

  public void navigateToStartScreen() {
    StartScreenModel model = new StartScreenModel();
    StartScreenController controller = new StartScreenController(model);
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
    StarCharSelectionController controller = new StarCharSelectionController(
        characterSelectionManager, view);
    setHandler(controller);
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

  public void navigateToStarGameScreen() {
    logger.info("Starting Star Game...");
    try {
      String savePath = "default_players.csv";
      GameStateCsvLoader.GameState gameState = GameStateCsvLoader.load(savePath);
      String boardpath = FileManager.STAR_GAME_DIR + "/default.json";

      StarFactory factory = new StarFactory();
      StarBoard board = factory.loadBoardFromFile(boardpath);

      StarGame game = new StarGame(board);
      game.addPlayer(new StarPlayer("olli", "peach", 1, 0));
      game.addPlayer(new StarPlayer("123", "bowser", 1, 0));

      StarGameController controller = new StarGameController(game);
      controller.notifyPlayerPositionChangedAll();
      StarGameView view = new StarGameView(controller);
      view.initializeUI();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void navigateToSNLGameScreen() {
    logger.info("Starting Snakes and Ladders game...");
    try {
      String savePath = ruleSelectionModel.getSavePath();
      GameStateCsvLoader.GameState gameState = GameStateCsvLoader.load(savePath);
      String boardPath = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + gameState.getBoardFile();
      logger.info("Final board path: " + boardPath);

      SNLFactory factory = new SNLFactory();
      SNLBoard board = factory.loadBoardFromFile(boardPath);

      SNLGame game = new SNLGame(board, gameState.getPlayers(), gameState.getDiceCount(),
          gameState.getCurrentTurnIndex());
      SNLGameScreenController controller = new SNLGameScreenController(game);

      controller.notifyPlayerPositionChangedAll();

      SNLGameScreenView gameScreenView = new SNLGameScreenView(controller);
      gameScreenView.initializeUI();

      setHandler(controller);
      setRoot(gameScreenView.getRoot());

      logger.info("Snakes and Ladders game screen initialized successfully.");
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
      logger.info("Navigated to Memory Game Rule Selection Screen");
    } catch (Exception e) {
      logger.error("Failed to load Memory Rule Selection", e);
      showAlert("Error", "Failed to load Memory Rule Selection");
    }
  }


  public void navigateToMemoryGame() {
    try {
      MemoryRuleSelectionController ruleCtrl =
          (MemoryRuleSelectionController) currentHandler;

      MemoryGameSettings settings = ruleCtrl.getSettings();

      MemoryGameController gameCtrl = new MemoryGameController(settings);
      setHandler(gameCtrl);
      setRoot(gameCtrl.getView().getRoot());
    } catch (Exception e) {
      logger.error("Failed to load Memory Game", e);
      showAlert("Error", "Failed to load Memory Game");
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

