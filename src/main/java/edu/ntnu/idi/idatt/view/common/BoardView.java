package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Ladder;
import edu.ntnu.idi.idatt.model.common.player.Player;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.List;

public class BoardView extends StackPane {

  private final Board board;
  private final List<Player> players;
  private final GridPane boardGrid = new GridPane(); // Board tiles
  private final Pane ladderOverlay = new Pane();     // Draw ladders
  private int tileSize = 90;

  public BoardView(Board board, List<Player> players) {
    this.board = board;
    this.players = players;

    boardGrid.setHgap(2);
    boardGrid.setVgap(2);
    boardGrid.setAlignment(Pos.CENTER);
    boardGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    boardGrid.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

    ladderOverlay.setPickOnBounds(false); // Allow clicks to pass through
    ladderOverlay.setMouseTransparent(true); // Don't block input

    getChildren().addAll(boardGrid, ladderOverlay); // Stack layers
    render();
  }

  public void render() {
    boardGrid.getChildren().clear();
    boardGrid.getColumnConstraints().clear();
    boardGrid.getRowConstraints().clear();
    ladderOverlay.getChildren().clear();

    int boardSize = 10;

    boardGrid.setPrefSize(tileSize * boardSize, tileSize * boardSize);
    boardGrid.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    boardGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    ladderOverlay.setPrefSize(tileSize * boardSize, tileSize * boardSize);

    for (int i = 0; i < boardSize; i++) {
      ColumnConstraints colConst = new ColumnConstraints(tileSize);
      colConst.setHalignment(HPos.CENTER);
      boardGrid.getColumnConstraints().add(colConst);

      RowConstraints rowConst = new RowConstraints(tileSize);
      rowConst.setValignment(VPos.CENTER);
      boardGrid.getRowConstraints().add(rowConst);
    }

    for (int i = 0; i < 100; i++) {
      int tileNum = i + 1;
      StackPane cell = new StackPane();
      cell.setPrefSize(tileSize, tileSize);
      cell.setMinSize(tileSize, tileSize);
      cell.setMaxSize(tileSize, tileSize);
      cell.setStyle("-fx-border-color: black; -fx-background-color: white;");

      Text tileNumber = new Text(String.valueOf(tileNum));
      tileNumber.setStyle("-fx-fill: #ccc;");
      cell.getChildren().add(tileNumber);

      for (Player p : players) {
        if (p.getPosition() == tileNum) {
          String characterName = p.getCharacter();
          Image image = new Image("PlayerIcons/" + characterName + ".png", 40, 40, true, true);
          ImageView icon = new ImageView(image);
          cell.getChildren().add(icon);
        }
      }

      int row = 9 - i / 10;
      int col = (row % 2 == 0) ? i % 10 : 9 - (i % 10);
      boardGrid.add(cell, col, row);
    }

    for (Ladder ladder : board.getLadders()) {
      drawLadder(ladder.start(), ladder.end());
    }
    for (Snak)
  }

  private void drawLadder(int start, int end) {
    int startRow = 9 - (start - 1) / 10;
    int startCol = (startRow % 2 == 0) ? (start - 1) % 10 : 9 - (start - 1) % 10;

    int endRow = 9 - (end - 1) / 10;
    int endCol = (endRow % 2 == 0) ? (end - 1) % 10 : 9 - (end - 1) % 10;

    double gap = boardGrid.getHgap();

    double startX = startCol * (tileSize + gap) + tileSize / 2.0;
    double startY = startRow * (tileSize + gap) + tileSize / 2.0;

    double endX = endCol * (tileSize + gap) + tileSize / 2.0;
    double endY = endRow * (tileSize + gap) + tileSize / 2.0;

    double dx = endX - startX;
    double dy = endY - startY;

    double offsetX = -dy / Math.sqrt(dx * dx + dy * dy) * 10;
    double offsetY = dx / Math.sqrt(dx * dx + dy * dy) * 10;

    Line left = new Line(startX + offsetX, startY + offsetY, endX + offsetX, endY + offsetY);
    Line right = new Line(startX - offsetX, startY - offsetY, endX - offsetX, endY - offsetY);

    left.setStroke(Color.DARKGREEN);
    right.setStroke(Color.DARKGREEN);
    left.setStrokeWidth(3);
    right.setStrokeWidth(3);

    ladderOverlay.getChildren().addAll(left, right);

    int steps = 7;
    for (int i = 1; i < steps; i++) {
      double ratio = i / (double) steps;
      double midX = startX + dx * ratio;
      double midY = startY + dy * ratio;

      Line rung = new Line(
              midX - offsetX, midY - offsetY,
              midX + offsetX, midY + offsetY
      );
      rung.setStroke(Color.BROWN);
      rung.setStrokeWidth(2);
      ladderOverlay.getChildren().add(rung);
    }
  }

  private void drawSnakes(int start, int end) {

  }
}