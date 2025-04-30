package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLBoardController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.BoardObserver;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.view.AbstractView;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.List;

public class SNLBoardView extends AbstractView implements BoardObserver {

  private static final int TILE_SIZE = 90;
  private static final int BOARD_SIZE = 10;

  private SNLBoardController controller;
  private GridPane boardGrid;
  private Pane ladderSnakeOverlay;
  private StackPane root;

  public SNLBoardView() {}

  public void initializeWithController(SNLBoardController controller) {
    this.controller = controller;
    this.controller.registerObserver(this);
    initializeUI();
  }

  @Override
  protected void createUI() {
    root = new StackPane();
    boardGrid = new GridPane();
    ladderSnakeOverlay = new Pane();

    initializeBoardGrid();
    initializeOverlay();

    root.getChildren().addAll(boardGrid, ladderSnakeOverlay);
    boardGrid.toBack();
    ladderSnakeOverlay.toFront();

    renderBoardGrid();
    renderLaddersAndSnakes();
  }

  @Override
  protected void setupEventHandlers() {
    // No direct interaction needed
  }

  @Override
  protected void applyInitialUIState() {
    controller.render();
  }

  @Override
  public void onBoardRendered() {
    renderBoardGrid();
  }

  @Override
  public void onPlayerMoved(Player player, int fromPosition, int toPosition) {
    renderBoardGrid();
  }

  @Override
  public void onSpecialTileActivated(int tileNumber, int destination, boolean isLadder) {
    // Future enhancement: visually highlight ladder/snake used
  }

  private void initializeBoardGrid() {
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
  }

  private void initializeOverlay() {
    ladderSnakeOverlay.setPickOnBounds(false);
    ladderSnakeOverlay.setMouseTransparent(true);
    ladderSnakeOverlay.setPrefSize(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);
  }

  private void renderBoardGrid() {
    boardGrid.getChildren().clear();

    for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
      int tileNum = i + 1;
      StackPane cell = createTile(tileNum);

      int row = BOARD_SIZE - 1 - (i / BOARD_SIZE);
      int col = (row % 2 == 0) ? i % BOARD_SIZE : (BOARD_SIZE - 1) - (i % BOARD_SIZE);

      boardGrid.add(cell, col, row);
    }
  }

  private StackPane createTile(int tileNum) {
    StackPane cell = new StackPane();
    cell.setPrefSize(TILE_SIZE, TILE_SIZE);

    cell.setStyle("-fx-border-color: black; -fx-background-color: " + controller.getTileColor(tileNum) + ";");

    Text tileNumber = new Text(String.valueOf(tileNum));
    tileNumber.setStyle("-fx-fill: #555;");
    cell.getChildren().add(tileNumber);

    List<Player> playersOnTile = controller.getPlayersAtPosition(tileNum);
    for (Player player : playersOnTile) {
      String characterName = (player.getCharacter() != null) ? player.getCharacter() : "default";
      Image image = new Image("player_icons/" + characterName + ".png", 40, 40, true, true);
      ImageView icon = new ImageView(image);
      cell.getChildren().add(icon);
    }

    return cell;
  }

  private void renderLaddersAndSnakes() {
    ladderSnakeOverlay.getChildren().clear();

    SNLBoard board = (SNLBoard) controller.getBoard();

    board.getLadders().forEach(ladder -> drawLadder(ladder.getStart(), ladder.getEnd()));
    board.getSnakes().forEach(snake -> drawSnake(snake.getStart(), snake.getEnd()));
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

    Line left = new Line(startPos[0] + offsetX, startPos[1] + offsetY, endPos[0] + offsetX, endPos[1] + offsetY);
    Line right = new Line(startPos[0] - offsetX, startPos[1] - offsetY, endPos[0] - offsetX, endPos[1] - offsetY);

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

      Line rung = new Line(midX - offsetX, midY - offsetY, midX + offsetX, midY + offsetY);
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

    CubicCurve snake = new CubicCurve(startPos[0], startPos[1], ctrlX1, ctrlY1, ctrlX2, ctrlY2, endPos[0], endPos[1]);
    snake.setStroke(Color.DARKRED);
    snake.setStrokeWidth(4);
    snake.setFill(null);

    Circle head = new Circle(startPos[0], startPos[1], 6, Color.DARKRED);
    Circle tail = new Circle(endPos[0], endPos[1], 4, Color.DARKRED);

    ladderSnakeOverlay.getChildren().addAll(snake, head, tail);
  }

  private double[] getTileCenter(int tileNum) {
    int i = tileNum - 1;
    int row = BOARD_SIZE - 1 - (i / BOARD_SIZE);
    int col = (row % 2 == 0) ? i % BOARD_SIZE : (BOARD_SIZE - 1) - (i % BOARD_SIZE);

    double x = col * TILE_SIZE + TILE_SIZE / 2.0;
    double y = row * TILE_SIZE + TILE_SIZE / 2.0;

    return new double[]{x, y};
  }

  @Override
  public Parent getRoot() {
    return root;
  }
}
