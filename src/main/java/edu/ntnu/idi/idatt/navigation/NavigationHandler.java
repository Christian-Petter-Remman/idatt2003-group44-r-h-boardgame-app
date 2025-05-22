package edu.ntnu.idi.idatt.navigation;

import javafx.scene.Parent;

/**
 * <h1>NavigationHandler</h1>
 *
 * <p>Interface for navigation-related actions in the application. Implementations handle routing
 * between UI views.
 */
public interface NavigationHandler {

  /**
   * <h2>navigateTo</h2>
   * Navigates to a view identified by the provided destination string.
   *
   * @param destination The identifier of the destination view.
   */
  void navigateTo(String destination);

  /**
   * <h2>navigateBack</h2>
   * Navigates to the previous view in the navigation stack.
   */
  void navigateBack();

  /**
   * <h2>setRoot</h2>
   * Sets the root node of the current scene.
   *
   * @param root The new root node to be displayed.
   */
  void setRoot(Parent root);
}