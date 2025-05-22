package edu.ntnu.idi.idatt.view.common.intro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>IntroScreenView</h1>
 *
 * JavaFX screen for the intro interface of the application.
 * Provides users with options to either start a new game or load a previously saved game.
 */
public class IntroScreenView implements IntroView {

  private Runnable startGameListener;
  private Runnable loadGameListener;

  private static final Logger logger = LoggerFactory.getLogger(IntroScreenView.class);
  private Parent root;

  /**
   * <h2>Constructor</h2>
   * Initializes an empty intro screen view.
   */
  public IntroScreenView() {}

  /**
   * <h2>initializeUI</h2>
   * Builds and prepares the UI layout for the intro screen.
   */
  public void initializeUI() {
    createUI();
  }

  /**
   * <h2>createUI</h2>
   * Constructs the main UI layout including background and button setup.
   */
  protected void createUI() {
    BorderPane mainContainer = new BorderPane();

    BackgroundImage bgImage = new BackgroundImage(
            new Image(getClass().getResource("/home_screen/snakesandladders.png").toExternalForm(),
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
    root = mainContainer;
  }

  /**
   * <h2>createGameSelectionBox</h2>
   * Creates the layout and logic for "New Game" and "Load Game" buttons.
   *
   * @return VBox containing the game option buttons
   */
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

  /**
   * <h2>styleGameButton</h2>
   * Applies custom styles and hover effects to the provided button.
   *
   * @param button     The button to style
   * @param baseColor  Base color in normal state
   * @param hoverColor Background color when hovered
   */
  private void styleGameButton(Button button, String baseColor, String hoverColor) {
    String baseStyle = "-fx-font-size: 20px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-background-color: " + baseColor + ";" +
            "-fx-background-radius: 20;" +
            "-fx-padding: 12px 28px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 4, 0.3, 0, 2);";

    String hoverStyle = baseStyle.replace(baseColor, hoverColor)
            .replace("rgba(0,0,0,0.4), 4", "rgba(0,0,0,0.5), 6");

    button.setStyle(baseStyle);
    button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
    button.setOnMouseExited(e -> button.setStyle(baseStyle));
  }

  /**
   * <h2>getRoot</h2>
   * Retrieves the root node of the intro screen UI.
   *
   * @return The root Parent node
   */
  @Override
  public Parent getRoot() {
    return root;
  }

  /**
   * <h2>setStartGameListener</h2>
   * Sets the action listener for when the "New Game" button is pressed.
   *
   * @param listener Runnable to execute on click
   */
  @Override
  public void setStartGameListener(Runnable listener) {
    this.startGameListener = listener;
    logger.info("Start game listener set");
  }

  /**
   * <h2>setLoadGameListener</h2>
   * Sets the action listener for when the "Load Game" button is pressed.
   *
   * @param listener Runnable to execute on click
   */
  @Override
  public void setLoadGameListener(Runnable listener) {
    this.loadGameListener = listener;
  }
}