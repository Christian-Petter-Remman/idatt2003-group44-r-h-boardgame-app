package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Ladder;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Snake;
import edu.ntnu.idi.idatt.model.common.Player;
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


import java.util.List;
public class BoardView extends StackPane {

  private final Board board;
  private final List<Player> players;
  private final GridPane boardGrid = new GridPane();
  private final Pane ladderSnakeOverlay = new Pane();
  private final int tileSize = 90;

  public BoardView(Board board, List<Player> players) {
    this.board = board;
    this.players = players;

    boardGrid.setHgap(2);
    boardGrid.setVgap(2);
    boardGrid.setAlignment(Pos.CENTER);
    boardGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    boardGrid.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

    ladderSnakeOverlay.setPickOnBounds(false);
    ladderSnakeOverlay.setMouseTransparent(true);

    getChildren().addAll(boardGrid, ladderSnakeOverlay);
    render();
  }

  public void render() {
    boardGrid.getChildren().clear();
    boardGrid.getColumnConstraints().clear();
    boardGrid.getRowConstraints().clear();
    ladderSnakeOverlay.getChildren().clear();

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

      StackPane cell = new StackPane();
      cell.setPrefSize(tileSize, tileSize);
      cell.setMinSize(tileSize, tileSize);
      cell.setMaxSize(tileSize, tileSize);

      String color = getTileColor(tileNum);
      cell.setStyle("-fx-border-color: black; -fx-background-color: " + color + ";");

      Text tileNumber = new Text(String.valueOf(tileNum));
      tileNumber.setStyle("-fx-fill: #ccc;");
      cell.getChildren().add(tileNumber);

      for (Player p : players) {
        if (p.getPosition() == tileNum) {
          String characterName = p.getCharacter();
          Image image = new Image("PlayerIcons/" + characterName + ".png", 40, 40, true, true);
          ImageView icon = new ImageView(image);
          cell.getChildren().add(icon);
        }
      }

      int row = 9 - i / 10;
      int col = (row % 2 == 0) ? i % 10 : 9 - (i % 10);
      boardGrid.add(cell, col, row);
    }


    for (Ladder ladder : board.getLadders()) {
      drawLadder(ladder.start(), ladder.end());
    }

    for (Snake snake : board.getSnakes()) {
      drawSnake(snake.start(), snake.end());
    }

    boardGrid.toBack();
    ladderSnakeOverlay.toFront();
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


  private double[] getTileCenter(int tileNumber) {
    int row = 9 - (tileNumber - 1) / 10;
    int col = (row % 2 == 0) ? (tileNumber - 1) % 10 : 9 - (tileNumber - 1) % 10;

    double gap = boardGrid.getHgap();
    double x = col * (tileSize + gap) + tileSize / 2.0;
    double y = row * (tileSize + gap) + tileSize / 2.0;

    return new double[]{x, y};
  }

  private int[] getSnakeStart() {
    List<Snake> snakes = board.getSnakes();
    int[] starts = new int[snakes.size()];

    for (int i = 0; i < snakes.size(); i++) {
      starts[i] = snakes.get(i).start();
    }
    return starts;
  }

  private int[] getSnakeEnd() {
    List<Snake> snakes = board.getSnakes();
    int[] ends = new int[snakes.size()];
    for (int i = 0; i < snakes.size(); i++) {
      ends[i] = snakes.get(i).end();
    }
    return ends;
  }

  private int[] getLadderStart() {
    List<Ladder> ladders = board.getLadders();
    int[] starts = new int[ladders.size()];
    for (int i = 0; i < ladders.size(); i++) {
      starts[i] = ladders.get(i).start();
    }
    return starts;
  }

  private int[] getLadderEnd() {
    List<Ladder> ladders = board.getLadders();
    int[] ends = new int[ladders.size()];
    for (int i = 0; i < ladders.size(); i++) {
      ends[i] = ladders.get(i).end();
    }
    return ends;
  }

  private boolean isIn(int tileNum, int[] positions) {
    for (int pos : positions) {
      if (tileNum == pos) return true;
    }
    return false;
  }

  private boolean isSnakeStart(int tileNum) {
    return isIn(tileNum, getSnakeStart());
  }

  private boolean isSnakeEnd(int tileNum) {
    return isIn(tileNum, getSnakeEnd());
  }

  private boolean isLadderStart(int tileNum) {
    return isIn(tileNum, getLadderStart());
  }

  private boolean isLadderEnd(int tileNum) {
    return isIn(tileNum, getLadderEnd());
  }

  private String getTileColor(int tileNum) {
    if (isSnakeStart(tileNum)) return "red";
    if (isSnakeEnd(tileNum)) return "pink";
    if (isLadderStart(tileNum)) return "darkgreen";
    if (isLadderEnd(tileNum)) return "lightgreen";
    return "white";
  }
}