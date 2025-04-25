package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.controller.snakesandladders.SalCharacterSelectionController;
import edu.ntnu.idi.idatt.model.model_observers.CharacterSelectionObserver;
import edu.ntnu.idi.idatt.view.AbstractView;
import java.util.Set;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SalCharacterSelectionView extends AbstractView implements CharacterSelectionObserver {
  private final SalCharacterSelectionController controller;
  private final VBox[] playerBoxes = new VBox[4];
  private Button startButton, backButton;

  public SalCharacterSelectionView(SalCharacterSelectionController controller) {
    this.controller = controller;
  }

  // Main UI construction
  @Override
  protected void createUI() {
    VBox mainContainer = new VBox(20);
    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setPadding(new Insets(30));

    // Assemble main components
    mainContainer.getChildren().addAll(
        createTitle(),
        createSubtitle(),
        createPlayerGrid(),
        createControlButtons()
    );

    root = mainContainer;
  }

  // Creates title label
  private Label createTitle() {
    Label title = new Label("Snakes and Ladders");
    title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    return title;
  }

  // Creates subtitle label
  private Label createSubtitle() {
    Label subtitle = new Label("Choose Your Characters");
    subtitle.setStyle("-fx-font-size: 18px;");
    return subtitle;
  }

  // Creates grid of player selection boxes
  private GridPane createPlayerGrid() {
    GridPane grid = new GridPane();
    grid.setHgap(20);
    grid.setVgap(20);

    for (int i = 0; i < 4; i++) {
      playerBoxes[i] = createPlayerBox(i);
      grid.add(playerBoxes[i], i % 2, i / 2);
    }

    return grid;
  }

  // Creates individual player container
  private VBox createPlayerBox(int playerIndex) {
    VBox box = new VBox(10);
    box.setPadding(new Insets(15));
    box.setStyle("-fx-background-color: #eee;");

    // Player name field
    TextField nameField = new TextField("Player " + (playerIndex + 1));
    nameField.textProperty().addListener((obs, old, newVal) ->
        controller.setPlayerName(playerIndex, newVal));

    // Character selection grid
    GridPane characterGrid = createCharacterGrid(playerIndex);

    box.getChildren().addAll(nameField, characterGrid);
    return box;
  }

  // Creates character selection buttons grid
  private GridPane createCharacterGrid(int playerIndex) {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    int col = 0, row = 0;
    for (String character : controller.getAvailableCharacters()) {
      ToggleButton btn = createCharacterButton(character, playerIndex);
      grid.add(btn, col++, row);
      if (col > 4) { col = 0; row++; }
    }

    return grid;
  }

  // Creates individual character button
  private ToggleButton createCharacterButton(String character, int playerIndex) {
    Image img = new Image("characters/" + character + ".png");
    ImageView imgView = new ImageView(img);
    imgView.setFitWidth(60);
    imgView.setFitHeight(60);

    ToggleButton btn = new ToggleButton("", imgView);
    btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
      if (isSelected) {
        controller.selectCharacter(playerIndex, character);
      } else {
        controller.deselectCharacter(playerIndex);
      }
    });

    return btn;
  }

  // Creates control buttons container
  private HBox createControlButtons() {
    HBox container = new HBox(20);
    container.setAlignment(Pos.CENTER);

    backButton = new Button("Back");
    startButton = new Button("Start Game");
    startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

    container.getChildren().addAll(backButton, startButton);
    return container;
  }

  @Override
  protected void setupEventHandlers() {
    backButton.setOnAction(e -> controller.navigateBack());
    startButton.setOnAction(e -> controller.startGame());
  }

  @Override
  protected void applyInitialUIState() {
    controller.registerObserver(this);
    updateAllCharacterAvailability();
  }

  // Updates UI when character availability changes
  private void updateAllCharacterAvailability() {
    Set<String> selected = controller.getSelectedCharacters();
    for (VBox box : playerBoxes) {
      updateBoxAvailability(box, selected);
    }
  }

  // Updates availability for a single player box
  private void updateBoxAvailability(VBox box, Set<String> selected) {
    for (Node node : box.getChildren()) {
      if (node instanceof GridPane grid) {
        updateGridAvailability(grid, selected);
      }
    }
  }

  // Updates buttons in a character grid
  private void updateGridAvailability(GridPane grid, Set<String> selected) {
    for (Node node : grid.getChildren()) {
      if (node instanceof ToggleButton btn) {
        String character = getCharacterFromButton(btn);
        boolean available = !selected.contains(character) || btn.isSelected();
        btn.setDisable(!available);
        btn.setOpacity(available ? 1.0 : 0.4);
      }
    }
  }

  // Extracts character name from button
  private String getCharacterFromButton(ToggleButton btn) {
    ImageView img = (ImageView) btn.getGraphic();
    String path = img.getImage().getUrl();
    return path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
  }

  // Interface implementation
  @Override
  public void onCharacterSelected(int playerIndex, String characterName) {
    Platform.runLater(this::updateAllCharacterAvailability);
  }

  @Override
  public void onAvailableCharactersChanged(Set<String> available, Set<String> selected) {
    Platform.runLater(this::updateAllCharacterAvailability);
  }

  @Override
  public void onPlayerActiveStatusChanged(int playerIndex, boolean isActive) {
    Platform.runLater(() ->
        playerBoxes[playerIndex].setDisable(!isActive));
  }
}
