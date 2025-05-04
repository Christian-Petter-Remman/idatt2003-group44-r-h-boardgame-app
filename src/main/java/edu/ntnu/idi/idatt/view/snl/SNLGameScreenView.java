package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.List;

public class SNLGameScreenView extends GameScreen {

  private final SNLGameScreenController controller;
  private final Pane overlayPane = new Pane();
  private Label diceResult;
  private Button rollButton;
  private Button saveButton;

  public SNLGameScreenView(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this);

    root = new BorderPane();

    // --- CENTER: board + overlay ---
    boardGrid = new GridPane();
    boardGrid.setHgap(2);
    boardGrid.setVgap(2);
    boardGrid.setAlignment(Pos.CENTER);
    boardGrid.setPrefSize(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);
    for (int i = 0; i < BOARD_SIZE; i++) {
      ColumnConstraints cc = new ColumnConstraints(TILE_SIZE);
      cc.setHalignment(HPos.CENTER);
      boardGrid.getColumnConstraints().add(cc);
      RowConstraints rc = new RowConstraints(TILE_SIZE);
      rc.setValignment(VPos.CENTER);
      boardGrid.getRowConstraints().add(rc);
    }
    renderBoardGrid();

    overlayPane.setPickOnBounds(false);
    overlayPane.setMouseTransparent(true);
    overlayPane.prefWidthProperty().bind(boardGrid.widthProperty());
    overlayPane.prefHeightProperty().bind(boardGrid.heightProperty());
    boardWithOverlay = new StackPane(boardGrid, overlayPane);
    StackPane.setAlignment(boardGrid, Pos.TOP_LEFT);
    StackPane.setAlignment(overlayPane, Pos.TOP_LEFT);
    root.setCenter(boardWithOverlay);

    // --- LEFT PANEL ---
    VBox left = new VBox(10);
    left.getStyleClass().add("snl-left-panel");
    left.setPrefWidth(220);
    left.setAlignment(Pos.TOP_CENTER);

    Label turnLabel = new Label("Current Turn");
    turnLabel.getStyleClass().add("title");
    Label currentPlayer = new Label();
    currentPlayer.getStyleClass().add("info");
    controller.getCurrentPlayerImage();
    currentPlayer.setText(controller.getCurrentPlayer().getName());
    playerImage = new ImageView(controller.getCurrentPlayerImage());
    playerImage.setFitWidth(50);
    playerImage.setFitHeight(50);

    left.getChildren().addAll(turnLabel, playerImage, currentPlayer);

    for (Player p : controller.getPlayers()) {
      HBox row = new HBox(5);
      row.getStyleClass().add("player-info-row");
      ImageView iv = new ImageView(getPlayerImage(p));
      iv.setFitWidth(30);
      iv.setFitHeight(30);
      Label name = new Label(p.getName());
      Label pos = new Label("Tile: " + p.getPosition());
      Label last = new Label("Last roll: -");
      last.getStyleClass().add("dice-label");
      row.getChildren().addAll(iv, name, pos, last);
      left.getChildren().add(row);
    }

    rollButton = new Button("Roll Dice");
    rollButton.getStyleClass().add("button");
    rollButton.setOnAction(e -> controller.handleRoll());
    diceResult = new Label("Last roll: -");
    diceResult.getStyleClass().add("dice-label");

    Button quit = new Button("Quit to Menu");
    quit.getStyleClass().add("button");
    quit.setOnAction(e -> controller.navigateTo("INTRO_SCREEN"));

    left.getChildren().addAll(rollButton, diceResult, quit);
    root.setLeft(left);

    // --- RIGHT PANEL ---
    VBox right = new VBox(10);
    right.getStyleClass().add("snl-right-panel");
    right.setPrefWidth(200);
    right.setAlignment(Pos.TOP_CENTER);

    Label instr = new Label(
        "How to play:\n- Roll the dice\n- Climb ladders\n- Avoid snakes\n- First to 100 wins");
    instr.getStyleClass().add("instructions-box");

    saveButton = new Button("Save Game");
    saveButton.getStyleClass().add("button");
    saveButton.setOnAction(e -> {
      // implement save logic in controller
      // e.g., controller.saveGame();
    });

    right.getChildren().addAll(instr, saveButton);
    root.setRight(right);

    // --- STYLE SHEET ---
    root.getStylesheets().add(getClass()
        .getResource("/css/SNLGameScreenStyleSheet.css.css")
        .toExternalForm());

    // --- LAYOUT LISTENERS ---
    boardGrid.widthProperty()
        .addListener((o, old, n) -> Platform.runLater(this::initializeOverlay));
    boardGrid.heightProperty()
        .addListener((o, old, n) -> Platform.runLater(this::initializeOverlay));

    Platform.runLater(this::initializeOverlay);
  }

  @Override
  protected Image getPlayerImage(Player p) {
    if (p.getCharacter() == null) {
      return null;
    }
    String c = p.getCharacter().toLowerCase();
    URL url = getClass().getResource("/player_icons/" + c + ".png");
    return url == null ? null : new Image(url.toExternalForm());
  }

  @Override
  public void onPlayerPositionChanged(Player player, int oldPos, int newPos) {
    Platform.runLater(() -> {
      renderBoardGrid();
      boardGrid.applyCss();
      boardGrid.layout();
      initializeOverlay();
      // update right-hand last-roll labels
     // diceResult.setText("Last roll: " + controller.getLastRoll());
    });
  }

  @Override
  public void onDiceRolled(int result) {
    diceResult.setText("Last roll: " + result);
  }

  protected void initializeOverlay() {
    overlayPane.getChildren().clear();
    SNLBoard b = (SNLBoard) controller.getBoard();
    for (var l : b.getLadders()) {
      drawLadder(l.getStart(), l.getEnd());
    }
    for (var s : b.getSnakes()) {
      drawSnake(s.getStart(), s.getEnd());
    }
  }

  private void drawLadder(int start, int end) {
    Point2D s = getCellCenter(start), e = getCellCenter(end);
    double dx = e.getX() - s.getX(), dy = e.getY() - s.getY(), dist = Math.hypot(dx, dy);
    double perpX = -dy / dist, perpY = dx / dist, w = Math.min(10, dist / 20), ox = perpX * w, oy =
        perpY * w;
    Line l1 = new Line(s.getX() + ox, s.getY() + oy, e.getX() + ox, e.getY() + oy);
    Line l2 = new Line(s.getX() - ox, s.getY() - oy, e.getX() - ox, e.getY() - oy);
    l1.setStrokeWidth(3);
    l2.setStrokeWidth(3);
    l1.setStroke(Color.BURLYWOOD);
    l2.setStroke(Color.BURLYWOOD);
    overlayPane.getChildren().addAll(l1, l2);
    int steps = (int) (dist / 15);
    for (int i = 1; i < steps; i++) {
      double t = i / (double) steps, cx = s.getX() + dx * t, cy = s.getY() + dy * t;
      Line rung = new Line(cx - ox, cy - oy, cx + ox, cy + oy);
      rung.setStrokeWidth(2);
      rung.setStroke(Color.SADDLEBROWN);
      overlayPane.getChildren().add(rung);
    }
  }

  private void drawSnake(int start, int end) {
    Point2D s = getCellCenter(start), e = getCellCenter(end);
    double dx = e.getX() - s.getX(), dy = e.getY() - s.getY(), dist = Math.hypot(dx, dy);
    double amp = Math.min(dist * 0.2, 25), perpX = -dy / dist, perpY = dx / dist;
    double c1x = s.getX() + dx * 0.3 + perpX * amp, c1y = s.getY() + dy * 0.3 + perpY * amp;
    double c2x = s.getX() + dx * 0.7 - perpX * amp, c2y = s.getY() + dy * 0.7 - perpY * amp;
    CubicCurve curve = new CubicCurve(
        s.getX(), s.getY(), c1x, c1y, c2x, c2y, e.getX(), e.getY()
    );
    curve.setStrokeWidth(4);
    curve.setStroke(Color.DARKRED);
    curve.setFill(null);
    Circle head = new Circle(s.getX(), s.getY(), 6, Color.DARKRED);
    Circle tail = new Circle(e.getX(), e.getY(), 4, Color.DARKRED);
    overlayPane.getChildren().addAll(curve, head, tail);
  }

  @Override
  protected String getTileColor(int t) {
    return controller.getTileColor(t);
  }

  @Override
  protected List<Player> getPlayersAtPosition(int t) {
    return controller.getPlayersAtPosition(t);
  }

  @Override
  protected void handleRoll() {
    controller.handleRoll();
  }

  @Override
  protected List<Player> getAllPlayers() {
    return controller.getPlayers();
  }
}
