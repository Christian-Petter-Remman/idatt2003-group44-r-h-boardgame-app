package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.common.BoardGameFactory;
import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.filehandling.FileManager;
//import edu.ntnu.idi.idatt.view.common.ConsoleUI;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ConsoleMain {
  private static final Logger logger = LoggerFactory.getLogger(ConsoleMain.class);

  public static void main(String[] args) {
    logger.info("Starting Snakes and Ladders application");

    try {
      // Ensure all required directories exist
      FileManager.ensureApplicationDirectoriesExist();

      // Create the game factory
      BoardGameFactory factory = new SnakesAndLaddersFactory();

      // Get available board configurations
      String[] configurations = factory.getAvailableConfigurations();

      // Let user select a configuration
      String selectedConfig = selectConfiguration(configurations);

      // Create the game with selected configuration
      BoardGame game = factory.createBoardGameFromConfiguration(selectedConfig);

      if (!(game instanceof SnakesAndLadders snakesAndLadders)) {
        throw new IllegalStateException("Created game is not an instance of SnakesAndLadders");
      }

      // Load players from file or create default players
      FileManager.loadOrCreateDefaultPlayers(snakesAndLadders);

      // Start the console UI
      logger.info("Starting console UI");
//      ConsoleUI ui = new ConsoleUI(snakesAndLadders);
//      ui.start();

      // Save players after the game ends
      FileManager.saveLastGamePlayers(snakesAndLadders);

    } catch (FileReadException | CsvFormatException | FileWriteException e) {
      logger.error("Error during file operations: {}", e.getMessage());
    } catch (Exception e) {
      logger.error("Unexpected error occurred: {}", e.getMessage());
    }

    logger.info("Application shutting down");
  }

  private static String selectConfiguration(String[] configurations) {
    System.out.println("Available board configurations:");
    for (int i = 0; i < configurations.length; i++) {
      System.out.println((i + 1) + ". " + configurations[i]);
    }

    Scanner scanner = new Scanner(System.in);
    int selection = 0;

    while (selection < 1 || selection > configurations.length) {
      System.out.print("Select a configuration (1-" + configurations.length + "): ");
      try {
        selection = Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Please enter a valid number.");
      }
    }

    return configurations[selection - 1];
  }
}
