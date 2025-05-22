package edu.ntnu.idi.idatt.controller.memorygame;

import edu.ntnu.idi.idatt.model.memorygame.MemoryBoardGame;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import edu.ntnu.idi.idatt.view.memorygame.MemoryGameView;
import javafx.scene.Parent;

/**
 * <h1>MemoryGameController</h1>
 *
 * <p>This controller handles the logic and UI integration for the memory game. It initializes the
 * game
 * model and view, wires up user interactions, and manages navigation actions.
 *
 * @author Oliver, Christian
 */
public class MemoryGameController implements NavigationHandler {

  private final MemoryGameView view;

  /**
   * <h2>Constructor</h2>
   *
   * <p>Constructs a new {@code MemoryGameController} with the given settings. Initializes the model
   * and view, sets up event listeners, and renders the view.
   *
   * @param settings the settings for configuring the memory game (board size, players, etc.)
   */
  public MemoryGameController(MemoryGameSettings settings) {
    MemoryBoardGame model = new MemoryBoardGame(settings);
    view = new MemoryGameView();
    view.initialize(settings);

    model.addObserver(view);
    view.setOnCardClick(model::flipCard);
    view.setOnRestart(model::reset);
    view.setOnQuit(this::navigateBack);

    view.render(model);
  }

  /**
   * <h2>navigateTo</h2>
   *
   * <p>Navigates to the specified destination defined in {@link NavigationTarget}.
   *
   * @param destination the name of the destination screen to navigate to
   */
  @Override
  public void navigateTo(String destination) {
    NavigationManager.getInstance().navigateTo(NavigationTarget.valueOf(destination));
  }

  /**
   * <h2>navigateBack</h2>
   *
   * <p>Navigates back to the application's start screen.
   */
  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateToStartScreen();
  }

  /**
   * <h2>setRoot</h2>
   *
   * <p>Sets the provided root node as the new root of the current scene.
   *
   * @param root the JavaFX {@link Parent} node to set as the scene root
   */
  @Override
  public void setRoot(Parent root) {
    NavigationManager.getInstance().setRoot(root);
  }

  /**
   * <h2>getView</h2>
   *
   * <p>Returns the associated {@link MemoryGameView} for rendering.
   *
   * @return the memory game view instance
   */
  public MemoryGameView getView() {
    return view;
  }
}