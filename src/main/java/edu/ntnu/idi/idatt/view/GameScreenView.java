package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.common.player.Player;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameScreenView {
  private final Stage stage;
  private final SnakesAndLadders game;

  private BoardView boardView;
  private Label currentPlayerLabel;
  private Label diceResultLabel;
  private Label positionLabel;
  private Button rollButton;
  private Button backButton;

  public GameScreenView(Stage stage, SnakesAndLadders game) {
    this.stage = stage;
    this.game = game;
  }

  public void show() {
    currentPlayerLabel = new Label();
    currentPlayerLabel.setStyle("-fx-font-size: 18px;");

    diceResultLabel = new Label("Roll result: -");
    positionLabel = new Label("Position: -");

    rollButton = new Button("Roll Dice");
    rollButton.setStyle("-fx-font-size: 14px;");
    rollButton.setOnAction(e -> handleRoll());

    backButton = new Button("Back");
    backButton.setStyle("-fx-font-size: 14px;");
    backButton.setOnAction(e -> {
              IntroScreenView intro = new IntroScreenView(stage);
              intro.prepareScene();
            });

    boardView = new BoardView(game.getBoard(), game.getPlayers());
    HBox backBox = new HBox(backButton);
    backBox.setAlignment(Pos.CENTER_LEFT);

    VBox root = new VBox(15,
            backBox,
            currentPlayerLabel,
            diceResultLabel,
            positionLabel,
            rollButton,
            boardView
    );
    root.setAlignment(Pos.CENTER);
    root.setStyle("-fx-padding: 20px;");
    updateView();
    stage.setScene(new Scene(root, 600, 700));
    stage.setTitle("Snakes and Ladders - Game");
    stage.show();
  }

  private void handleRoll() {
    Player currentPlayer = game.getCurrentPlayer();
    int roll = game.rollDice();
    currentPlayer.move(roll, game.getBoard());
    diceResultLabel.setText("Roll result: " + roll);
    positionLabel.setText("Position: " + currentPlayer.getPosition());

    if (currentPlayer.hasWon()) {
      showWinner(currentPlayer);
    } else {
      game.advanceTurn();
      updateView();
      boardView.render();
    }
  }

  private void updateView() {
    Player currentPlayer = game.getCurrentPlayer();
    currentPlayerLabel.setText("Current turn: " + currentPlayer.getName());
    positionLabel.setText("Position: " + currentPlayer.getPosition());
  }

  private void showWinner(Player winner) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Over");
    alert.setHeaderText("🎉 " + winner.getName() + " has won the game!");
    alert.setContentText("Final position: " + winner.getPosition());
    alert.showAndWait();
    rollButton.setDisable(true);
  }
}