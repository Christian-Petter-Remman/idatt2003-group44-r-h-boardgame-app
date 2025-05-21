package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionData;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import edu.ntnu.idi.idatt.view.common.character.CharacterSelectionScreen;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>CharacterSelectionController</h1>
 * Handles interactions related to character selection in the game setup phase.
 * It mediates between the CharacterSelectionManager and the UI screen.
 * Implements navigation for transitioning between views.
 *
 * @author Your Name
 */
public class CharacterSelectionController implements NavigationHandler {

  private final CharacterSelectionManager manager;
  private static final Logger logger = LoggerFactory.getLogger(CharacterSelectionController.class);

  /**
   * <h2>CharacterSelectionController</h2>
   * Constructor that sets the handler for the CharacterSelectionScreen and initializes the manager.
   *
   * @param manager the manager handling character and player logic
   * @param screen  the UI screen for character selection
   */
  public CharacterSelectionController(CharacterSelectionManager manager, CharacterSelectionScreen screen) {
    this.manager = manager;
    screen.setHandler(this);
  }

  /**
   * <h2>handleCharacterSelection</h2>
   * Assigns a character to a player if the selection is valid and the character is available.
   *
   * @param playerId  the ID of the player attempting to select a character
   * @param character the character being selected
   */
  public void handleCharacterSelection(int playerId, CharacterSelectionData character) {
    PlayerData player = manager.getPlayerById(playerId);
    if (player != null && player.isActive() &&
            (!character.isSelected() || character.getSelectedBy() == player)) {
      manager.selectCharacter(player, character);
    }
  }

  /**
   * <h2>activatePlayer</h2>
   * Activates a player slot by ID, enabling character selection.
   *
   * @param playerId the ID of the player to activate
   */
  public void activatePlayer(int playerId) {
    manager.activatePlayer(playerId);
  }

  /**
   * <h2>deactivatePlayer</h2>
   * Deactivates a player slot and clears character selection.
   *
   * @param playerId the ID of the player to deactivate
   */
  public void deactivatePlayer(int playerId) {
    manager.deactivatePlayer(playerId);
  }

  /**
   * <h2>navigateTo</h2>
   * Navigates to a specified screen based on the destination name.
   *
   * @param destination the enum name of the target screen
   */
  @Override
  public void navigateTo(String destination) {
    NavigationManager.getInstance().navigateTo(NavigationTarget.valueOf(destination));
  }

  /**
   * <h2>navigateBack</h2>
   * Navigates to the previous screen.
   */
  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
  }

  /**
   * <h2>setRoot</h2>
   * Sets the root node for the current view.
   *
   * @param root the new root node to set
   */
  @Override
  public void setRoot(Parent root) {
    NavigationManager.getInstance().setRoot(root);
  }
}