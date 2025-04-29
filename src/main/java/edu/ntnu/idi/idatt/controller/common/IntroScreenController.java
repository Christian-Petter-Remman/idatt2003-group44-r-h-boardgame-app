package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.common.intro.IntroScreenView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntroScreenController implements NavigationHandler {
  private final IntroScreenView view;
  private static final Logger logger = LoggerFactory.getLogger(IntroScreenController.class);

  public IntroScreenController() {
    this.view = new IntroScreenView();
    this.view.setStartGameListener(this::startSnakesAndLadders);
  }

  private void startSnakesAndLadders() {
      navigateTo("CHARACTER_SELECTION");
      logger.info("Starting game: {}", "Snakes and Ladders");
  }


  @Override
  public void navigateTo(String destination) {
    NavigationManager.getInstance().navigateTo(
        NavigationManager.NavigationTarget.valueOf(destination)
    );
  }

  @Override
  public void navigateBack() {
    logger.warn("No possible back navigation from the Intro Screen");
  }

  public IntroScreenView getView() {
    return view;
  }
}