package edu.ntnu.idi.idatt.view.common;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadScreenView {

  private final Stage stage;
  private final Logger logger = LoggerFactory.getLogger(LoadScreenView.class);
  private final LoadController controller;

  public LoadScreenView(Stage stage) {
    this.stage = stage;
    this.controller = new LoadController(stage);
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
    btn.setOnAction(e -> controller.handleLoad("data/user-data/player-files/SNL_20250418_1744983119000.csv"));
    return btn;
  }
}