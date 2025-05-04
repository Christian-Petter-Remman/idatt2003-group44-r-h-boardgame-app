package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;

public class SNLGameScreenView extends GameScreen {
  private final SNLGameScreenController controller;
  private final Pane overlay = new Pane();

  public SNLGameScreenView(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this);
    createUI();
    overlay.prefWidthProperty().bind(boardGrid.widthProperty());
    overlay.prefHeightProperty().bind(boardGrid.heightProperty());
    boardWithOverlay.getChildren().add(overlay);
    boardGrid.widthProperty().addListener((o,old,n) -> Platform.runLater(this::initializeOverlay));
    boardGrid.heightProperty().addListener((o,old,n) -> Platform.runLater(this::initializeOverlay));
    Platform.runLater(this::initializeOverlay);
  }

  protected Pane getOverlay() {
    return overlay;
  }

  @Override
  public void onPlayerPositionChanged(Player player, int oldPos, int newPos) {
    Platform.runLater(() -> {
      renderBoardGrid();
      boardGrid.applyCss();
      boardGrid.layout();
      initializeOverlay();
    });
  }

  protected void initializeOverlay() {
    overlay.getChildren().clear();
    SNLBoard b = (SNLBoard) controller.getBoard();
    b.getLadders().forEach(l -> drawLadder(l.getStart(), l.getEnd()));
    b.getSnakes().forEach(s -> drawSnake(s.getStart(), s.getEnd()));
  }

  @Override protected List<Player> getAllPlayers()            { return controller.getPlayers(); }
  @Override protected List<Player> getPlayersAtPosition(int t) { return controller.getPlayersAtPosition(t); }
  @Override protected String        getTileColor(int t)        { return controller.getTileColor(t); }
  @Override protected void          handleRoll()               { controller.handleRoll(); }
  @Override protected Image         getPlayerImage(Player p) {
    String name = p.getCharacter() != null
        ? p.getCharacter().toLowerCase()
        : "default";
    URL url = getClass().getResource("/player_icons/" + name + ".png");
    return url == null
        ? null
        : new Image(
            url.toExternalForm(),
            TILE_SIZE * 0.5,
            TILE_SIZE * 0.5,
            true, true
        );
  }

  private void drawLadder(int start, int end) {
    Point2D s = getCellCenter(start), e = getCellCenter(end);
    double dx = e.getX()-s.getX(), dy = e.getY()-s.getY(), dist = Math.hypot(dx,dy);
    double perpX = -dy/dist, perpY = dx/dist, w = Math.min(10,dist/20), ox = perpX*w, oy = perpY*w;
    Line l1 = new Line(s.getX()+ox, s.getY()+oy, e.getX()+ox, e.getY()+oy);
    Line l2 = new Line(s.getX()-ox, s.getY()-oy, e.getX()-ox, e.getY()-oy);
    l1.setStrokeWidth(3); l2.setStrokeWidth(3);
    l1.setStroke(Color.BURLYWOOD); l2.setStroke(Color.BURLYWOOD);
    overlay.getChildren().addAll(l1,l2);
    int steps = (int)(dist/15);
    for (int i = 1; i < steps; i++) {
      double t = i/(double)steps, cx = s.getX()+dx*t, cy = s.getY()+dy*t;
      Line rung = new Line(cx-ox, cy-oy, cx+ox, cy+oy);
      rung.setStrokeWidth(2); rung.setStroke(Color.SADDLEBROWN);
      overlay.getChildren().add(rung);
    }
  }

  private void drawSnake(int start, int end) {
    Point2D s = getCellCenter(start), e = getCellCenter(end);
    double dx=e.getX()-s.getX(), dy=e.getY()-s.getY(), dist=Math.hypot(dx,dy);
    double amp=Math.min(dist*0.2,25), perpX=-dy/dist, perpY=dx/dist;
    double c1x=s.getX()+dx*0.3+perpX*amp, c1y=s.getY()+dy*0.3+perpY*amp;
    double c2x=s.getX()+dx*0.7-perpX*amp, c2y=s.getY()+dy*0.7-perpY*amp;
    CubicCurve curve = new CubicCurve(
        s.getX(), s.getY(),
        c1x,     c1y,
        c2x,     c2y,
        e.getX(), e.getY()
    );
    curve.setStrokeWidth(4); curve.setStroke(Color.DARKRED); curve.setFill(null);
    Circle head = new Circle(s.getX(), s.getY(), 6, Color.DARKRED);
    Circle tail = new Circle(e.getX(), e.getY(), 4, Color.DARKRED);
    overlay.getChildren().addAll(curve, head, tail);
  }
}
