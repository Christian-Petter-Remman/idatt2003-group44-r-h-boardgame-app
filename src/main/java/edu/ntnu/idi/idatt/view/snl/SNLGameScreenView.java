package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;

import java.util.List;

public class SNLGameScreenView extends GameScreen {
  private final SNLGameScreenController controller;

  public SNLGameScreenView(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this);
    createUI();
  }

  @Override protected Image getCurrentPlayerImage() {
    return controller.getCurrentPlayerImage();
  }
  @Override protected void handleRoll() {
    controller.handleRoll();
  }
  @Override protected String getTileColor(int tileNumber) {
    return controller.getTileColor(tileNumber);
  }
  @Override protected List<Player> getPlayersAtPosition(int tileNumber) {
    return controller.getPlayersAtPosition(tileNumber);
  }

  @Override protected void initializeOverlay() {
    overlayPane.getChildren().clear();
    overlayPane.setPickOnBounds(false);
    overlayPane.setMouseTransparent(true);
    SNLBoard board = (SNLBoard) controller.getBoard();
    board.getLadders().forEach(l -> drawLadder(l.getStart(), l.getEnd()));
    board.getSnakes().forEach(s -> drawSnake(s.getStart(), s.getEnd()));
  }

  private void drawLadder(int start, int end) {
    var s = getCellCenter(start);
    var e = getCellCenter(end);
    double dx = e.getX()-s.getX(), dy = e.getY()-s.getY();
    double dist = Math.hypot(dx,dy);
    double perpX = -dy/dist, perpY = dx/dist;
    double w = Math.min(10, Math.max(5, dist/20));
    double ox = perpX*w, oy = perpY*w;
    Line l1 = new Line(s.getX()+ox, s.getY()+oy, e.getX()+ox, e.getY()+oy);
    Line l2 = new Line(s.getX()-ox, s.getY()-oy, e.getX()-ox, e.getY()-oy);
    l1.setStrokeWidth(3); l1.setStroke(Color.BURLYWOOD);
    l2.setStrokeWidth(3); l2.setStroke(Color.BURLYWOOD);
    overlayPane.getChildren().addAll(l1,l2);
    int steps = (int)(dist/15);
    for(int i=1;i<steps;i++){
      double t=i/(double)steps;
      double cx=s.getX()+dx*t, cy=s.getY()+dy*t;
      Line rung=new Line(cx-ox,cy-oy,cx+ox,cy+oy);
      rung.setStrokeWidth(2); rung.setStroke(Color.SADDLEBROWN);
      overlayPane.getChildren().add(rung);
    }
  }

  private void drawSnake(int start, int end) {
    var s = getCellCenter(start);
    var e = getCellCenter(end);
    double dx = e.getX()-s.getX(), dy = e.getY()-s.getY();
    double dist = Math.hypot(dx,dy);
    double amp = Math.min(dist*0.2,25);
    double perpX = -dy/dist, perpY = dx/dist;
    double c1x = s.getX()+dx*0.3+perpX*amp, c1y = s.getY()+dy*0.3+perpY*amp;
    double c2x = s.getX()+dx*0.7-perpX*amp, c2y = s.getY()+dy*0.7-perpY*amp;
    CubicCurve sc=new CubicCurve(s.getX(),s.getY(),c1x,c1y,c2x,c2y,e.getX(),e.getY());
    sc.setStrokeWidth(4); sc.setStroke(Color.DARKRED); sc.setFill(null);
    Circle head=new Circle(s.getX(),s.getY(),6,Color.DARKRED);
    Circle tail=new Circle(e.getX(),e.getY(),4,Color.DARKRED);
    overlayPane.getChildren().addAll(sc,head,tail);
  }
}
