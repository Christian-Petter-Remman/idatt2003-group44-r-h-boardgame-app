package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import edu.ntnu.idi.idatt.view.common.intro.StarIntroView;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>StarIntroScreenController</h1>
 * Controller for managing the intro screen of the Star game mode.
 * Handles UI interactions for starting a new game or loading an existing one.
 * Implements {@link NavigationHandler} to support application navigation.
 *
 * @author Oliver
 */
public class StarIntroScreenController implements NavigationHandler {

  private final StarIntroView view;
  private static final Logger logger = LoggerFactory.getLogger(StarIntroScreenController.class);

  /**
   * <h2>Constructor</h2>
   * Initializes the intro view and sets up event listeners for game start and load actions.
   */
  public StarIntroScreenController() {
    this.view = new StarIntroView();
    this.view.setStartGameListener(this::startStarGame);
    this.view.setLoadGameListener(this::loadStarGame);
  }

  /**
   * <h2>startStarGame</h2>
   * Navigates to the character selection screen for the Star game.
   */
  private void startStarGame() {
    navigateTo("STAR_CHARACTER_SELECTION");
    logger.info("Starting game: Star Game");
  }

  /**
   * <h2>loadStarGame</h2>
   * Navigates to the load screen for existing Star game saves.
   */
  private void loadStarGame() {
    navigateTo("STAR_LOAD_SCREEN");
    logger.info("Navigating to Load Game screen");
  }

  /**
   * <h2>navigateTo</h2>
   * Transitions the UI to a given navigation target.
   *
   * @param destination the name of the destination screen
   */
  @Override
  public void navigateTo(String destination) {
    NavigationManager.getInstance().navigateTo(NavigationTarget.valueOf(destination));
  }

  /**
   * <h2>navigateBack</h2>
   * Displays a warning as back navigation is not applicable from this screen.
   */
  @Override
  public void navigateBack() {
    logger.warn("No possible back navigation from the Intro Screen");
  }

  /**
   * <h2>setRoot</h2>
   * Sets a new root scene in the view hierarchy.
   *
   * @param root the new scene root
   */
  @Override
  public void setRoot(Parent root) {
    view.getRoot().getScene().setRoot(root);
  }

  /**
   * <h2>getView</h2>
   * Returns the intro view associated with this controller.
   *
   * @return the {@link StarIntroView} instance
   */
  public StarIntroView getView() {
    return view;
  }
}