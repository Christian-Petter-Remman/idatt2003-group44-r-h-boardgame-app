package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.snl.Ladder;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.snl.Snake;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        renderBoardGrid();
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

    createUI(); // Call after controller is ready
  }

  public void initializeUI() {
    createUI();
  }

  @Override
  protected Image getCurrentPlayerImage() {
    Player currentPlayer = controller.getCurrentPlayer();
    if (currentPlayer != null && currentPlayer.getCharacter() != null) {
      String characterName = currentPlayer.getCharacter().toLowerCase();
      URL url = getClass().getResource("/player_icons/" + characterName + ".png");
      if (url != null) {
        return new Image(url.toExternalForm());
      } else {
        logger.warn("No image found for character: {}", characterName);
      }
    }
    return null;
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
    renderLaddersAndSnakes();
  }

  @Override
  protected Pane getOverlay() {
    return ladderSnakeOverlay;
  }

  private void renderLaddersAndSnakes() {
    ladderSnakeOverlay.getChildren().clear();
    SNLBoard board = (SNLBoard) controller.getBoard();

    board.getLadders().forEach(ladder -> drawLadder(ladder.getStart(), ladder.getEnd()));
    board.getSnakes().forEach(snake -> drawSnake(snake.getStart(), snake.getEnd()));
  }

  private void drawLadder(int start, int end) {
    double[] startPos = getTileCenter(start);
    double[] endPos = getTileCenter(end);

    double dx = endPos[0] - startPos[0];
    double dy = endPos[1] - startPos[1];
    double distance = Math.sqrt(dx * dx + dy * dy);

    double dirX = dx / distance;
    double dirY = dy / distance;
    double perpX = -dirY;
    double perpY = dirX;
    double ladderWidth = Math.min(10, Math.max(5, distance / 20));

    double offsetX = perpX * ladderWidth;
    double offsetY = perpY * ladderWidth;

    Line left = new Line(startPos[0] + offsetX, startPos[1] + offsetY,
            endPos[0] + offsetX, endPos[1] + offsetY);
    Line right = new Line(startPos[0] - offsetX, startPos[1] - offsetY,
            endPos[0] - offsetX, endPos[1] - offsetY);

    left.setStroke(Color.BURLYWOOD);
    right.setStroke(Color.BURLYWOOD);
    left.setStrokeWidth(3);
    right.setStrokeWidth(3);

    ladderSnakeOverlay.getChildren().addAll(left, right);


    int steps = (int) (distance / 15);
    for (int i = 1; i < steps; i++) {
      double t = (double) i / steps;
      double centerX = startPos[0] + dx * t;
      double centerY = startPos[1] + dy * t;

      Line rung = new Line(centerX - offsetX, centerY - offsetY,
              centerX + offsetX, centerY + offsetY);
      rung.setStroke(Color.SADDLEBROWN);
      rung.setStrokeWidth(2);

      ladderSnakeOverlay.getChildren().add(rung);
    }
  }

  private void drawSnake(int start, int end) {
    double[] startPos = getTileCenter(start);
    double[] endPos = getTileCenter(end);

    double dx = endPos[0] - startPos[0];
    double dy = endPos[1] - startPos[1];
    double distance = Math.sqrt(dx * dx + dy * dy);

    double curveAmplitude = Math.min(distance * 0.2, 25);
    double perpX = -dy / distance;
    double perpY = dx / distance;

    double ctrlX1 = startPos[0] + dx * 0.3 + perpX * curveAmplitude;
    double ctrlY1 = startPos[1] + dy * 0.3 + perpY * curveAmplitude;
    double ctrlX2 = startPos[0] + dx * 0.7 - perpX * curveAmplitude;
    double ctrlY2 = startPos[1] + dy * 0.7 - perpY * curveAmplitude;

    CubicCurve snake = new CubicCurve(
            startPos[0], startPos[1],
            ctrlX1, ctrlY1,
            ctrlX2, ctrlY2,
            endPos[0], endPos[1]
    );
    snake.setStroke(Color.DARKRED);
    snake.setStrokeWidth(4);
    snake.setFill(null);

    Circle head = new Circle(startPos[0], startPos[1], 6, Color.DARKRED);
    Circle tail = new Circle(endPos[0], endPos[1], 4, Color.DARKRED);

    ladderSnakeOverlay.getChildren().addAll(snake, head, tail);
  }



  private void showGameOverAlert(Player winner) {
    // Optional: JavaFX Alert dialog or modal
  }

  private void showGameSavedAlert(String filePath) {
    // Optional: Confirm save with user
  }

  @Override
  public Parent getRoot() {
    return root;
  }
}