package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.view.ConsoleUI;
import java.io.File;

public class Main {
  public static void main(String[] args) {
    ensureApplicationDirectoriesExist();

    SnakesAndLadders game = new SnakesAndLadders();
    game.initialize();

    game.addPlayer("Player 1");
    game.addPlayer("Player 2");

    ConsoleUI ui = new ConsoleUI(game);
    ui.start();
  }

  private static void ensureApplicationDirectoriesExist() {
    File logsDir = new File("logs");
    boolean created = logsDir.mkdirs();
    if (created) {
      System.out.println("Logs directory created at: " + logsDir.getAbsolutePath());
    } else {
      if (logsDir.exists()) {
        System.out.println("Logs directory already exists");
      } else {
        System.out.println("Failed to create logs directory");
      }
    }
  }
}
