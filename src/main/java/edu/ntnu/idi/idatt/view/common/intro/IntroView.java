package edu.ntnu.idi.idatt.view.common.intro;

import javafx.scene.Parent;

/**
 * <h1>IntroView</h1>
 *
 * <p>Interface for JavaFX views that represent the intro screen of a game application. Provides
 * access
 * to the root UI node and allows setting up listeners for user actions.
 */
public interface IntroView {

  /**
   * <h2>getRoot</h2>
   *
   * <p>Returns the root JavaFX node representing this intro view.
   *
   * @return the root node (Parent) of the view
   */
  Parent getRoot();

  /**
   * <h2>setStartGameListener</h2>
   *
   * <p>Sets a listener that triggers when the user chooses to start a new game.
   *
   * @param listener a {@link Runnable} to be executed on "New Game" action
   */
  void setStartGameListener(Runnable listener);

  /**
   * <h2>setLoadGameListener</h2>
   *
   * <p>Sets a listener that triggers when the user chooses to load an existing game.
   *
   * @param listener a {@link Runnable} to be executed on "Load Game" action
   */
  void setLoadGameListener(Runnable listener);
}