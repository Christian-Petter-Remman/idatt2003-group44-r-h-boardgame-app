package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.controller.common.GameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GameScreenView implements GameScreenObserver {
  private static final Logger logger = LoggerFactory.getLogger(GameScreenView.class);

  private final GameScreenController controller;
  private final BorderPane root;

  private BoardView boardView;
  private Label currentPlayerLabel;
  private ImageView currentPlayerImageView;
  private Label diceResultLabel;
  private Label positionLabel;
  private Button rollButton;

  private Label player1TurnLabel;
  private Label player2TurnLabel;
  private Label player3TurnLabel;
  private Label player4TurnLabel;

  public GameScreenView(GameScreenController controller) {
    this.controller = controller;
    this.root = new BorderPane();
    controller.registerObserver(this);
    initialize();
  }

  private void initialize() {
    player1TurnLabel = new Label();
    player2TurnLabel = new Label();
    player3TurnLabel = new Label();
    player4TurnLabel = new Label();

    List<Player> players = controller.getPlayers();
    boardView = new BoardView(controller.getBoard(), players);

    currentPlayerLabel = new Label("Current turn: ");
    currentPlayerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    currentPlayerLabel.setMinWidth(150);

    diceResultLabel = new Label("Roll result: -");
    positionLabel = new Label("Position: -");

    String characterName = controller.getCurrentPlayer().getCharacter();
    if (characterName == null || characterName.isEmpty()) {
      characterName = "Unknown-Player";
    }
    Image currentPlayerImage = new Image("PlayerIcons/" + characterName + ".png", 150, 150, true, true);
    currentPlayerImageView = new ImageView(currentPlayerImage);

    rollButton = new Button("Roll Dice");
    rollButton.setStyle("-fx-font-size: 12px;");
    rollButton.setOnAction(e -> controller.handleRoll());

    Button backButton = new Button("Back");
    backButton.setStyle("-fx-font-size: 12px;");
    backButton.setOnAction(e -> controller.navigateBack());

    Button saveButton = new Button("Save Game");
    saveButton.setStyle("-fx-font-size: 12px;");
    saveButton.setOnAction(e -> controller.saveGame());

    Label playerLabel = new Label("Players");
    playerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    playerLabel.setAlignment(Pos.CENTER);
    playerLabel.setMaxWidth(Double.MAX_VALUE);

    GridPane playerGrid = createPlayerGrid(controller.getPlayers(), controller.getCharacterNames());

    HBox topRightInfo = new HBox(60, currentPlayerLabel, currentPlayerImageView);
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

    HBox mainContent = new HBox(40, boardView, rightColumn);
    mainContent.setAlignment(Pos.CENTER_LEFT);
    mainContent.setPadding(new Insets(20));

    root.setCenter(mainContent);

    updatePlayerTurnLabels();
    updateCurrentPlayerView(controller.getCurrentPlayer());
  }

  private GridPane createPlayerGrid(List<Player> players, List<String> characterNames) {
    GridPane playerGrid = new GridPane();
    playerGrid.setHgap(60);
    playerGrid.setVgap(60);
    playerGrid.setAlignment(Pos.CENTER);

    if (players.size() >= 1) {
      Player player1 = players.get(0);
      String player1Character = !characterNames.isEmpty() ? characterNames.get(0) : "default";
      VBox player1Box = createPlayerBox(player1, player1Character, player1TurnLabel);
      playerGrid.add(player1Box, 0, 0);
    }

    if (players.size() >= 2) {
      Player player2 = players.get(1);
      String player2Character = characterNames.size() > 1 ? characterNames.get(1) : "default";
      VBox player2Box = createPlayerBox(player2, player2Character, player2TurnLabel);
      playerGrid.add(player2Box, 1, 0);
    }

    if (players.size() >= 3) {
      Player player3 = players.get(2);
      String player3Character = characterNames.size() > 2 ? characterNames.get(2) : "default";
      VBox player3Box = createPlayerBox(player3, player3Character, player3TurnLabel);
      playerGrid.add(player3Box, 0, 1);
    }

    if (players.size() >= 4) {
      Player player4 = players.get(3);
      String player4Character = characterNames.size() > 3 ? characterNames.get(3) : "default";
      VBox player4Box = createPlayerBox(player4, player4Character, player4TurnLabel);
      playerGrid.add(player4Box, 1, 1);
    }

    return playerGrid;
  }

  private VBox createPlayerBox(Player player, String characterName, Label positionLabel) {
    Image playerImage = new Image("PlayerIcons/" + characterName + ".png", 75, 75, true, true);
    ImageView playerImageView = new ImageView(playerImage);
    Label playerNameLabel = new Label(player.getName());
    positionLabel.setText(String.valueOf(player.getPosition()));

    VBox playerBox = new VBox(5, playerImageView, playerNameLabel, positionLabel);
    playerBox.setAlignment(Pos.CENTER);
    return playerBox;
  }

  private void updatePlayerTurnLabels() {
    List<Player> players = controller.getPlayers();

    if (players.size() >= 1) {
      player1TurnLabel.setText(String.valueOf(players.get(0).getPosition()));
    }

    if (players.size() >= 2) {
      player2TurnLabel.setText(String.valueOf(players.get(1).getPosition()));
    }

    if (players.size() >= 3) {
      player3TurnLabel.setText(String.valueOf(players.get(2).getPosition()));
    }

    if (players.size() >= 4) {
      player4TurnLabel.setText(String.valueOf(players.get(3).getPosition()));
    }
  }

  private void updateCurrentPlayerView(Player currentPlayer) {
    currentPlayerLabel.setText("Current turn: " + currentPlayer.getName());
    positionLabel.setText("Position: " + currentPlayer.getPosition());

    String characterName = currentPlayer.getCharacter();
    if (characterName == null || characterName.isEmpty()) {
      characterName = "default";
    }
    Image currentPlayerImage = new Image("PlayerIcons/" + characterName + ".png", 150, 150, true, true);
    currentPlayerImageView.setImage(currentPlayerImage);
  }

  public void show() {
    controller.displayGameScreen(this);
  }

  public BorderPane getRoot() {
    return root;
  }

  @Override
  public void onPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
    boardView.render();
    updatePlayerTurnLabels();

    if (player.equals(controller.getCurrentPlayer())) {
      positionLabel.setText("Position: " + newPosition);
    }
  }

  @Override
  public void onDiceRolled(int result) {
    diceResultLabel.setText("Roll result: " + result);
    rollButton.setDisable(true);
  }

  @Override
  public void onPlayerTurnChanged(Player currentPlayer) {
    updateCurrentPlayerView(currentPlayer);
    rollButton.setDisable(false);
  }

  @Override
  public void onGameOver(Player winner) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Over");
    alert.setHeaderText("🎉 " + winner.getName() + " has won the game!");
    alert.setContentText("Final position: " + winner.getPosition());
    alert.showAndWait();
    rollButton.setDisable(true);
  }

  @Override
  public void onGameSaved(String filePath) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Saved");
    alert.setHeaderText("Game saved successfully");
    alert.setContentText("Game saved to: " + filePath);
    alert.showAndWait();
  }
}
