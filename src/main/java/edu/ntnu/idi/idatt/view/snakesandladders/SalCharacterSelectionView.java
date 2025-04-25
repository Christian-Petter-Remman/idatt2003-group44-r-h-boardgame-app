package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.controller.snakesandladders.SalCharacterSelectionController;
import edu.ntnu.idi.idatt.model.model_observers.CharacterSelectionObserver;
import edu.ntnu.idi.idatt.view.AbstractView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Set;

public class SalCharacterSelectionView extends AbstractView implements CharacterSelectionObserver {
  private final SalCharacterSelectionController controller;

  private VBox player1Box;
  private VBox player2Box;
  private VBox player3Box;
  private VBox player4Box;
  private boolean isPlayer3Active = false;
  private boolean isPlayer4Active = false;
  private Button startButton;
  private Button backButton;

  public SalCharacterSelectionView(SalCharacterSelectionController controller) {
    this.controller = controller;
    controller.registerObserver(this);
  }

  @Override
  protected void createUI() {
    VBox mainContainer = new VBox(20);
    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setPadding(new Insets(30));

    // Header
    Label titleLabel = getTitleLabel();
    Label headerLabel = getHeaderLabel();

    // Player selection area
    HBox topRow = new HBox(20);
    topRow.setAlignment(Pos.CENTER);

    createActivePlayers(topRow);

    HBox bottomRow = new HBox(20);
    bottomRow.setAlignment(Pos.CENTER);
    createInactivePlayers(bottomRow);

    // Navigation buttons
    HBox navigationBox = getNavigationBox();

    // Main container
    setMainContainer(mainContainer, titleLabel, headerLabel, topRow, bottomRow, navigationBox);
  }

  private void setMainContainer(VBox mainContainer, Label titleLabel, Label headerLabel, HBox topRow,
      HBox bottomRow, HBox navigationBox) {
    mainContainer.getChildren().addAll(
        titleLabel,
        headerLabel,
        new Separator(),
        topRow,
        bottomRow,
        new Separator(),
        navigationBox
    );

    root = mainContainer;
  }

  private HBox getNavigationBox() {
    HBox navigationBox = new HBox(20);
    navigationBox.setAlignment(Pos.CENTER);

    backButton = new Button("Back");
    backButton.setPrefWidth(100);

    startButton = new Button("Start Game");
    startButton.setPrefWidth(100);
    startButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

    navigationBox.getChildren().addAll(backButton, startButton);
    return navigationBox;
  }

  private void createInactivePlayers(HBox bottomRow) {
    player3Box = createInactivePlayerBox(2, "Player 3");
    player4Box = createInactivePlayerBox(3, "Player 4");
    bottomRow.getChildren().addAll(player3Box, player4Box);
  }

  private void createActivePlayers(HBox topRow) {
    player1Box = createActivePlayerBox(0, "Player 1");
    player2Box = createActivePlayerBox(1, "Player 2");
    topRow.getChildren().addAll(player1Box, player2Box);
  }

  private Label getHeaderLabel() {
    Label headerLabel = new Label(getHeaderText());
    headerLabel.setStyle("-fx-font-size: 18px;");
    return headerLabel;
  }

  private Label getTitleLabel() {
    Label titleLabel = new Label(getGameTitle());
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    return titleLabel;
  }

  @Override
  protected void setupEventHandlers() {
    startButton.setOnAction(e -> handleStartAction());
    backButton.setOnAction(e -> handleBackAction());
  }

  @Override
  protected void applyInitialUIState() {
    // Initialize any default state
    updateCharacterAvailability();
  }


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
        controller.deselectCharacter(playerIndex);
      }
    });

    return button;
  }

  protected VBox createInactivePlayerBox(int playerIndex, String playerLabel) {
    VBox box = new VBox();
    box.setPrefSize(300, 270);
    box.setAlignment(Pos.CENTER);
    box.setStyle("-fx-background-color: #ccc;");

    Image backgroundImage = new Image("images/SALCharacterScreen.png");
    ImageView backgroundView = new ImageView(backgroundImage);
    backgroundView.setFitWidth(300);
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

  private GridPane findGridPane(VBox box) {
    for (Node child : box.getChildren()) {
      if (child instanceof GridPane) {
        return (GridPane) child;
      } else if (child instanceof VBox) {
        for (Node innerChild : ((VBox) child).getChildren()) {
          if (innerChild instanceof GridPane) {
            return (GridPane) innerChild;
          }
        }
      }
    }
    return null;
  }

  private String extractCharacterName(ToggleButton button) {
    if (button.getGraphic() instanceof ImageView imageView) {
      String url = imageView.getImage().getUrl();
      String filename = url.substring(url.lastIndexOf("/") + 1);
      return filename.substring(0, filename.lastIndexOf("."));
    }
    return "";
  }

  private void highlightSelectedButton(ToggleGroup group) {
    for (Toggle toggle : group.getToggles()) {
      if (toggle instanceof ToggleButton button) {
        if (button.isSelected()) {
          button.setStyle("-fx-background-color: #bde; -fx-padding: 5; -fx-border-color: #58a; -fx-border-radius: 10;");
        } else {
          button.setStyle("-fx-background-color: transparent; -fx-padding: 5; -fx-border-color: transparent; -fx-border-radius: 10;");
        }
      }
    }
  }

  protected void handleStartAction() {
    controller.startGame();
  }

  protected void handleBackAction() {
    controller.navigateBack();
  }

  protected String getHeaderText() {
    return "Choose your Characters";
  }

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
    // Implementation needed if required
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
