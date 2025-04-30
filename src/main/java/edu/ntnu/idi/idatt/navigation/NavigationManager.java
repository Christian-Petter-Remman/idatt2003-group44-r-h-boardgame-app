package edu.ntnu.idi.idatt.navigation;

import edu.ntnu.idi.idatt.controller.common.*;
import edu.ntnu.idi.idatt.controller.snl.*;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.filehandling.GameStateCsvLoader;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.snl.*;
import edu.ntnu.idi.idatt.view.common.character.CharacterSelectionScreen;
import edu.ntnu.idi.idatt.view.common.game.GameScreenView;
import edu.ntnu.idi.idatt.view.common.intro.IntroScreenView;
import edu.ntnu.idi.idatt.view.snl.SNLBoardView;
import edu.ntnu.idi.idatt.view.snl.SNLRuleSelectionView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class NavigationManager {
  private static final Logger logger = LoggerFactory.getLogger(NavigationManager.class);
  private static final NavigationManager instance = new NavigationManager();

  private Stage stage;
  private NavigationHandler currentHandler;
  private CharacterSelectionManager characterSelectionManager;
  private SNLRuleSelectionModel ruleSelectionModel;

  private NavigationManager() {}

  public static NavigationManager getInstance() {
    return instance;
  }

  public void initialize(Stage stage) {
    this.stage = stage;
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
    if (stage != null) {
      stage.setScene(new Scene(root));
    } else if (currentHandler != null) {
      currentHandler.setRoot(root);
    }
  }

  public void navigateTo(NavigationTarget target) {
    switch (target) {
      case INTRO_SCREEN -> navigateToIntroScreen();
      case CHARACTER_SELECTION -> navigateToCharacterSelection();
      case SAL_RULE_SELECTION -> navigateToSalRuleSelection();
      case SAL_GAME_SCREEN -> navigateToSNLGameScreen();
    }
  }

  public void navigateBack() {
    // Placeholder if you implement back-navigation
  }

  public void navigateToIntroScreen() {
    IntroScreenController controller = new IntroScreenController();
    NavigationManager.getInstance().setHandler(controller);
    NavigationManager.getInstance().setRoot(controller.getView().getRoot());
  }

  public void navigateToCharacterSelection() {
    characterSelectionManager = new CharacterSelectionManager();
    CharacterSelectionScreen view = new CharacterSelectionScreen(characterSelectionManager);
    CharacterSelectionController controller = new CharacterSelectionController(characterSelectionManager, view);
    setCharacterSelectionManager(characterSelectionManager);
    setHandler(controller);
    setRoot(view.getView());
  }

  public void navigateToSalRuleSelection() {
    try {
      ruleSelectionModel = new SNLRuleSelectionModel();
      SNLRuleSelectionController controller = new SNLRuleSelectionController(ruleSelectionModel, characterSelectionManager);
      SNLRuleSelectionView view = new SNLRuleSelectionView(ruleSelectionModel, controller);
      setRuleSelectionModel(ruleSelectionModel);
      setHandler(controller);
      setRoot(view.getRoot());
      logger.info("Navigated to Rule Selection Screen");
    } catch (Exception e) {
      logger.error("The RuleSelection module could not be loaded", e);
    }
  }

  public void navigateToSNLGameScreen() {
    logger.info("Starting Snakes and Ladders game...");
    try {
      String savePath = ruleSelectionModel.getSavePath();
      GameStateCsvLoader.GameState gameState = GameStateCsvLoader.load(savePath);
      String boardPath = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + gameState.getBoardFile();
      SNLBoard board = new SNLBoard(100);
      board.initializeBoardFromFile(boardPath);
      SNLGame game = new SNLGame(board, gameState.getPlayers(), gameState.getDiceCount(), gameState.getCurrentTurnIndex());
      SNLGameScreenController controller = new SNLGameScreenController(game);
      SNLBoardController boardController = new SNLBoardController(board, gameState.getPlayers());
      SNLBoardView boardView = new SNLBoardView(boardController);
      GameScreenView gameScreenView = new GameScreenView(controller);

      BorderPane combinedView = new BorderPane();
      combinedView.setCenter(boardView.getRoot());
      combinedView.setRight(gameScreenView.getRoot());

      setRoot(combinedView);
      logger.info("Snakes and Ladders game screen initialized successfully.");

    } catch (Exception e) {
      logger.error("Failed to load Snakes and Ladders game from save file", e);
    }
  }

  public enum NavigationTarget {
    INTRO_SCREEN,
    CHARACTER_SELECTION,
    SAL_RULE_SELECTION,
    SAL_GAME_SCREEN
  }
}