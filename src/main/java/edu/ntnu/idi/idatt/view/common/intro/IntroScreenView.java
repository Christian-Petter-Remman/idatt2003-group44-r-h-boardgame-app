package edu.ntnu.idi.idatt.view.common.intro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntroScreenView implements IntroView {

  private Runnable startGameListener;
  private Runnable loadGameListener;

  private static final Logger logger = LoggerFactory.getLogger(IntroScreenView.class);

  private Parent root;

  public IntroScreenView() {}

  public void initializeUI() {
    createUI();
  }

  protected void createUI() {
    BorderPane mainContainer = new BorderPane();

    // Background image
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

  private VBox createGameSelectionBox() {
    Button newGameButton = new Button("New Game");
    styleGameButton(newGameButton,"#ffca28", "#ff9800");
    newGameButton.setOnAction(e -> {
      if (startGameListener != null) {
        logger.info("Start game listener triggered.");
        startGameListener.run();
      } else {
        logger.warn("Start Game listener is not set.");
      }
    });

    Button loadGameButton = new Button("Load Game");
    styleGameButton(loadGameButton,"#81d4fa", "#039be5");
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