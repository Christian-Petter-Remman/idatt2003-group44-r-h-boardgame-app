package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.common.intro.IntroScreenView;
import edu.ntnu.idi.idatt.view.common.intro.StarIntroView;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StarIntroScreenController implements NavigationHandler {

  private final StarIntroView view;
  private static final Logger logger = LoggerFactory.getLogger(IntroScreenController.class);

  public StarIntroScreenController() {
    this.view = new StarIntroView();
    this.view.setStartGameListener(this::startStarGame);
  }

  private void startStarGame() {
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

  @Override
  public void setRoot(Parent root) {
    view.getRoot().getScene().setRoot(root);
  }

  public StarIntroView getView() {
    return view;
  }
}

