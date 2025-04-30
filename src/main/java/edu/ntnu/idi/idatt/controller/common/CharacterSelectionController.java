package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionData;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.common.character.CharacterSelectionScreen;

import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharacterSelectionController implements NavigationHandler {
  private final CharacterSelectionManager manager;

  private static final Logger logger = LoggerFactory.getLogger(CharacterSelectionController.class);

  public CharacterSelectionController(CharacterSelectionManager manager, CharacterSelectionScreen screen) {
    this.manager = manager;
    screen.setHandler(this);
  }

  public void handleCharacterSelection(int playerId, CharacterSelectionData character) {
    PlayerData player = manager.getPlayerById(playerId);
    if (player != null && player.isActive() &&
            (!character.isSelected() || character.getSelectedBy() == player)) {
      manager.selectCharacter(player, character);
    }
  }

  public void activatePlayer(int playerId) {
    manager.activatePlayer(playerId);
  }

  public void deactivatePlayer(int playerId) {
    manager.deactivatePlayer(playerId);
  }

  @Override
  public void navigateTo(String destination) {
    NavigationManager.getInstance().navigateTo(NavigationManager.NavigationTarget.valueOf(destination));
  }

  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
  }

  @Override
  public void setRoot(Parent root) {
    NavigationManager.getInstance().setRoot(root);
  }
}
