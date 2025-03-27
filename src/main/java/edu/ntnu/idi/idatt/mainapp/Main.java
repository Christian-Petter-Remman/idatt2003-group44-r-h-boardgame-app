package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.view.ConsoleUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    try {
      FileManager.ensureApplicationDirectoriesExist();
      FileManager.saveDefaultBoard(new Board());

      int players = FileManager.loadOrCreateDefaultPlayers(new SnakesAndLadders());
      System.out.println("Players loaded: " + players);

    } catch (FileReadException | FileWriteException | CsvFormatException e) {
      System.err.println("Startup error: " + e.getMessage());
      e.printStackTrace();

    }
  }
}
