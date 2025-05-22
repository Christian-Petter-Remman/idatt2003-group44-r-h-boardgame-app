package edu.ntnu.idi.idatt.view.star;

import edu.ntnu.idi.idatt.controller.common.load.StarLoadGameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.File;

/**
 * <h1>StarLoadGameView</h1>
 * JavaFX view that displays a list of recent saved games
 * for the Star game and allows the user to load one of them.
 */
public class StarLoadGameView {

  private final StarLoadGameController controller;
  private final StackPane root;

  /**
   * <h2>Constructor</h2>
   * Creates the view and sets up the background, UI layout,
   * recent save file buttons, and a back navigation button.
   *
   * @param controller the controller responsible for loading Star game saves
   */
  public StarLoadGameView(StarLoadGameController controller) {
    this.controller = controller;
    this.root = new StackPane();

    setupBackground();
    VBox content = createContent();
    AnchorPane anchoredBack = createBackButton();

    root.getChildren().addAll(new Region(), content, anchoredBack);
  }

  /**
   * <h2>setupBackground</h2>
   * Applies the background image to the root container.
   */
  private void setupBackground() {
    Image backgroundImage = new Image(
            getClass().getResource("/home_screen/stargame1.png").toExternalForm()
    );
    BackgroundImage bgImage = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(1.0, 1.0, true, true, false, false)
    );
    Region background = new Region();
    background.setBackground(new Background(bgImage));
    root.getChildren().add(background);
  }

  /**
   * <h2>createContent</h2>
   * Creates the vertical container with the header and file selection buttons.
   *
   * @return the VBox containing content
   */
  private VBox createContent() {
    VBox content = new VBox(20);
    content.setAlignment(Pos.TOP_CENTER);
    content.setPadding(new Insets(100, 20, 40, 20));

    Label header = new Label("Load Previous Game");
    header.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #ffffff; " +
            "-fx-effect: dropshadow(gaussian, black, 4, 0.5, 0, 2);");
    content.getChildren().add(header);

    File[] recentFiles = controller.getRecentSaveFiles(8);
    for (File file : recentFiles) {
      Button fileButton = createFileButton(file);
      content.getChildren().add(fileButton);
    }

    return content;
  }

  /**
   * <h2>createFileButton</h2>
   * Creates a button for a given save file.
   *
   * @param file the save file
   * @return configured button for loading that file
   */
  private Button createFileButton(File file) {
    String displayName = file.getName().replaceFirst("\\.csv$", "");
    Button fileButton = new Button(displayName);
    fileButton.setPrefWidth(320);
    styleButton(fileButton, "#ffffff");

    fileButton.setOnMouseEntered(e -> styleButton(fileButton, "#ffcc00"));
    fileButton.setOnMouseExited(e -> styleButton(fileButton, "#ffffff"));
    fileButton.setOnAction(e -> controller.loadStarGame(file));

    return fileButton;
  }

  /**
   * <h2>styleButton</h2>
   * Applies consistent styling to a given button with specified background.
   *
   * @param button the Button to style
   * @param backgroundColor the background color to apply
   */
  private void styleButton(Button button, String backgroundColor) {
    button.setStyle("-fx-font-size: 18px;" +
            "-fx-background-color: " + backgroundColor + ";" +
            "-fx-text-fill: #000000;" +
            "-fx-background-radius: 25;" +
            "-fx-padding: 10 20;" +
            "-fx-cursor: hand;");
  }

  /**
   * <h2>createBackButton</h2>
   * Creates and pins a back button to the bottom-left corner.
   *
   * @return the AnchorPane containing the back button
   */
  private AnchorPane createBackButton() {
    Button backButton = new Button("← Back");
    backButton.setStyle("-fx-font-size: 16px;" +
            "-fx-background-color: #dddddd;" +
            "-fx-text-fill: black;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 16;" +
            "-fx-cursor: hand;");
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

    AnchorPane anchor = new AnchorPane();
    anchor.setPickOnBounds(false);
    anchor.getChildren().add(backButton);
    AnchorPane.setLeftAnchor(backButton, 20.0);
    AnchorPane.setBottomAnchor(backButton, 20.0);

    return anchor;
  }

  /**
   * <h2>getRoot</h2>
   * Returns the root node of this view.
   *
   * @return root StackPane
   */
  public Parent getRoot() {
    return root;
  }
}