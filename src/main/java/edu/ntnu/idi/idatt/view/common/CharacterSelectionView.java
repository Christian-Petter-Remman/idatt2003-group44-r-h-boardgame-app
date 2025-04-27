package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.controller.common.CharacterController;
import edu.ntnu.idi.idatt.model.common.CharacterSelectionModel;
import edu.ntnu.idi.idatt.view.AbstractView;
import javafx.collections.SetChangeListener;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.beans.binding.Bindings;

public class CharacterSelectionView extends AbstractView {
  private final CharacterController controller;
  private final CharacterSelectionModel model;

  private final CheckBox[] activeCheckboxes = new CheckBox[4];
  private final ToggleButton[][] characterButtons = new ToggleButton[4][10];
  private Button startButton;

  public CharacterSelectionView(CharacterController controller, CharacterSelectionModel model) {
    this.controller = controller;
    this.model = model;
    initializeComponents();
    initializeUI();
    initializeBindings();
  }

  private void initializeComponents() {
    for(int i = 0; i < activeCheckboxes.length; i++) {
      activeCheckboxes[i] = new CheckBox("Active");
    }
  }

  @Override
  protected void createUI() {
    VBox mainContainer = new VBox(20);
    mainContainer.setPadding(new Insets(20));

    GridPane playerGrid = new GridPane();
    playerGrid.setHgap(20);
    playerGrid.setVgap(20);

    for(int i = 0; i < 4; i++) {
      VBox playerBox = createPlayerBox(i);
      playerGrid.add(playerBox, i % 2, i / 2);
    }

    startButton = new Button("Start Game");
    mainContainer.getChildren().addAll(new Label("Select Characters"), playerGrid, startButton);
    root = mainContainer;
  }

  private VBox createPlayerBox(int playerIndex) {
    VBox box = new VBox(10);
    box.setPadding(new Insets(15));
    box.setStyle("-fx-background-color: #f0f0f0;");

    HBox header = new HBox(10);
    CheckBox activeCheckbox = activeCheckboxes[playerIndex];
    activeCheckbox.setSelected(model.getActivePlayers().get(playerIndex));

    activeCheckbox.selectedProperty().addListener((obs, oldVal, newVal) -> controller.setPlayerActive(playerIndex, newVal));

    TextField nameField = new TextField(model.getPlayerNames().get(playerIndex));
    header.getChildren().addAll(activeCheckbox, nameField);

    GridPane characterGrid = new GridPane();
    characterGrid.setHgap(10);
    characterGrid.setVgap(10);

    int col = 0, row = 0;
    for(String character : model.getAvailableCharacters()) {
      ToggleButton btn = createCharacterButton(character, playerIndex);
      characterGrid.add(btn, col++, row);
      if(col > 4) { col = 0; row++; }
    }

    box.getChildren().addAll(header, characterGrid);
    return box;
  }

  private ToggleButton createCharacterButton(String character, int playerIndex) {
    Image img = new Image("player_icons/" + character + ".png");
    ImageView imgView = new ImageView(img);
    imgView.setFitWidth(60);
    imgView.setFitHeight(60);

    ToggleButton btn = new ToggleButton("", imgView);
    btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    btn.setUserData(playerIndex);

    btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
      int pIndex = (int) btn.getUserData();
      controller.selectCharacter(pIndex, isSelected ? character : null);
    });

    return btn;
  }

  private void initializeBindings() {
    startButton.disableProperty().bind(
        Bindings.createBooleanBinding(
            () -> !controller.validateSelections(),
            model.getActivePlayers(),
            model.getSelectedCharacters()
        )
    );

    model.getSelectedCharacters().addListener((SetChangeListener.Change<? extends String> change) -> updateButtonAppearances());
  }

  private void updateButtonAppearances() {
    for(int p = 0; p < 4; p++) {
      for(ToggleButton btn : characterButtons[p]) {
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

  }
}
