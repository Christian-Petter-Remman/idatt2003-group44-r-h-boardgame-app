package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

import java.util.List;

public class SNLGameScreenView extends GameScreen implements GameScreenObserver {

  private final SNLGameScreenController controller;

  public SNLGameScreenView(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this);
    createUI();
    initializeOverlay();
  }

  @Override
  protected Image getCurrentPlayerImage() {
    return controller.getCurrentPlayerImage();
  }

  @Override
  protected void handleRoll() {
    controller.handleRoll();
  }

  @Override
  protected String getTileColor(int tileNumber) {
    return controller.getTileColor(tileNumber);
  }

  @Override
  protected List<Player> getPlayersAtPosition(int tileNumber) {
    return controller.getPlayersAtPosition(tileNumber);
  }

  @Override
  protected void initializeOverlay() {
    overlayPane.getChildren().clear();
    overlayPane.setPickOnBounds(false);
    overlayPane.setMouseTransparent(true);
    SNLBoard board = (SNLBoard) controller.getBoard();
    board.getLadders().forEach(l -> drawLadder(l.getStart(), l.getEnd()));
    board.getSnakes().forEach(s -> drawSnake(s.getStart(), s.getEnd()));
  }

  private Point2D getActualCenter(int tileNum) {
    int i = tileNum - 1;
    int row = BOARD_SIZE - 1 - (i / BOARD_SIZE);
    int col = (row % 2 == 0) ? (i % BOARD_SIZE) : (BOARD_SIZE - 1 - (i % BOARD_SIZE));
    for (Node cell : boardGrid.getChildren()) {
      Integer r = GridPane.getRowIndex(cell), c = GridPane.getColumnIndex(cell);
      if (r != null && c != null && r == row && c == col) {
        Bounds local = cell.getBoundsInLocal();
        Bounds scene = cell.localToScene(local);
        double cx = scene.getMinX() + scene.getWidth() / 2;
        double cy = scene.getMinY() + scene.getHeight() / 2;
        return overlayPane.sceneToLocal(cx, cy);
      }
    }
    return new Point2D(0, 0);
  }

  private void drawLadder(int start, int end) {
    Point2D s = getActualCenter(start), e = getActualCenter(end);
    double dx = e.getX() - s.getX(), dy = e.getY() - s.getY();
    double dist = Math.hypot(dx, dy);
    double perpX = -dy / dist, perpY = dx / dist;
    double w = Math.min(10, Math.max(5, dist / 20));
    double ox = perpX * w, oy = perpY * w;

    Line l1 = new Line(s.getX() + ox, s.getY() + oy, e.getX() + ox, e.getY() + oy);
    Line l2 = new Line(s.getX() - ox, s.getY() - oy, e.getX() - ox, e.getY() - oy);
    l1.setStrokeWidth(3);
    l2.setStrokeWidth(3);
    l1.setStroke(Color.BURLYWOOD);
    l2.setStroke(Color.BURLYWOOD);
    overlayPane.getChildren().addAll(l1, l2);

    int steps = (int) (dist / 15);
    for (int i = 1; i < steps; i++) {
      double t = (double) i / steps;
      double cx = s.getX() + dx * t, cy = s.getY() + dy * t;
      Line rung = new Line(cx - ox, cy - oy, cx + ox, cy + oy);
      rung.setStrokeWidth(2);
      rung.setStroke(Color.SADDLEBROWN);
      overlayPane.getChildren().add(rung);
    }
  }

  private void drawSnake(int start, int end) {
    Point2D s = getActualCenter(start), e = getActualCenter(end);
    double dx = e.getX() - s.getX(), dy = e.getY() - s.getY();
    double dist = Math.hypot(dx, dy);
    double amp = Math.min(dist * 0.2, 25);
    double perpX = -dy / dist, perpY = dx / dist;
    double c1x = s.getX() + dx * 0.3 + perpX * amp, c1y = s.getY() + dy * 0.3 + perpY * amp;
    double c2x = s.getX() + dx * 0.7 - perpX * amp, c2y = s.getY() + dy * 0.7 - perpY * amp;

    CubicCurve snake = new CubicCurve(s.getX(), s.getY(), c1x, c1y, c2x, c2y, e.getX(), e.getY());
    snake.setStrokeWidth(4);
    snake.setStroke(Color.DARKRED);
    snake.setFill(null);

    Circle head = new Circle(s.getX(), s.getY(), 6, Color.DARKRED);
    Circle tail = new Circle(e.getX(), e.getY(), 4, Color.DARKRED);
    overlayPane.getChildren().addAll(snake, head, tail);
  }

  private void showGameOverAlert(Player winner) {
    Alert winAlert = new Alert(AlertType.INFORMATION);

    winAlert.setTitle("Game Over");
    winAlert.setHeaderText("Winner!");
    winAlert.setContentText(winner.getName() + " has won the game.");
    winAlert.showAndWait();
  }

  private void showGameSavedAlert(String filePath) {
    Alert a = new Alert(AlertType.INFORMATION);
    a.setTitle("Game Saved");
    a.setHeaderText("Game Saved");
    a.setContentText("Saved to: " + filePath);
    a.showAndWait();
  }

  @Override
  public void onPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
    renderBoardGrid();
    boardGrid.applyCss();
    boardGrid.layout();
    initializeOverlay();
  }

  @Override
  public void onDiceRolled(int result) {
    diceResultLabel.setText("Roll result: " + result);
  }

  @Override
  public void onPlayerTurnChanged(Player currentPlayer) {
    currentPlayerLabel.setText("Current turn: " + currentPlayer.getName());
    positionLabel.setText("Position: " + currentPlayer.getPosition());
  }

  @Override
  public void onGameOver(Player winner) {
    showGameOverAlert(winner);
  }

  @Override
  public void onGameSaved(String filePath) {
    showGameSavedAlert(filePath);
  }

  @Override
  public Parent getRoot() {
    return root;
  }
}
