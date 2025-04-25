package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.controller.snakesandladders.SalCharacterSelectionController;
import edu.ntnu.idi.idatt.model.model_observers.CharacterSelectionObserver;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.common.AbstractCharacterSelectionView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Set;

public class SalCharacterSelectionView extends AbstractCharacterSelectionView implements CharacterSelectionObserver {
  private static final Logger logger = LoggerFactory.getLogger(SalCharacterSelectionView.class);

  private final SalCharacterSelectionController controller;

  public SalCharacterSelectionView() {
    super();
    this.controller = new SalCharacterSelectionController();
    controller.registerObserver(this);
  }

  @Override
  protected void displayView() {
    NavigationManager.getInstance().setRoot(root);
  }

  @Override
  protected VBox createPlayerBox(int playerIndex, String defaultName) {
    TextField nameField = new TextField(defaultName);
    nameField.setMaxWidth(150);
    nameField.setStyle("-fx-font-size: 16px; -fx-alignment: center; -fx-font-weight: bold;");
    nameField.setOnKeyReleased(e -> controller.setPlayerName(playerIndex, nameField.getText()));

    GridPane grid = new GridPane();
    grid.setHgap(15);
    grid.setVgap(15);
    grid.setAlignment(Pos.CENTER);

    ToggleGroup toggleGroup = new ToggleGroup();

    int index = 0;
    for (String character : controller.getAvailableCharacters()) {
      ToggleButton button = createCharacterButton(character, toggleGroup, playerIndex);
      grid.add(button, index % 5, index / 5);
      index++;
    }

    VBox box = new VBox(15, nameField, grid);
    box.setPrefSize(300, 270);
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(15));
    box.setStyle("-fx-background-color: #ddd;");
    return box;
  }

  private ToggleButton createCharacterButton(String character, ToggleGroup toggleGroup, int playerIndex) {
    Image image = new Image("PlayerIcons/" + character + ".png", 75, 75, true, true);
    ImageView imageView = new ImageView(image);

    ToggleButton button = new ToggleButton();
    button.setGraphic(imageView);
    button.setToggleGroup(toggleGroup);
    button.setStyle("-fx-background-color: transparent; -fx-padding: 5; -fx-border-color: transparent; -fx-border-radius: 10;");

    button.setOnAction(e -> {
      if (button.isSelected()) {
        if (controller.isCharacterAvailable(character)) {
          controller.selectCharacter(playerIndex, character);
        } else {
          button.setSelected(false);
        }
      } else {
        // Character was deselected - add this method to controller
        controller.deselectCharacter(playerIndex);
      }
    });

    return button;
  }

  @Override
  protected VBox createInactivePlayerBox(int playerIndex, String playerLabel) {
    VBox box = new VBox();
    box.setPrefSize(300, 270);
    box.setAlignment(Pos.CENTER);
    box.setStyle("-fx-background-color: #ccc;");

    Image backgroundImage = new Image("images/SALCharacterScreen.png");
    ImageView backgroundView = new ImageView(backgroundImage);
    backgroundView.setFitWidth(400);
    backgroundView.setFitHeight(270);
    backgroundView.setOpacity(0.3);

    Label plusLabel = new Label("+");
    plusLabel.setStyle("-fx-font-size: 64px; -fx-text-fill: #666;");

    StackPane content = new StackPane(backgroundView, plusLabel);
    content.setAlignment(Pos.CENTER);

    box.getChildren().add(content);

    box.setOnMouseClicked(e -> {
      controller.setPlayerActive(playerIndex, true);
      e.consume();
    });
    return box;
  }

  private VBox createActivePlayerBox(int playerIndex, String playerLabel) {
    VBox activeBox = createPlayerBox(playerIndex, playerLabel);

    Button removeButton = new Button("✖");
    removeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: #444; -fx-padding: 0;");
    removeButton.setPrefSize(24, 24);
    removeButton.setFocusTraversable(false);

    HBox topRow = new HBox();
    topRow.setAlignment(Pos.TOP_RIGHT);
    topRow.getChildren().add(removeButton);

    VBox container = new VBox();
    container.getChildren().addAll(topRow, activeBox);

    removeButton.setOnAction(ev -> {
      controller.setPlayerActive(playerIndex, false);
      ev.consume();
    });

    return container;
  }

  @Override
  protected void updateCharacterAvailability() {
    Set<String> selectedChars = controller.getSelectedCharacters();
    updateVBoxAvailability(player1Box, selectedChars);
    updateVBoxAvailability(player2Box, selectedChars);
    updateVBoxAvailability(player3Box, selectedChars);
    updateVBoxAvailability(player4Box, selectedChars);
  }

  private void updateVBoxAvailability(VBox box, Set<String> selectedCharacters) {
    try {
      GridPane grid = findGridPane(box);
      if (grid == null) return;

      for (Node child : grid.getChildren()) {
        if (child instanceof ToggleButton button) {
          String character = extractCharacterName(button);

          if (selectedCharacters.contains(character) && !button.isSelected()) {
            button.setDisable(true);
            button.setOpacity(0.3);
          } else {
            button.setDisable(false);
            button.setOpacity(1.0);
          }
        }
      }
    } catch (Exception e) {
      logger.error("Error updating VBox availability: {}", e.getMessage());
    }
  }

  @Override
  protected void handleStartAction() {
    controller.startGame();
  }

  @Override
  protected void handleBackAction() {
    controller.navigateBack();
  }

  @Override
  protected String getHeaderText() {
    return "Choose your Characters";
  }

  @Override
  protected String getGameTitle() {
    return "Snakes and Ladders";
  }

  @Override
  public void onCharacterSelected(int playerIndex, String characterName) {
    VBox playerBox = getPlayerBoxByIndex(playerIndex);
    if (playerBox != null) {
      GridPane grid = findGridPane(playerBox);
      if (grid != null) {
        for (Node node : grid.getChildren()) {
          if (node instanceof ToggleButton button) {
            String character = extractCharacterName(button);
            if (character.equals(characterName)) {
              button.setSelected(true);
              highlightSelectedButton(button.getToggleGroup());
              break;
            }
          }
        }
      }
    }
  }

  @Override
  public void onPlayerNameChanged(int playerIndex, String newName) {
  }

  @Override
  public void onAvailableCharactersChanged(Set<String> availableCharacters, Set<String> selectedCharacters) {
    updateVBoxAvailability(player1Box, selectedCharacters);
    updateVBoxAvailability(player2Box, selectedCharacters);
    updateVBoxAvailability(player3Box, selectedCharacters);
    updateVBoxAvailability(player4Box, selectedCharacters);
  }

  @Override
  public void onPlayerActiveStatusChanged(int playerIndex, boolean isActive) {
    VBox currentBox = getPlayerBoxByIndex(playerIndex);
    if (currentBox == null) return;

    HBox parentRow = (HBox) currentBox.getParent();
    if (parentRow == null) return;

    if (isActive) {
      VBox activeBox = createActivePlayerBox(playerIndex, "Player " + (playerIndex + 1));

      int index = 0;
      for (Node child : parentRow.getChildren()) {
        if (child == currentBox) {
          break;
        }
        index++;
      }

      parentRow.getChildren().set(index, activeBox);
      setPlayerBoxByIndex(playerIndex, activeBox);

      if (playerIndex == 2) isPlayer3Active = true;
      if (playerIndex == 3) isPlayer4Active = true;
    } else {
      VBox inactiveBox = createInactivePlayerBox(playerIndex, "Player " + (playerIndex + 1));

      int index = 0;
      for (Node child : parentRow.getChildren()) {
        if (child == currentBox) {
          break;
        }
        index++;
      }

      parentRow.getChildren().set(index, inactiveBox);
      setPlayerBoxByIndex(playerIndex, inactiveBox);

      if (playerIndex == 2) isPlayer3Active = false;
      if (playerIndex == 3) isPlayer4Active = false;
    }
  }

  private VBox getPlayerBoxByIndex(int playerIndex) {
    return switch (playerIndex) {
      case 0 -> player1Box;
      case 1 -> player2Box;
      case 2 -> player3Box;
      case 3 -> player4Box;
      default -> null;
    };
  }

  private void setPlayerBoxByIndex(int playerIndex, VBox box) {
    switch (playerIndex) {
      case 0 -> player1Box = box;
      case 1 -> player2Box = box;
      case 2 -> player3Box = box;
      case 3 -> player4Box = box;
    }
  }
}
