package edu.ntnu.idi.idatt.navigation;

import edu.ntnu.idi.idatt.controller.snl.SNLBoardController;
import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.controller.snl.SNLRuleSelectionController;
import edu.ntnu.idi.idatt.filehandling.GameStateCsvLoader;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.SNLGame;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import edu.ntnu.idi.idatt.view.common.game.GameScreenView;
import edu.ntnu.idi.idatt.view.snl.SNLBoardView;
import edu.ntnu.idi.idatt.view.snl.SNLRuleSelectionView;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NavigationManager {

  private static final Logger logger = LoggerFactory.getLogger(NavigationManager.class);
  private static NavigationManager instance;
  private NavigationHandler currentHandler;

  private CharacterSelectionManager characterSelectionManager;
  private SNLRuleSelectionModel ruleSelectionModel;

  private NavigationManager() {}

  public static NavigationManager getInstance() {
    if (instance == null) {
      instance = new NavigationManager();
    }
    return instance;
  }

  public void setHandler(NavigationHandler handler) {
    this.currentHandler = handler;
  }

  public void setRoot(javafx.scene.Parent root) {
    if (currentHandler != null) {
      currentHandler.setRoot(root);
    }
  }

  public void navigateBack() {
    if (currentHandler != null) {
      currentHandler.navigateBack();
    }
  }

  public void navigateToRuleSelectionScreen(CharacterSelectionManager sharedManager) {
    logger.info("Navigated to Rule Selection Screen");

    this.characterSelectionManager = sharedManager; // <-- Share the manager correctly
    this.ruleSelectionModel = new SNLRuleSelectionModel();

    SNLRuleSelectionController controller = new SNLRuleSelectionController(ruleSelectionModel, sharedManager);
    SNLRuleSelectionView view = new SNLRuleSelectionView(ruleSelectionModel, controller);

    setRoot(view.getRoot());
  }

  public void navigateToSNLGameScreen() {
    logger.info("Starting Snakes and Ladders game...");

    try {
      // Load from CSV path stored in rule selection model
      String savePath = ruleSelectionModel.getSavePath();
      GameStateCsvLoader.GameState gameState = GameStateCsvLoader.load(savePath);

      SNLBoard board = new SNLBoard(100);
      board.initializeBoardFromFile("data/saved_games/custom_boards/snakes_and_ladders/" + gameState.boardFile);

      SNLGame game = new SNLGame(board, gameState.players, gameState.diceCount, gameState.currentTurnIndex);
      SNLGameScreenController controller = new SNLGameScreenController(game);
      SNLBoardController boardController = new SNLBoardController(board, gameState.players);

      SNLBoardView boardView = new SNLBoardView(boardController);
      GameScreenView gameScreenView = new GameScreenView(controller);

      BorderPane combined = new BorderPane();
      combined.setCenter(boardView.getRoot());
      combined.setRight(gameScreenView.getRoot());

      setRoot(combined);
      logger.info("Snakes and Ladders game screen initialized successfully.");

    } catch (Exception e) {
      logger.error("Failed to load Snakes and Ladders game from save file", e);
    }
  }
}
