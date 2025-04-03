package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.common.player.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

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
    currentPlayerLabel = new Label("Current turn: ");
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

    List<Player> genericPlayers = game.getPlayers();
    boardView = new BoardView(game.getBoard(), genericPlayers);

    VBox topRightInfo = new VBox(10, currentPlayerLabel, diceResultLabel, positionLabel);
    topRightInfo.setAlignment(Pos.TOP_CENTER);
    topRightInfo.setPadding(new Insets(10));

    HBox buttonBox = new HBox(20, backButton, rollButton);
    buttonBox.setAlignment(Pos.BOTTOM_CENTER);
    buttonBox.setPadding(new Insets(10));

    BorderPane rightColumn = new BorderPane();
    rightColumn.setTop(topRightInfo);
    rightColumn.setBottom(buttonBox);
    rightColumn.setPrefWidth(300);

    HBox root = new HBox(40, boardView, rightColumn);
    root.setAlignment(Pos.CENTER_LEFT);
    root.setPadding(new Insets(20));

    stage.setScene(new Scene(root));
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