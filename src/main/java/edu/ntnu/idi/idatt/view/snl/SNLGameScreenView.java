package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.Ladder;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.Snake;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SNLGameScreenView extends GameScreen implements GameScreenObserver {

  private final SNLGameScreenController controller;
  private Pane ladderSnakeOverlay;
  private final Map<Player, Node> playerTokens = new HashMap<>();

  public SNLGameScreenView(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this);
    createUI();
    setBackListener(() -> NavigationManager.getInstance().navigateToStartScreen());
    setSaveListener(() -> {
      File tempFile = controller.getCsvFile();
      TextInputDialog dialog = new TextInputDialog("save_" + System.currentTimeMillis());
      dialog.setTitle("Save Game");
      dialog.setHeaderText("Name your save file:");
      dialog.setContentText("Filename:");
      dialog.showAndWait().ifPresent(filename -> controller.saveGame(tempFile, filename + ".csv"));
    });
  }

  public void initializeUI() {
    createUI();
    getRoot().layoutBoundsProperty().addListener((obs, oldVal, newVal) -> Platform.runLater(this::renderBoardGrid));
    Platform.runLater(this::initializePlayerTokens);
  }

  private void initializePlayerTokens() {
    controller.getPlayers().forEach(player -> {
      Image tokenImage = getImageForPlayer(player);
      if (tokenImage != null) addPlayerToken(player, tokenImage);
    });
  }

  private void addPlayerToken(Player player, Image tokenImage) {
    ImageView token = new ImageView(tokenImage);
    token.setFitHeight(30);
    token.setFitWidth(30);
    StackPane tokenContainer = new StackPane(token);
    playerTokens.put(player, tokenContainer);
    getOverlay().getChildren().add(tokenContainer);
    moveTokenToPixelPosition(tokenContainer, player.getPosition());
  }

  private Image getImageForPlayer(Player player) {
    if (player != null && player.getCharacter() != null) {
      String name = player.getCharacter().toLowerCase();
      URL url = getClass().getResource("/player_icons/" + name + ".png");
      if (url != null) return new Image(url.toExternalForm());
    }
    return null;
  }

  private double[] calculateTokenPosition(int position, Node tokenNode) {
    StackPane tile = findTileNode(position);
    if (tile == null) return null;
    Bounds tileBounds = tile.localToScene(tile.getBoundsInLocal());
    Bounds boardBounds = boardGrid.localToScene(boardGrid.getBoundsInLocal());
    Bounds tokenBounds = tokenNode.getBoundsInLocal();
    double x = tileBounds.getMinX() - boardBounds.getMinX() + (tileBounds.getWidth() - tokenBounds.getWidth()) / 2;
    double y = tileBounds.getMinY() - boardBounds.getMinY() + (tileBounds.getHeight() - tokenBounds.getHeight()) / 2;
    return new double[]{x, y};
  }

  private void moveTokenToPixelPosition(Node tokenNode, int position) {
    double[] pos = calculateTokenPosition(position, tokenNode);
    if (pos != null) {
      tokenNode.setLayoutX(pos[0]);
      tokenNode.setLayoutY(pos[1]);
    }
  }

  @Override
  protected Image getCurrentPlayerImage() {
    Player currentPlayer = controller.getCurrentPlayer();
    return getImageForPlayer(currentPlayer);
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
  protected String getTileColor(int tileNumber) {
    return controller.getTileColor(tileNumber);
  }

  @Override
  protected List<Player> getPlayersAtPosition(int tileNumber) {
    return controller.getPlayersAtPosition(tileNumber);
  }

  @Override
  protected void initializeOverlay() {
    ladderSnakeOverlay = new Pane();
    ladderSnakeOverlay.setPickOnBounds(false);
    ladderSnakeOverlay.setMouseTransparent(false);
    ladderSnakeOverlay.setPrefSize(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);
  }

  @Override
  protected Pane getOverlay() {
    return ladderSnakeOverlay;
  }

  @Override
  protected void renderBoardGrid() {
    super.renderBoardGrid();
    Platform.runLater(() -> {
      boardGrid.applyCss();
      boardGrid.layout();
      renderLaddersAndSnakes();
    });
  }

  private void renderLaddersAndSnakes() {
    ladderSnakeOverlay.getChildren().removeIf(node ->
        node instanceof Line || node instanceof CubicCurve || node instanceof Circle
    );

    SNLBoard board = (SNLBoard) controller.getBoard();
    for (Ladder ladder : board.getLadders()) drawLadder(ladder.getStart(), ladder.getEnd());
    for (Snake snake : board.getSnakes()) drawSnake(snake.getStart(), snake.getEnd());
  }

  private void drawLadder(int start, int end) {
    StackPane startTile = findTileNode(start);
    StackPane endTile = findTileNode(end);
    if (startTile != null && endTile != null) {
      Bounds startBounds = ladderSnakeOverlay.sceneToLocal(startTile.localToScene(startTile.getBoundsInLocal()));
      Bounds endBounds = ladderSnakeOverlay.sceneToLocal(endTile.localToScene(endTile.getBoundsInLocal()));
      Line left = new Line(startBounds.getCenterX() - 5, startBounds.getCenterY(), endBounds.getCenterX() - 5, endBounds.getCenterY());
      Line right = new Line(startBounds.getCenterX() + 5, startBounds.getCenterY(), endBounds.getCenterX() + 5, endBounds.getCenterY());
      ladderSnakeOverlay.getChildren().addAll(left, right);
      for (int i = 1; i < 5; i++) {
        double t = i / 5.0;
        double rungStartX = (1 - t) * left.getStartX() + t * left.getEndX();
        double rungStartY = (1 - t) * left.getStartY() + t * left.getEndY();
        double rungEndX = (1 - t) * right.getStartX() + t * right.getEndX();
        double rungEndY = (1 - t) * right.getStartY() + t * right.getEndY();
        Line rung = new Line(rungStartX, rungStartY, rungEndX, rungEndY);
        ladderSnakeOverlay.getChildren().add(rung);
      }
    }
  }

  private void drawSnake(int start, int end) {
    StackPane startTile = findTileNode(start);
    StackPane endTile = findTileNode(end);
    if (startTile != null && endTile != null) {
      Bounds startBounds = ladderSnakeOverlay.sceneToLocal(startTile.localToScene(startTile.getBoundsInLocal()));
      Bounds endBounds = ladderSnakeOverlay.sceneToLocal(endTile.localToScene(endTile.getBoundsInLocal()));
      CubicCurve curve = new CubicCurve(startBounds.getCenterX(), startBounds.getCenterY(),
          startBounds.getCenterX(), endBounds.getCenterY(),
          endBounds.getCenterX(), startBounds.getCenterY(),
          endBounds.getCenterX(), endBounds.getCenterY());
      curve.setStrokeWidth(4);
      curve.setStroke(Color.DARKRED);
      curve.setFill(null);
      Circle head = new Circle(startBounds.getCenterX(), startBounds.getCenterY(), 6, Color.DARKRED);
      ladderSnakeOverlay.getChildren().addAll(curve, head);
    }
  }

  private StackPane findTileNode(int tileNum) {
    int i = tileNum - 1;
    int row = BOARD_SIZE - 1 - (i / BOARD_SIZE);
    int col = (row % 2 == 0) ? i % BOARD_SIZE : (BOARD_SIZE - 1 - i % BOARD_SIZE);
    for (Node node : boardGrid.getChildren()) {
      if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row && node instanceof StackPane)
        return (StackPane) node;
    }
    return null;
  }

  @Override
  public void onPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
    Platform.runLater(() -> {
      Node tokenNode = playerTokens.get(player);
      if (tokenNode == null) return;
      double[] newPos = calculateTokenPosition(newPosition, tokenNode);
      if (newPos == null) return;

      double newX = newPos[0], newY = newPos[1];
      double oldX = tokenNode.getLayoutX();
      double oldY = tokenNode.getLayoutY();
      double deltaX = newX - oldX;
      double deltaY = newY - oldY;

      tokenNode.setOpacity(0.0);

      FadeTransition fade = new FadeTransition(Duration.millis(300), tokenNode);
      fade.setFromValue(0.0);
      fade.setToValue(1.0);

      TranslateTransition move = new TranslateTransition(Duration.millis(300), tokenNode);
      move.setByX(deltaX);
      move.setByY(deltaY);

      ParallelTransition transition = new ParallelTransition(move, fade);
      transition.setOnFinished(e -> {
        tokenNode.setTranslateX(0);
        tokenNode.setTranslateY(0);
        tokenNode.setLayoutX(newX);
        tokenNode.setLayoutY(newY);
      });

      transition.play();
    });
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
  public void onGameOver(Player winner) {}

  @Override
  public void onGameSaved(String filePath) {}

  @Override
  public Parent getRoot() {
    return root;
  }
}
