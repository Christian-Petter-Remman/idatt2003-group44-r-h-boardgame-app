package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntroScreenController implements NavigationHandler {
  private static final Logger logger = LoggerFactory.getLogger(IntroScreenController.class);

  public void startSnakesAndLadders() {
    // This string can be replaced with an enum or constant for better type safety
    navigateTo("CHARACTER_SELECTION");
    logger.info("Navigating to CHARACTER_SELECTION for Snakes and Ladders");
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
      case "CHARACTER_SELECTION" -> {
        // The NavigationManager is responsible for constructing the view and controller,
        // and for wiring up the model as needed.
        NavigationManager.getInstance().navigateToCharacterSelection();
        logger.info("Navigated to Character Selection Screen");
      }
      case "LOAD_SCREEN" -> {
        //NavigationManager.getInstance().navigateToLoadScreen();
        logger.info("Navigated to Load Screen");
      }
      default -> logger.warn("Unknown destination: {}", destination);
    }
  }

  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
    logger.info("Navigated back from Intro Screen");
  }
}
