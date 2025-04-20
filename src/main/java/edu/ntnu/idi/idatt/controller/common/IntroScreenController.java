package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntroScreenController {
  private static final Logger logger = LoggerFactory.getLogger(IntroScreenController.class);
  private NavigationHandler navigationHandler;

  public void setNavigationHandler(NavigationHandler navigationHandler) {
    this.navigationHandler = navigationHandler;
  }

  public void startSnakesAndLadders() {
    try {
      if (navigationHandler != null) {
        navigationHandler.navigateTo("CHARACTER_SELECTION");
        logger.info("Navigating to Snakes and Ladders character selection");
      } else {
        logger.error("Navigation handler is not set");
      }
    } catch (Exception e) {
      logger.error("Error starting Snakes and Ladders: {}", e.getMessage());
    }
  }

  public void startGame(String gameType) {
    // This method can be expanded to handle different game types
    if ("SNAKES_AND_LADDERS".equals(gameType)) {
      startSnakesAndLadders();
    } else {
      logger.warn("Unknown game type: {}", gameType);
    }
  }
}
