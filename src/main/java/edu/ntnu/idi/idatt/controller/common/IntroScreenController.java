package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.common.IntroScreenView;
import edu.ntnu.idi.idatt.view.snakesandladders.SalCharacterSelectionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntroScreenController implements NavigationHandler {
  private static final Logger logger = LoggerFactory.getLogger(IntroScreenController.class);

  public void displayIntroScreen(IntroScreenView view) {
    NavigationManager.getInstance().setRoot(view.getRoot());
  }

  public void startSnakesAndLadders() {
    try {
      navigateTo("CHARACTER_SELECTION_SAL");
      logger.info("Navigating to Snakes and Ladders character selection");
    } catch (Exception e) {
      logger.error("Error starting Snakes and Ladders: {}", e.getMessage());
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
      case "CHARACTER_SELECTION_SAL":
        SalCharacterSelectionView characterSelectionView = new SalCharacterSelectionView();
        characterSelectionView.show();
        logger.info("Navigated to Character Selection Screen");
        break;

      default:
        logger.warn("Unknown destination: {}", destination);
        break;
    }
  }

  @Override
  public void navigateBack() {
    logger.warn("Cannot navigate back from Intro Screen");
  }
}
