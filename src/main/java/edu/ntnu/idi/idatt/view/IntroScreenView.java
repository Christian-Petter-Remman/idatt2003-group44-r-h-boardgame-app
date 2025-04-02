package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.filehandling.PlayerCsvHandler;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersPlayer;
import edu.ntnu.idi.idatt.model.common.player.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IntroScreenView {

  private final Stage stage;

  private String player1Character;
  private String player2Character;
  private String player3Character;
  private String player4Character;

  private String player1Name = "Player 1";
  private String player2Name = "Player 2";
  private String player3Name = "Player 3";
  private String player4Name = "Player 4";

  public IntroScreenView(Stage stage) {
    this.stage = stage;
  }

  private final List<String> availableCharacters = List.of(
          "bowser", "peach", "mario", "toad", "charmander", "fish", "luigi", "yoshi", "rock","snoopdogg"
  );

  public void show() {
    Label header = new Label("Chose your Characters");
    Label gameLabel = new Label("Snakes and Ladders");
    header.setStyle("-fx-font-weight: bold ; -fx-font-size: 24px;");
    gameLabel.setStyle("-fx-font-weight: bold ; -fx-font-size: 18px;");

    Button backButton = new Button("Back");
    backButton.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

    Button startButton = new Button("Start");
    startButton.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    startButton.setOnAction(e -> {
      List<SnakesAndLaddersPlayer> players = new ArrayList<>();
      players.add(createSnakesAndLaddersPlayer(player1Name, player1Character));
      players.add(createSnakesAndLaddersPlayer(player2Name, player2Character));

      if (player3Character != null) {
        players.add(createSnakesAndLaddersPlayer(player3Name, player3Character));
      }
      if (player4Character != null) {
        players.add(createSnakesAndLaddersPlayer(player4Name, player4Character));
      }

      String fileName = createCustomFileName(player1Name, player2Name);
      try {
        savePlayersToFile(players, fileName);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }

      SnakesAndLadders game = new SnakesAndLadders();
      players.forEach(game::addPlayer);
      game.initialize();

      GameScreenView gameScreen = new GameScreenView(stage, game);
      gameScreen.show();
    });

    VBox player1Box = createPlayerBox("Player 1", c -> player1Name = c, c -> player1Character = c);
    VBox player2Box = createPlayerBox("Player 2", c -> player2Name = c, c -> player2Character = c);
    StackPane player3Box = createInactivePlayerBox("Player 3", c -> player3Name = c, c -> player3Character = c);
    StackPane player4Box = createInactivePlayerBox("Player 4", c -> player4Name = c, c -> player4Character = c);

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

    VBox root = new VBox(50, headerElements, centerBox, buttonBox);
    root.setAlignment(Pos.TOP_CENTER);
    root.setPadding(new Insets(40));
    root.setStyle("-fx-background-color: white;");

    stage.setScene(new Scene(root, 1100, 700));
    stage.setTitle("Character Selection - Snakes and Ladders");
    stage.show();
  }

  private VBox createPlayerBox(String defaultName, Consumer<String> nameChangedCallback, Consumer<String> characterSelectedCallback) {
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
      String character = availableCharacters.get(i);
      Image image = new Image("PlayerIcons/" + character + ".png", 75, 75, true, true);
      ImageView imageView = new ImageView(image);

      ToggleButton button = new ToggleButton();
      button.setGraphic(imageView);
      button.setToggleGroup(toggleGroup);
      button.setStyle("-fx-background-color: transparent; -fx-padding: 5; -fx-border-color: transparent; -fx-border-radius: 10;");

      button.setOnAction(e -> {
        characterSelectedCallback.accept(character);
        highlightSelectedButton(toggleGroup);
      });

      int row = i / 5;
      int col = i % 5;
      grid.add(button, col, row);
    }

    VBox box = new VBox(15, nameField, grid);
    box.setPrefSize(300, 270);
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(15));
    box.setStyle("-fx-background-color: #ddd;");

    return box;
  }

  private StackPane createInactivePlayerBox(String playerLabel, Consumer<String> onNameChanged, Consumer<String> onCharacterSelected) {
    VBox previewBox = createPlayerBox("", s -> {}, s -> {});
    previewBox.setStyle("-fx-background-color: #ccc; -fx-opacity: 0.5;");

    Label plusLabel = new Label("+");
    plusLabel.setStyle("-fx-font-size: 64px; -fx-text-fill: #666;");
    StackPane.setAlignment(plusLabel, Pos.CENTER);

    StackPane container = new StackPane(previewBox, plusLabel);
    container.setPrefSize(300, 270);

    container.setOnMouseClicked(e -> {
      VBox activeBox = createPlayerBox(playerLabel, onNameChanged, onCharacterSelected);

      Button removeButton = new Button("\u2716");
      removeButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px; -fx-text-fill: #444; -fx-padding: 0;");
      removeButton.setPrefSize(24, 24);
      removeButton.setFocusTraversable(false);
      StackPane.setAlignment(removeButton, Pos.TOP_RIGHT);
      StackPane.setMargin(removeButton, new Insets(5));

      StackPane activeStack = new StackPane(activeBox, removeButton);
      activeStack.setPrefSize(300, 270);

      removeButton.setOnAction(ev -> {
        int index = ((Pane) activeStack.getParent()).getChildren().indexOf(activeStack);
        ((Pane) activeStack.getParent()).getChildren().set(index,
                createInactivePlayerBox(playerLabel, onNameChanged, onCharacterSelected));
        ev.consume();
      });

      int index = ((Pane) container.getParent()).getChildren().indexOf(container);
      ((Pane) container.getParent()).getChildren().set(index, activeStack);
      e.consume();
    });

    return container;
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

  private SnakesAndLaddersPlayer createSnakesAndLaddersPlayer(String playerName, String character) {
    return new SnakesAndLaddersPlayer(playerName, character);
  }

  private void savePlayersToFile(List<SnakesAndLaddersPlayer> ladderPlayers, String fileName) throws IOException {
    List<Player> basePlayers = new ArrayList<>(ladderPlayers);
    PlayerCsvHandler playerCsvHandler = new PlayerCsvHandler();
    playerCsvHandler.saveToFile(basePlayers, fileName);
  }

  private String createCustomFileName(String name1, String name2) {
    String date = LocalDate.now().toString();
    return date + "_" + name1 + "_" + name2 + ".csv";
  }
}
