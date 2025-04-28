package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.controller.common.BoardController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.BoardObserver;
import edu.ntnu.idi.idatt.model.snakesladders.SNLBoard;
import edu.ntnu.idi.idatt.model.snakesladders.Ladder;
import edu.ntnu.idi.idatt.model.snakesladders.Snake;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BoardView extends StackPane implements BoardObserver {
  private static final Logger logger = LoggerFactory.getLogger(BoardView.class);

  private final BoardController controller;
  private final GridPane boardGrid = new GridPane();
  private final Pane ladderSnakeOverlay = new Pane();
  private final int tileSize = 90;

  public BoardView(SNLBoard board, List<Player> players) {
    this.controller = new BoardController(board, players);
    controller.registerObserver(this);

    initializeLayout();
    render();
  }

  private void initializeLayout() {
    boardGrid.setHgap(2);
    boardGrid.setVgap(2);
    boardGrid.setAlignment(Pos.CENTER);
    boardGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    boardGrid.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

    ladderSnakeOverlay.setPickOnBounds(false);
    ladderSnakeOverlay.setMouseTransparent(true);

    getChildren().addAll(boardGrid, ladderSnakeOverlay);
  }

  public void render() {
    controller.render();
  }

  private void renderBoardGrid() {
    boardGrid.getChildren().clear();
    boardGrid.getColumnConstraints().clear();
    boardGrid.getRowConstraints().clear();

    int boardSize = 10;

    boardGrid.setPrefSize(tileSize * boardSize, tileSize * boardSize);
    boardGrid.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    boardGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    ladderSnakeOverlay.setPrefSize(tileSize * boardSize, tileSize * boardSize);

    for (int i = 0; i < boardSize; i++) {
      ColumnConstraints colConst = new ColumnConstraints(tileSize);
      colConst.setHalignment(HPos.CENTER);
      boardGrid.getColumnConstraints().add(colConst);

      RowConstraints rowConst = new RowConstraints(tileSize);
      rowConst.setValignment(VPos.CENTER);
      boardGrid.getRowConstraints().add(rowConst);
    }


    for (int i = 0; i < 100; i++) {
      int tileNum = i + 1;

      StackPane cell = createTile(tileNum);

      int row = 9 - i / 10;
      int col = (row % 2 == 0) ? i % 10 : 9 - (i % 10);
      boardGrid.add(cell, col, row);
    }
  }


  private StackPane createTile(int tileNum) {
    StackPane cell = new StackPane();
    cell.setPrefSize(tileSize, tileSize);
    cell.setMinSize(tileSize, tileSize);
    cell.setMaxSize(tileSize, tileSize);

    String color = controller.getTileColor(tileNum);
    cell.setStyle("-fx-border-color: black; -fx-background-color: " + color + ";");

    Text tileNumber = new Text(String.valueOf(tileNum));
    tileNumber.setStyle("-fx-fill: #555;");
    cell.getChildren().add(tileNumber);

    List<Player> playersOnTile = controller.getPlayersAtPosition(tileNum);
    for (Player player : playersOnTile) {
      String characterName = player.getCharacter();
      if (characterName == null || characterName.isEmpty()) {
        characterName = "default";
      }
      Image image = new Image("PlayerIcons/" + characterName + ".png", 40, 40, true, true);
      ImageView icon = new ImageView(image);
      cell.getChildren().add(icon);
    }
    return cell;
  }

  private void renderLaddersAndSnakes() {
    ladderSnakeOverlay.getChildren().clear();

    for (Ladder ladder : controller.getLadders()) {
      drawLadder(ladder.start(), ladder.end());
    }
    for (Snake snake : controller.getSnakes()) {
      drawSnake(snake.start(), snake.end());
    }
  }

  private void drawLadder(int start, int end) {
    double[] startPos = getTileCenter(start);
    double[] endPos = getTileCenter(end);

    double dx = endPos[0] - startPos[0];
    double dy = endPos[1] - startPos[1];
    double distance = Math.sqrt(dx * dx + dy * dy);

    double dirX = dx / distance;
    double dirY = dy / distance;

    double perpX = -dirY;
    double perpY = dirX;

    double ladderWidth = Math.min(10, Math.max(5, distance / 20));

    double offsetX = perpX * ladderWidth;
    double offsetY = perpY * ladderWidth;

    Line left = new Line(
        startPos[0] + offsetX, startPos[1] + offsetY,
        endPos[0] + offsetX, endPos[1] + offsetY
    );
    Line right = new Line(
        startPos[0] - offsetX, startPos[1] - offsetY,
        endPos[0] - offsetX, endPos[1] - offsetY
    );

    left.setStroke(Color.BURLYWOOD);
    right.setStroke(Color.BURLYWOOD);
    left.setStrokeWidth(3);
    right.setStrokeWidth(3);

    ladderSnakeOverlay.getChildren().addAll(left, right);

    int steps = Math.max(3, Math.min(8, (int)(distance / 30)));

    for (int i = 1; i < steps; i++) {
      double ratio = i / (double) steps;
      double midX = startPos[0] + dx * ratio;
      double midY = startPos[1] + dy * ratio;

      Line rung = new Line(
          midX - offsetX, midY - offsetY,
          midX + offsetX, midY + offsetY
      );
      rung.setStroke(Color.BURLYWOOD);
      rung.setStrokeWidth(2);
      ladderSnakeOverlay.getChildren().add(rung);
    }
  }

  private void drawSnake(int start, int end) {
    double[] startPos = getTileCenter(start);
    double[] endPos = getTileCenter(end);

    double dx = endPos[0] - startPos[0];
    double dy = endPos[1] - startPos[1];
    double distance = Math.sqrt(dx * dx + dy * dy);

    double curveAmplitude = Math.min(distance * 0.2, 25);

    double perpX = -dy / distance;
    double perpY = dx / distance;

    double ctrlX1 = startPos[0] + dx * 0.3 + perpX * curveAmplitude;
    double ctrlY1 = startPos[1] + dy * 0.3 + perpY * curveAmplitude;
    double ctrlX2 = startPos[0] + dx * 0.7 - perpX * curveAmplitude;
    double ctrlY2 = startPos[1] + dy * 0.7 - perpY * curveAmplitude;

    CubicCurve snake = new CubicCurve(
        startPos[0], startPos[1],
        ctrlX1, ctrlY1,
        ctrlX2, ctrlY2,
        endPos[0], endPos[1]
    );
    snake.setStroke(Color.DARKRED);
    snake.setStrokeWidth(4);
    snake.setFill(null);

    ladderSnakeOverlay.getChildren().add(snake);

    Circle head = new Circle(startPos[0], startPos[1], 6, Color.DARKRED);
    Circle tail = new Circle(endPos[0], endPos[1], 4, Color.DARKRED);
    ladderSnakeOverlay.getChildren().addAll(head, tail);
  }

  private double[] getTileCenter(int tileNum) {
    int i = tileNum - 1;
    int row = 9 - i / 10;
    int col = (row % 2 == 0) ? i % 10 : 9 - (i % 10);

    double x = col * tileSize + tileSize / 2.0;
    double y = row * tileSize + tileSize / 2.0;

    return new double[]{x, y};
  }


  @Override
  public void onBoardRendered() {
    renderBoardGrid();
    renderLaddersAndSnakes();

    boardGrid.toBack();
    ladderSnakeOverlay.toFront();
  }

  @Override
  public void onPlayerMoved(Player player, int fromPosition, int toPosition) {
    renderBoardGrid();
  }

  @Override
  public void onSpecialTileActivated(int tileNumber, int destination, boolean isLadder) {
    logger.debug("Special tile activated: {} -> {}, isLadder: {}", tileNumber, destination, isLadder);
  }
}
