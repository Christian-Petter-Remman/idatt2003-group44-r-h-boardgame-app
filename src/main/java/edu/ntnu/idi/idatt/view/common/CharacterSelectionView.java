package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.controller.common.CharacterSelectionController;
import edu.ntnu.idi.idatt.model.common.CharacterSelectionModel;
import edu.ntnu.idi.idatt.view.AbstractView;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CharacterSelectionView extends AbstractView {
  private static final Logger logger = LoggerFactory.getLogger(CharacterSelectionView.class);

  private final CharacterSelectionController controller;
  private final CharacterSelectionModel model;

  // UI components
  private final CheckBox[] activeCheckboxes = new CheckBox[4];
  private final ToggleButton[][] characterButtons = new ToggleButton[4][10];
  private Button startButton;

  public CharacterSelectionView(CharacterSelectionController controller, CharacterSelectionModel model) {
    this.controller = controller;
    this.model = model;

    // Initialize all checkboxes upfront to prevent null references
    initializeCheckboxes();

    // Create UI components
    initializeUI();

    // Set up model bindings after UI is initialized
    initializeModelBindings();
  }

  // Initialize all checkboxes in the constructor
  private void initializeCheckboxes() {
    for (int i = 0; i < activeCheckboxes.length; i++) {
      activeCheckboxes[i] = new CheckBox("Active");
    }
  }

  @Override
  protected void createUI() {
    VBox mainContainer = new VBox(20);
    mainContainer.setPadding(new Insets(20));

    // Create player grid
    GridPane playerGrid = new GridPane();
    playerGrid.setHgap(20);
    playerGrid.setVgap(20);

    // Create player boxes
    for (int i = 0; i < 4; i++) {
      VBox playerBox = createPlayerBox(i);
      playerGrid.add(playerBox, i % 2, i / 2);
    }

    // Initialize start button
    startButton = new Button("Start Game");
    startButton.setDisable(true);

    mainContainer.getChildren().addAll(
        new Label("Select Characters"),
        playerGrid,
        startButton
    );

    root = mainContainer;
  }

  private VBox createPlayerBox(int playerIndex) {
    VBox box = new VBox(10);
    box.setPadding(new Insets(15));
    box.setStyle("-fx-background-color: #f0f0f0;");

    HBox header = new HBox(10);

    // Use pre-initialized checkbox
    CheckBox activeCheckbox = activeCheckboxes[playerIndex];
    activeCheckbox.setText("Active");

    // Bind checkbox state to controller
    activeCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> controller.setPlayerActive(playerIndex, newVal));

    TextField nameField = new TextField("Player " + (playerIndex + 1));
    header.getChildren().addAll(activeCheckbox, nameField);

    // Create character selection grid
    GridPane characterGrid = createCharacterGrid(playerIndex);
    box.getChildren().addAll(header, characterGrid);

    return box;
  }

  private GridPane createCharacterGrid(int playerIndex) {
    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);

    // Create character buttons
    int col = 0, row = 0;
    for (String character : model.getAvailableCharacters()) {
      ToggleButton btn = createCharacterButton(character, playerIndex);
      grid.add(btn, col++, row);
      if (col > 4) {
        col = 0;
        row++;
      }
    }
    return grid;
  }

  private ToggleButton createCharacterButton(String character, int playerIndex) {
    Image img = new Image("player_icons/" + character + ".png");
    ImageView imgView = new ImageView(img);
    imgView.setFitWidth(60);
    imgView.setFitHeight(60);

    ToggleButton btn = new ToggleButton("", imgView);
    btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    btn.setUserData(character); // Store character reference

    // Handle selection changes
    btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
      if (isSelected) {
        controller.selectCharacter(playerIndex, character);
      } else {
        controller.selectCharacter(playerIndex, null);
      }
    });

    return btn;
  }

  private void initializeModelBindings() {
    // Update button states when selections change
    model.getSelectedCharacters().addListener(
        (SetChangeListener.Change<? extends String> change) -> Platform.runLater(
            this::updateButtonAppearances)
    );

    // Enable start button only when valid
    startButton.disableProperty().bind(
        Bindings.createBooleanBinding(
            () -> controller.validateSelections(),
            model.getSelectedCharacters(),
            model.getActivePlayers()
        )
    );
  }

  private void updateButtonAppearances() {
    for (int p = 0; p < 4; p++) {
      for (ToggleButton btn : characterButtons[p]) {
        String character = (String) btn.getUserData();
        boolean selected = model.getSelectedCharacters().contains(character);
        btn.setSelected(selected);
        btn.setStyle(selected ? "-fx-border-color: blue; -fx-border-width: 2px;" : "");
      }
    }
  }

  @Override
  protected void setupEventHandlers() {
    startButton.setOnAction(e -> controller.startGame());
  }

  @Override
  protected void applyInitialUIState() {
    // Initialize active players from model
    for (int i = 0; i < 4; i++) {
      activeCheckboxes[i].setSelected(model.getActivePlayers().get(i));
    }
  }
}
