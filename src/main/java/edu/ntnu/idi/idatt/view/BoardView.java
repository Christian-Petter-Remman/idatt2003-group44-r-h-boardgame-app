package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.common.player.Player;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    render();
  }

  public void render() {
    getChildren().clear();

    for (int i = 0; i < 100; i++) {
      int tileNum = i + 1;
      StackPane cell = new StackPane();
      cell.setPrefSize(100, 100);
      cell.setStyle("-fx-border-color: black; -fx-background-color: white;");

      StringBuilder cellText = new StringBuilder(String.valueOf(tileNum));

      for (Player p : players) {
        if (p.getPosition() == tileNum) {
          cellText.append(" ").append(p.getName().charAt(0)); // f.eks. "A" eller "B"
        }
      }

      cell.getChildren().add(new Text(cellText.toString()));

      int row = 9 - i / 10;
      int col = (row % 2 == 0) ? i % 10 : 9 - (i % 10);

      add(cell, col, row);
    }
  }
}