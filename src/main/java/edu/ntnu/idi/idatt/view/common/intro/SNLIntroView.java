package edu.ntnu.idi.idatt.view.common.intro;

import edu.ntnu.idi.idatt.navigation.NavigationManager;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>SNLIntroView</h1>
 *
 * <p>Represents the introduction view for the Snakes and Ladders game. It provides options to
 * start a new game or load an existing one.
 *
 * <P>AI: active involvement as sparring partner and created underlying frame for view development.
 * </P>
 */
public class SNLIntroView implements IntroView {

  private Runnable startGameListener;
  private Runnable loadGameListener;

  private static final Logger logger = LoggerFactory.getLogger(SNLIntroView.class);

  private Parent root;

  /**
   * <h2>Constructor</h2>
   *
   * <p>Initializes the SNLIntroView.
   */
  public SNLIntroView() {
  }

  /**
   * <h2>initializeUI</h2>
   *
   * <p>Sets up the UI components for the introduction view.
   */
  public void initializeUI() {
    createUI();
  }

  /**
   * <h2>createUI</h2>
   *
   * <p>Creates the UI components for the introduction view.
   */
  protected void createUI() {
    BorderPane mainContainer = new BorderPane();

    BackgroundImage bgImage = new BackgroundImage(
        new Image(
            Objects.requireNonNull(getClass().getResource("/home_screen/snakesandladders.png"))
                .toExternalForm(),
            0, 0, true, true),
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        new BackgroundSize(1.0, 1.0, true, true, false, false)
    );
    mainContainer.setBackground(new Background(bgImage));

    VBox gameSelectionBox = createGameSelectionBox();

    VBox content = new VBox(20, gameSelectionBox);
    content.setAlignment(Pos.CENTER);
    content.setPadding(new Insets(10));

    mainContainer.setCenter(content);

    Button backButton = new Button("⟵ Back");
    backButton.setStyle(
        "-fx-font-size: 17px;"
            + "-fx-font-weight: bold;"
            + "-fx-text-fill: white;"
            + "-fx-background-color: #5c5470;"
            + "-fx-background-radius: 18;"
            + "-fx-border-radius: 18;"
            + "-fx-padding: 8px 30px;"
            + "-fx-cursor: hand;"
            + "-fx-border-color: #c9a7e1;"
            + "-fx-border-width: 2;"
            + "-fx-effect: dropshadow(gaussian, #000, 2, 0.1, 0, 2);"
    );
    backButton.setOnMouseEntered(e -> backButton.setStyle(
        "-fx-font-size: 17px;"
            + "-fx-font-weight: bold;"
            + "-fx-text-fill: #44276a;"
            + "-fx-background-color: #c9a7e1;"
            + "-fx-background-radius: 18;"
            + "-fx-border-radius: 18;"
            + "-fx-padding: 8px 30px;"
            + "-fx-cursor: hand;"
            + "-fx-border-color: #44276a;"
            + "-fx-border-width: 2;"
            + "-fx-effect: dropshadow(gaussian, #000, 2, 0.1, 0, 2);"
    ));
    backButton.setOnMouseExited(e -> backButton.setStyle(
        "-fx-font-size: 17px;"
            + "-fx-font-weight: bold;"
            + "-fx-text-fill: white;"
            + "-fx-background-color: #5c5470;"
            + "-fx-background-radius: 18;"
            + "-fx-border-radius: 18;"
            + "-fx-padding: 8px 30px;"
            + "-fx-cursor: hand;"
            + "-fx-border-color: #c9a7e1;"
            + "-fx-border-width: 2;"
            + "-fx-effect: dropshadow(gaussian, #000, 2, 0.1, 0, 2);"
    ));
    backButton.setOnAction(e -> NavigationManager.getInstance().navigateBack());

    HBox backBox = new HBox(backButton);
    backBox.setAlignment(Pos.BOTTOM_LEFT);
    backBox.setPadding(new Insets(0, 0, 28, 28));
    mainContainer.setBottom(backBox);

    root = mainContainer;
  }

  private VBox createGameSelectionBox() {
    Button newGameButton = new Button("New Game");
    styleGameButton(newGameButton, "#ffca28", "#ff9800");
    newGameButton.setOnAction(e -> {
      if (startGameListener != null) {
        logger.info("Start game listener triggered.");
        startGameListener.run();
      } else {
        logger.warn("Start Game listener is not set.");
      }
    });

    Button loadGameButton = new Button("Load Game");
    styleGameButton(loadGameButton, "#81d4fa", "#039be5");
    loadGameButton.setOnAction(e -> {
      if (loadGameListener != null) {
        logger.info("Load game listener triggered.");
        loadGameListener.run();
      } else {
        logger.warn("Load Game listener is not set.");
      }
    });

    VBox box = new VBox(10, newGameButton, loadGameButton);
    box.setAlignment(Pos.CENTER);
    return box;
  }

  private void styleGameButton(Button button, String baseColor, String hoverColor) {
    button.setStyle(
        "-fx-font-size: 20px;"
            + "-fx-font-weight: bold;"
            + "-fx-text-fill: white;"
            + "-fx-background-color: "
            + baseColor + ";" + "-fx-background-radius: 20;"
            + "-fx-padding: 12px 28px;"
            + "-fx-cursor: hand;"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 4, 0.3, 0, 2);"
    );

    button.setOnMouseEntered(e ->
        button.setStyle(
            "-fx-font-size: 20px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: white;"
                + "-fx-background-color: " + hoverColor + ";"
                + "-fx-background-radius: 20;"
                + "-fx-padding: 12px 28px;"
                + "-fx-cursor: hand;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 6, 0.3, 0, 3);"
        )
    );

    button.setOnMouseExited(e ->
        button.setStyle(
            "-fx-font-size: 20px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: white;"
                + "-fx-background-color: " + baseColor + ";"
                + "-fx-background-radius: 20;"
                + "-fx-padding: 12px 28px;"
                + "-fx-cursor: hand;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 4, 0.3, 0, 2);"
        )
    );
  }

  @Override
  public Parent getRoot() {
    return root;
  }

  @Override
  public void setStartGameListener(Runnable listener) {
    this.startGameListener = listener;
    logger.info("Start game listener set");
  }

  @Override
  public void setLoadGameListener(Runnable listener) {
    this.loadGameListener = listener;
  }
}