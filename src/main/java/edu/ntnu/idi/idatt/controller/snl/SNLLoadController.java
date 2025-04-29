package edu.ntnu.idi.idatt.controller.snl;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.filehandling.SNLBoardJsonHandler;
import edu.ntnu.idi.idatt.model.snl.SNLGame;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.common.game.GameScreenView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

public class SNLLoadController implements NavigationHandler {

  private final Logger logger = LoggerFactory.getLogger(SNLLoadController.class);
  public SNLLoadController() {
  }

  /**
   * Handles the load action given a CSV path.
   * Opens the game screen if successful, otherwise shows an alert.
   */
//  public void handleLoad(String csvPath) {
//    SNLBoardJsonHandler handler = new SNLBoardJsonHandler();
//    String boardPath = extractBoardPathFromCsv(csvPath);
//
//    if (boardPath == null) {
//      showAlert("Error", "Failed to read board path from CSV.");
//      return;
//    }
//
//    try {
//      SNLGame snakeGame = handler.loadGameFromFile(boardPath, SNLGame::new);
//      int players = 2;
//      //TODO set dice and players from file
//      logger.info("Loaded {} players from {}", players, csvPath);
//
//      new GameScreenView(new SNLGameScreenController(snakeGame));
//
//    } catch (FileReadException | JsonParsingException ex) {
//      logger.error("Failed to load game: {}", ex.getMessage());
//      showAlert("Load Error", "Could not load saved game: " + ex.getMessage());
//    }
//  }

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

  @Override
  public void navigateTo(String destination) {  // TODO: Figure out how this method implements the navigationHandler since it loads from JSON and does not open a new screen
    switch (destination) {
      case "CHARACTER_SELECTION":
        logger.info("Navigated to Intro Screen");
        break;

      default:
        logger.warn("Unknown destination: {}", destination);
        break;
    }
  }

  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
    logger.info("Navigated back to previous screen");
  }
}
