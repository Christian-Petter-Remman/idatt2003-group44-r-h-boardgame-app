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
import java.util.Arrays;

/**
 * <h1>SNLLoadGameView</h1>
 *
 * JavaFX view for loading previous save files in the Snakes and Ladders game.
 * Displays recent save files as clickable buttons and allows navigation back to the previous screen.
 */
public class SNLLoadGameView {

  private final SNLLoadGameController controller;
  private final StackPane root;

  /**
   * <h2>Constructor</h2>
   * Initializes the view with background, list of save files, and a back button.
   *
   * @param controller the controller responsible for loading saved SNL games
   */
  public SNLLoadGameView(SNLLoadGameController controller) {
    this.controller = controller;
    this.root = new StackPane();

    setupBackground();
    VBox content = setupContent();
    AnchorPane anchoredBack = setupBackButton();

    root.getChildren().addAll(content, anchoredBack);
  }

  /**
   * <h2>setupBackground</h2>
   * Configures the background image for the view.
   */
  private void setupBackground() {
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
    root.getChildren().add(background);
  }

  /**
   * <h2>setupContent</h2>
   * Builds the list of recent save files as buttons and the view title.
   *
   * @return a VBox containing the main content layout
   */
  private VBox setupContent() {
    VBox content = new VBox(20);
    content.setAlignment(Pos.TOP_CENTER);
    content.setPadding(new Insets(100, 20, 40, 20));

    Label header = new Label("Load Previous Game");
    header.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-effect: dropshadow(gaussian, black, 4, 0.5, 0, 2);");
    content.getChildren().add(header);

    File[] recentFiles = controller.getRecentSaveFiles(8);
    Arrays.stream(recentFiles).forEach(file -> content.getChildren().add(createFileButton(file)));

    return content;
  }

  /**
   * <h2>createFileButton</h2>
   * Creates a styled button for a given save file.
   *
   * @param file the save file
   * @return the button to load that file
   */
  private Button createFileButton(File file) {
    String displayName = file.getName().replaceFirst("\\.csv$", "");
    Button fileButton = new Button(displayName);
    fileButton.setPrefWidth(320);
    fileButton.setStyle(baseStyle());

    fileButton.setOnMouseEntered(e -> fileButton.setStyle(hoverStyle()));
    fileButton.setOnMouseExited(e -> fileButton.setStyle(baseStyle()));
    fileButton.setOnAction(e -> controller.loadSNLGame(file));

    return fileButton;
  }

  /**
   * <h2>setupBackButton</h2>
   * Creates the "Back" button and places it anchored in the bottom-left corner.
   *
   * @return an AnchorPane containing the back button
   */
  private AnchorPane setupBackButton() {
    Button backButton = new Button("← Back");
    backButton.setStyle(backButtonStyle());
    backButton.setOnMouseEntered(e -> backButton.setStyle(backButtonHoverStyle()));
    backButton.setOnMouseExited(e -> backButton.setStyle(backButtonStyle()));
    backButton.setOnAction(e -> controller.navigateBack());

    AnchorPane anchor = new AnchorPane();
    anchor.setPickOnBounds(false);
    anchor.getChildren().add(backButton);
    AnchorPane.setLeftAnchor(backButton, 20.0);
    AnchorPane.setBottomAnchor(backButton, 20.0);

    return anchor;
  }

  /**
   * <h2>baseStyle</h2>
   * Returns base CSS style string for file buttons.
   *
   * @return CSS string
   */
  private String baseStyle() {
    return "-fx-font-size: 18px;" +
            "-fx-background-color: #ffffff;" +
            "-fx-text-fill: #000000;" +
            "-fx-background-radius: 25;" +
            "-fx-padding: 10 20;" +
            "-fx-cursor: hand;";
  }

  /**
   * <h2>hoverStyle</h2>
   * Returns hover CSS style string for file buttons.
   *
   * @return CSS string
   */
  private String hoverStyle() {
    return "-fx-font-size: 18px;" +
            "-fx-background-color: #ffcc00;" +
            "-fx-text-fill: #000000;" +
            "-fx-background-radius: 25;" +
            "-fx-padding: 10 20;" +
            "-fx-cursor: hand;";
  }

  /**
   * <h2>backButtonStyle</h2>
   * Returns base CSS style string for the back button.
   *
   * @return CSS string
   */
  private String backButtonStyle() {
    return "-fx-font-size: 16px;" +
            "-fx-background-color: #dddddd;" +
            "-fx-text-fill: black;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 16;" +
            "-fx-cursor: hand;";
  }

  /**
   * <h2>backButtonHoverStyle</h2>
   * Returns hover CSS style string for the back button.
   *
   * @return CSS string
   */
  private String backButtonHoverStyle() {
    return "-fx-font-size: 16px;" +
            "-fx-background-color: #bbbbbb;" +
            "-fx-text-fill: black;" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 8 16;" +
            "-fx-cursor: hand;";
  }

  /**
   * <h2>getRoot</h2>
   * Returns the root node of the view.
   *
   * @return a JavaFX Parent node
   */
  public Parent getRoot() {
    return root;
  }
}