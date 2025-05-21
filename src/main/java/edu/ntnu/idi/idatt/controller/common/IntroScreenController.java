package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import edu.ntnu.idi.idatt.view.common.intro.SNLIntroView;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntroScreenController implements NavigationHandler {
  private final SNLIntroView view;
  private static final Logger logger = LoggerFactory.getLogger(IntroScreenController.class);

  public IntroScreenController() {
    this.view = new SNLIntroView();
    this.view.setStartGameListener(this::startSnakesAndLadders);
    this.view.setLoadGameListener(this::loadSNLGame);
  }

  private void startSnakesAndLadders() {
      navigateTo("CHARACTER_SELECTION");
      logger.info("Starting game: {}", "Snakes and Ladders");
  }

  private void loadSNLGame() {
    navigateTo("SNL_LOAD_SCREEN");
    logger.info("Navigating to Load Game screen");
  }


  @Override
  public void navigateTo(String destination) {
    NavigationManager.getInstance().navigateTo(
        NavigationTarget.valueOf(destination)
    );
  }

  @Override
  public void navigateBack() {
    logger.warn("No possible back navigation from the Intro Screen");
  }

  @Override
  public void setRoot(Parent root) {
    view.getRoot().getScene().setRoot(root);
  }

  public SNLIntroView getView() {
    return view;
  }
}