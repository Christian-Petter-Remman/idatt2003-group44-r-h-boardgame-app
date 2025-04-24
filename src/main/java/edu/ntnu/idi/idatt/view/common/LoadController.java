package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.filehandling.BoardJsonHandler;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.common.Dice;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

public class LoadController {

  private final Stage stage;
  private final Logger logger = LoggerFactory.getLogger(LoadController.class);

  public LoadController(Stage stage) {
    this.stage = stage;
  }

  /**
   * Handles the load action given a CSV path.
   * Opens the game screen if successful, otherwise shows an alert.
   */
  public void handleLoad(String csvPath) {
    BoardJsonHandler handler = new BoardJsonHandler();
    String boardPath = extractBoardPathFromCsv(csvPath);

    if (boardPath == null) {
      showAlert("Error", "Failed to read board path from CSV.");
      return;
    }

    try {
      SnakesAndLadders snakeGame = handler.loadGameFromFile(boardPath, SnakesAndLadders::new);
      int players = snakeGame.loadPlayersFromCsv(csvPath);
      Dice dice = new Dice(1);
      snakeGame.setDice(dice);
      logger.info("Loaded {} players from {}", players, csvPath);
      new GameScreenView(stage, snakeGame, boardPath, csvPath).show();
    } catch (FileReadException | JsonParsingException ex) {
      logger.error("Failed to load game: {}", ex.getMessage());
      showAlert("Load Error", "Could not load saved game: " + ex.getMessage());
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  private String extractBoardPathFromCsv(String csvPath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
      String firstLine = reader.readLine();
      if (firstLine != null && firstLine.startsWith("SNLBoard:")) {
        return firstLine.replace("SNLBoard:", "").trim();
      } else {
        logger.warn("No board path found in CSV file: {}", csvPath);
      }
    } catch (IOException e) {
      logger.error("Error reading CSV file {}: {}", csvPath, e.getMessage());
    }
    return null;
  }

  public static List<File> getLastNPlayerFiles(String directoryPath, int n) {
    File folder = new File(directoryPath);
    File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

    if (files == null || files.length == 0) return Collections.emptyList();

    return Arrays.stream(files)
            .sorted(Comparator.comparingLong(File::lastModified).reversed())
            .limit(n)
            .collect(Collectors.toList());
  }
}
