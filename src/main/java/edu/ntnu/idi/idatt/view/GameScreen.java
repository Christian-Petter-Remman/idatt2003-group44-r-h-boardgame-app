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

  protected Runnable saveListener;
  protected Runnable backListener;

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

    overlayPane = new Pane();
    overlayPane.setPickOnBounds(false);
    overlayPane.setMouseTransparent(true);
    overlayPane.prefWidthProperty().bind(boardGrid.widthProperty());
    overlayPane.prefHeightProperty().bind(boardGrid.heightProperty());

    boardWithOverlay = new StackPane(boardGrid, overlayPane);
    StackPane.setAlignment(boardGrid, Pos.TOP_LEFT);
    StackPane.setAlignment(overlayPane, Pos.TOP_LEFT);
    root.setLeft(boardWithOverlay);


    initializeBoardGrid();
    initializeOverlay();

    StackPane boardWithOverlay = new StackPane();
    boardWithOverlay.getChildren().addAll(boardGrid, getOverlay());
    boardGrid.toBack();
    if (getOverlay() != null) getOverlay().toFront();
    root.setLeft(boardWithOverlay);

    VBox currentPlayerBox = new VBox(5);
    currentPlayerBox.setAlignment(Pos.CENTER);
    
    
    
// SPLIT MASTER 
  
  
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

    playerImage.setPreserveRatio(true);
    currentPlayerBox.getChildren().addAll(currentPlayerLabel, playerImage);

    playerInfoList = new VBox(10);
    playerInfoList.setAlignment(Pos.CENTER_LEFT);

    VBox bottomBox = new VBox(10);
    positionLabel = new Label("Position:");
    diceResultLabel = new Label("Roll result:");
    rollButton = new Button("Roll Dice");
    rollButton.setOnAction(e -> handleRoll());

    Button saveButton = new Button("Save Game");
    saveButton.setOnAction(e -> {
      if (saveListener != null) {
        saveListener.run();
      }
    });

    Button homeButton = new Button("Home");
    homeButton.setOnAction(e -> {
      if (backListener != null) {
        backListener.run();
      }
    });
    homeButton.setStyle(
            "-fx-font-size: 14px;" +
                    "-fx-background-color: #cccccc;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-radius: 12;" +
                    "-fx-padding: 6 14;" +
                    "-fx-cursor: hand;"
    );

    bottomBox.setAlignment(Pos.CENTER);
    bottomBox.getChildren().addAll(positionLabel, diceResultLabel, rollButton, saveButton, homeButton);

    VBox infoPanel = new VBox(30);
    infoPanel.setAlignment(Pos.TOP_CENTER);
    infoPanel.getChildren().addAll(currentPlayerBox, playerInfoList, bottomBox);
    root.setRight(infoPanel);
    updatePlayerImages();

  }

  protected void updatePlayerImages() {
    playerInfoList.getChildren().clear();
    for (Player player : getAllPlayers()) {
      String characterName = player.getCharacter() != null ? player.getCharacter().toLowerCase() : "default";
      try {
        URL url = getClass().getResource("/player_icons/" + characterName + ".png");
        Image image = url != null ? new Image(url.toExternalForm(), 50, 50, true, true) : null;
        ImageView imageView = image != null ? new ImageView(image) : new ImageView();

        Label name = new Label(player.getName());
        Label pos = new Label("Pos: " + player.getPosition());
        Label points = new Label("Pts: " + player.getPoints());

        HBox row = new HBox(10, imageView, name, pos, points);
        row.setAlignment(Pos.CENTER_LEFT);

        playerInfoList.getChildren().add(row);
      } catch (Exception e) {
        logger.error("Error loading image for character: {}", characterName, e);
      }
    }
  }

  protected void initializeBoardGrid() {
    boardGrid = new GridPane();
    boardGrid.setHgap(2);
    boardGrid.setVgap(2);
    boardGrid.setAlignment(Pos.CENTER);
    boardGrid.setPrefSize(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);

    for (int i = 0; i < BOARD_SIZE; i++) {
      ColumnConstraints colConst = new ColumnConstraints(TILE_SIZE);
      colConst.setHalignment(HPos.CENTER);
      boardGrid.getColumnConstraints().add(colConst);


    VBox infoPanel = new VBox(20, currentBox, playerInfoList, bottomBox);
    infoPanel.setAlignment(Pos.TOP_CENTER);
    infoPanel.setPrefWidth(200);
    infoPanel.setMaxWidth(200);
    root.setRight(infoPanel);

    updatePlayerImages();
  }

  protected abstract Image getCurrentPlayerImage();

  protected void renderBoardGrid() {
    boardGrid.getChildren().clear();
    for (int i = 1; i <= BOARD_SIZE * BOARD_SIZE; i++) {
      StackPane cell = new StackPane();
      cell.setPrefSize(TILE_SIZE, TILE_SIZE);
      cell.setStyle("-fx-border-color: black; -fx-background-color: " + getTileColor(i) + ";");
      Text txt = new Text(String.valueOf(i));
      txt.setStyle("-fx-fill: #555;");
      cell.getChildren().add(txt);

      List<Player> players = getPlayersAtPosition(i);
      for (int idx = 0; idx < players.size(); idx++) {
        Player p = players.get(idx);
        Image img = getPlayerImage(p);
        if (img == null) {
          continue;
        }
        ImageView iv = new ImageView(img);
        iv.setFitWidth(TILE_SIZE * 0.5);
        iv.setFitHeight(TILE_SIZE * 0.5);
        iv.setPreserveRatio(true);
        iv.setTranslateY(10 * idx);
        cell.getChildren().add(iv);
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

  protected StackPane createTile(int tileNum) {
    StackPane cell = new StackPane();
    cell.setPrefSize(TILE_SIZE, TILE_SIZE);

    cell.setStyle("-fx-border-color: black; -fx-background-color: " + getTileColor(tileNum) + ";");

    Text tileNumber = new Text(String.valueOf(tileNum));
    tileNumber.setStyle("-fx-fill: #555;");
    cell.getChildren().add(tileNumber);

    List<Player> playersOnTile = getPlayersAtPosition(tileNum);
    for (Player player : playersOnTile) {
      String characterName = player.getCharacter() != null ? player.getCharacter().toLowerCase() : "default";
      try {
        var url = getClass().getResource("/player_icons/" + characterName + ".png");
        if (url == null) continue;


  protected void updatePlayerImages() {
    playerInfoList.getChildren().clear();
    for (Player p : getAllPlayers()) {
      Image img = getPlayerImage(p);
      if (img == null) {
        continue;
      }
      ImageView iv = new ImageView(img);
      iv.setFitWidth(40);
      iv.setFitHeight(40);
      iv.setPreserveRatio(true);
      Label name = new Label(p.getName());
      Label pos = new Label("Pos: " + p.getPosition());
      HBox row = new HBox(5, iv, name, pos);
      row.setAlignment(Pos.CENTER_LEFT);
      playerInfoList.getChildren().add(row);
    }
  }

  @Override
  public void onPlayerPositionChanged(Player player, int oldPos, int newPos) {
  }

  @Override
  public void onDiceRolled(int result) {
    diceResultLabel.setText("Roll result: " + result);
  }

  @Override
  public void onPlayerTurnChanged(Player current) {
    currentPlayerLabel.setText("Current turn: " + current.getName());
    positionLabel.setText("Position: " + current.getPosition());
  }

  @Override
  public void onGameOver(Player winner) {
  }

  @Override
  public void onGameSaved(String path) {
  }

  protected abstract List<Player> getAllPlayers();

  protected abstract Image getPlayerImage(Player player);

  public void setSaveListener(Runnable listener) {
    this.saveListener = listener;
  }

  public void setBackListener(Runnable listener) {
    this.backListener = listener;
  }


  protected abstract void handleRoll();

  protected abstract String getTileColor(int tileNumber);

  protected abstract List<Player> getPlayersAtPosition(int tileNumber);
  protected abstract Pane getOverlay();
  protected abstract List<Player> getAllPlayers();  // NEW abstract method
  protected void initializeOverlay() {}


  protected abstract List<Player> getPlayersAtPosition(int tileNumber);
}
