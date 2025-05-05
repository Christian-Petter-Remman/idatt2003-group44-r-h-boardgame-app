package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.application.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SNLGameScreenView extends GameScreen {
  private final SNLGameScreenController controller;
  private final Pane overlay = new Pane();
  private final Label lastRollLabel = new Label("Last roll: -");
  private final Map<Player, Label> playerPositionLabels = new HashMap<>();
  private final Map<Player, ImageView> playerTokenViews = new HashMap<>();
  private final Map<Player, Integer> playerTokenPositions = new HashMap<>();
  private final ImageView currentTurnImageView = new ImageView();
  private Button rollDiceButton;
  private int lastRollValue = 0;

  public SNLGameScreenView(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this);

    root = new BorderPane();

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

    overlay.setPickOnBounds(false);
    overlay.setMouseTransparent(true);
    overlay.prefWidthProperty().bind(boardGrid.widthProperty());
    overlay.prefHeightProperty().bind(boardGrid.heightProperty());

    boardWithOverlay = new StackPane(boardGrid, overlay);
    StackPane.setAlignment(boardGrid, Pos.TOP_LEFT);
    StackPane.setAlignment(overlay, Pos.TOP_LEFT);
    root.setCenter(boardWithOverlay);

    root.setLeft(createLeftPanel());
    root.setRight(createRightPanel());

    root.getStylesheets().add(
        Objects.requireNonNull(getClass()
                .getResource("/css/SNLGameScreenStyleSheet.css"))
            .toExternalForm()
    );

    boardGrid.widthProperty().addListener((obs, old, nw) ->
        Platform.runLater(this::initializeOverlay)
    );
    boardGrid.heightProperty().addListener((obs, old, nw) ->
        Platform.runLater(this::initializeOverlay)
    );

    Platform.runLater(() -> {
      initializeOverlay();
      updateCurrentPlayerDisplay();
      initializeTokens();
    });
  }

  private VBox createLeftPanel() {
    VBox leftPanel = new VBox(10);
    leftPanel.getStyleClass().add("snl-left-panel");
    leftPanel.setPrefWidth(220);
    leftPanel.setAlignment(Pos.TOP_CENTER);

    Label currentTurnHeader = new Label("Current Turn");
    currentTurnHeader.getStyleClass().add("title");

    currentTurnImageView.setFitWidth(50);
    currentTurnImageView.setFitHeight(50);

    currentPlayerLabel = new Label();
    currentPlayerLabel.getStyleClass().add("info");

    leftPanel.getChildren().addAll(
        currentTurnHeader,
        currentTurnImageView,
        currentPlayerLabel
    );

    for (Player player : controller.getPlayers()) {
      HBox infoRow = new HBox(5);
      infoRow.getStyleClass().add("player-info-row");
      ImageView avatar = new ImageView(getPlayerImage(player));
      avatar.setFitWidth(30);
      avatar.setFitHeight(30);
      Label nameLabel = new Label(player.getName());
      Label positionLabel = new Label("Tile: " + player.getPosition());
      positionLabel.getStyleClass().add("info");
      playerPositionLabels.put(player, positionLabel);
      infoRow.getChildren().addAll(avatar, nameLabel, positionLabel);
      leftPanel.getChildren().add(infoRow);
    }

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    leftPanel.getChildren().add(spacer);

    rollDiceButton = new Button("Roll Dice");
    rollDiceButton.getStyleClass().add("button");
    rollDiceButton.setOnAction(e -> {
      rollDiceButton.setDisable(true);
      controller.handleRoll();
    });

    Button quitButton = new Button("Quit to Menu");
    quitButton.getStyleClass().add("button");
    quitButton.setOnAction(e -> controller.navigateTo("INTRO_SCREEN"));

    leftPanel.getChildren().addAll(
        rollDiceButton,
        lastRollLabel,
        quitButton
    );
    return leftPanel;
  }

  private VBox createRightPanel() {
    VBox rightPanel = new VBox(10);
    rightPanel.getStyleClass().add("snl-right-panel");
    rightPanel.setPrefWidth(200);
    rightPanel.setAlignment(Pos.CENTER);
    Region topSpacer = new Region();
    Region bottomSpacer = new Region();
    VBox.setVgrow(topSpacer, Priority.ALWAYS);
    VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

    Label instructions = new Label(
        """
            How to play:
            - Roll the dice
            - Climb ladders
            - Avoid snakes
            - First to 100 wins"""
    );
    instructions.getStyleClass().add("instructions-box");

    Button saveButton = new Button("Save Game");
    saveButton.getStyleClass().add("button");

    rightPanel.getChildren().addAll(
        topSpacer,
        instructions,
        saveButton,
        bottomSpacer
    );
    return rightPanel;
  }

  private void initializeTokens() {
    for (Player player : controller.getPlayers()) {
      ImageView tokenView = playerTokenViews.get(player);
      if (tokenView == null) {
        Image tokenImage = getPlayerImage(player);
        if (tokenImage == null) continue;
        tokenView = new ImageView(tokenImage);
        tokenView.setFitWidth(TILE_SIZE * 0.6);
        tokenView.setFitHeight(TILE_SIZE * 0.6);
        overlay.getChildren().add(tokenView);
        playerTokenViews.put(player, tokenView);
      }
      int startPos = player.getPosition();
      playerTokenPositions.put(player, startPos);
      Point2D center = getCellCenter(startPos);
      tokenView.setLayoutX(center.getX() - tokenView.getFitWidth() / 2);
      tokenView.setLayoutY(center.getY() - tokenView.getFitHeight() / 2);
    }
  }

  @Override
  protected Image getPlayerImage(Player player) {
    if (player.getCharacter() == null) return null;
    URL url = getClass().getResource(
        "/player_icons/" + player.getCharacter().toLowerCase() + ".png"
    );
    return url == null ? null : new Image(url.toExternalForm());
  }

  @Override
  public void onDiceRolled(int result) {
    lastRollValue = result;
    Platform.runLater(() ->
        lastRollLabel.setText("Last roll: " + result)
    );
  }

  @Override
  public void onPlayerTurnChanged(Player current) {
    Platform.runLater(this::updateCurrentPlayerDisplay);
  }

  private void updateCurrentPlayerDisplay() {
    Player p = controller.getCurrentPlayer();
    currentPlayerLabel.setText(p.getName());
    currentTurnImageView.setImage(controller.getCurrentPlayerImage());
  }

  @Override
  public void onPlayerPositionChanged(Player player, int oldPos, int newPos) {
    Platform.runLater(() -> {
      ImageView tokenView = playerTokenViews.get(player);
      if (tokenView == null) return;

      int startPos = playerTokenPositions.get(player);
      int landingPos = startPos + lastRollValue;
      boolean hasModifier = (landingPos != newPos);

      int stepDuration = 200;
      int pauseDuration = 750;
      Timeline timeline = new Timeline();

      for (int i = 1; i <= lastRollValue; i++) {
        int tile = startPos + i;
        timeline.getKeyFrames().add(new KeyFrame(
            Duration.millis(stepDuration * i),
            e -> moveToken(tokenView, player, tile)
        ));
      }

      if (hasModifier) {


        timeline.getKeyFrames().add(new KeyFrame(
            Duration.millis(stepDuration * lastRollValue),
            e -> moveToken(tokenView, player, landingPos)
        ));
        timeline.getKeyFrames().add(new KeyFrame(
            Duration.millis(stepDuration * lastRollValue + pauseDuration),
            e -> moveToken(tokenView, player, newPos)
        ));
      }

      timeline.setOnFinished(e -> rollDiceButton.setDisable(false));
      timeline.play();

      playerTokenPositions.put(player, newPos);
      lastRollValue = 0;
    });
  }

  private void moveToken(ImageView tokenView, Player player, int tile) {
    Point2D center = getCellCenter(tile);
    tokenView.setLayoutX(center.getX() - tokenView.getFitWidth() / 2);
    tokenView.setLayoutY(center.getY() - tokenView.getFitHeight() / 2);
    playerPositionLabels.get(player).setText("Tile: " + tile);
  }

  protected void initializeOverlay() {
    overlay.getChildren().retainAll(playerTokenViews.values());
    SNLBoard board = (SNLBoard) controller.getBoard();
    for (var ladder : board.getLadders()) {
      drawLadder(ladder.getStart(), ladder.getEnd());
    }
    for (var snake : board.getSnakes()) {
      drawSnake(snake.getStart(), snake.getEnd());
    }
  }

  private void drawLadder(int start, int end) {
    Point2D src = getCellCenter(start), dst = getCellCenter(end);
    double dx = dst.getX() - src.getX(), dy = dst.getY() - src.getY();
    double dist = Math.hypot(dx, dy);
    double perpX = -dy / dist, perpY = dx / dist;
    double width = Math.min(10, dist / 20), offX = perpX * width, offY = perpY * width;

    Line rail1 = new Line(
        src.getX() + offX, src.getY() + offY,
        dst.getX() + offX, dst.getY() + offY
    );
    Line rail2 = new Line(
        src.getX() - offX, src.getY() - offY,
        dst.getX() - offX, dst.getY() - offY
    );
    rail1.setStrokeWidth(3); rail2.setStrokeWidth(3);
    rail1.setStroke(Color.BURLYWOOD); rail2.setStroke(Color.BURLYWOOD);
    overlay.getChildren().addAll(rail1, rail2);

    int steps = (int)(dist / 15);
    for (int i = 1; i < steps; i++) {
      double t = i / (double)steps;
      double cx = src.getX() + dx * t, cy = src.getY() + dy * t;
      Line rung = new Line(cx - offX, cy - offY, cx + offX, cy + offY);
      rung.setStrokeWidth(2); rung.setStroke(Color.SADDLEBROWN);
      overlay.getChildren().add(rung);
    }
  }

  private void drawSnake(int start, int end) {
    Point2D src = getCellCenter(start), dst = getCellCenter(end);
    CubicCurve body = getBody(dst, src);
    body.setStrokeWidth(4);
    body.setStroke(Color.DARKRED);
    body.setFill(null);

    Circle head = new Circle(src.getX(), src.getY(), 6, Color.DARKRED);
    Circle tail = new Circle(dst.getX(), dst.getY(), 4, Color.DARKRED);

    overlay.getChildren().addAll(body, head, tail);
  }

  private CubicCurve getBody(Point2D dst, Point2D src) {
    double dx = dst.getX() - src.getX(), dy = dst.getY() - src.getY();
    double dist = Math.hypot(dx, dy);
    double amp = Math.min(dist * 0.2, 25), perpX = -dy / dist, perpY = dx / dist;

    double c1x = src.getX() + dx * 0.3 + perpX * amp,
        c1y = src.getY() + dy * 0.3 + perpY * amp;
    double c2x = src.getX() + dx * 0.7 - perpX * amp,
        c2y = src.getY() + dy * 0.7 - perpY * amp;

    return new CubicCurve(
        src.getX(), src.getY(),
        c1x, c1y, c2x, c2y,
        dst.getX(), dst.getY()
    );
  }

  @Override protected String getTileColor(int tile)            { return controller.getTileColor(tile); }
  @Override protected List<Player> getPlayersAtPosition(int t)  { return controller.getPlayersAtPosition(t); }
  @Override protected void handleRoll()                         { controller.handleRoll(); }
  @Override protected List<Player> getAllPlayers()              { return controller.getPlayers(); }

  @Override
  protected void renderBoardGrid() {
    boardGrid.getChildren().clear();
    for (int i = 1; i <= BOARD_SIZE * BOARD_SIZE; i++) {
      StackPane cell = new StackPane();
      cell.setPrefSize(TILE_SIZE, TILE_SIZE);
      cell.setStyle(
          "-fx-border-color: black; " +
              "-fx-background-color: " + getTileColor(i) + ";"
      );

      Text number = new Text(String.valueOf(i));
      number.setStyle("-fx-fill: #555;");
      cell.getChildren().add(number);

      int row = BOARD_SIZE - 1 - ((i - 1) / BOARD_SIZE);
      int col = ((row % 2) == 0)
          ? ((i - 1) % BOARD_SIZE)
          : (BOARD_SIZE - 1 - ((i - 1) % BOARD_SIZE));
      boardGrid.add(cell, col, row);
    }
  }
}
