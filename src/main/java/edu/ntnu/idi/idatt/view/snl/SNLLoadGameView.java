package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.common.load.SNLLoadGameController;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;

public class SNLLoadGameView {

  private final StackPane root;

  public SNLLoadGameView(SNLLoadGameController controller) {
    this.root = new StackPane();

    Image backgroundImage = new Image(
        Objects.requireNonNull(getClass().getResource("/home_screen/snakesandladders.png")).toExternalForm());
    BackgroundImage bgImage = new BackgroundImage(
        backgroundImage,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        new BackgroundSize(1.0, 1.0, true, true, false, false)
    );
    Region background = new Region();
    background.setBackground(new Background(bgImage));

    VBox content = new VBox(20);
    content.setAlignment(Pos.TOP_CENTER);
    content.setPadding(new Insets(100, 20, 40, 20));

    Label header = new Label("Load Previous Game");
    header.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-effect: dropshadow(gaussian, black, 4, 0.5, 0, 2);");
    content.getChildren().add(header);

    File[] recentFiles = controller.getRecentSaveFiles(8);
    for (File file : recentFiles) {
      Button fileButton = getFileButton(controller, file);
      content.getChildren().add(fileButton);
    }

    Button backButton = getBackButton();

    AnchorPane anchoredBack = new AnchorPane();
    anchoredBack.setPickOnBounds(false);
    anchoredBack.getChildren().add(backButton);
    AnchorPane.setLeftAnchor(backButton, 24.0);
    AnchorPane.setBottomAnchor(backButton, 24.0);

    root.getChildren().addAll(background, content, anchoredBack);
  }

  private Button getBackButton() {
    Button backButton = new Button("⟵ Back");
    backButton.setStyle(
        "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: #5c5470;" +
            "-fx-background-radius: 18;" +
            "-fx-border-radius: 18;" +
            "-fx-padding: 8px 30px;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: #c9a7e1;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, #000, 2, 0.1, 0, 2);"
    );
    backButton.setOnMouseEntered(e -> backButton.setStyle(
        "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #44276a;" +
            "-fx-background-color: #c9a7e1;" +
            "-fx-background-radius: 18;" +
            "-fx-border-radius: 18;" +
            "-fx-padding: 8px 30px;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: #44276a;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, #000, 2, 0.1, 0, 2);"
    ));
    backButton.setOnMouseExited(e -> backButton.setStyle(
        "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: #5c5470;" +
            "-fx-background-radius: 18;" +
            "-fx-border-radius: 18;" +
            "-fx-padding: 8px 30px;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: #c9a7e1;" +
            "-fx-border-width: 2;" +
            "-fx-effect: dropshadow(gaussian, #000, 2, 0.1, 0, 2);"
    ));
    backButton.setOnAction(e -> NavigationManager.getInstance().navigateBack());
    return backButton;
  }

  private Button getFileButton(SNLLoadGameController controller, File file) {
    String displayName = file.getName().replaceFirst("\\.csv$", "");
    Button fileButton = new Button(displayName);
    fileButton.setPrefWidth(320);
    fileButton.setStyle(
        "-fx-font-size: 18px;" +
            "-fx-background-color: #ffffff;" +
            "-fx-text-fill: #000000;" +
            "-fx-background-radius: 25;" +
            "-fx-padding: 10 20;" +
            "-fx-cursor: hand;"
    );

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
    return fileButton;
  }

  public Parent getRoot() {
    return root;
  }
}
