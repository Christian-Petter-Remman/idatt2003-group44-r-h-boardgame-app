package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.common.LoadScreenView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntroScreenController implements NavigationHandler {
  private static final Logger logger = LoggerFactory.getLogger(IntroScreenController.class);

  public void startSnakesAndLadders() {
    try {
      navigateTo("SAL_LOAD_SCREEN");
      logger.info("Navigating to LOAD_SCREEN for Snakes and Ladders");
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
    if (destination.equals("SAL_LOAD_SCREEN")) {
      LoadController loadController = new LoadController();
      LoadScreenView loadScreenView = new LoadScreenView(loadController);
      NavigationManager.getInstance().setRoot(loadScreenView.getRoot());
      logger.info("Navigated to Load Screen");
    } else {
      logger.warn("Unknown destination: {}", destination);
    }
  }

  @Override
  public void navigateBack() {
    logger.warn("Cannot navigate back from Intro Screen");
  }
}
