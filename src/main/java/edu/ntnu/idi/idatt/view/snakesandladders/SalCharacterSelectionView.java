package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.controller.snakesandladders.SalCharacterSelectionController;
import edu.ntnu.idi.idatt.model.common.screens.CharacterController;
import edu.ntnu.idi.idatt.model.common.screens.CharacterSelectionModel;
import edu.ntnu.idi.idatt.view.AbstractView;
import java.util.List;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SalCharacterSelectionView extends AbstractView {
  private final CharacterController controller;
  private final CharacterSelectionModel model;

  private final ToggleButton[][] characterButtons = new ToggleButton[4][10];
  private final CheckBox[] activeCheckboxes = new CheckBox[4];
  private Button startButton;

  public SalCharacterSelectionView(CharacterController controller, CharacterSelectionModel model) {
    this.controller = controller;
    this.model = model;
    initializeModelBindings();
  }

  @Override
  protected void createUI() {
    VBox mainContainer = new VBox(20);
    mainContainer.setPadding(new Insets(20));

    GridPane playerGrid = new GridPane();
    playerGrid.setHgap(20);
    playerGrid.setVgap(20);

    for (int i = 0; i < 4; i++) {
      VBox playerBox = createPlayerBox(i);
      playerGrid.add(playerBox, i % 2, i / 2);
    }

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
    activeCheckboxes[playerIndex] = new CheckBox("Active");

    activeCheckboxes[playerIndex].selectedProperty().addListener((obs, oldVal, newVal) -> {
      controller.setPlayerActive(playerIndex, newVal);
    });

    TextField nameField = new TextField("Player " + (playerIndex + 1));
    header.getChildren().addAll(activeCheckboxes[playerIndex], nameField);

    GridPane characterGrid = new GridPane();
    characterGrid.setHgap(10);
    characterGrid.setVgap(10);

    List<String> characters = model.getAvailableCharacters();
    for (int i = 0; i < characters.size(); i++) {
      ToggleButton btn = createCharacterButton(characters.get(i), playerIndex);
      characterGrid.add(btn, i % 4, i / 4);
    }

    box.getChildren().addAll(header, characterGrid);
    return box;
  }

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
        controller.selectCharacter(playerIndex, null);
      }
    });
    btn.setUserData(character);
    return btn;
  }

  private void initializeModelBindings() {
    // Bind active players to checkboxes
    for (int i = 0; i < 4; i++) {
      int index = i;
      model.getActivePlayers().addListener((Observable observable) -> {
        boolean active = model.getActivePlayers().get(index);
        Platform.runLater(() -> {
          activeCheckboxes[index].setSelected(active);
        });
      });
      activeCheckboxes[i].selectedProperty().addListener((obs, oldVal, newVal) -> {
        model.getActivePlayers().set(index, newVal);
      });
    }

    // Bind character selections to button states
    model.getSelectedCharacters().addListener((SetChangeListener.Change<? extends String> change) -> {
      Platform.runLater(() -> {
        for (int p = 0; p < 4; p++) {
          for (ToggleButton btn : characterButtons[p]) {
            String character = (String) btn.getUserData();
            boolean selected = model.getSelectedCharacters().contains(character);
            btn.setSelected(selected);
            btn.setStyle(selected ? "-fx-border-color: blue; -fx-border-width: 2px;" : "");
          }
        }
      });
    });

    // Bind start button state
    startButton.disableProperty().bind(Bindings.createBooleanBinding(
        () -> !IntStream.range(0, 4)
            .filter(i -> model.getActivePlayers().get(i))
            .allMatch(i -> model.getSelectedCharacters().stream()
                .anyMatch(c -> getPlayerForCharacter(c) == i)),
        model.getActivePlayers(), model.getSelectedCharacters()
    ));
  }

  @Override
  protected void setupEventHandlers() {
    startButton.setOnAction(e -> controller.startGame());
  }

  private int getPlayerForCharacter(String character) {
    return model.getCharacterOwners().getOrDefault(character, -1);
  }


  @Override
  protected void applyInitialUIState() {
    // Initialize checkbox states from model
    for (int i = 0; i < 4; i++) {
      activeCheckboxes[i].setSelected(model.getActivePlayers().get(i));
    }

    // Initialize character button states
    model.getSelectedCharacters().forEach(character -> {
      for (int p = 0; p < 4; p++) {
        for (ToggleButton btn : characterButtons[p]) {
          if (btn.getUserData().equals(character)) {
            btn.setSelected(true);
            btn.setStyle("-fx-border-color: blue; -fx-border-width: 2px;");
          }
        }
      }
    });
  }



}
