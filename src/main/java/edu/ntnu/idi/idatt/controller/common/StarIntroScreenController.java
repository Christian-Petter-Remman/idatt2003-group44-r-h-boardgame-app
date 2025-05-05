package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
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
    this.view.setLoadGameListener(this::loadStarGame); // 👈 ADDED THIS
  }

  private void startStarGame() {
    navigateTo("STAR_CHARACTER_SELECTION");
    logger.info("Starting game: Star Game");
  }

  private void loadStarGame() {
    navigateTo("STAR_LOAD_SCREEN"); // 👈 Make sure this target is defined in NavigationTarget enum
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

  public StarIntroView getView() {
    return view;
  }
}