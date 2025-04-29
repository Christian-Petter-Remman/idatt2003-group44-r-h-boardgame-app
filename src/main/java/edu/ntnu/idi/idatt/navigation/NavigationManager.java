package edu.ntnu.idi.idatt.navigation;

import edu.ntnu.idi.idatt.controller.common.CharacterSelectionController;
import edu.ntnu.idi.idatt.controller.snl.SNLBoardController;
import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.controller.common.IntroScreenController;
import edu.ntnu.idi.idatt.controller.snl.SNLLoadController;
import edu.ntnu.idi.idatt.controller.snl.SNLRuleSelectionController;
import edu.ntnu.idi.idatt.filehandling.GameStateCsvExporter;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.SNLGame;
import edu.ntnu.idi.idatt.model.snl.SNLPlayer;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.view.common.game.GameScreenView;
import edu.ntnu.idi.idatt.view.common.intro.IntroScreenView;
import edu.ntnu.idi.idatt.view.common.character.CharacterSelectionScreen;
import edu.ntnu.idi.idatt.view.common.game.LoadScreenView;


import edu.ntnu.idi.idatt.view.snl.SNLBoardView;
import edu.ntnu.idi.idatt.view.snl.SNLRuleSelectionView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
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


  public void navigateTo(NavigationTarget target) {
    switch (target) {
      case INTRO_SCREEN -> showIntroScreen();
      case CHARACTER_SELECTION -> navigateToCharacterSelection();
      case LOAD_SCREEN -> navigateToLoadScreen();
      case SAL_RULE_SELECTION -> navigateToSalRuleSelection();
      case SAL_GAME_SCREEN -> navigateToSNLGameScreen();

      default -> logger.warn("Unhandled navigation target: {}", target);
    }
  }

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

  public void navigateToSalRuleSelection() {
    logger.info("Loading Rule Selection Screen...");

    try {
      SNLRuleSelectionModel model = new SNLRuleSelectionModel();
      SNLRuleSelectionController controller = new SNLRuleSelectionController(model);
      SNLRuleSelectionView view = new SNLRuleSelectionView(model, controller);
      setRoot(view.getRoot());
    } catch (Exception e) {
      logger.error("Failed to load Rule Selection screen", e);
    }

    logger.info("Navigated to Rule Selection Screen.");
  }

  public void navigateToSNLGameScreen() {
    logger.info("Starting Snakes and Ladders game...");

    try {
      String filename = "data/saved_games/custom_boards/snakes_and_ladders/default.json";
      List<Player> players = new ArrayList<>();
      players.add(new SNLPlayer("olle", "bowser", 1));
      players.add(new SNLPlayer("cgris", "peach", 1));

      int dice = 1;
      SNLBoard board = new SNLBoard(100);
      board.initializeBoardFromFile(filename);

      SNLGame game = new SNLGame(board, players, dice);
      SNLGameScreenController controller = new SNLGameScreenController(game);
      SNLBoardController boardController = new SNLBoardController(board, players);
      SNLBoardView boardView = new SNLBoardView(boardController);
      GameScreenView gameScreenView = new GameScreenView(controller);

      setRoot(gameScreenView.getRoot());
    } catch (Exception e) {
      logger.error("Failed to navigate to SNL Game Screen", e);
    }
  }

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
