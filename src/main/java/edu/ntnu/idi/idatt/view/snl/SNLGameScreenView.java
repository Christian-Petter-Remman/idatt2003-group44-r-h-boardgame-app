package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.modelobservers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.Ladder;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.SNLPlayer;
import edu.ntnu.idi.idatt.model.snl.Snake;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.GameScreen;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.WinnerDialogs;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>SNLGameScreenView</h1>
 *
 * <p>The SNLGameScreenView class is responsible for rendering the Snakes and Ladders game UI.
 * It extends the common {@link GameScreen} class and implements {@link GameScreenObserver}
 * to respond to game state changes. This includes drawing the board, snakes, ladders,
 * animating player tokens, and handling user interactions such as rolling dice, saving,
 * and navigating.</p>
 *
 * <P>AI: active involvement as sparring partner and created underlying frame for view development.
 * </P>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>Initialize and layout UI components.</li>
 *   <li>Render static elements (snakes and ladders) on the board.</li>
 *   <li>Manage and animate player token movements.</li>
 *   <li>Display game instructions and current game status.</li>
 *   <li>Handle save game operations via popup dialogs.</li>
 *   <li>Notify controller of user actions (roll dice, navigate, save).</li>
 * </ul>
 */
public class SNLGameScreenView extends GameScreen implements GameScreenObserver {

  private static final Logger logger = LoggerFactory.getLogger(SNLGameScreenView.class);
  private final SNLGameScreenController controller;
  private final Map<Player, Node> playerTokens = new HashMap<>();
  private StackPane overlayPane;
  private Pane tokenLayer;
  private Canvas staticCanvas;

  /**
   * <h2>Constructor</h2>
   * Constructs a new SNLGameScreenView, registers it as an observer to the controller,
   * initializes the overlay and UI, and sets up styles and listeners.
   *
   * @param controller the {@link SNLGameScreenController} driving the game logic
   */
  public SNLGameScreenView(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this);
    initializeOverlay();
    createUI();

    URL css = getClass().getResource("/css/styles.css");
    if (css != null) {
      root.getStylesheets().add(css.toExternalForm());
    } else {
      logger.warn("Could not find styles.css");
    }

    setupInstructionPane();
    renderBoardGrid();
    Platform.runLater(this::initializePlayerTokens);

    setBackListener(() -> NavigationManager.getInstance().navigateToStartScreen());
    setSaveListener(this::showSaveGamePopup);
  }

  /**
   * <h2>Instruction Pane Setup</h2>
   * Sets up the instruction pane displayed on the left side of the screen.
   */
  private void setupInstructionPane() {
    BorderPane bp = root;
    VBox instructionBox = new VBox(10);
    instructionBox.setId("instruction-box");
    instructionBox.setAlignment(Pos.TOP_LEFT);
    instructionBox.setPrefWidth(200);
    Label title = new Label("INSTRUCTIONS");
    title.setFont(Font.font("Courier New", 18));
    Label instr = new Label(
            "• Roll the dice\n"
                    + "• Move that many steps\n"
                    + "• Climb ladders\n"
                    + "• Slide down snakes\n"
                    + "• First to tile " + (BOARD_SIZE * BOARD_SIZE) + " wins"
    );
    instr.setWrapText(true);
    instructionBox.getChildren().addAll(title, instr);
    bp.setLeft(instructionBox);
  }

  /**
   * <h2>Save Game Popup</h2>
   * Displays a popup allowing the user to save the current game state to a CSV file.
   */
  private void showSaveGamePopup() {
    Popup popup = new Popup();
    VBox content = new VBox(10);
    content.setStyle(
            "-fx-background-color: #e0f7fa; -fx-padding: 15;"
                    + " -fx-border-color: #00acc1; -fx-border-width: 2;");
    Label header = new Label("Save Game");
    header.setStyle("-fx-font-weight: bold; -fx-text-fill: #006064;");
    TextField filenameField = new TextField("snl_save_" + System.currentTimeMillis());
    filenameField.setPrefWidth(220);
    Label feedbackLabel = createFeedbackLabel();
    Button confirm = createConfirmButton(filenameField, feedbackLabel, popup);
    content.getChildren().addAll(header, filenameField, confirm, feedbackLabel);
    popup.getContent().add(content);
    popup.setAutoHide(true);
    popup.setHideOnEscape(true);
    javafx.stage.Window window = root.getScene().getWindow();
    popup.show(window,
            window.getX() + window.getWidth() / 2 - 150,
            window.getY() + window.getHeight() / 2 - 70
    );
  }

  /**
   * <h2>Create Feedback Label</h2>
   * Creates a feedback label used in the save game popup.
   *
   * @return a styled Label for feedback messages
   */
  private Label createFeedbackLabel() {
    Label label = new Label();
    label.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
    label.setVisible(false);
    return label;
  }

  /**
   * <h2>Create Confirm Button</h2>
   * Creates the confirm button for saving the game and sets its action.
   *
   * @param filenameField the TextField containing the desired save filename
   * @param feedbackLabel the Label to show save confirmation
   * @param popup the Popup to close upon successful save
   * @return a configured Button
   */
  private Button createConfirmButton(TextField filenameField, Label feedbackLabel,
                                     Popup popup) {
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
          try { Thread.sleep(1000); } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            logger.error("Error while sleeping thread: {}", ex.getMessage());
          }
          Platform.runLater(popup::hide);
        }).start();
      }
    });
    return confirm;
  }

  /**
   * <h2>initializeOverlay</h2>
   * {@inheritDoc} Initializes the overlay pane and layers for static board elements and token movements.
   */
  @Override
  protected void initializeOverlay() {
    overlayPane = new StackPane();
    staticCanvas = new Canvas(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);
    tokenLayer = new Pane();
    tokenLayer.setMouseTransparent(true);
    overlayPane.getChildren().setAll(staticCanvas, tokenLayer);
  }

  /**
   * <h2>getOverlay</h2>
   * {@inheritDoc}
   */
  @Override
  protected Pane getOverlay() {
    return overlayPane;
  }

  /**
   * <h2>renderBoardGrid</h2>
   * {@inheritDoc} Schedules rendering of static elements after base grid is drawn.
   */
  @Override
  protected void renderBoardGrid() {
    super.renderBoardGrid();
    Platform.runLater(this::renderStaticElements);
  }

  /**
   * <h2>renderStaticElements</h2>
   * Draws snakes and ladders on the static canvas.
   */
  private void renderStaticElements() {
    GraphicsContext gc = staticCanvas.getGraphicsContext2D();
    gc.clearRect(0, 0, staticCanvas.getWidth(), staticCanvas.getHeight());
    SNLBoard board = (SNLBoard) controller.getBoard();
    for (Ladder lad : board.getLadders()) {
      drawLadder(gc, lad.start(), lad.end());
    }
    for (Snake sn : board.getSnakes()) {
      drawSnake(gc, sn.start(), sn.end());
    }
  }

  /**
   * <h2>drawLadder</h2>
   * Renders a ladder between two tiles on the board.
   *
   * <P>AI: Heavy AI involvement
   * </P>
   *
   * @param gc the GraphicsContext to draw on
   * @param s  the starting tile number
   * @param e  the ending tile number
   */
  private void drawLadder(GraphicsContext gc, int s, int e) {
    StackPane ts = findTileNode(s);
    StackPane te = findTileNode(e);
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

  /**
   * <h2>drawSnake</h2>
   * Renders a snake curve between two tiles on the board.
   *
   * <P>Heavi AI involvement
   * </P>
   *
   * @param gc the GraphicsContext to draw on
   * @param s  the starting tile number (snake's head)
   * @param e  the ending tile number (snake's tail)
   */
  private void drawSnake(GraphicsContext gc, int s, int e) {
    StackPane ts = findTileNode(s);
    StackPane te = findTileNode(e);
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

  /**
   * <h2>createTile</h2>
   * {@inheritDoc} Customizes tile styling and removes default ImageView.
   */
  @Override
  protected StackPane createTile(int tileNum) {
    StackPane cell = super.createTile(tileNum);
    cell.getChildren().removeIf(n -> n instanceof ImageView);
    cell.getStyleClass().add("tile");
    cell.getStyleClass().add(tileNum % 2 == 0 ? "tile-even" : "tile-odd");
    return cell;
  }

  /**
   * <h2>initializePlayerTokens</h2>
   * Creates and places player token nodes on the board based on their starting positions.
   */
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

  /**
   * <h2>getImageForPlayer</h2>
   * Helper to load the player icon image based on the chosen character.
   *
   * @param p the Player whose icon should be loaded
   * @return the Image if found, otherwise null
   */
  private Image getImageForPlayer(Player p) {
    if (p.getCharacter() != null) {
      URL u = getClass().getResource("/player_icons/" + p.getCharacter().toLowerCase() + ".png");
      if (u != null) {
        return new Image(u.toExternalForm());
      }
    }
    return null;
  }

  /**
   * <h2>moveToken</h2>
   * Animates a token to a new tile position without transition.
   * @param n   the token Node to move
   * @param pos the target tile number
   */
  private void moveToken(Node n, int pos) {
    StackPane t = findTileNode(pos);
    if (t == null) return;
    Bounds tb = t.localToScene(t.getBoundsInLocal());
    Bounds bb = boardGrid.localToScene(boardGrid.getBoundsInLocal());
    Bounds nb = n.getBoundsInLocal();
    double x = tb.getMinX() - bb.getMinX() + (tb.getWidth() - nb.getWidth()) / 2;
    double y = tb.getMinY() - bb.getMinY() + (tb.getHeight() - nb.getHeight()) / 2;
    n.setLayoutX(x);
    n.setLayoutY(y);
  }

  /**
   * <h2>findTileNode</h2>
   * Locates the StackPane for a given tile number within the grid.
   *
   * @param tileNum the tile index (1-based)
   * @return the corresponding StackPane or null if not found
   */
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

  /**
   * <h2>onPlayerPositionChanged</h2>
   * AI: Heavy involvement in this method
   * {@inheritDoc} Animates player token movement with fade and translate transitions.
   */
  @Override
  public void onPlayerPositionChanged(Player p, int oldPos, int newPos) {
    Platform.runLater(() -> {
      Node token = playerTokens.get(p);
      if (token == null) return;
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

  /**
   * <h2>onGameOver</h2>
   * {@inheritDoc} Shows winner dialog and cleans up saved game.
   */
  @Override
  public void onGameOver(Player winner) {
    if (winner instanceof SNLPlayer sp) {
      Platform.runLater(() -> showWinner(sp));
      File gameFile = controller.getCsvFile();
      controller.deleteGame(gameFile);
    }
  }

  /**
   * <h2>showWinner</h2>
   * Displays the winner dialog for the SNLPlayer.
   *
   * @param winner the winning SNLPlayer
   */
  private void showWinner(SNLPlayer winner) {
    WinnerDialogs dialogs = new WinnerDialogs();
    dialogs.showWinnerDialog(winner);
  }

  /**
   * <h2>onDiceRolled</h2>
   * {@inheritDoc} Updates the dice result label.
   */
  @Override
  public void onDiceRolled(int r) {
    diceResultLabel.setText("Roll result: " + r);
  }

  /**
   * <h2>onPlayerTurnChanged</h2>
   * {@inheritDoc} Updates labels indicating the current player and position.
   */
  @Override
  public void onPlayerTurnChanged(Player c) {
    currentPlayerLabel.setText("Current turn: " + c.getName());
    positionLabel.setText("Position: " + c.getPosition());
  }

  /**
   * <h2>getCurrentPlayerImage</h2>
   * {@inheritDoc} Retrieves the current player's image for display.
   */
  @Override
  protected Image getCurrentPlayerImage() {
    return getImageForPlayer(controller.getCurrentPlayer());
  }

  /**
   * <h2>getAllPlayers</h2>
   * {@inheritDoc} Provides the list of all players in the game.
   */
  @Override
  protected List<Player> getAllPlayers() {
    return controller.getPlayers();
  }

  /**
   * <h2>handleRoll</h2>
   * {@inheritDoc} Delegates dice roll handling to controller.
   */
  @Override
  protected void handleRoll() {
    controller.handleRoll();
  }

  /**
   * <h2>getTileColor</h2>
   * {@inheritDoc} Asks controller for tile color styling.
   */
  @Override
  protected String getTileColor(int n) {
    return controller.getTileColor(n);
  }

  /**
   * <h2>getPlayersAtPosition</h2>
   * {@inheritDoc} Retrieves players at a given tile for rendering.
   */
  @Override
  protected List<Player> getPlayersAtPosition(int n) {
    return controller.getPlayersAtPosition(n);
  }
}