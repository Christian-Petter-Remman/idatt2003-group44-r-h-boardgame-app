package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.model.common.Player;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

public abstract class GameScreen {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected static final int TILE_SIZE = 60;
  protected static final int BOARD_SIZE = 10;

  protected BorderPane root;
  protected GridPane boardGrid;
  protected VBox playerImagesBox;
  protected Label currentPlayerLabel;
  protected Label positionLabel;
  protected Label diceResultLabel;
  protected Button rollButton;
  protected ImageView playerImage;
  protected VBox playerInfoList;

  public GameScreen() {}

  protected void createUI() {
    root = new BorderPane();
    root.setStyle("-fx-padding: 30;");

    // 🎮 Board (left)
    initializeBoardGrid();
    initializeOverlay();

    StackPane boardWithOverlay = new StackPane();
    boardWithOverlay.getChildren().addAll(boardGrid, getOverlay());
    boardGrid.toBack();
    if (getOverlay() != null) getOverlay().toFront();
    root.setLeft(boardWithOverlay);

    // 🧍‍♂️ Current player (top)
    VBox currentPlayerBox = new VBox(5);
    currentPlayerBox.setAlignment(Pos.CENTER);
    currentPlayerLabel = new Label("Current turn:");
    playerImage = new ImageView();
    playerImage.setFitWidth(70);
    playerImage.setFitHeight(70);
    playerImage.setPreserveRatio(true);
    currentPlayerBox.getChildren().addAll(currentPlayerLabel, playerImage);

    // 📊 All players info (middle)
    playerInfoList = new VBox(10);
    playerInfoList.setAlignment(Pos.CENTER_LEFT);

    // 🎲 Roll button (bottom)
    VBox bottomBox = new VBox(10);
    positionLabel = new Label("Position:");
    diceResultLabel = new Label("Roll result:");
    rollButton = new Button("Roll Dice");
    rollButton.setOnAction(e -> handleRoll());
    bottomBox.setAlignment(Pos.CENTER);
    bottomBox.getChildren().addAll(positionLabel, diceResultLabel, rollButton);

    // 📦 Assemble info panel (right side)
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

      RowConstraints rowConst = new RowConstraints(TILE_SIZE);
      rowConst.setValignment(VPos.CENTER);
      boardGrid.getRowConstraints().add(rowConst);
    }

    renderBoardGrid();
  }

  protected abstract Image getCurrentPlayerImage();

  protected void renderBoardGrid() {
    boardGrid.getChildren().clear();

    for (int i = 0; i < 100; i++) {
      int tileNum = i + 1;
      StackPane cell = createTile(tileNum);

      int row = 9 - (i / BOARD_SIZE);
      int col = (row % 2 == 0) ? i % BOARD_SIZE : (BOARD_SIZE - 1 - i % BOARD_SIZE);

      boardGrid.add(cell, col, row);
    }
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

        Image image = new Image(url.toExternalForm(), TILE_SIZE * 0.5, TILE_SIZE * 0.5, true, true);
        ImageView icon = new ImageView(image);
        icon.setTranslateY(TILE_SIZE * 0.15 * playersOnTile.indexOf(player));
        cell.getChildren().add(icon);
      } catch (Exception e) {
        logger.error("Error loading image for character: {}", characterName, e);
      }
    }

    return cell;
  }

  protected double[] getTileCenter(int tileNum) {
    int i = tileNum - 1;
    int row = 9 - (i / BOARD_SIZE);
    int col = (row % 2 == 0) ? i % BOARD_SIZE : (BOARD_SIZE - 1 - i % BOARD_SIZE);

    double x = col * TILE_SIZE + TILE_SIZE / 2.0;
    double y = row * TILE_SIZE + TILE_SIZE / 2.0;

    return new double[]{x, y};
  }

  protected abstract void handleRoll();
  protected abstract String getTileColor(int tileNumber);
  protected abstract List<Player> getPlayersAtPosition(int tileNumber);
  protected abstract Pane getOverlay();
  protected abstract List<Player> getAllPlayers();  // NEW abstract method
  protected void initializeOverlay() {}

  public Parent getRoot() {
    return root;
  }
}