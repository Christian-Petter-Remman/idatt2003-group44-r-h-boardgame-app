package edu.ntnu.idi.idatt.view.common.character;

import edu.ntnu.idi.idatt.controller.common.CharacterSelectionController;
import edu.ntnu.idi.idatt.model.common.characterselection.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <h1>CharacterSelectionScreen</h1>
 *
 * JavaFX view for selecting characters before the game starts.
 * Supports activating players, naming them, and assigning characters visually.
 */
public final class CharacterSelectionScreen implements CharacterSelectionObserver {

  private final CharacterSelectionManager manager;
  private CharacterSelectionController handler;

  private final StackPane root = new StackPane();
  private final GridPane grid = new GridPane();
  private final List<PlayerPanel> panels = new ArrayList<>();

  /**
   * <h2>Constructor</h2>
   * Initializes the character selection screen and binds the manager.
   *
   * @param manager the character selection manager
   */
  public CharacterSelectionScreen(CharacterSelectionManager manager) {
    this.manager = manager;
    manager.addObserver(this);
    buildRoot();
    root.getStylesheets().add(Objects.requireNonNull(
            getClass().getResource("/css/CharacterSelectionScreenStyles.css")).toExternalForm());
  }

  /**
   * <h2>setHandler</h2>
   * Assigns a controller to handle navigation and logic.
   *
   * @param handler controller instance
   */
  public void setHandler(CharacterSelectionController handler) {
    this.handler = handler;
    panels.forEach(p -> p.setHandler(handler));
    update();
  }

  /**
   * <h2>getView</h2>
   * Returns the JavaFX root node.
   *
   * @return the root node
   */
  public Parent getView() {
    return root;
  }

  /**
   * <h2>update</h2>
   * Notifies all player panels to synchronize with the model.
   */
  @Override
  public void update() {
    panels.forEach(PlayerPanel::syncWithModel);
  }

  /**
   * <h2>buildRoot</h2>
   * Builds the root UI layout and attaches it to the root StackPane.
   */
  private void buildRoot() {
    ImageView bg = new ImageView(new Image("/images/snakesbackground.jpg"));
    bg.setFitWidth(1280);
    bg.setFitHeight(800);
    bg.setOpacity(0.15);

    grid.setHgap(40);
    grid.setVgap(40);
    grid.setAlignment(Pos.CENTER);

    for (int i = 0; i < 4; i++) {
      PlayerPanel p = new PlayerPanel(manager.getPlayers().get(i));
      panels.add(p);
      grid.add(p.node, i % 2, i / 2);
    }

    Label title = new Label("Character Selection");
    title.setFont(Font.font(24));
    title.setStyle("-fx-text-fill:#333;");

    Button back = new Button("Back");
    Button next = new Button("Continue");
    back.getStyleClass().add("button");
    next.getStyleClass().add("button");

    back.setOnAction(e -> handler.navigateBack());
    next.setOnAction(e -> {
      boolean allSelected = manager.allActivePlayersHaveSelectedCharacters();
      boolean allNamed = manager.getPlayers().stream()
              .filter(PlayerData::isActive)
              .allMatch(p -> p.getName() != null && !p.getName().trim().isEmpty());

      if (!allNamed) {
        showNameMissingWarning();
        return;
      }

      if (!allSelected) {
        showCharacterMissingWarning();
        return;
      }

      handler.navigateTo("SAL_RULE_SELECTION");
    });



    HBox btnRow = new HBox(20, back, new Region(), next);
    btnRow.setAlignment(Pos.CENTER);
    HBox.setHgrow(btnRow.getChildren().get(1), Priority.ALWAYS);
    btnRow.setPadding(new Insets(10));

    VBox card = new VBox(20, title, grid, btnRow);
    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(40));
    card.setMaxWidth(1000);
    card.setStyle("-fx-background-color:rgba(204,204,204,0.8); -fx-background-radius:20;");

    VBox wrap = new VBox(card);
    wrap.setAlignment(Pos.CENTER);
    wrap.setPadding(new Insets(30));

    root.getChildren().addAll(bg, wrap);
  }

  private void showNameMissingWarning() {
    Label warning = new Label("Please enter names for all active players!");
    warning.setStyle(
            "-fx-text-fill: red;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-padding: 10;" +
                    "-fx-background-color: #ffeeee;" +
                    "-fx-border-color: red;" +
                    "-fx-border-radius: 5;" +
                    "-fx-background-radius: 5;"
    );
    StackPane.setAlignment(warning, Pos.TOP_CENTER);
    StackPane.setMargin(warning, new Insets(20));

    StackPane rootPane = (StackPane) root.getScene().getRoot(); // Ensure `root` is your StackPane
    if (!rootPane.getChildren().contains(warning)) {
      rootPane.getChildren().add(warning);
      new Thread(() -> {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException ignored) {}
        javafx.application.Platform.runLater(() -> rootPane.getChildren().remove(warning));
      }).start();
    }
  }
  /**
   * <h2>showCharacterMissingWarning</h2>
   * Displays a popup warning if not all active players have selected a character.
   */
  private void showCharacterMissingWarning() {
    javafx.stage.Popup popup = new javafx.stage.Popup();

    VBox content = new VBox(10);
    content.setStyle("-fx-background-color: #f8d7da; -fx-padding: 15; -fx-border-color: #f5c2c7; -fx-border-width: 2;");
    Label header = new Label("Missing Character");
    header.setStyle("-fx-font-weight: bold; -fx-text-fill: #721c24;");
    Label message = new Label("Each active player must select a character before continuing.");
    message.setStyle("-fx-text-fill: #721c24;");
    content.getChildren().addAll(header, message);

    popup.getContent().add(content);
    popup.setAutoHide(true);
    popup.setHideOnEscape(true);

    javafx.stage.Window window = root.getScene().getWindow();
    popup.show(window, window.getX() + window.getWidth() / 2 - 150, window.getY() + window.getHeight() / 2 - 50);
  }

  /**
   * <h1>PlayerPanel</h1>
   *
   * Inner class that represents a UI block for one player in the selection screen.
   */
  private final class PlayerPanel {

    private final PlayerData player;
    private CharacterSelectionController handler;
    private final StackPane node = new StackPane();
    private final VBox activeBox = new VBox(10);
    private final HBox row1 = new HBox(10), row2 = new HBox(10);
    private final Button removeBtn = new Button("X");

    private boolean shownActive = false;
    private final List<VBox> portraits = new ArrayList<>();

    PlayerPanel(PlayerData player) {
      this.player = player;
      node.getStyleClass().add("player-slot");
      node.setPrefSize(300, 250);

      removeBtn.getStyleClass().add("remove-button");

      TextField nameField = new TextField(player.getName());
      nameField.textProperty().addListener((obs, oldVal, newVal) -> player.setName(newVal));
      nameField.setFont(Font.font(16));
      nameField.getStyleClass().add("player-name-field");

      activeBox.setAlignment(Pos.CENTER);
      activeBox.setPadding(new Insets(10));
      activeBox.getChildren().addAll(nameField, row1, row2);

      manager.getAvailableCharacters().forEach(c -> {
        VBox portrait = createPortrait(c);
        portraits.add(portrait);
        if (row1.getChildren().size() < 5) row1.getChildren().add(portrait);
        else row2.getChildren().add(portrait);
      });

      rebuildOuter();
      applyStylesAndListeners();
    }

    void setHandler(CharacterSelectionController handler) {
      this.handler = handler;
    }

    void syncWithModel() {
      if (player.isActive() != shownActive) {
        shownActive = player.isActive();
        rebuildOuter();
      }
      applyStylesAndListeners();
    }

    private void rebuildOuter() {
      node.getChildren().clear();
      if (shownActive) {
        node.getStyleClass().remove("inactive-player");
        StackPane active = new StackPane(activeBox);
        if (player.getId() > 2) {
          active.getChildren().add(removeBtn);
          StackPane.setAlignment(removeBtn, Pos.TOP_RIGHT);
        }
        node.getChildren().add(active);
      } else {
        node.getStyleClass().add("inactive-player");
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        ImageView plus = new ImageView(new Image("/images/plus_icon.png", 80, 80, true, true));
        Label label = new Label("Activate");
        label.setStyle("-fx-text-fill:#333; -fx-font-size:18;");
        box.getChildren().addAll(plus, label);

        box.setOnMouseClicked(e -> {
          handler.activatePlayer(player.getId());
          manager.notifyObservers();
        });

        node.getChildren().add(box);
      }
    }

    private void applyStylesAndListeners() {
      if (shownActive && player.getId() > 2 && handler != null) {
        removeBtn.setOnAction(e -> {
          handler.deactivatePlayer(player.getId());
          manager.notifyObservers();
        });
      }

      for (VBox portrait : portraits) {
        CharacterSelectionData data = (CharacterSelectionData) portrait.getUserData();

        portrait.getStyleClass().removeAll("selected-character", "disabled-character");

        boolean taken = manager.isCharacterTaken(data);

        if (player.getSelectedCharacter() != null && player.getSelectedCharacter().equals(data)) {
          portrait.getStyleClass().add("selected-character");
        } else if (taken) {
          portrait.getStyleClass().add("disabled-character");
        }

        if (!taken && handler != null) {
          portrait.setOnMouseClicked(e -> {
            handler.handleCharacterSelection(player.getId(), data);
            manager.notifyObservers();
          });
        } else {
          portrait.setOnMouseClicked(null);
        }
      }
    }

    private VBox createPortrait(CharacterSelectionData character) {
      VBox box = new VBox();
      box.setAlignment(Pos.CENTER);
      box.getStyleClass().add("character-container");
      box.setUserData(character);

      ImageView img = new ImageView(new Image(character.getImagePath(), 60, 60, true, true));
      box.getChildren().add(img);

      return box;
    }
  }
}