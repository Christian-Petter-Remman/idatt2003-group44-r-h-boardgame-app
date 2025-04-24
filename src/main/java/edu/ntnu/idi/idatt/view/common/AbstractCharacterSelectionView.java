package edu.ntnu.idi.idatt.view.common;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCharacterSelectionView {
  protected static final Logger logger = LoggerFactory.getLogger(AbstractCharacterSelectionView.class);

  protected final BorderPane root;
  protected Set<String> selectedCharacters = new HashSet<>();

  protected VBox player1Box;
  protected VBox player2Box;
  protected VBox player3Box;
  protected VBox player4Box;

  protected boolean isPlayer3Active = false;
  protected boolean isPlayer4Active = false;

  public AbstractCharacterSelectionView() {
    this.root = new BorderPane();
  }

  public void show() {
    initializeUI();
    displayView();
  }

  protected void initializeUI() {
    // Create header
    Label header = new Label(getHeaderText());
    Label gameLabel = new Label(getGameTitle());
    header.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");
    gameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");

    // Create navigation buttons
    Button backButton = new Button("Back");
    backButton.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    backButton.setOnAction(e -> handleBackAction());

    Button startButton = new Button("Start");
    startButton.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    startButton.setOnAction(e -> handleStartAction());

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

    ScrollPane scrollPane = new ScrollPane(content);
    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.getStyleClass().add("edge-to-edge");
    scrollPane.setStyle("-fx-background-color: transparent;");

    // Set background
    setBackground();

    root.setCenter(scrollPane);
  }

  protected void setBackground() {
    try {
      ImageView background = new ImageView(new Image("images/snakesbackground.jpg"));
      background.setFitWidth(800);
      background.setFitHeight(700);
      background.setPreserveRatio(false);
      background.setOpacity(0.3);

      StackPane backgroundPane = new StackPane(background);
      root.getChildren().add(0, backgroundPane);
    } catch (Exception e) {
      logger.error("Error setting background: {}", e.getMessage());
    }
  }

  // Abstract methods to be implemented by subclasses
  protected abstract VBox createPlayerBox(int playerIndex, String defaultName);
  protected abstract VBox createInactivePlayerBox(int playerIndex, String playerLabel);
  protected abstract void handleStartAction();
  protected abstract void handleBackAction();
  protected abstract String getHeaderText();
  protected abstract String getGameTitle();
  protected abstract void displayView();
  protected abstract void updateCharacterAvailability();

  // Utility methods for subclasses
  protected GridPane findGridPane(VBox box) {
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

  protected String extractCharacterName(ToggleButton button) {
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

  protected void highlightSelectedButton(ToggleGroup toggleGroup) {
    for (Toggle toggle : toggleGroup.getToggles()) {
      ToggleButton button = (ToggleButton) toggle;
      if (button.isSelected()) {
        button.setStyle("-fx-background-color: transparent; -fx-padding: 5; -fx-border-color: #006fff; -fx-border-width: 3px; -fx-border-radius: 10;");
      } else {
        button.setStyle("-fx-background-color: transparent; -fx-padding: 5; -fx-border-color: transparent;");
      }
    }
  }

  public BorderPane getRoot() {
    return root;
  }
}
