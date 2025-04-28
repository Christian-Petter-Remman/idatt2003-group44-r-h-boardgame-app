//package edu.ntnu.idi.idatt.view.common;
//
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.io.File;
//import java.util.List;
//
//import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;
//import static edu.ntnu.idi.idatt.view.common.LoadController.getLastNPlayerFiles;
//
//public class LoadScreenView {
//
//  private final Stage stage;
//  private final Logger logger = LoggerFactory.getLogger(LoadScreenView.class);
//  private final LoadController controller;
//
//  public LoadScreenView(Stage stage) {
//    this.stage = stage;
//    this.controller = new LoadController(stage);
//  }
//
//  public void show() {
//    try {
//      VBox layout = new VBox(20,
//              createTitle(),
//              createGridOfSlots()
//      );
//      layout.setAlignment(Pos.TOP_CENTER);
//      layout.setPadding(new Insets(40));
//
//      Scene scene = new Scene(layout, 800, 600);
//      stage.setScene(scene);
//      stage.setTitle("Load Game");
//
//      logger.info("Load screen displayed successfully.");
//    } catch (Exception e) {
//      logger.error("Failed to load LoadScreenView: {}", e.getMessage());
//    }
//  }
//
//  private Label createTitle() {
//    Label title = new Label("Load Saved Game");
//    title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
//    return title;
//  }
//
//  private GridPane createGridOfSlots() {
//    GridPane grid = new GridPane();
//    grid.setHgap(20);
//    grid.setVgap(20);
//    grid.setPadding(new Insets(20));
//    grid.setAlignment(Pos.CENTER);
//
//    List<File> latestFiles = getLastNPlayerFiles("data/user-data/player-files/", 8);
//
//    for (int i = 0; i < latestFiles.size(); i++) {
//      File file = latestFiles.get(i);
//      grid.add(createSlotBox(i, file), i % 4, i / 4);
//    }
//
//    return grid;
//  }
//
//  private VBox createSlotBox(int slotIndex, File file) {
//    VBox box = new VBox();
//    box.setPrefSize(150, 150);
//    box.setAlignment(Pos.CENTER);
//    box.setStyle("-fx-background-color: #ddd; -fx-border-color: #444;");
//
//    Label label = new Label("Save Slot " + (slotIndex + 1));
//    box.getChildren().add(label);
//    box.getChildren().add(createLoadButton(file));
//
//    return box;
//  }
//  private Button createLoadButton(File file) {
//    Button btn = new Button("Load");
//    btn.setOnAction(e -> controller.handleLoad(file.getPath()));
//    return btn;
//  }
//  }