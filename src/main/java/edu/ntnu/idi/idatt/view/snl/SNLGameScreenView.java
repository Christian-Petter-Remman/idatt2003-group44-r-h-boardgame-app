package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.Ladder;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.Snake;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SNLGameScreenView extends GameScreen implements GameScreenObserver {

  private final SNLGameScreenController controller;
  private final Map<Player, Node> playerTokens = new HashMap<>();
  private StackPane overlayPane;
  private Pane tokenLayer;
  private Canvas staticCanvas;

  public SNLGameScreenView(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this);
    createUI();
    renderStaticElements();
    Platform.runLater(this::initializePlayerTokens);
    setBackListener(() -> NavigationManager.getInstance().navigateToStartScreen());
    setSaveListener(() -> {
      File temp = controller.getCsvFile();
      TextInputDialog d = new TextInputDialog("save_" + System.currentTimeMillis());
      d.setTitle("Save Game");
      d.setHeaderText("Name your save file:");
      d.setContentText("Filename:");
      d.showAndWait().ifPresent(n -> controller.saveGame(temp, n + ".csv"));
    });
  }

  @Override
  protected void initializeOverlay() {
    overlayPane = new StackPane();
    staticCanvas = new Canvas(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);
    tokenLayer = new Pane();
    tokenLayer.setMouseTransparent(true);
    overlayPane.getChildren().addAll(staticCanvas, tokenLayer);
  }

  @Override
  protected Pane getOverlay() {
    return overlayPane;
  }

  @Override
  protected void renderBoardGrid() {
    super.renderBoardGrid();
    Platform.runLater(this::renderStaticElements);
  }

  private void renderStaticElements() {
    GraphicsContext gc = staticCanvas.getGraphicsContext2D();
    gc.clearRect(0, 0, staticCanvas.getWidth(), staticCanvas.getHeight());
    SNLBoard b = (SNLBoard) controller.getBoard();
    for (Ladder l : b.getLadders()) drawLadder(gc, l.getStart(), l.getEnd());
    for (Snake s : b.getSnakes()) drawSnake(gc, s.getStart(), s.getEnd());
  }

  private void drawLadder(GraphicsContext gc, int s, int e) {
    StackPane ts = findTileNode(s), te = findTileNode(e);
    if (ts != null && te != null) {
      Bounds sb = staticCanvas.sceneToLocal(ts.localToScene(ts.getBoundsInLocal()));
      Bounds eb = staticCanvas.sceneToLocal(te.localToScene(te.getBoundsInLocal()));
      gc.setStroke(Color.BURLYWOOD);
      gc.setLineWidth(3);
      gc.strokeLine(sb.getCenterX() - 5, sb.getCenterY(), eb.getCenterX() - 5, eb.getCenterY());
      gc.strokeLine(sb.getCenterX() + 5, sb.getCenterY(), eb.getCenterX() + 5, eb.getCenterY());
      for (int i = 1; i < 5; i++) {
        double t = i / 5.0;
        double x1 = (1 - t) * (sb.getCenterX() - 5) + t * (eb.getCenterX() - 5);
        double y1 = (1 - t) * sb.getCenterY() + t * eb.getCenterY();
        double x2 = (1 - t) * (sb.getCenterX() + 5) + t * (eb.getCenterX() + 5);
        double y2 = (1 - t) * sb.getCenterY() + t * eb.getCenterY();
        gc.setStroke(Color.SADDLEBROWN);
        gc.setLineWidth(2);
        gc.strokeLine(x1, y1, x2, y2);
      }
    }
  }

  private void drawSnake(GraphicsContext gc, int s, int e) {
    StackPane ts = findTileNode(s), te = findTileNode(e);
    if (ts != null && te != null) {
      Bounds sb = staticCanvas.sceneToLocal(ts.localToScene(ts.getBoundsInLocal()));
      Bounds eb = staticCanvas.sceneToLocal(te.localToScene(te.getBoundsInLocal()));
      gc.setStroke(Color.DARKRED);
      gc.setLineWidth(4);
      gc.beginPath();
      gc.moveTo(sb.getCenterX(), sb.getCenterY());
      gc.bezierCurveTo(sb.getCenterX(), eb.getCenterY(), eb.getCenterX(), sb.getCenterY(), eb.getCenterX(), eb.getCenterY());
      gc.stroke();
      gc.setFill(Color.DARKRED);
      gc.fillOval(sb.getCenterX() - 3, sb.getCenterY() - 3, 6, 6);
    }
  }

  private void initializePlayerTokens() {
    for (Player p : controller.getPlayers()) {
      Image img = getImageForPlayer(p);
      if (img != null) {
        ImageView iv = new ImageView(img);
        iv.setFitWidth(30);
        iv.setFitHeight(30);
        Group g = new Group(iv);
        playerTokens.put(p, g);
        tokenLayer.getChildren().add(g);
        moveToken(g, p.getPosition());
      }
    }
  }

  private Image getImageForPlayer(Player p) {
    if (p.getCharacter() != null) {
      URL u = getClass().getResource("/player_icons/" + p.getCharacter().toLowerCase() + ".png");
      if (u != null) return new Image(u.toExternalForm());
    }
    return null;
  }

  private double[] tokenCoords(int pos, Node n) {
    StackPane t = findTileNode(pos);
    if (t == null) return null;
    n.applyCss();
    Bounds tb = t.localToScene(t.getBoundsInLocal());
    Bounds bb = boardGrid.localToScene(boardGrid.getBoundsInLocal());
    Bounds nb = n.getBoundsInLocal();
    double x = tb.getMinX() - bb.getMinX() + (tb.getWidth() - nb.getWidth()) / 2;
    double y = tb.getMinY() - bb.getMinY() + (tb.getHeight() - nb.getHeight()) / 2;
    return new double[]{x, y};
  }

  private void moveToken(Node n, int pos) {
    double[] c = tokenCoords(pos, n);
    if (c != null) {
      n.setLayoutX(c[0]);
      n.setLayoutY(c[1]);
      overlayPane.applyCss();
      overlayPane.requestLayout();
    }
  }

  private StackPane findTileNode(int tileNum) {
    int i = tileNum - 1;
    int row = BOARD_SIZE - 1 - (i / BOARD_SIZE);
    int col = (row % 2 == 0) ? i % BOARD_SIZE : (BOARD_SIZE - 1 - i % BOARD_SIZE);
    for (Node node : boardGrid.getChildren()) {
      if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row && node instanceof StackPane) {
        return (StackPane) node;
      }
    }
    return null;
  }

  @Override
  protected StackPane createTile(int tileNum) {
    StackPane cell = super.createTile(tileNum);
    cell.getChildren().removeIf(n -> n instanceof ImageView);
    return cell;
  }

  @Override
  public void onPlayerPositionChanged(Player p, int o, int n) {
    Platform.runLater(() -> {
      Node nn = playerTokens.get(p);
      if (nn == null) return;
      double[] np = tokenCoords(n, nn);
      if (np == null) return;
      double nx = np[0], ny = np[1];
      double ox = nn.getLayoutX(), oy = nn.getLayoutY();
      double dx = nx - ox, dy = ny - oy;
      nn.setOpacity(0);
      FadeTransition f = new FadeTransition(Duration.millis(300), nn);
      f.setFromValue(0);
      f.setToValue(1);
      TranslateTransition t = new TranslateTransition(Duration.millis(300), nn);
      t.setByX(dx);
      t.setByY(dy);
      ParallelTransition pt = new ParallelTransition(t, f);
      pt.setOnFinished(e -> {
        nn.setTranslateX(0);
        nn.setTranslateY(0);
        nn.setLayoutX(nx);
        nn.setLayoutY(ny);
        overlayPane.applyCss();
        overlayPane.requestLayout();
      });
      pt.play();
    });
  }

  @Override public void onDiceRolled(int r) { diceResultLabel.setText("Roll result: " + r); }
  @Override public void onPlayerTurnChanged(Player c) {
    currentPlayerLabel.setText("Current turn: " + c.getName());
    positionLabel.setText("Position: " + c.getPosition());
  }
  @Override public void onGameOver(Player w) {}
  @Override public void onGameSaved(String f) {}
  @Override public Parent getRoot() { return root; }

  @Override protected Image getCurrentPlayerImage() { return getImageForPlayer(controller.getCurrentPlayer()); }
  @Override protected List<Player> getAllPlayers() { return controller.getPlayers(); }
  @Override protected void handleRoll() { controller.handleRoll(); }
  @Override protected String getTileColor(int n) { return controller.getTileColor(n); }
  @Override protected List<Player> getPlayersAtPosition(int n) { return controller.getPlayersAtPosition(n); }
}
