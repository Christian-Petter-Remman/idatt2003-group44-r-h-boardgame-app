package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import edu.ntnu.idi.idatt.view.common.intro.SNLIntroView;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>IntroScreenController</h1>
 * Controls the logic for the application's intro screen, handling navigation
 * to character selection or game load screens and coordinating the view's event listeners.
 * Implements {@link NavigationHandler} for navigation responsibilities.
 *
 * @author Christian
 */
public class IntroScreenController implements NavigationHandler {
  private final SNLIntroView view;
  private static final Logger logger = LoggerFactory.getLogger(IntroScreenController.class);

  /**
   * <h2>IntroScreenController</h2>
   * Initializes the intro screen and binds event listeners for game start and load buttons.
   */
  public IntroScreenController() {
    this.view = new SNLIntroView();
    this.view.setStartGameListener(this::startSnakesAndLadders);
    this.view.setLoadGameListener(this::loadSNLGame);
  }

  /**
   * <h2>startSnakesAndLadders</h2>
   * Navigates to the character selection screen for starting a new game.
   */
  private void startSnakesAndLadders() {
    navigateTo("CHARACTER_SELECTION");
    logger.info("Starting game: {}", "Snakes and Ladders");
  }

  /**
   * <h2>loadSNLGame</h2>
   * Navigates to the load game screen for continuing a previously saved game.
   */
  private void loadSNLGame() {
    navigateTo("SNL_LOAD_SCREEN");
    logger.info("Navigating to Load Game screen");
  }

  /**
   * <h2>navigateTo</h2>
   * Navigates to the specified screen.
   *
   * @param destination the string name of the navigation target
   */
  @Override
  public void navigateTo(String destination) {
    NavigationManager.getInstance().navigateTo(
            NavigationTarget.valueOf(destination)
    );
  }

  /**
   * <h2>navigateBack</h2>
   * Does nothing, since the intro screen is the application's entry point.
   */
  @Override
  public void navigateBack() {
    logger.warn("No possible back navigation from the Intro Screen");
  }

  /**
   * <h2>setRoot</h2>
   * Sets the root view of the intro screen.
   *
   * @param root the new root to display
   */
  @Override
  public void setRoot(Parent root) {
    view.getRoot().getScene().setRoot(root);
  }

  /**
   * <h2>getView</h2>
   * Returns the associated view for the intro screen.
   *
   * @return the {@link SNLIntroView} instance
   */
  public SNLIntroView getView() {
    return view;
  }
}