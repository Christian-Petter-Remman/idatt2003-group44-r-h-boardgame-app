package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

public class IntroScreenView {

  private final Stage stage;

  private String player1Character;
  private String player2Character;

  public IntroScreenView(Stage stage) {
    this.stage = stage;
  }

  private final List<String> availableCharacters = List.of(
          "bowser", "peach", "mario", "yoshi"
  );

  public void show() {
    Label header = new Label("Snakes and Ladders");
    header.setStyle("-fx-font-weight: bold ; -fx-font-size: 20px;");

    Button startButton = new Button("Start");
    startButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
    startButton.setOnAction(e -> {

      SnakesAndLadders game = new SnakesAndLadders();
      game.addPlayer("Player 1");
      game.addPlayer("Player 2");
      game.initialize();

      GameScreenView gameScreen = new GameScreenView(stage, game);
      gameScreen.show();
    });

    VBox player1Box = createPlayerBox("Player 1", c -> player1Character = c);
    VBox player2Box = createPlayerBox("Player 2", c -> player2Character = c);

    HBox headerBox = new HBox(30, header);
    headerBox.setAlignment(Pos.TOP_CENTER);
    headerBox.setPadding(new Insets(10, 0, 10, 0));

    HBox playerRow = new HBox(30, player1Box, player2Box);
    playerRow.setAlignment(Pos.CENTER);

    VBox root = new VBox(40,headerBox, playerRow, startButton);
    root.setAlignment(Pos.TOP_CENTER);
    root.setPadding(new Insets(30));
    root.setStyle("-fx-background-color: white;");

    stage.setScene(new Scene(root, 800, 500));
    stage.setTitle("Character Selection - Snakes and Ladders");
    stage.show();
  }

  private VBox createPlayerBox(String title, Consumer<String> characterSelectedCallback) {
    Label titleLabel = new Label(title);
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setAlignment(Pos.CENTER);

    ToggleGroup toggleGroup = new ToggleGroup();

    for (int i = 0; i < availableCharacters.size(); i++) {
      String character = availableCharacters.get(i);
      Image image = new Image("PlayerIcons/" + "Bows_no_bg" + ".png", 50, 50, true, true);
      ImageView imageView = new ImageView(image);

      ToggleButton button = new ToggleButton();
      button.setGraphic(imageView);
      button.setToggleGroup(toggleGroup);
      button.setStyle("-fx-background-color: transparent; -fx-padding: 5;");

      button.setOnAction(e -> characterSelectedCallback.accept(character));

      int row = i / 5;
      int col = i % 5;
      grid.add(button, col, row);
    }

    VBox box = new VBox(10, titleLabel, grid);
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(10));
    box.setStyle("-fx-background-color: #ddd;");

    return box;
  }
}