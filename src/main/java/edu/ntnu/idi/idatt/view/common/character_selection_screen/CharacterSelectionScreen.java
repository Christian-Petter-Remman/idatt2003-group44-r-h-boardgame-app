package edu.ntnu.idi.idatt.view.common.character_selection_screen;

import edu.ntnu.idi.idatt.controller.common.CharacterSelectionHandler;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionObserver;
import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import javafx.scene.layout.GridPane;
import javafx.scene.Parent;
import java.util.ArrayList;
import java.util.List;

public class CharacterSelectionScreen implements CharacterSelectionObserver {
  private CharacterSelectionManager manager;
  private CharacterSelectionHandler handler;

  private GridPane mainLayout;
  private List<PlayerSelectionPanel> playerPanels = new ArrayList<>();

  public CharacterSelectionScreen(CharacterSelectionManager manager) {
    this.manager = manager;
    manager.addObserver(this);
    initializeView();
  }

  private void initializeView() {
    mainLayout = new GridPane();
    mainLayout.setHgap(20);
    mainLayout.setVgap(20);

    // Create player panels
    for (int i = 0; i < 4; i++) {
      PlayerData player = manager.getPlayers().get(i);
      PlayerSelectionPanel panel = new PlayerSelectionPanel(player, manager.getAvailableCharacters());
      playerPanels.add(panel);

      // Add to layout (2x2 grid)
      mainLayout.add(panel.getView(), i % 2, i / 2);
    }
  }

  @Override
  public void update() {
    // Update all player panels
    for (PlayerSelectionPanel panel : playerPanels) {
      panel.refresh();
    }
  }

  public void setHandler(CharacterSelectionHandler handler) {
    this.handler = handler;

    // Set up event handlers
    for (int i = 0; i < playerPanels.size(); i++) {
      final int playerId = i + 1;
      PlayerSelectionPanel panel = playerPanels.get(i);

      // Set up character selection handlers
      for (CharacterPortrait portrait : panel.getCharacterPortraits()) {
        portrait.getImageView().setOnMouseClicked(event -> {
          handler.handleCharacterSelection(playerId, portrait.getCharacter());
        });
      }

      // Set up add/remove player handlers
      panel.getAddButton().setOnMouseClicked(event -> {
        handler.activatePlayer(playerId);
      });

      panel.getRemoveButton().setOnMouseClicked(event -> {
        handler.deactivatePlayer(playerId);
      });
    }
  }

  public Parent getView() {
    return mainLayout;
  }
}
