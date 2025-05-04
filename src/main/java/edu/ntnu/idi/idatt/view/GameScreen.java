
package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.util.List;

public abstract class GameScreen implements GameScreenObserver {
  protected static final int TILE_SIZE = 60;
  protected static final int BOARD_SIZE = 10;

  protected BorderPane root;
  protected GridPane boardGrid;
  protected StackPane overlayPane = new StackPane();
  protected Label currentPlayerLabel;
  protected Label positionLabel;
  protected Label diceResultLabel;
  protected Button rollButton;

  protected abstract Image getCurrentPlayerImage();
  protected abstract String getTileColor(int tileNumber);
  protected abstract List<Player> getPlayersAtPosition(int tileNumber);
  protected abstract void initializeOverlay();
  protected abstract void handleRoll();

  public Parent getRoot() {
    return root;
  }

  protected void createUI() {
    root = new BorderPane();
    root.setStyle("-fx-padding: 30;");

    initializeBoardGrid();
    Platform.runLater(() -> {
      boardGrid.applyCss();
      boardGrid.layout();
      initializeOverlay();
    });

    StackPane boardWithOverlay = new StackPane(boardGrid, overlayPane);
    overlayPane.toFront();
    boardGrid.toBack();
    root.setLeft(boardWithOverlay);

    VBox infoBox = new VBox(20);
    infoBox.setAlignment(Pos.TOP_CENTER);
    currentPlayerLabel = new Label("Current turn:");
    positionLabel = new Label("Position:");
    diceResultLabel = new Label("Roll result:");
    ImageView playerImage = new ImageView();
    playerImage.setFitWidth(60);
    playerImage.setFitHeight(60);
    playerImage.setPreserveRatio(true);
    Image image = getCurrentPlayerImage();
    if (image != null) playerImage.setImage(image);
    infoBox.getChildren().addAll(playerImage, currentPlayerLabel, positionLabel, diceResultLabel);
    root.setRight(infoBox);

    rollButton = new Button("Roll Dice");
    rollButton.setOnAction(e -> handleRoll());
    BorderPane.setAlignment(rollButton, Pos.CENTER);
    root.setBottom(rollButton);
  }

  protected void initializeBoardGrid() {
    boardGrid = new GridPane();
    boardGrid.setHgap(2);
    boardGrid.setVgap(2);
    boardGrid.setAlignment(Pos.CENTER);
    boardGrid.setPrefSize(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);

    for (int i = 0; i < BOARD_SIZE; i++) {
      ColumnConstraints cc = new ColumnConstraints(TILE_SIZE);
      cc.setHalignment(HPos.CENTER);
      boardGrid.getColumnConstraints().add(cc);
      RowConstraints rc = new RowConstraints(TILE_SIZE);
      rc.setValignment(VPos.CENTER);
      boardGrid.getRowConstraints().add(rc);
    }

    renderBoardGrid();
  }

  protected void renderBoardGrid() {
    boardGrid.getChildren().clear();
    for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
      int tileNum = i + 1;
      StackPane cell = new StackPane();
      cell.setPrefSize(TILE_SIZE, TILE_SIZE);
      cell.setStyle("-fx-border-color: black; -fx-background-color: " + getTileColor(tileNum) + ";");
      Text t = new Text(String.valueOf(tileNum));
      t.setStyle("-fx-fill: #555;");
      cell.getChildren().add(t);
      List<Player> players = getPlayersAtPosition(tileNum);
      for (int idx = 0; idx < players.size(); idx++) {
        Player p = players.get(idx);
        String cn = p.getCharacter() != null ? p.getCharacter().toLowerCase() : "default";
        var url = getClass().getResource("/player_icons/" + cn + ".png");
        if (url == null) continue;
        Image pi = new Image(url.toExternalForm(), TILE_SIZE * 0.5, TILE_SIZE * 0.5, true, true);
        ImageView iv = new ImageView(pi);
        iv.setTranslateY(TILE_SIZE * 0.15 * idx);
        cell.getChildren().add(iv);
      }
      int row = BOARD_SIZE - 1 - (i / BOARD_SIZE);
      int col = (row % 2 == 0) ? (i % BOARD_SIZE) : (BOARD_SIZE - 1 - (i % BOARD_SIZE));
      boardGrid.add(cell, col, row);
    }
  }

  protected void animatePlayerMove(int fromTile, int toTile) {
    Image avatar = getCurrentPlayerImage();
    if (avatar == null) return;
    Point2D start = getCellCenter(fromTile);
    Point2D end   = getCellCenter(toTile);
    ImageView icon = new ImageView(avatar);
    icon.setFitWidth(TILE_SIZE * 0.5);
    icon.setFitHeight(TILE_SIZE * 0.5);
    icon.setLayoutX(start.getX() - TILE_SIZE * 0.25);
    icon.setLayoutY(start.getY() - TILE_SIZE * 0.25);
    overlayPane.getChildren().add(icon);
    TranslateTransition tt = new TranslateTransition(Duration.millis(400), icon);
    tt.setByX(end.getX() - start.getX());
    tt.setByY(end.getY() - start.getY());
    tt.setOnFinished(e -> Platform.runLater(() -> {
      overlayPane.getChildren().remove(icon);
      boardGrid.applyCss();
      boardGrid.layout();
      initializeOverlay();
    }));
    tt.play();
  }

  protected Point2D getCellCenter(int tileNum) {
    int i = tileNum - 1;
    int row = BOARD_SIZE - 1 - (i / BOARD_SIZE);
    int col = (row % 2 == 0) ? (i % BOARD_SIZE) : (BOARD_SIZE - 1 - (i % BOARD_SIZE));
    for (var node : boardGrid.getChildren()) {
      Integer r = GridPane.getRowIndex(node), c = GridPane.getColumnIndex(node);
      if (r != null && c != null && r == row && c == col) {
        var b = node.localToParent(node.getBoundsInLocal());
        return new Point2D(b.getMinX() + b.getWidth()/2, b.getMinY() + b.getHeight()/2);
      }
    }
    return new Point2D(0,0);
  }

  @Override public void onPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
    animatePlayerMove(oldPosition, newPosition);
  }
  @Override public void onDiceRolled(int result) {
    diceResultLabel.setText("Roll result: " + result);
  }
  @Override public void onPlayerTurnChanged(Player currentPlayer) {
    currentPlayerLabel.setText("Current turn: " + currentPlayer.getName());
    positionLabel.setText("Position: " + currentPlayer.getPosition());
  }
  @Override public void onGameOver(Player winner) {
    Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
    gameOverAlert.initOwner(root.getScene().getWindow());
    gameOverAlert.initModality(Modality.WINDOW_MODAL);
    gameOverAlert.setTitle("Game Over");
    gameOverAlert.setHeaderText("Winner!");
    gameOverAlert.setContentText(winner.getName() + " has won the game.");
    gameOverAlert.showAndWait();
  }
  @Override public void onGameSaved(String filePath) {
    Alert savedGameAlert = new Alert(Alert.AlertType.INFORMATION);
    savedGameAlert.initOwner(root.getScene().getWindow());
    savedGameAlert.initModality(Modality.WINDOW_MODAL);
    savedGameAlert.setTitle("Game Saved");
    savedGameAlert.setHeaderText("Game Saved");
    savedGameAlert.setContentText("Saved to: " + filePath);
    savedGameAlert.showAndWait();
  }
}
