package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.Ladder;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.SNLPlayer;
import edu.ntnu.idi.idatt.model.snl.Snake;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.GameScreen;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.WinnerDialogs;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SNLGameScreenView extends GameScreen implements GameScreenObserver {

  private static final Logger logger = LoggerFactory.getLogger(SNLGameScreenView.class);
  private final SNLGameScreenController controller;
  private final Map<Player, Node> playerTokens = new HashMap<>();
  private StackPane overlayPane;
  private Pane tokenLayer;
  private Canvas staticCanvas;

  /**
   * <h1>SNLGameScreenView</h1>
   *
   * View class for the Snakes and Ladders game. Extends the common {@link GameScreen} class and implements the
   * {@link GameScreenObserver} interface to reflect game state changes. This class handles rendering of ladders,
   * snakes, player movement animations, and game instructions.
   */

  public SNLGameScreenView(SNLGameScreenController controller) {
    this.controller = controller;
    File tempFile = controller.getCsvFile();
    controller.registerObserver(this);
    initializeOverlay();
    createUI();

    URL css = getClass().getResource("/css/styles.css");
    if (css != null) {
      root.getStylesheets().add(css.toExternalForm());
    } else {
      logger.warn("Could not find styles.css");
    }

    boardGrid.setId("boardGrid");
    diceResultLabel.setId("diceResultLabel");
    currentPlayerLabel.setId("currentPlayerLabel");
    positionLabel.setId("positionLabel");

    BorderPane bp = root;
    VBox instructionBox = new VBox(10);
    instructionBox.setId("instruction-box");
    instructionBox.setAlignment(Pos.TOP_LEFT);
    instructionBox.setPrefWidth(200);
    Label title = new Label("INSTRUCTIONS");
    title.setFont(Font.font("Courier New", 18));
    Label instr = new Label(
        "• Roll the dice\n" +
            "• Move that many steps\n" +
            "• Climb ladders\n" +
            "• Slide down snakes\n" +
            "• First to tile " + (BOARD_SIZE * BOARD_SIZE) + " wins"
    );
    instr.setWrapText(true);
    instructionBox.getChildren().addAll(title, instr);
    bp.setLeft(instructionBox);

    StackPane center = new StackPane(boardGrid, overlayPane);
    center.setAlignment(Pos.CENTER);
    bp.setCenter(center);

    renderBoardGrid();
    Platform.runLater(this::initializePlayerTokens);

    setBackListener(() -> NavigationManager.getInstance().navigateToStartScreen());
    setSaveListener(() -> showSaveGamePopup());
  }

  private void showSaveGamePopup() {
    javafx.stage.Popup popup = new javafx.stage.Popup();

    VBox content = new VBox(10);
    content.setStyle("-fx-background-color: #e0f7fa; -fx-padding: 15; -fx-border-color: #00acc1; -fx-border-width: 2;");
    Label header = new Label("Save Game");
    header.setStyle("-fx-font-weight: bold; -fx-text-fill: #006064;");

    TextField filenameField = new TextField("snl_save_" + System.currentTimeMillis());
    filenameField.setPrefWidth(220);

    Label feedbackLabel = new Label();
    feedbackLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
    feedbackLabel.setVisible(false);

    Button confirm = new Button("Save");
    confirm.setStyle("-fx-cursor: hand;");
    confirm.setOnAction(e -> {
      String filename = filenameField.getText().trim();
      if (!filename.isEmpty()) {
        FileManager.writeSNLGameStateToCSV(
                controller.getCsvFile(),
                controller.getPlayers(),
                controller.getShortenBoardPath(controller.getBoardPath()),
                controller.getDiceCount(),
                controller.getCurrentPlayerIndex()
        );
        controller.saveGame(controller.getCsvFile(), filename + ".csv");
        feedbackLabel.setText("Game saved");
        feedbackLabel.setVisible(true);
        new Thread(() -> {
          try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
          javafx.application.Platform.runLater(popup::hide);
        }).start();
      }
    });

    content.getChildren().addAll(header, filenameField, confirm, feedbackLabel);
    popup.getContent().add(content);
    popup.setAutoHide(true);
    popup.setHideOnEscape(true);

    javafx.stage.Window window = root.getScene().getWindow();
    popup.show(window, window.getX() + window.getWidth() / 2 - 150, window.getY() + window.getHeight() / 2 - 70);
  }

  @Override
  protected void initializeOverlay() {
    overlayPane = new StackPane();
    staticCanvas = new Canvas(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);
    tokenLayer = new Pane();
    tokenLayer.setMouseTransparent(true);
    overlayPane.getChildren().setAll(staticCanvas, tokenLayer);
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
    SNLBoard board = (SNLBoard) controller.getBoard();
    for (Ladder lad : board.getLadders()) {
      drawLadder(gc, lad.getStart(), lad.getEnd());
    }
    for (Snake sn : board.getSnakes()) {
      drawSnake(gc, sn.getStart(), sn.getEnd());
    }
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

      double x1 = sb.getCenterX();
      double y1 = sb.getCenterY();
      double x2 = eb.getCenterX();
      double y2 = eb.getCenterY();

      gc.setStroke(Color.DARKRED);
      gc.setLineWidth(4);
      gc.beginPath();
      gc.moveTo(x1, y1);

      double dx = x2 - x1;
      double dy = y2 - y1;

      gc.bezierCurveTo(
          x1 + dx * 0.25, y1 + dy * 0.1,
          x1 + dx * 0.25, y1 + dy * 0.4,
          x1 + dx * 0.5, y1 + dy * 0.5
      );
      gc.bezierCurveTo(
          x1 + dx * 0.75, y1 + dy * 0.6,
          x1 + dx * 0.75, y1 + dy * 0.9,
          x2, y2
      );
      gc.stroke();

      gc.setFill(Color.DARKRED);
      gc.fillOval(x1 - 7, y1 - 7, 14, 14);
    }
  }


  @Override
  protected StackPane createTile(int tileNum) {
    StackPane cell = super.createTile(tileNum);
    cell.getChildren().removeIf(n -> n instanceof ImageView);
    cell.getStyleClass().add("tile");
    cell.getStyleClass().add(tileNum % 2 == 0 ? "tile-even" : "tile-odd");
    return cell;
  }


  private void initializePlayerTokens() {
    for (Player p : controller.getPlayers()) {
      Image img = getImageForPlayer(p);
      if (img != null) {
        ImageView iv = new ImageView(img);
        iv.setFitWidth(30);
        iv.setFitHeight(30);
        Group g = new Group(iv);
        g.getStyleClass().add("player-token");
        playerTokens.put(p, g);
        tokenLayer.getChildren().add(g);
        moveToken(g, p.getPosition());
      }
    }
  }

  private Image getImageForPlayer(Player p) {
    if (p.getCharacter() != null) {
      URL u = getClass().getResource("/player_icons/" + p.getCharacter().toLowerCase() + ".png");
      if (u != null) {
        return new Image(u.toExternalForm());
      }
    }
    return null;
  }

  private void moveToken(Node n, int pos) {
    StackPane t = findTileNode(pos);
    if (t == null) {
      return;
    }
    Bounds tb = t.localToScene(t.getBoundsInLocal());
    Bounds bb = boardGrid.localToScene(boardGrid.getBoundsInLocal());
    Bounds nb = n.getBoundsInLocal();
    double x = tb.getMinX() - bb.getMinX() + (tb.getWidth() - nb.getWidth()) / 2;
    double y = tb.getMinY() - bb.getMinY() + (tb.getHeight() - nb.getHeight()) / 2;
    n.setLayoutX(x);
    n.setLayoutY(y);
  }

  private StackPane findTileNode(int tileNum) {
    int i = tileNum - 1;
    int row = BOARD_SIZE - 1 - (i / BOARD_SIZE);
    int col = (row % 2 == 0) ? i % BOARD_SIZE : BOARD_SIZE - 1 - (i % BOARD_SIZE);
    for (Node node : boardGrid.getChildren()) {
      if (GridPane.getColumnIndex(node) == col
          && GridPane.getRowIndex(node) == row
          && node instanceof StackPane) {
        return (StackPane) node;
      }
    }
    return null;
  }

  @Override
  public void onPlayerPositionChanged(Player p, int oldPos, int newPos) {
    Platform.runLater(() -> {
      Node token = playerTokens.get(p);
      if (token == null) {
        return;
      }

      double oldX = token.getLayoutX();
      double oldY = token.getLayoutY();

      moveToken(token, newPos);

      double newX = token.getLayoutX();
      double newY = token.getLayoutY();

      double dx = oldX - newX;
      double dy = oldY - newY;

      token.setTranslateX(dx);
      token.setTranslateY(dy);
      token.setOpacity(0);

      FadeTransition fade = new FadeTransition(Duration.millis(300), token);
      fade.setFromValue(0);
      fade.setToValue(1);

      TranslateTransition slide = new TranslateTransition(Duration.millis(300), token);
      slide.setToX(0);
      slide.setToY(0);

      ParallelTransition anim = new ParallelTransition(fade, slide);
      anim.setOnFinished(e -> {
        token.setTranslateX(0);
        token.setTranslateY(0);
      });
      anim.play();
    });
  }

  @Override
  public void onGameOver(Player winner) {
    if (winner instanceof SNLPlayer SNLPlayer) {
      Platform.runLater(() -> showWinner(SNLPlayer));
      File gameFile = controller.getCsvFile();
      controller.deleteGame(gameFile);
    }
  }

  private void showWinner(SNLPlayer winner) {
    WinnerDialogs dialogs = new WinnerDialogs();
    dialogs.showWinnerDialog(winner);
  }


  @Override
  public void onDiceRolled(int r) {
    diceResultLabel.setText("Roll result: " + r);
  }

  @Override
  public void onPlayerTurnChanged(Player c) {
    currentPlayerLabel.setText("Current turn: " + c.getName());
    positionLabel.setText("Position: " + c.getPosition());
  }

  @Override
  public void onGameSaved(String f) {
  }

  @Override
  protected Image getCurrentPlayerImage() {
    return getImageForPlayer(controller.getCurrentPlayer());
  }

  @Override
  protected List<Player> getAllPlayers() {
    return controller.getPlayers();
  }

  @Override
  protected void handleRoll() {
    controller.handleRoll();
  }

  @Override
  protected String getTileColor(int n) {
    return controller.getTileColor(n);
  }

  @Override
  protected List<Player> getPlayersAtPosition(int n) {
    return controller.getPlayersAtPosition(n);
  }
}
