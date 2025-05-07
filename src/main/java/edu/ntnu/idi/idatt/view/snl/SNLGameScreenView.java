package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.Ladder;
import edu.ntnu.idi.idatt.model.snl.Snake;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.GameScreen;
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
        Platform.runLater(() -> renderBoardGrid());
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

      Line line = new Line(
          startBounds.getCenterX(), startBounds.getCenterY(),
          endBounds.getCenterX(), endBounds.getCenterY()
      );
      line.setStrokeWidth(4);
      line.setStroke(Color.BURLYWOOD);
      ladderSnakeOverlay.getChildren().add(line);
    }
  }

  private void drawSnake(int start, int end) {
    StackPane startTile = findTileNode(start);
    StackPane endTile = findTileNode(end);

    if (startTile != null && endTile != null) {
      Bounds startBounds = ladderSnakeOverlay.sceneToLocal(startTile.localToScene(startTile.getBoundsInLocal()));
      Bounds endBounds = ladderSnakeOverlay.sceneToLocal(endTile.localToScene(endTile.getBoundsInLocal()));

      CubicCurve curve = new CubicCurve(
          startBounds.getCenterX(), startBounds.getCenterY(),
          startBounds.getCenterX(), endBounds.getCenterY(),
          endBounds.getCenterX(), startBounds.getCenterY(),
          endBounds.getCenterX(), endBounds.getCenterY()
      );
      curve.setStrokeWidth(4);
      curve.setStroke(Color.DARKRED);
      curve.setFill(null);
      ladderSnakeOverlay.getChildren().add(curve);
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

  private void showGameOverAlert(Player winner) {}

  private void showGameSavedAlert(String filePath) {}

  @Override
  public Parent getRoot() {
    return root;
  }
}