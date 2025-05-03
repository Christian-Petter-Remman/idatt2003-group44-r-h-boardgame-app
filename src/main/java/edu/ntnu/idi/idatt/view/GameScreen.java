package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.model.common.Player;
import javafx.application.Platform;
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
  protected Label currentPlayerLabel;
  protected Label positionLabel;
  protected Label diceResultLabel;
  protected Button rollButton;
  protected abstract Image getCurrentPlayerImage();

  public GameScreen() {}

  protected void createUI() {
    root = new BorderPane();
    root.setStyle("-fx-padding: 30;");

    // Left: board
    initializeBoardGrid();
    Platform.runLater(this::initializeOverlay);

    StackPane boardWithOverlay = new StackPane();
    boardWithOverlay.getChildren().addAll(boardGrid, getOverlay());
    boardGrid.toBack();
    if (getOverlay() != null) getOverlay().toFront();

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
    if (image != null) {
      playerImage.setImage(image);
    }

    infoBox.getChildren().addAll( playerImage, currentPlayerLabel, positionLabel, diceResultLabel);
    root.setRight(infoBox);

    // Bottom: roll button
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
      ColumnConstraints colConst = new ColumnConstraints(TILE_SIZE);
      colConst.setHalignment(HPos.CENTER);
      boardGrid.getColumnConstraints().add(colConst);

      RowConstraints rowConst = new RowConstraints(TILE_SIZE);
      rowConst.setValignment(VPos.CENTER);
      boardGrid.getRowConstraints().add(rowConst);
    }

    renderBoardGrid();
  }

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
      String characterName = (player.getCharacter() != null) ? player.getCharacter().toLowerCase() : "default";
      try {
        var url = getClass().getResource("/player_icons/" + characterName + ".png");
        if (url == null) {
          logger.warn("Image not found for character: {}", characterName);
          continue;
        }

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
    int i   = tileNum - 1;
    int row = BOARD_SIZE - 1 - (i / BOARD_SIZE);
    int col = (row % 2 == 0)
        ? (i % BOARD_SIZE)
        : (BOARD_SIZE - 1 - (i % BOARD_SIZE));

    double hgap = boardGrid.getHgap();
    double vgap = boardGrid.getVgap();
    double x    = col * (TILE_SIZE + hgap) + TILE_SIZE / 2.0;
    double y    = row * (TILE_SIZE + vgap) + TILE_SIZE / 2.0;

    return new double[]{ x, y };
  }



  protected abstract void handleRoll();
  protected abstract String getTileColor(int tileNumber);
  protected abstract List<Player> getPlayersAtPosition(int tileNumber);
  protected abstract Pane getOverlay();
  protected void initializeOverlay() {}

  public Parent getRoot() {
    return root;
  }
}