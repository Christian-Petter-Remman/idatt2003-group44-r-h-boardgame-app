package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.view.ConsoleUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    logger.info("Starting Snakes and Ladders application");

    FileManager.ensureApplicationDirectoriesExist();

    SnakesAndLadders game = new SnakesAndLadders();
    game.initialize();

    FileManager.saveDefaultBoard(game.getBoard());

    FileManager.loadOrCreateDefaultPlayers(game);

    logger.info("Starting console UI");
    ConsoleUI ui = new ConsoleUI(game);
    ui.start();

    FileManager.saveLastGamePlayers(game);

    logger.info("Application shutting down");
  }
}
