package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersPlayer;
import edu.ntnu.idi.idatt.model.common.player.Player;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
          cell.getChildren().add(icon);  // add on top of tile number
        }
      }

      int row = 9 - i / 10;
      int col = (row % 2 == 0) ? i % 10 : 9 - (i % 10);
      add(cell, col, row);
    }
  }
}