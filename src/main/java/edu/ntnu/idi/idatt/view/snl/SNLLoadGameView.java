package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.common.load.SNLLoadGameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;

public class SNLLoadGameView {

  private final SNLLoadGameController controller;
  private final StackPane root;

  public SNLLoadGameView(SNLLoadGameController controller) {
    this.controller = controller;
    this.root = new StackPane();

    // 🖼️ Background image
    Image backgroundImage = new Image(getClass().getResource("/home_screen/snakesandladders.png").toExternalForm());
    BackgroundImage bgImage = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(1.0, 1.0, true, true, false, false)
    );
    Region background = new Region();
    background.setBackground(new Background(bgImage));

    // 📦 Foreground content
    VBox content = new VBox(20);
    content.setAlignment(Pos.TOP_CENTER);
    content.setPadding(new Insets(100, 20, 40, 20));

    Label header = new Label("Load Previous Game");
    header.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-effect: dropshadow(gaussian, black, 4, 0.5, 0, 2);");
    content.getChildren().add(header);

    File[] recentFiles = controller.getRecentSaveFiles(8);
    for (File file : recentFiles) {
      Button fileButton = new Button(file.getName());
      fileButton.setPrefWidth(320);
      fileButton.setStyle(
              "-fx-font-size: 18px;" +
                      "-fx-background-color: #ffffff;" +
                      "-fx-text-fill: #000000;" +
                      "-fx-background-radius: 25;" +
                      "-fx-padding: 10 20;" +
                      "-fx-cursor: hand;"
      );

      // Hover effect
      fileButton.setOnMouseEntered(e -> fileButton.setStyle(
              "-fx-font-size: 18px;" +
                      "-fx-background-color: #ffcc00;" +
                      "-fx-text-fill: #000000;" +
                      "-fx-background-radius: 25;" +
                      "-fx-padding: 10 20;" +
                      "-fx-cursor: hand;"
      ));
      fileButton.setOnMouseExited(e -> fileButton.setStyle(
              "-fx-font-size: 18px;" +
                      "-fx-background-color: #ffffff;" +
                      "-fx-text-fill: #000000;" +
                      "-fx-background-radius: 25;" +
                      "-fx-padding: 10 20;" +
                      "-fx-cursor: hand;"
      ));

      fileButton.setOnAction(e -> controller.loadSNLGame(file));
      content.getChildren().add(fileButton);
    }

    // ⬅️ Back button pinned bottom-left
    Button backButton = new Button("← Back");
    backButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #dddddd;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-radius: 20;" +
                    "-fx-padding: 8 16;" +
                    "-fx-cursor: hand;"
    );
    backButton.setOnMouseEntered(e -> backButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #bbbbbb;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-radius: 20;" +
                    "-fx-padding: 8 16;" +
                    "-fx-cursor: hand;"
    ));
    backButton.setOnMouseExited(e -> backButton.setStyle(
            "-fx-font-size: 16px;" +
                    "-fx-background-color: #dddddd;" +
                    "-fx-text-fill: black;" +
                    "-fx-background-radius: 20;" +
                    "-fx-padding: 8 16;" +
                    "-fx-cursor: hand;"
    ));
    backButton.setOnAction(e -> controller.navigateBack());

    AnchorPane anchoredBack = new AnchorPane();
    anchoredBack.setPickOnBounds(false);
    anchoredBack.getChildren().add(backButton);
    AnchorPane.setLeftAnchor(backButton, 20.0);
    AnchorPane.setBottomAnchor(backButton, 20.0);

    root.getChildren().addAll(background, content, anchoredBack);
  }

  public Parent getRoot() {
    return root;
  }
}