package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.view.GameScreenView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IntroScreenView {

  private final Stage stage;

  public IntroScreenView(Stage stage) {
    this.stage = stage;
  }

  public void show() {
    Label titleLabel = new Label("Snakes and Ladders");
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

    TextField player1Field = new TextField();
    player1Field.setPromptText("Player 1 name");

    TextField player2Field = new TextField();
    player2Field.setPromptText("Player 2 name");

    Button startButton = new Button("Start Game");
    startButton.setOnAction(e -> {
      String name1 = player1Field.getText().trim();
      String name2 = player2Field.getText().trim();

      if (name1.isEmpty() || name2.isEmpty()) {
        showAlert("Please enter names for both players.");
        return;
      }

      SnakesAndLadders game = new SnakesAndLadders();
      game.addPlayer(name1);
      game.addPlayer(name2);
      game.initialize();

      GameScreenView gameScreen = new GameScreenView(stage, game);
      gameScreen.show();
    });

    VBox root = new VBox(10, titleLabel, player1Field, player2Field, startButton);
    root.setAlignment(Pos.CENTER);
    root.setStyle("-fx-padding: 30px;");

    stage.setScene(new Scene(root, 400, 300));
    stage.setTitle("Snakes and Ladders - Intro");
    stage.show();
  }

  private void showAlert(String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
    alert.setHeaderText(null);
    alert.showAndWait();
  }
}