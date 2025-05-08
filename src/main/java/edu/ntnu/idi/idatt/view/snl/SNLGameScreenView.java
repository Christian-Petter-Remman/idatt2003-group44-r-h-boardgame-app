package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.Ladder;
import edu.ntnu.idi.idatt.model.snl.Snake;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;

public class SNLGameScreenView extends GameScreen {

  private static final Logger logger = LoggerFactory.getLogger(SNLGameScreenView.class);
  private final SNLGameScreenController controller;
  private Pane ladderSnakeOverlay;

  public SNLGameScreenView(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(new GameScreenObserver() {
      @Override
      public void onPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
        animatePlayerMovement(player, oldPosition, newPosition, null);
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
    });
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
  }

  @Override
  protected Image getCurrentPlayerImage() {
    Player currentPlayer = controller.getCurrentPlayer();
    if (currentPlayer != null && currentPlayer.getCharacter() != null) {
      String name = currentPlayer.getCharacter().toLowerCase();
      URL url = getClass().getResource("/player_icons/" + name + ".png");
      if (url != null) {
        return new Image(url.toExternalForm());
      } else {
        logger.warn("No image for character: {}", name);
      }
    }
    return null;
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
    ladderSnakeOverlay.setMouseTransparent(true);
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
    ladderSnakeOverlay.getChildren().clear();
    SNLBoard board = (SNLBoard) controller.getBoard();
    for (Ladder ladder : board.getLadders()) {
      drawLadder(ladder.getStart(), ladder.getEnd());
    }
    for (Snake snake : board.getSnakes()) {
      drawSnake(snake.getStart(), snake.getEnd());
    }
  }

  private void drawLadder(int start, int end) {
    StackPane startTile = findTileNode(start);
    StackPane endTile = findTileNode(end);
    if (startTile != null && endTile != null) {
      Bounds startBounds = ladderSnakeOverlay.sceneToLocal(startTile.localToScene(startTile.getBoundsInLocal()));
      Bounds endBounds = ladderSnakeOverlay.sceneToLocal(endTile.localToScene(endTile.getBoundsInLocal()));
      Line left = new Line(startBounds.getCenterX() - 5, startBounds.getCenterY(), endBounds.getCenterX() - 5, endBounds.getCenterY());
      Line right = new Line(startBounds.getCenterX() + 5, startBounds.getCenterY(), endBounds.getCenterX() + 5, endBounds.getCenterY());
      left.setStrokeWidth(3);
      left.setStroke(Color.BURLYWOOD);
      right.setStrokeWidth(3);
      right.setStroke(Color.BURLYWOOD);
      ladderSnakeOverlay.getChildren().addAll(left, right);
      int rungs = 5;
      for (int i = 1; i < rungs; i++) {
        double t = i / (double) rungs;
        double rungStartX = (1 - t) * left.getStartX() + t * left.getEndX();
        double rungStartY = (1 - t) * left.getStartY() + t * left.getEndY();
        double rungEndX = (1 - t) * right.getStartX() + t * right.getEndX();
        double rungEndY = (1 - t) * right.getStartY() + t * right.getEndY();
        Line rung = new Line(rungStartX, rungStartY, rungEndX, rungEndY);
        rung.setStrokeWidth(2);
        rung.setStroke(Color.SADDLEBROWN);
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
      CubicCurve curve = new CubicCurve(startBounds.getCenterX(), startBounds.getCenterY(), startBounds.getCenterX(), endBounds.getCenterY(), endBounds.getCenterX(), startBounds.getCenterY(), endBounds.getCenterX(), endBounds.getCenterY());
      curve.setStrokeWidth(4);
      curve.setStroke(Color.DARKRED);
      curve.setFill(null);
      javafx.scene.shape.Circle head = new javafx.scene.shape.Circle(startBounds.getCenterX(), startBounds.getCenterY(), 6, Color.DARKRED);
      ladderSnakeOverlay.getChildren().addAll(curve, head);
    }
  }

  private StackPane findTileNode(int tileNum) {
    int i = tileNum - 1;
    int row = BOARD_SIZE - 1 - (i / BOARD_SIZE);
    int col = (row % 2 == 0) ? i % BOARD_SIZE : (BOARD_SIZE - 1 - i % BOARD_SIZE);
    for (Node node : boardGrid.getChildren()) {
      if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row && node instanceof StackPane) return (StackPane) node;
    }
    return null;
  }

  private void animatePlayerMovement(Player player, int oldPosition, int newPosition, Runnable onComplete) {
    int current = player.getPosition();
    if (current == newPosition) {
      renderBoardGrid();
      if (onComplete != null) onComplete.run();
      return;
    }
    SequentialTransition sequence = new SequentialTransition();
    int step = current < newPosition ? 1 : -1;
    for (int i = current + step; i != newPosition + step; i += step) {
      final int pos = i;
      PauseTransition pause = new PauseTransition(Duration.millis(200));
      pause.setOnFinished(e -> {
        player.setPosition(pos);
        renderBoardGrid();
      });
      sequence.getChildren().add(pause);
    }
    sequence.setOnFinished(e -> {
      player.setPosition(newPosition);
      renderBoardGrid();
      SNLBoard board = (SNLBoard) controller.getBoard();
      Integer jump = board.getSpecialTiles().get(newPosition);
      if (jump != null && jump != newPosition) {
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(ev -> {
          player.setPosition(jump);
          renderBoardGrid();
          if (onComplete != null) onComplete.run();
        });
        delay.play();
      } else if (onComplete != null) {
        onComplete.run();
      }
    });
    sequence.play();
  }

  private void showGameOverAlert(Player winner) {}

  private void showGameSavedAlert(String filePath) {}

  @Override
  public Parent getRoot() {
    return root;
  }
}
