package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.controller.snakesandladders.SalCharacterSelectionController;
import edu.ntnu.idi.idatt.controller.snakesandladders.SalRuleSelectionController;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.model_observers.CharacterSelectionObserver;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class SalCharacterSelectionView implements CharacterSelectionObserver, NavigationHandler {
  private static final Logger logger = LoggerFactory.getLogger(SalCharacterSelectionView.class);

  private final BorderPane root;
  private final SalCharacterSelectionController controller;

  private VBox player1Box;
  private VBox player2Box;
  private VBox player3Box;
  private VBox player4Box;

  public SalCharacterSelectionView() {
    this.root = new BorderPane();
    this.controller = new SalCharacterSelectionController();
    controller.registerObserver(this);
    controller.setNavigationHandler(this);
  }

  public void show() {
    initializeUI();
    NavigationManager.getInstance().setRoot(root);
  }

  private void initializeUI() {
    // Create header
    Label header = new Label("Choose your Characters");
    Label gameLabel = new Label("Snakes and Ladders");
    header.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");
    gameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

    // Create navigation buttons
    Button backButton = new Button("Back");
    backButton.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    backButton.setOnAction(e -> controller.navigateBack());

    Button startButton = new Button("Start");
    startButton.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    startButton.setOnAction(e -> controller.startGame());

    // Create player boxes
    player1Box = createPlayerBox(0, "Player 1");
    player2Box = createPlayerBox(1, "Player 2");
    player3Box = createInactivePlayerBox(2, "Player 3");
    player4Box = createInactivePlayerBox(3, "Player 4");

    // Layout components
    HBox row1 = new HBox(50, player1Box, player2Box);
    HBox row2 = new HBox(50, player3Box, player4Box);
    row1.setAlignment(Pos.CENTER);
    row2.setAlignment(Pos.CENTER);

    VBox centerBox = new VBox(40, row1, row2);
    centerBox.setAlignment(Pos.CENTER);

    HBox headerBox = new HBox(header);
    headerBox.setAlignment(Pos.CENTER);

    HBox gameLabelBox = new HBox(gameLabel);
    gameLabelBox.setAlignment(Pos.CENTER_RIGHT);

    VBox headerElements = new VBox(gameLabelBox, headerBox);

    HBox buttonBox = new HBox(100, backButton, startButton);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setPadding(new Insets(20, 0, 40, 0));

    VBox content = new VBox(30, headerElements, centerBox, buttonBox);
    content.setAlignment(Pos.TOP_CENTER);
    content.setPadding(new Insets(40));

    // Set background
    setBackground();

    root.setCenter(content);
  }

  private void setBackground() {
    try {
      ImageView background = new ImageView(new Image("images/snakesbackground.jpg"));
      background.setFitWidth(800);
      background.setFitHeight(700);
      background.setPreserveRatio(false);
      background.setOpacity(0.3);

      StackPane backgroundPane = new StackPane(background);
      root.setBackground(new Background(new BackgroundFill(
          Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

      root.getChildren().add(0, backgroundPane);
    } catch (Exception e) {
      logger.error("Error setting background: {}", e.getMessage());
    }
  }

  private VBox createPlayerBox(int playerIndex, String defaultName) {
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
      }
    });

    return button;
  }

  private VBox createInactivePlayerBox(int playerIndex, String playerLabel) {
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

  private void updateCharacterAvailability(Set<String> availableCharacters, Set<String> selectedCharacters) {
    updateVBoxAvailability(player1Box, selectedCharacters);
    updateVBoxAvailability(player2Box, selectedCharacters);
    updateVBoxAvailability(player3Box, selectedCharacters);
    updateVBoxAvailability(player4Box, selectedCharacters);
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

  private void highlightSelectedButton(ToggleGroup toggleGroup) {
    for (Toggle toggle : toggleGroup.getToggles()) {
      ToggleButton button = (ToggleButton) toggle;
      if (button.isSelected()) {
        button.setStyle("-fx-background-color: transparent; -fx-padding: 5; -fx-border-color: #006fff; -fx-border-width: 3px; -fx-border-radius: 10;");
      } else {
        button.setStyle("-fx-background-color: transparent; -fx-padding: 5; -fx-border-color: transparent;");
      }
    }
  }

  private GridPane findGridPane(VBox box) {
    if (box == null || box.getChildren().isEmpty()) return null;

    for (Node child : box.getChildren()) {
      if (child instanceof GridPane) {
        return (GridPane) child;
      } else if (child instanceof VBox) {
        GridPane grid = findGridPane((VBox) child);
        if (grid != null) return grid;
      }
    }

    return null;
  }

  private String extractCharacterName(ToggleButton button) {
    if (button.getGraphic() instanceof ImageView imageView) {
      String url = imageView.getImage().getUrl();
      String[] parts = url.split("/");
      if (parts.length > 2) {
        String filename = parts[parts.length - 1];
        return filename.split("\\.")[0];
      }
    }
    return "";
  }

  public BorderPane getRoot() {
    return root;
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
    updateCharacterAvailability(availableCharacters, selectedCharacters);
  }

  @Override
  public void onPlayerActiveStatusChanged(int playerIndex, boolean isActive) {
    VBox currentBox = getPlayerBoxByIndex(playerIndex);
    if (currentBox == null) return;

    VBox parentBox = (VBox) currentBox.getParent();
    if (parentBox == null) return;

    if (isActive) {
      VBox activeBox = createActivePlayerBox(playerIndex, "Player " + (playerIndex + 1));

      int index = 0;
      for (Node child : parentBox.getChildren()) {
        if (child == currentBox) {
          break;
        }
        index++;
      }

      parentBox.getChildren().set(index, activeBox);

      setPlayerBoxByIndex(playerIndex, activeBox);
    } else {
      VBox inactiveBox = createInactivePlayerBox(playerIndex, "Player " + (playerIndex + 1));

      int index = 0;
      for (Node child : parentBox.getChildren()) {
        if (child == currentBox) {
          break;
        }
        index++;
      }

      parentBox.getChildren().set(index, inactiveBox);

      setPlayerBoxByIndex(playerIndex, inactiveBox);
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


  @Override
  public void navigateTo(String destination) {
    switch (destination) {
      case "RULE_SELECTION" -> {
        SalRuleSelectionController ruleController =
            new SalRuleSelectionController(new SnakesAndLaddersFactory());
        SalRuleSelectionView ruleView =
            new SalRuleSelectionView(ruleController);
        ruleView.setPlayers(controller.getPlayers());
        ruleView.setBaseName(controller.getBaseName());
        ruleView.show();
        logger.info("Navigated to Rule Selection Screen");
      }
      case "INTRO_SCREEN" -> {
        NavigationManager.getInstance().navigateBack();
        logger.info("Navigated back to Intro Screen");
      }
      default -> logger.warn("Unknown destination: {}", destination);
    }
  }

  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
    logger.info("Navigated back");
  }
}
