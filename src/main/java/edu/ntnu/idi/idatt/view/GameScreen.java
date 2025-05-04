package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.List;

public abstract class GameScreen implements GameScreenObserver {
  protected static final int TILE_SIZE = 60;
  protected static final int BOARD_SIZE = 10;
  protected BorderPane root;
  protected GridPane boardGrid;
  protected Pane overlayPane;
  protected StackPane boardWithOverlay;
  protected Label currentPlayerLabel;
  protected Label positionLabel;
  protected Label diceResultLabel;
  protected Button rollButton;
  protected ImageView playerImage;
  protected VBox playerInfoList;

  public Parent getRoot() {
    return root;
  }

  protected void createUI() {
    root = new BorderPane();
    initializeBoardGrid();
    overlayPane = new Pane();
    overlayPane.setPickOnBounds(false);
    overlayPane.setMouseTransparent(true);
    overlayPane.prefWidthProperty().bind(boardGrid.widthProperty());
    overlayPane.prefHeightProperty().bind(boardGrid.heightProperty());
    boardWithOverlay = new StackPane(boardGrid, overlayPane);
    StackPane.setAlignment(boardGrid, Pos.TOP_LEFT);
    StackPane.setAlignment(overlayPane, Pos.TOP_LEFT);
    root.setLeft(boardWithOverlay);
    currentPlayerLabel = new Label("Current turn:");
    playerImage = new ImageView();
    playerImage.setFitWidth(70);
    playerImage.setFitHeight(70);
    VBox currentBox = new VBox(5, currentPlayerLabel, playerImage);
    currentBox.setAlignment(Pos.CENTER);
    playerInfoList = new VBox(10);
    playerInfoList.setAlignment(Pos.CENTER_LEFT);
    positionLabel = new Label("Position:");
    diceResultLabel = new Label("Roll result:");
    rollButton = new Button("Roll Dice");
    rollButton.setOnAction(e -> handleRoll());
    VBox bottomBox = new VBox(5, positionLabel, diceResultLabel, rollButton);
    bottomBox.setAlignment(Pos.CENTER);
    VBox infoPanel = new VBox(20, currentBox, playerInfoList, bottomBox);
    infoPanel.setAlignment(Pos.TOP_CENTER);
    root.setRight(infoPanel);
    updatePlayerImages();
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
    for (int i = 1; i <= BOARD_SIZE * BOARD_SIZE; i++) {
      StackPane cell = new StackPane();
      cell.setPrefSize(TILE_SIZE, TILE_SIZE);
      cell.setStyle("-fx-border-color: black; -fx-background-color: " + getTileColor(i));
      Text txt = new Text(String.valueOf(i));
      txt.setStyle("-fx-fill: #555;");
      cell.getChildren().add(txt);
      List<Player> players = getPlayersAtPosition(i);
      for (int idx = 0; idx < players.size(); idx++) {
        Image img = getCurrentPlayerImage();
        if (img != null) {
          ImageView iv = new ImageView(img);
          iv.setFitWidth(TILE_SIZE * 0.5);
          iv.setFitHeight(TILE_SIZE * 0.5);
          iv.setTranslateY(10 * idx);
          cell.getChildren().add(iv);
        }
      }
      int row = BOARD_SIZE - 1 - ((i - 1) / BOARD_SIZE);
      int col = (row % 2 == 0)
          ? ((i - 1) % BOARD_SIZE)
          : (BOARD_SIZE - 1 - ((i - 1) % BOARD_SIZE));
      boardGrid.add(cell, col, row);
    }
  }

  protected Point2D getCellCenter(int tileNum) {
    int idx = tileNum - 1;
    int row = BOARD_SIZE - 1 - (idx / BOARD_SIZE);
    int col = (row % 2 == 0)
        ? (idx % BOARD_SIZE)
        : (BOARD_SIZE - 1 - (idx % BOARD_SIZE));
    for (Node n : boardGrid.getChildren()) {
      Integer r = GridPane.getRowIndex(n), c = GridPane.getColumnIndex(n);
      if (r != null && c != null && r == row && c == col) {
        Bounds b = n.localToParent(n.getBoundsInLocal());
        return new Point2D(b.getMinX() + b.getWidth() / 2, b.getMinY() + b.getHeight() / 2);
      }
    }
    return new Point2D(0, 0);
  }

  protected void updatePlayerImages() {
    playerInfoList.getChildren().clear();
    for (Player p : getAllPlayers()) {
      Label name = new Label(p.getName());
      Label pos = new Label("Pos: " + p.getPosition());
      HBox row = new HBox(5, name, pos);
      playerInfoList.getChildren().add(row);
    }
  }

  @Override public void onPlayerPositionChanged(Player player, int oldPos, int newPos) {}
  @Override public void onDiceRolled(int result) { diceResultLabel.setText("Roll result: " + result); }
  @Override public void onPlayerTurnChanged(Player current) {
    currentPlayerLabel.setText("Current turn: " + current.getName());
    positionLabel.setText("Position: " + current.getPosition());
  }
  @Override public void onGameOver(Player winner) {}
  @Override public void onGameSaved(String path) {}

  protected abstract List<Player> getAllPlayers();
  protected abstract Image getCurrentPlayerImage();
  protected abstract void handleRoll();
  protected abstract String getTileColor(int tileNumber);
  protected abstract List<Player> getPlayersAtPosition(int tileNumber);
  protected abstract Pane getOverlay();
}
