package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.view.AbstractView;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.List;

public class SNLGameScreenView extends AbstractView {

  private SNLGameScreenController controller;
  private VBox root;
  private Label currentPlayerLabel;
  private Label positionLabel;
  private Label diceResultLabel;
  private Button rollButton;

  // Components for the board view
  private GridPane boardGrid;
  private Pane ladderSnakeOverlay;

  public void initializeWithController(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this::onPlayerPositionChanged);
    initializeUI();
  }

  @Override
  protected void createUI() {
    root = new VBox(20);
    root.setStyle("-fx-padding: 30; -fx-alignment: center;");

    // Game controls
    currentPlayerLabel = new Label("Current turn:");
    positionLabel = new Label("Position:");
    diceResultLabel = new Label("Roll result:");
    rollButton = new Button("Roll Dice");

    rollButton.setOnAction(e -> controller.handleRoll());

    root.getChildren().addAll(currentPlayerLabel, positionLabel, diceResultLabel, rollButton);

    // Board components
    StackPane mainContainer = new StackPane();
    initializeBoardGrid();
    initializeOverlay();

    mainContainer.getChildren().addAll(boardGrid, ladderSnakeOverlay);
    root.getChildren().add(mainContainer);

    boardGrid.toBack();
    ladderSnakeOverlay.toFront();
  }

  @Override
  protected void setupEventHandlers() {

  }

  @Override
  protected void applyInitialUIState() {

  }

  private void initializeBoardGrid() {
    boardGrid = new GridPane();
    boardGrid.setHgap(2);
    boardGrid.setVgap(2);
    boardGrid.setAlignment(Pos.CENTER);
    boardGrid.setPrefSize(90 * 10, 90 * 10);

    for (int i = 0; i < 10; i++) {
      ColumnConstraints colConst = new ColumnConstraints(90);
      colConst.setHalignment(HPos.CENTER);
      boardGrid.getColumnConstraints().add(colConst);

      RowConstraints rowConst = new RowConstraints(90);
      rowConst.setValignment(VPos.CENTER);
      boardGrid.getRowConstraints().add(rowConst);
    }

    renderBoardGrid();
  }

  private void initializeOverlay() {
    ladderSnakeOverlay = new Pane();
    ladderSnakeOverlay.setPickOnBounds(false);
    ladderSnakeOverlay.setMouseTransparent(true);
    ladderSnakeOverlay.setPrefSize(90 * 10, 90 * 10);
  }

  private void renderBoardGrid() {
    boardGrid.getChildren().clear();

    for (int i = 0; i < 100; i++) {
      int tileNum = i + 1;
      StackPane cell = createTile(tileNum);

      int row = 9 - (i / 10);
      int col = (row % 2 == 0) ? i % 10 : (9 - i % 10);

      boardGrid.add(cell, col, row);
    }
  }

  private StackPane createTile(int tileNum) {
    StackPane cell = new StackPane();
    cell.setPrefSize(90, 90);

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

    Circle head = new Circle(startPos[0], startPos[1], 6, Color.DARKRED);
    Circle tail = new Circle(endPos[0], endPos[1], 4, Color.DARKRED);

    ladderSnakeOverlay.getChildren().addAll(snake, head, tail);
  }

  private double[] getTileCenter(int tileNum) {
    int i = tileNum - 1;
    int row = 9 - (i / 10);
    int col = (row % 2 == 0) ? i % 10 : (9 - i % 10);

    double x = col * 90 + 90 / 2.0;
    double y = row * 90 + 90 / 2.0;

    return new double[]{x, y};
  }

  @Override
  public Parent getRoot() {
    return root;
  }
}