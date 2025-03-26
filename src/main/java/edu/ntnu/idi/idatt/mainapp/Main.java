package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.view.ConsoleUI;

public class Main {
  public static void main(String[] args) {
    SnakesAndLadders game = new SnakesAndLadders();
    game.initialize();

    game.addPlayer("Player 1");
    game.addPlayer("Player 2");

    ConsoleUI ui = new ConsoleUI(game);
    ui.start();
  }
}
