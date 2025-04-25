package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.model.snakesladders.SnakesAndLadders;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;
import static edu.ntnu.idi.idatt.view.common.AbstractCharacterSelectionView.logger;

public class GameScreenView {
  private final Stage stage;
  private final SnakesAndLadders game;
  private final String boardFile;
  private final String csvFileName;

  private BoardView boardView;
  private Label currentPlayerLabel;
  private Image currentPlayerImage;
  private Label diceResultLabel;
  private Label positionLabel;
  private Button rollButton;
  private ImageView imageView;
  private Button saveButton;

  Label player1TurnLabel = new Label();
  Label player2TurnLabel = new Label();
  Label player3TurnLabel = new Label();
  Label player4TurnLabel = new Label();

  public GameScreenView(Stage stage, SnakesAndLadders game, String boardFile, String csvFileName) {
    this.stage = stage;
    this.game = game;
    this.boardFile = boardFile;
    this.csvFileName = csvFileName;
  }

  public void show() {
    currentPlayerLabel = new Label("Current turn: ");
    currentPlayerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    currentPlayerLabel.setMinWidth(150);

    diceResultLabel = new Label("Roll result: -");
    positionLabel = new Label("Position: -");

    String characterName = game.getCurrentPlayer().getCharacter();
    if (characterName == null || characterName.isEmpty()) {
      characterName = "Unknown-Player";
    }
    currentPlayerImage = new Image("PlayerIcons/" + characterName + ".png", 150, 150, true, true);
    imageView = new ImageView(currentPlayerImage);

    Label playerLabel = new Label("Players");
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

    String player1Character =
        !game.getCharacterNames().isEmpty() ? game.getCharacterNames().getFirst() : "default";
    Image player1Image = new Image("PlayerIcons/" + player1Character + ".png", 75, 75, true, true);
    ImageView player1ImageView = new ImageView(player1Image);
    Label player1Label = new Label(game.getPlayers().getFirst().getName());
    player1TurnLabel.setText(Player1Position);
    VBox player1Box = new VBox(5, player1ImageView, player1Label, player1TurnLabel);
    player1Box.setAlignment(Pos.CENTER);
    playerGrid.add(player1Box, 0, 0);

    String player2Character = game.getCharacterNames().size() > 1 ? game.getCharacterNames().get(1) : "default";
    Image player2Image = new Image("PlayerIcons/" + player2Character + ".png", 75, 75, true, true);
    ImageView player2ImageView = new ImageView(player2Image);
    Label player2Label = new Label(game.getPlayers().get(1).getName());
    player2TurnLabel.setText(Player2Position);
    VBox player2Box = new VBox(5, player2ImageView, player2Label, player2TurnLabel);
    player2Box.setAlignment(Pos.CENTER);
    playerGrid.add(player2Box, 1, 0);

    if (game.getCharacterNames().size() >= 3) {
      String player3Character = game.getCharacterNames().get(2);
      Image player3Image = new Image("PlayerIcons/" + player3Character + ".png", 75, 75, true,
          true);
      ImageView player3ImageView = new ImageView(player3Image);
      Label player3Label = new Label(game.getPlayers().get(2).getName());
      Player Player3 = players.get(2);
      String Player3Position = String.valueOf(Player3.getPosition());
      player3TurnLabel.setText(Player3Position);
      VBox player3Box = new VBox(5, player3ImageView, player3Label, player3TurnLabel);
      player3Box.setAlignment(Pos.CENTER);
      playerGrid.add(player3Box, 0, 1);
    }

    if (game.getCharacterNames().size() >= 4) {
      String player4Character = game.getCharacterNames().get(3);
      Image player4Image = new Image("PlayerIcons/" + player4Character + ".png", 75, 75, true,
          true);
      ImageView player4ImageView = new ImageView(player4Image);
      Label player4Label = new Label(game.getPlayers().get(3).getName());
      Player Player4 = players.get(3);
      String Player4Position = String.valueOf(Player4.getPosition());
      player4TurnLabel.setText(Player4Position);
      VBox player4Box = new VBox(5, player4ImageView, player4Label, player4TurnLabel);
      player4Box.setAlignment(Pos.CENTER);
      playerGrid.add(player4Box, 1, 1);
    }

    rollButton = new Button("Roll Dice");
    rollButton.setStyle("-fx-font-size: 12px;");
    rollButton.setOnAction(e -> handleRoll());

    Button backButton = new Button("Back");
    backButton.setStyle("-fx-font-size: 12px;");
    backButton.setOnAction(e -> {
      IntroScreenView intro = new IntroScreenView(stage);
      intro.prepareScene();
    });

    saveButton = new Button("Save Game");
    saveButton.setStyle("-fx-font-size: 12px;");
    saveButton.setOnAction(e -> saveGame());

    List<Player> genericPlayers = game.getPlayers();
    boardView = new BoardView(game.getBoard(), genericPlayers);

    HBox topRightInfo = new HBox(60, currentPlayerLabel, imageView);
    topRightInfo.setAlignment(Pos.CENTER_LEFT);
    topRightInfo.setPadding(new Insets(20, 10, 0, 10));

    VBox playerLabelBox = new VBox(20, playerLabel, playerGrid);
    playerLabelBox.setAlignment(Pos.TOP_CENTER);
    playerLabelBox.setPadding(new Insets(150, 0, 0, 0));

    HBox buttonBox = new HBox(20, backButton, rollButton, saveButton);
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

    updateView();

    stage.setScene(new Scene(root));
    stage.setTitle("Snakes and Ladders - Game");
    stage.show();
  }

  private void saveGame() {
    String csvFilePath = csvFileName;

    List<String> lines = new ArrayList<>();
    lines.add("SNLBoard:" + boardFile); // Add the board name first

    for (Player player : game.getPlayers()) {
      lines.add(player.getName() + "," + player.getCharacter() + "," + player.getPosition());
    }

    try {
      Files.write(Paths.get(csvFilePath), lines);
      logger.info("Game saved to {}", csvFilePath);
    } catch (IOException e) {
      logger.error("Failed to save game: {}", e.getMessage());
      showAlert("Save Error", "Failed to save game: " + e.getMessage());
    }
  }

  private void handleRoll() {
    Player currentPlayer = game.getCurrentPlayer();

    String characterName = currentPlayer.getCharacter();
    if (characterName == null || characterName.isEmpty()) {
      characterName = "default";
    }
    currentPlayerImage = new Image("PlayerIcons/" + characterName + ".png", 150, 150, true, true);
    imageView.setImage(currentPlayerImage);

    int roll = game.rollDice();
    int tentative = currentPlayer.getPosition() + roll;

    if (tentative > 100) {
      diceResultLabel.setText("Roll result: " + roll + " (invalid move)");
      return;
    }

    diceResultLabel.setText("Roll result: " + roll);
    positionLabel.setText("Position: " + tentative);
    currentPlayer.setPosition(tentative);
    boardView.render();
    rollButton.setDisable(true);
    PauseTransition pause = getPauseTransition(tentative, currentPlayer);
    pause.play();
  }

  private PauseTransition getPauseTransition(int tentative, Player currentPlayer) {
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
        updateView();
        rollButton.setDisable(false);
      }
    });
    return pause;
  }

  private void updateView() {
    Player currentPlayer = game.getCurrentPlayer();
    currentPlayerLabel.setText("Current turn: " + currentPlayer.getName());
    positionLabel.setText("Position: " + currentPlayer.getPosition());

    String characterName = currentPlayer.getCharacter();
    if (characterName == null || characterName.isEmpty()) {
      characterName = "default";
    }
    currentPlayerImage = new Image("PlayerIcons/" + characterName + ".png", 150, 150, true, true);
    imageView.setImage(currentPlayerImage);
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
