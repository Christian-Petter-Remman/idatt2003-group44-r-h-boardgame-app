package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.filehandling.PlayerCsvHandler;
import edu.ntnu.idi.idatt.model.common.Player;
import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractCharacterSelectionView {
  protected static final Logger logger = LoggerFactory.getLogger(AbstractCharacterSelectionView.class);

  protected final Stage stage;
  protected Set<String> selectedCharacters = new HashSet<>();

  protected String player1Character;
  protected String player2Character;
  protected String player3Character;
  protected String player4Character;
  protected abstract String getGamePrefix();
  protected String baseName;

  VBox player1Box;
  VBox player2Box;
  VBox player3Box;
  VBox player4Box;

  private boolean isPlayer3Active = false;
  private boolean isPlayer4Active = false;

  protected String player1Name = "Player 1";
  protected String player2Name = "Player 2";
  protected String player3Name = "Player 3";
  protected String player4Name = "Player 4";

  protected abstract String getBackgroundStyle();

  public final List<String> availableCharacters = List.of(
      "bowser", "peach", "mario", "toad", "charmander", "fish", "luigi", "yoshi", "rock", "snoopdogg"
  );

  public AbstractCharacterSelectionView(Stage stage) {
    this.stage = stage;
  }

  public void show() {
    Label header = new Label(getHeaderText());
    Label gameLabel = new Label(getGameTitle());
    header.setStyle("-fx-font-weight: bold ; -fx-font-size: 24px;");
    gameLabel.setStyle("-fx-font-weight: bold ; -fx-font-size: 18px;");

    Button backButton = new Button("Back");
    backButton.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    backButton.setOnAction(e -> onBack());

    Button startButton = getStartButton();

    player1Box = createPlayerBox("Player 1", c -> player1Name = c, c -> player1Character = c);
    player2Box = createPlayerBox("Player 2", c -> player2Name = c, c -> player2Character = c);
    player3Box = createInactivePlayerBox("Player 3", c -> player3Name = c, c -> player3Character = c);
    player4Box = createInactivePlayerBox("Player 4", c -> player4Name = c, c -> player4Character = c);

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

    ImageView background = new ImageView(new Image("images/snakesbackground.jpg"));
    background.setFitWidth(stage.getWidth());
    background.setFitHeight(stage.getHeight());
    background.setPreserveRatio(false);
    background.setOpacity(0.3);

    StackPane root = new StackPane(background, scrollPane);
    root.setStyle(getBackgroundStyle());

    Scene scene = new Scene(root, 800, 700);
    stage.setScene(scene);
    stage.setTitle("Character Selection");
    stage.setFullScreenExitHint("");
    stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
  }

  private Button getStartButton() {
    Button startButton = new Button("Start");
    startButton.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    startButton.setOnAction(e -> {
      List<Player> players = new ArrayList<>();
      players.add(createPlayer(player1Name, player1Character));
      players.add(createPlayer(player2Name, player2Character));
      baseName = getGamePrefix() + "_" +
              LocalDate.now().toString().replace("-", "") + "_" +
              System.currentTimeMillis();

      String csvPath = "data/user-data/player-files/" + baseName + ".csv";

      if (player3Character != null) {
        players.add(createPlayer(player3Name, player3Character));
      }
      if (player4Character != null) {
        players.add(createPlayer(player4Name, player4Character));
      }

      try {
        savePlayersToFile(players, csvPath);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      PlayerCsvHandler playerCsvHandler = new PlayerCsvHandler();
      List<Player> playersFromFile;
      try {
        playersFromFile = getPlayersFromFile(playerCsvHandler.loadFromFile(csvPath));
      } catch (IOException | FileReadException | CsvFormatException ex) {
        throw new RuntimeException(ex);
      }

      onStart(playersFromFile, baseName);
    });
    return startButton;
  }

  private List<Player> getPlayersFromFile(List<Player> players) throws IOException {
    return players;
  }

  protected VBox createPlayerBox(String defaultName, Consumer<String> nameChangedCallback, Consumer<String> characterSelectedCallback) {
    TextField nameField = new TextField(defaultName);
    nameField.setMaxWidth(150);
    nameField.setStyle("-fx-font-size: 16px; -fx-alignment: center; -fx-font-weight: bold;");
    nameField.setOnKeyReleased(e -> nameChangedCallback.accept(nameField.getText()));

    GridPane grid = new GridPane();
    grid.setHgap(15);
    grid.setVgap(15);
    grid.setAlignment(Pos.CENTER);

    ToggleGroup toggleGroup = new ToggleGroup();

    for (int i = 0; i < availableCharacters.size(); i++) {
      ToggleButton button = getToggleButton(characterSelectedCallback, i, toggleGroup);
      grid.add(button, i % 5, i / 5);
    }

    VBox box = new VBox(15, nameField, grid);
    box.setPrefSize(300, 270);
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(15));
    box.setStyle("-fx-background-color: #ddd;");
    return box;
  }

  private ToggleButton getToggleButton(Consumer<String> characterSelectedCallback, int i, ToggleGroup toggleGroup) {
    String character = availableCharacters.get(i);
    Image image = new Image("PlayerIcons/" + character + ".png", 75, 75, true, true);
    ImageView imageView = new ImageView(image);

    ToggleButton button = new ToggleButton();
    button.setGraphic(imageView);
    button.setToggleGroup(toggleGroup);
    button.setStyle("-fx-background-color: transparent; -fx-padding: 5; -fx-border-color: transparent; -fx-border-radius: 10;");

    button.setOnAction(e -> {
      if (button.isSelected()) {
        if (!selectedCharacters.contains(character)) {
          characterSelectedCallback.accept(character);
          selectedCharacters.add(character);
          updateCharacterAvailability();
        } else {
          button.setSelected(false);
        }
      } else {
        selectedCharacters.remove(character);
        updateCharacterAvailability();
      }
      highlightSelectedButton(toggleGroup);
    });

    return button;
  }

  protected VBox createInactivePlayerBox(String playerLabel, Consumer<String> onNameChanged, Consumer<String> onCharacterSelected) {
    VBox box = new VBox();
    box.setPrefSize(300, 270);
    box.setAlignment(Pos.CENTER);
    box.setStyle("-fx-background-color: #ccc;");

    getInactivePlayerBackground(box);

    activateInactivePlayer(playerLabel, onNameChanged, onCharacterSelected, box);

    return box;
  }

  private void activateInactivePlayer(String playerLabel, Consumer<String> onNameChanged,
      Consumer<String> onCharacterSelected, VBox box) {
    box.setOnMouseClicked(e -> {
      VBox activeBox = createPlayerBox(playerLabel, onNameChanged, onCharacterSelected);

      Button removeButton = new Button("✖");
      removeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: #444; -fx-padding: 0;");
      removeButton.setPrefSize(24, 24);
      removeButton.setFocusTraversable(false);

      StackPane buttonContainer = new StackPane(removeButton);
      buttonContainer.setAlignment(Pos.TOP_RIGHT);
      buttonContainer.setPadding(new Insets(5));

      removeButton.setOnAction(ev -> {
        deactivatePlayer(box, playerLabel, onNameChanged, onCharacterSelected);
        updateCharacterAvailability();
        ev.consume();
      });

      HBox topRow = new HBox();
      topRow.setAlignment(Pos.TOP_RIGHT);
      topRow.getChildren().add(removeButton);

      VBox container = new VBox();
      container.getChildren().addAll(topRow, activeBox);

      box.getChildren().clear();
      box.getChildren().add(container);
      box.setStyle("-fx-background-color: #ddd;");

      if (box == player3Box) isPlayer3Active = true;
      if (box == player4Box) isPlayer4Active = true;

      updateCharacterAvailability();
      e.consume();
    });
  }

  private void getInactivePlayerBackground(VBox box) {
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
  }

  private void deactivatePlayer(VBox box, String playerLabel, Consumer<String> onNameChanged, Consumer<String> onCharacterSelected) {
    try {
      GridPane grid = findGridPane(box);
      if (grid != null) {
        for (Node node : grid.getChildren()) {
          if (node instanceof ToggleButton button) {
            if (button.isSelected()) {
              String character = extractCharacterName(button);
              selectedCharacters.remove(character);
            }
          }
        }
      }

      if (box == player3Box) {
        isPlayer3Active = false;
        player3Character = null;
      } else if (box == player4Box) {
        isPlayer4Active = false;
        player4Character = null;
      }

      box.getChildren().clear();

      getInactivePlayerBackground(box);
      box.setStyle("-fx-background-color: #ccc;");

      activateInactivePlayer(playerLabel, onNameChanged, onCharacterSelected, box);
    } catch (Exception ex) {
      logger.error("Error deactivating player: {}", ex.getMessage());
    }
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

  protected void updateCharacterAvailability() {
    try {
      updateVBoxAvailability(player1Box);
      updateVBoxAvailability(player2Box);
      if (isPlayer3Active) updateVBoxAvailability(player3Box);
      if (isPlayer4Active) updateVBoxAvailability(player4Box);
    } catch (Exception e) {
      logger.error("Error updating character availability: {}", e.getMessage());
    }
  }

  private void updateVBoxAvailability(VBox box) {
    try {
      GridPane grid = findGridPane(box);
      if (grid == null) {
        for (Node child : box.getChildren()) {
          if (child instanceof VBox) {
            grid = findGridPane((VBox) child);
            if (grid != null) break;
          } else if (child instanceof HBox) {
            for (Node hboxChild : ((HBox) child).getChildren()) {
              if (hboxChild instanceof VBox) {
                grid = findGridPane((VBox) hboxChild);
                if (grid != null) break;
              }
            }
            if (grid != null) break;
          }
        }

        if (grid == null) return;
      }

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
    if (box == null || box.getChildren().isEmpty()) return null;

    for (Node child : box.getChildren()) {
      if (child instanceof GridPane) {
        return (GridPane) child;
      }
    }

    if (box.getChildren().size() > 1) {
      Node node = box.getChildren().get(1);
      if (node instanceof GridPane) {
        return (GridPane) node;
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

  protected void savePlayersToFile(List<Player> players, String fileName) throws IOException {
    PlayerCsvHandler playerCsvHandler = new PlayerCsvHandler();
    playerCsvHandler.saveToFile(players, fileName);
  }

  protected String createCustomFileName(String name1, String name2) {
    String date = LocalDate.now().toString();
    return date + "_" + name1 + "_" + name2 + ".csv";
  }


  protected abstract Player createPlayer(String name, String character);
  protected abstract String getHeaderText();
  protected abstract String getGameTitle();
  protected abstract void onStart(List<Player> players, String baseName);
  protected abstract void onBack();
}
