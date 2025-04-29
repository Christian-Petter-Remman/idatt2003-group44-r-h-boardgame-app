package edu.ntnu.idi.idatt.view.common.character;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionData;
import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import java.util.ArrayList;
import java.util.List;

public class PlayerSelectionPanel {
  private PlayerData player;
  private List<CharacterSelectionData> availableCharacters;
  private VBox panelBox;
  private HBox charactersBox;
  private List<CharacterPortrait> characterPortraits = new ArrayList<>();
  private ImageView addButton;
  private Button removeButton;

  public PlayerSelectionPanel(PlayerData player, List<CharacterSelectionData> availableCharacters) {
    this.player = player;
    this.availableCharacters = availableCharacters;
    createView();
  }

  private void createView() {
    panelBox = new VBox(10);
    panelBox.getStyleClass().add("player-slot");

    // Player label
    Label playerLabel = new Label("Player " + player.getId());
    playerLabel.getStyleClass().add("player-label");
    panelBox.getChildren().add(playerLabel);

    // Characters box
    charactersBox = new HBox(5);
    charactersBox.getStyleClass().add("characters-box");

    // Create character portraits
    for (CharacterSelectionData character : availableCharacters) {
      CharacterPortrait portrait = new CharacterPortrait(character);
      characterPortraits.add(portrait);
      charactersBox.getChildren().add(portrait.getView());
    }

    // Add button (+ sign)
    addButton = new ImageView(new Image("images/SALCharacterScreen.png"));
    addButton.setFitWidth(50);
    addButton.setFitHeight(50);
    addButton.setPickOnBounds(true); // Enable click events on transparent areas

    // Remove button (only visible for players 3 and 4)
    removeButton = new Button("Remove");
    removeButton.getStyleClass().add("remove-button");
    if (player.getId() <= 2) {
      removeButton.setVisible(false);
    }

    // Add components based on player active status
    refresh();
  }

  public void refresh() {
    panelBox.getChildren().clear();
    panelBox.getChildren().add(new Label("Player " + player.getId()));

    if (player.isActive()) {
      // Show character selection
      panelBox.getChildren().add(charactersBox);

      // Update character portraits
      for (CharacterPortrait portrait : characterPortraits) {
        portrait.refresh();

        // If character is selected by another player, disable it
        if (portrait.getCharacter().isSelected() &&
            portrait.getCharacter().getSelectedBy() != player) {
          portrait.getImageView().setOpacity(0.5);
          portrait.getImageView().setDisable(true);
        } else {
          portrait.getImageView().setOpacity(1.0);
          portrait.getImageView().setDisable(false);
        }

        // Add blue border if selected by this player
        boolean isSelectedByThisPlayer = portrait.getCharacter().isSelected() &&
            portrait.getCharacter().getSelectedBy() == player;
        portrait.setSelected(isSelectedByThisPlayer, true);
      }

      // Show remove button for players 3 and 4
      if (player.getId() > 2) {
        panelBox.getChildren().add(removeButton);
      }
    } else {
      // Show add button for inactive players
      StackPane addPane = new StackPane();
      addPane.getStyleClass().add("add-player-pane");
      addPane.getChildren().add(addButton);
      panelBox.getChildren().add(addPane);
    }
  }

  public Parent getView() {
    return panelBox;
  }

  public List<CharacterPortrait> getCharacterPortraits() {
    return characterPortraits;
  }

  public ImageView getAddButton() {
    return addButton;
  }

  public Button getRemoveButton() {
    return removeButton;
  }
}
