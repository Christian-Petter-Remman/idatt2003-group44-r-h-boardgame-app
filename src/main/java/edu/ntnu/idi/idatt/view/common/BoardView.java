package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Ladder;
import edu.ntnu.idi.idatt.model.common.player.Player;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.List;

public class BoardView extends GridPane {

  private final Board board;
  private final List<Player> players;

  public BoardView(Board board, List<Player> players) {
    this.board = board;
    this.players = players;

    setHgap(2);
    setVgap(2);
    setAlignment(Pos.CENTER);
    setPrefSize(900, 900);
    setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    render();
  }

  public void render() {
    getChildren().clear();

    for (int i = 0; i < 100; i++) {
      int tileNum = i + 1;
      StackPane cell = new StackPane();
      cell.setPrefSize(100, 100);
      cell.setStyle("-fx-border-color: black; -fx-background-color: white;");

      // Optional: still show tile number in background
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
      add(cell, col, row);
    }

    // === 2. Draw ladders ===
    for (Ladder ladder : board.getLadders()) {
      drawLadder(ladder.start(), ladder.end());
    }
  }

  private void drawLadder(int start, int end) {
    int startRow = 9 - (start - 1) / 10;
    int startCol = (startRow % 2 == 0) ? (start - 1) % 10 : 9 - (start - 1) % 10;

    int endRow = 9 - (end - 1) / 10;
    int endCol = (endRow % 2 == 0) ? (end - 1) % 10 : 9 - (end - 1) % 10;

    double cellSize = 100;
    double gap = getHgap();

    double startX = startCol * (cellSize + gap) + cellSize / 2.0;
    double startY = startRow * (cellSize + gap) + cellSize / 2.0;

    double endX = endCol * (cellSize + gap) + cellSize / 2.0;
    double endY = endRow * (cellSize + gap) + cellSize / 2.0;

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

    getChildren().addAll(left, right);

    int steps = 5;
    for (int i = 1; i < steps; i++) {
      double ratio = i / (double) steps;
      double midX = startX + dx * ratio;
      double midY = startY + dy * ratio;

      Line rung = new Line(
              midX - offsetX, midY - offsetY,
              midX + offsetX, midY + offsetY
      );
      rung.setStroke(Color.BURLYWOOD);
      rung.setStrokeWidth(2);
      getChildren().add(rung);
    }
  }
}