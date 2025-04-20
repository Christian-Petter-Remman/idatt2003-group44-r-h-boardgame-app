package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.filehandling.BoardJsonHandler;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

public class LoadScreenView {

  private final Stage stage;
  private final Logger logger = LoggerFactory.getLogger(LoadScreenView.class);
  SnakesAndLadders snakeGame;

  public LoadScreenView(Stage stage) {
    this.stage = stage;
  }

  public void show() {
    try {
      VBox layout = new VBox(20,
              createTitle(),
              createGridOfSlots()
      );
      layout.setAlignment(Pos.TOP_CENTER);
      layout.setPadding(new Insets(40));

      Scene scene = new Scene(layout, 800, 600);
      stage.setScene(scene);
      stage.setTitle("Load Game");

      logger.info("Load screen displayed successfully.");
    } catch (Exception e) {
      logger.error("Failed to load LoadScreenView: {}", e.getMessage());
    }
  }

  private Label createTitle() {
    Label title = new Label("Load Saved Game");
    title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
    return title;
  }

  private GridPane createGridOfSlots() {
    GridPane grid = new GridPane();
    grid.setHgap(20);
    grid.setVgap(20);
    grid.setPadding(new Insets(20));
    grid.setAlignment(Pos.CENTER);

    for (int i = 0; i < 8; i++) {
      grid.add(createSlotBox(i), i % 4, i / 4);
    }

    return grid;
  }

  private VBox createSlotBox(int slotIndex) {
    VBox box = new VBox();
    box.setPrefSize(150, 150);
    box.setAlignment(Pos.CENTER);
    box.setStyle("-fx-background-color: #ddd; -fx-border-color: #444;");

    Label label = new Label("Save Slot " + (slotIndex + 1));
    box.getChildren().add(label);

    if (slotIndex == 0) {
      box.getChildren().add(createLoadButton());
    }

    return box;
  }

  private Button createLoadButton() {
    Button btn = new Button("Load");
    btn.setOnAction(e -> handleLoad());
    return btn;
  }

  private void handleLoad() {
    String csvPath = "data/user-data/player-files/SNL_20250418_1744983119000.csv";
    BoardJsonHandler handler = new BoardJsonHandler();
    String boardPath = extractBoardPathFromCsv(csvPath);

    if (boardPath == null) {
      showAlert("Error", "Failed to read board path from CSV.");
      return;
    }

    try {
      snakeGame = handler.loadGameFromFile(boardPath, SnakesAndLadders::new);
      int players = snakeGame.loadPlayersFromCsv(csvPath);
      logger.info("Loaded {} players from {}", players, csvPath);
      new GameScreenView(stage, snakeGame, boardPath, csvPath).show();
    } catch (FileReadException | JsonParsingException ex) {
      logger.error("Failed to load game: {}", ex.getMessage());
      showAlert("Load Error", "Could not load saved game: " + ex.getMessage());
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  public String extractBoardPathFromCsv(String csvPath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
      String firstLine = reader.readLine();
      if (firstLine != null && firstLine.startsWith("Board:")) {
        return firstLine.replace("Board:", "").trim();
      } else {
        logger.warn("No board path found in CSV file: {}", csvPath);
      }
    } catch (IOException e) {
      logger.error("Error reading CSV file {}: {}", csvPath, e.getMessage());
    }
    return null;
  }
}