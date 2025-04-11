package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.common.Player;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class GameScreenView {
  private final Stage stage;
  private final SnakesAndLadders game;

  private BoardView boardView;
  private Label currentPlayerLabel;
  private Image currentPlayerImage;
  private Label diceResultLabel;
  private Label positionLabel;
  private Button rollButton;
  private Button backButton;
  private ImageView imageView;
  private Label playerLabel;
  private Image player1Image;
  private ImageView player1ImageView;
  private Image player2Image;
  private ImageView player2ImageView;
  private Image player3Image;
  private ImageView player3ImageView;
  private Image player4Image;
  private ImageView player4ImageView;
  private Label player1Label;
  private Label player2Label;
  private Label player3Label;
  private Label player4Label;

  Label player1TurnLabel = new Label();
  Label player2TurnLabel = new Label();
  Label player3TurnLabel = new Label();
  Label player4TurnLabel = new Label();

  public GameScreenView(Stage stage, SnakesAndLadders game) {
    this.stage = stage;
    this.game = game;
  }

  public void show() {
    currentPlayerLabel = new Label("Current turn: ");
    currentPlayerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    currentPlayerLabel.setMinWidth(150);

    diceResultLabel = new Label("Roll result: -");
    positionLabel = new Label("Position: -");

    currentPlayerImage = new Image("PlayerIcons/" + game.getCurrentPlayer().getCharacter() + ".png", 150, 150, true, true);
    imageView = new ImageView(currentPlayerImage);

    playerLabel = new Label("Players");
    playerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    playerLabel.setAlignment(Pos.CENTER);
    playerLabel.setMaxWidth(Double.MAX_VALUE);

    GridPane playerGrid = new GridPane();
    playerGrid.setHgap(60);
    playerGrid.setVgap(60);
    playerGrid.setAlignment(Pos.CENTER);

    List<Player> players = game.getPlayers();
    Player Player1 = players.get(0);
    Player Player2 = players.get(1);
    String Player1Position = String.valueOf(Player1.getPosition());
    String Player2Position = String.valueOf(Player2.getPosition());


    player1Image = new Image("PlayerIcons/" + game.getCharacterNames().get(0) + ".png", 75, 75, true, true);
    player1ImageView = new ImageView(player1Image);
    player1Label = new Label(game.getPlayers().get(0).getName());
    player1TurnLabel.setText(Player1Position);
    VBox player1Box = new VBox(5, player1ImageView, player1Label, player1TurnLabel);
    player1Box.setAlignment(Pos.CENTER);
    playerGrid.add(player1Box, 0, 0);

    player2Image = new Image("PlayerIcons/" + game.getCharacterNames().get(1) + ".png", 75, 75, true, true);
    player2ImageView = new ImageView(player2Image);
    player2Label = new Label(game.getPlayers().get(1).getName());
    player2TurnLabel.setText(Player2Position);
    VBox player2Box = new VBox(5, player2ImageView, player2Label, player2TurnLabel);
    player2Box.setAlignment(Pos.CENTER);
    playerGrid.add(player2Box, 1, 0);

    if (game.getCharacterNames().size() >= 3 ) {
      player3Image = new Image("PlayerIcons/" + game.getCharacterNames().get(2) + ".png", 75, 75, true, true);
      player3ImageView = new ImageView(player3Image);
      player3Label = new Label(game.getPlayers().get(2).getName());
      Player Player3 = players.get(2);
      String Player3Position = String.valueOf(Player3.getPosition());
      player3TurnLabel.setStyle(Player3Position);
      VBox player3Box = new VBox(5, player3ImageView, player3Label, player3TurnLabel);
      player3Box.setAlignment(Pos.CENTER);
      playerGrid.add(player3Box, 0, 1);
    }

    if (game.getCharacterNames().size() >= 4 ) {
      player4Image = new Image("PlayerIcons/" + game.getCharacterNames().get(3) + ".png", 75, 75, true, true);
      player4ImageView = new ImageView(player4Image);
      player4Label = new Label(game.getPlayers().get(1).getName());
      Player Player4 = players.get(3);
      String Player4Position = String.valueOf(Player4.getPosition());
      player4TurnLabel.setStyle(Player4Position);
      VBox player4Box = new VBox(5, player4ImageView, player4Label, player4TurnLabel);
      player4Box.setAlignment(Pos.CENTER);
      playerGrid.add(player4Box, 1, 1);
    }

    rollButton = new Button("Roll Dice");
    rollButton.setStyle("-fx-font-size: 12px;");
    rollButton.setOnAction(e -> handleRoll());

    backButton = new Button("Back");
    backButton.setStyle("-fx-font-size: 12px;");
    backButton.setOnAction(e -> {
      IntroScreenView intro = new IntroScreenView(stage);
      intro.prepareScene();
    });

    List<Player> genericPlayers = game.getPlayers();
    boardView = new BoardView(game.getBoard(), genericPlayers);

    HBox topRightInfo = new HBox(60, currentPlayerLabel, imageView);
    topRightInfo.setAlignment(Pos.CENTER_LEFT);
    topRightInfo.setPadding(new Insets(20, 10, 0, 10));

    VBox playerLabelBox = new VBox(20, playerLabel, playerGrid);
    playerLabelBox.setAlignment(Pos.TOP_CENTER);
    playerLabelBox.setPadding(new Insets(150, 0, 0, 0)); //

    HBox buttonBox = new HBox(20, backButton, rollButton);
    buttonBox.setAlignment(Pos.BOTTOM_CENTER);
    buttonBox.setPadding(new Insets(10));

    BorderPane rightColumn = new BorderPane();
    rightColumn.setTop(topRightInfo);
    rightColumn.setCenter(playerLabelBox);
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
    currentPlayerImage = new Image("PlayerIcons/" + currentPlayer.getCharacter() + ".png", 150, 150, true, true);
    imageView.setImage(currentPlayerImage);

    int roll = game.rollDice();
    int tentative = currentPlayer.getPosition() + roll;

    if (tentative > 100) {
      diceResultLabel.setText("Roll result: " + roll + " (invalid move)");
      return;
    }

    // Step 1: Show landing tile
    diceResultLabel.setText("Roll result: " + roll);
    positionLabel.setText("Position: " + tentative);
    currentPlayer.setPosition(tentative);
    boardView.render();
    rollButton.setDisable(true);

    PauseTransition pause = new PauseTransition(Duration.seconds(1));
    pause.setOnFinished(event -> {
      int finalPos = game.getBoard().getFinalPosition(tentative);

      if (finalPos != tentative) {
        currentPlayer.setPosition(finalPos);
        boardView.render();
      }

      currentPlayer.setPosition(finalPos);
      positionLabel.setText("Position: " + finalPos);

      updatePositionLabel();

      if (currentPlayer.hasWon()) {
        showWinner(currentPlayer);
      } else {
        game.advanceTurn();
        currentPlayerLabel.setText("Current turn:");
        rollButton.setDisable(false);
      }
    });
    pause.play();
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

  private void updatePositionLabel() {
    List<Player> players = game.getPlayers();

    if (!players.isEmpty()) {
      Player player1 = players.getFirst();
      player1TurnLabel.setText(String.valueOf(player1.getPosition()));
    }

    if (players.size() >= 2) {
      Player player2 = players.get(1);
      player2TurnLabel.setText(String.valueOf(player2.getPosition()));
    }

    if (players.size() >= 3) {
      Player player3 = players.get(2);
      player3TurnLabel.setText(String.valueOf(player3.getPosition()));
    }

    if (players.size() >= 4) {
      Player player4 = players.get(3);
      player4TurnLabel.setText(String.valueOf(player4.getPosition()));
    }
  }
}