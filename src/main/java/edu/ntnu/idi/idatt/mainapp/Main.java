package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.filehandling.BoardJsonHandler;
import edu.ntnu.idi.idatt.filehandling.PlayerCsvHandler;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.common.player.Player;
import edu.ntnu.idi.idatt.view.ConsoleUI;
import java.io.File;
import java.util.List;
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

  private static void saveDefaultBoard() {
    try {
      Board board = new Board();
      BoardJsonHandler  jsonHandler = new BoardJsonHandler();
      jsonHandler.saveToFile(board, "data/custom_boards/default_board.json");
    } catch (Exception e) {
      System.err.println("Failed to save default board: " + e.getMessage());
    }
  }

  private static void savePlayersToFile(SnakesAndLadders game) {
    try {
      PlayerCsvHandler csvHandler = new PlayerCsvHandler();
      csvHandler.saveToFile(game.getPlayers(), "data/players/last_game_players.csv");
      System.out.println("Players saved to file");
    } catch (Exception e) {
      System.err.println("Failed to save players to file: " + e.getMessage());
    }
  }

  private static void loadPlayersFromFile(SnakesAndLadders game) {
    try {
      PlayerCsvHandler csvHandler = new PlayerCsvHandler();
      List<Player> players = csvHandler.loadFromFile("data/players/last_game_players.csv");

      for (Player player : players) {
        game.addPlayer(player);
      }

      System.out.println("Players loaded from file");

    } catch (Exception e) {
      System.err.println("Failed to load players from file: " + e.getMessage());
    }
  }
}
