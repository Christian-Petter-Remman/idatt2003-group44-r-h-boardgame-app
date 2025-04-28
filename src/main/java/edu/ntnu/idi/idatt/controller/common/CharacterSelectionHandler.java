package edu.ntnu.idi.idatt.controller.common;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.model.common.character_selection_screen.CharacterSelectionData;
import edu.ntnu.idi.idatt.model.common.character_selection_screen.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.character_selection_screen.PlayerData;
import edu.ntnu.idi.idatt.view.common.character_selection_screen.CharacterSelectionScreen;
import javafx.scene.control.Alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharacterSelectionHandler {
  private CharacterSelectionManager manager;
  private CharacterSelectionScreen screen;

  private static final Logger logger = LoggerFactory.getLogger(CharacterSelectionHandler.class);

  public CharacterSelectionHandler(CharacterSelectionManager manager, CharacterSelectionScreen screen) {
    this.manager = manager;
    this.screen = screen;
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

  // Navigation methods
  public void continueToNextScreen() {
    // TODO: Validate all active players have selected characters
    if (manager.areAllActivePlayersReady()) {
      // TODO: Navigate to next screen using your existing navigation system
      logger.warn("Not implemented yet");
    } else {
      showAlert("Error", "All active players must select a character before continuing.");
    }
  }
}
