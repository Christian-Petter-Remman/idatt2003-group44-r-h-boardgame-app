package edu.ntnu.idi.idatt.controller.memorygame;

import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.model.memorygame.MemoryPlayer;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * <h1>MemoryRuleSelectionController</h1>
 *
 * Controller class for managing the rule selection screen in the memory game.
 * It handles user input for board size and player names, and prepares settings
 * for launching the game.
 */
public class MemoryRuleSelectionController implements NavigationHandler {

  private MemoryGameSettings settings;
  private MemoryGameSettings.BoardSize selectedSize = MemoryGameSettings.BoardSize.FOUR_BY_FOUR;
  private ToggleGroup sizeGroup;
  private TextField player1Field;
  private TextField player2Field;
  private final NavigationManager nav = NavigationManager.getInstance();

  /**
   * <h2>setSizeGroup</h2>
   *
   * Binds a toggle group to select the board size and listens for user changes.
   *
   * @param sizeGroup the toggle group used to select board size
   */
  public void setSizeGroup(ToggleGroup sizeGroup) {
    this.sizeGroup = sizeGroup;
    sizeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
      if (newToggle != null) {
        selectedSize = (MemoryGameSettings.BoardSize) newToggle.getUserData();
      }
    });
  }

  /**
   * <h2>setPlayerFields</h2>
   *
   * Sets the text fields for the two player name inputs.
   *
   * @param player1Field text field for player 1
   * @param player2Field text field for player 2
   */
  public void setPlayerFields(TextField player1Field, TextField player2Field) {
    this.player1Field = player1Field;
    this.player2Field = player2Field;
  }

  /**
   * <h2>setBackButton</h2>
   *
   * Assigns navigation logic to the back button.
   *
   * @param backBtn the JavaFX button that navigates back
   */
  public void setBackButton(Button backBtn) {
    backBtn.setOnAction(e -> navigateBack());
  }

  /**
   * <h2>setContinueButton</h2>
   *
   * Assigns logic to the continue button. When pressed, it collects the
   * board size and player names, and navigates to the memory game screen.
   *
   * @param continueBtn the JavaFX button that continues to the game
   */
  public void setContinueButton(Button continueBtn) {
    continueBtn.setOnAction(e -> {
      settings = new MemoryGameSettings(
              selectedSize,
              List.of(
                      new MemoryPlayer(player1Field.getText()),
                      new MemoryPlayer(player2Field.getText())
              )
      );
      navigateTo(NavigationTarget.MEMORY_GAME_SCREEN.name());
    });
  }

  /**
   * <h2>getSettings</h2>
   *
   * Returns the memory game settings selected by the user.
   *
   * @return configured {@link MemoryGameSettings}
   */
  public MemoryGameSettings getSettings() {
    return settings;
  }

  /**
   * <h2>navigateTo</h2>
   *
   * Navigates to the specified screen.
   *
   * @param destination the name of the target screen
   */
  @Override
  public void navigateTo(String destination) {
    nav.navigateTo(NavigationTarget.valueOf(destination));
  }

  /**
   * <h2>navigateBack</h2>
   *
   * Navigates back to the previous screen in the navigation stack.
   */
  @Override
  public void navigateBack() {
    nav.navigateBack();
  }

  /**
   * <h2>setRoot</h2>
   *
   * Sets the specified node as the new root of the application.
   *
   * @param root the new root scene node
   */
  @Override
  public void setRoot(Parent root) {
    nav.setRoot(root);
  }
}