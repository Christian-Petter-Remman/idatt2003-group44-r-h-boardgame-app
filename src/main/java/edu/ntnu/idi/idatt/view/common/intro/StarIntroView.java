package edu.ntnu.idi.idatt.view.common.intro;

import edu.ntnu.idi.idatt.navigation.NavigationManager;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>StarIntroView</h1>
 *
 * This class represents the introduction screen for the Star game.
 * It provides buttons for starting a new game or loading a previously saved game.
 * It implements {@link IntroView} and defines the visual layout and button behaviors.
 */
public class StarIntroView implements IntroView {
  private Runnable startGameListener;
  private Runnable loadGameListener;

  Logger logger = LoggerFactory.getLogger(SNLIntroView.class);

  private Parent root;

  /**
   * <h2>Constructor</h2>
   *
   * Initializes an empty instance. The UI is not built until {@code initializeUI()} is called.
   */
  public StarIntroView() {}

  /**
   * <h2>initializeUI</h2>
   *
   * Triggers creation of the view's layout and graphical elements.
   */
  public void initializeUI() {
    createUI();
  }

  /**
   * <h2>createUI</h2>
   *
   * Constructs the main layout for the intro screen, sets background, and adds game selection buttons.
   */
  protected void createUI() {
    BorderPane mainContainer = new BorderPane();

    BackgroundImage bgImage = new BackgroundImage(
        new Image(Objects.requireNonNull(getClass().getResource("/home_screen/stargame1.png")).toExternalForm(),
            0, 0, true, true),
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        new BackgroundSize(1.0, 1.0, true, true, false, false)
    );
    mainContainer.setBackground(new Background(bgImage));

    VBox content = new VBox(20, createGameSelectionBox());
    content.setAlignment(Pos.CENTER);
    content.setPadding(new Insets(10));
    mainContainer.setCenter(content);

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

    HBox backBox = new HBox(backButton);
    backBox.setAlignment(Pos.BOTTOM_LEFT);
    backBox.setPadding(new Insets(0, 0, 28, 28));

    mainContainer.setBottom(backBox);

    root = mainContainer;
  }

  /**
   * <h2>getRoot</h2>
   *
   * @return the root JavaFX node representing the view.
   */
  @Override
  public Parent getRoot() {
    return root;
  }

  /**
   * <h2>createGameSelectionBox</h2>
   *
   * Builds the vertical layout box containing "New Game" and "Load Game" buttons with action listeners.
   *
   * @return a {@link VBox} containing the game option buttons
   */
  private VBox createGameSelectionBox() {
    Button newGameButton = new Button("New Game");
    styleGameButton(newGameButton, "#ffca28", "#ff9800");

    Button loadGameButton = new Button("Load Game");
    styleGameButton(loadGameButton, "#81d4fa", "#039be5");

    newGameButton.setOnAction(e -> {
      if (startGameListener != null) {
        logger.info("Start game listener triggered.");
        startGameListener.run();
      } else {
        logger.warn("Start Game listener is not set.");
      }
    });

    loadGameButton.setOnAction(e -> {
      if (loadGameListener != null) {
        logger.info("Load game listener triggered.");
        loadGameListener.run();
      } else {
        logger.warn("Load Game listener is not set.");
      }
    });

    VBox box = new VBox(20, newGameButton, loadGameButton);
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(20));
    return box;
  }

  /**
   * <h2>styleGameButton</h2>
   *
   * Applies visual styles and hover behavior to game option buttons.
   *
   * @param button     the button to style
   * @param baseColor  background color when idle
   * @param hoverColor background color when hovered
   */
  private void styleGameButton(Button button, String baseColor, String hoverColor) {
    button.setStyle(
        "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: " + baseColor + ";" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 12px 28px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 4, 0.3, 0, 2);"
    );

    button.setOnMouseEntered(e ->
        button.setStyle(
            "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: " + hoverColor + ";" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 12px 28px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 6, 0.3, 0, 3);"
        )
    );

    button.setOnMouseExited(e ->
        button.setStyle(
            "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: " + baseColor + ";" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 12px 28px;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 4, 0.3, 0, 2);"
        )
    );
  }

  /**
   * <h2>setStartGameListener</h2>
   *
   * Assigns a listener that will be triggered when the user clicks "New Game".
   *
   * @param listener a {@link Runnable} to execute on new game start
   */
  @Override
  public void setStartGameListener(Runnable listener) {
    this.startGameListener = listener;
    logger.info("Start game listener set");
  }

  /**
   * <h2>setLoadGameListener</h2>
   *
   * Assigns a listener that will be triggered when the user clicks "Load Game".
   *
   * @param listener a {@link Runnable} to execute on load game action
   */
  @Override
  public void setLoadGameListener(Runnable listener) {
    this.loadGameListener = listener;
  }
}