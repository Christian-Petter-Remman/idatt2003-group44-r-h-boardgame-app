package edu.ntnu.idi.idatt.controller.common;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.controller.snakesandladders.SalCharacterSelectionController;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.common.LoadScreenView;
import edu.ntnu.idi.idatt.view.snakesandladders.SalCharacterSelectionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntroScreenController implements NavigationHandler {
  private static final Logger logger = LoggerFactory.getLogger(IntroScreenController.class);

  public void startSnakesAndLadders() {
    try {
      navigateTo("SAL_CHARACTER_SELECTION_SCREEN");
      logger.info("Navigating to SAL_CHARACTER_SELECTION_SCREEN for Snakes and Ladders");
    } catch (Exception e) {
      logger.error("Error starting Snakes and Ladders: {}", e.getMessage());
      showAlert("Error", "Could not start the game");
    }
  }

  public void startGame(String gameType) {
    if ("SNAKES_AND_LADDERS".equals(gameType)) {
      startSnakesAndLadders();
    } else {
      logger.warn("Unknown game type: {}", gameType);
    }
  }

  @Override
  public void navigateTo(String destination) {
    switch (destination) {
      case "SAL_CHARACTER_SELECTION_SCREEN" -> {
        SalCharacterSelectionController characterSelectionController = new SalCharacterSelectionController();
        SalCharacterSelectionView characterSelectionView = new SalCharacterSelectionView(characterSelectionController);
        characterSelectionView.createUI();

        if (characterSelectionView.getRoot() == null) {
          logger.error("Character Selection View root is null");
          showAlert("Error", "Character Selection View failed to load, because it is null.");
        } else {
          NavigationManager.getInstance().setRoot(characterSelectionView.getRoot());
          logger.info("Navigated to Character Selection Screen");
        }
      }
      case "SAL_LOAD_SCREEN" -> {
        LoadController loadController = new LoadController();
        LoadScreenView loadScreenView = new LoadScreenView(loadController);
        NavigationManager.getInstance().setRoot(loadScreenView.getRoot());
        logger.info("Navigated to Load Screen");
      }
    }
  }

    @Override
    public void navigateBack () {
      logger.warn("Cannot navigate back from Intro Screen");
    }
}
