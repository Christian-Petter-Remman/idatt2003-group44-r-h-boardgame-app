package edu.ntnu.idi.idatt.view.common.intro;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StarIntroView implements IntroView {
  private Runnable startGameListener;
  private Runnable loadGameListener;

  Logger logger = LoggerFactory.getLogger(IntroScreenView.class);

  private Parent root;

  public StarIntroView() {
  }

  public void initializeUI() {
    createUI();
  }

  protected void createUI() {
    BorderPane mainContainer = new BorderPane();

    HBox gameSelectionBox = createGameSelectionBox();

    VBox content = new VBox(20, gameSelectionBox);
    content.setAlignment(Pos.CENTER);
    content.setPadding(new Insets(10));

    mainContainer.setCenter(content);
    root = mainContainer;
  }


  @Override
  public Parent getRoot() {
    return root;
  }

  private HBox createGameSelectionBox() {
    Image image = new Image("home_screen/star.png");
    ImageView StarGame = createGameIcon(image, "STAR_GAME");

    HBox gameSelectionBox = new HBox(20, StarGame);
    gameSelectionBox.setAlignment(Pos.CENTER);
    gameSelectionBox.setPadding(new Insets(10));
    return gameSelectionBox;
  }

  private ImageView createGameIcon(Image image, String gameType) {
    ImageView imageView = new ImageView(image);
    imageView.setFitHeight(150);
    imageView.setFitWidth(200);
    imageView.setPreserveRatio(false);

    if (gameType != null) {
      logger.info("Game type: {}", gameType);
      imageView.setOnMouseClicked(e -> {
        if (startGameListener != null) {
          startGameListener.run();
          logger.info("Start game listener executed for game type: {}", gameType);
        } else {
          logger.warn("Start game listener is not set");
        }
      });
      imageView.setStyle("-fx-cursor: hand;");
    }

    return imageView;
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