package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.view.ConsoleUI;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);
  public static void main(String[] args) {
    ensureApplicationDirectoriesExist();



    SnakesAndLadders game = new SnakesAndLadders();
    game.initialize();

    game.addPlayer("Player 1");
    game.addPlayer("Player 2");

    ConsoleUI ui = new ConsoleUI(game);

    logger.info("This is an error message");
    ui.start();

  }

  private static void ensureApplicationDirectoriesExist() {
      String[] directories = {
          "logs",
          "data",
          "data/players",
          "data/saved_games",
          "data/custom_boards"
      };

      for (String dir : directories) {
        File directory = new File(dir);
        if (!directory.exists()) {
          boolean created = directory.mkdirs();
          if (created) {
            System.out.println("Directory created at: " + directory.getAbsolutePath());
          } else {
            System.out.println("Failed to create directory: " + directory.getAbsolutePath());
          }
        } else {
          System.out.println("Directory already exists: " + directory.getAbsolutePath());
        }
      }
  }
}
