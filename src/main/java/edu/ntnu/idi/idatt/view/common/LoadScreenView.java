package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.controller.common.LoadController;
import edu.ntnu.idi.idatt.view.AbstractView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

import static edu.ntnu.idi.idatt.controller.common.LoadController.getLastNPlayerFiles;

public class LoadScreenView extends AbstractView {

  private final LoadController controller;

  private Button backButton;

  public LoadScreenView(LoadController controller) {
    this.controller = controller;
  }

  @Override
  protected void createUI() {
    try {
      VBox mainContainer = new VBox(20);
      mainContainer.setAlignment(Pos.TOP_CENTER);
      mainContainer.setPadding(new Insets(40));

      // Create title
      Label title = createTitle();

      // Create grid of save slots
      GridPane grid = createGridOfSlots();

      // Create back button
      backButton = new Button("Back");
      backButton.setPrefWidth(100);

      // Add components to main container
      mainContainer.getChildren().addAll(
          title,
          grid,
          backButton
      );

      root = mainContainer;

      logger.info("Load screen UI created successfully.");
    } catch (Exception e) {
      logger.error("Failed to create LoadScreenView UI: {}", e.getMessage());
    }
  }

  @Override
  protected void setupEventHandlers() {
    backButton.setOnAction(e -> controller.navigateTo("CHARACTER_SELECTION_SCREEN"));
  }

  @Override
  protected void applyInitialUIState() {
    // No initial state to apply
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

    List<File> latestFiles = getLastNPlayerFiles("data/user-data/player-files/", 8);

    for (int i = 0; i < latestFiles.size(); i++) {
      File file = latestFiles.get(i);
      grid.add(createSlotBox(i, file), i % 4, i / 4);
    }

    return grid;
  }

  private VBox createSlotBox(int slotIndex, File file) {
    VBox box = new VBox();
    box.setPrefSize(150, 150);
    box.setAlignment(Pos.CENTER);
    box.setStyle("-fx-background-color: #ddd; -fx-border-color: #444;");

    Label label = new Label("Save Slot " + (slotIndex + 1));
    box.getChildren().add(label);
    box.getChildren().add(createLoadButton(file));

    return box;
  }

  private Button createLoadButton(File file) {
    Button btn = new Button("Load");
    btn.setOnAction(e -> controller.handleLoad(file.getPath()));
    return btn;
  }
}
