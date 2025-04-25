package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.controller.common.IntroScreenController;
import edu.ntnu.idi.idatt.view.AbstractView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntroScreenView extends AbstractView {
  private static final Logger logger = LoggerFactory.getLogger(IntroScreenView.class);

  private final IntroScreenController controller;

  public IntroScreenView() {
    this.controller = new IntroScreenController();
  }

  @Override
  protected void createUI() {
    BorderPane mainContainer = new BorderPane();

    Label titleLabel = new Label("The BoardGame App");
    titleLabel.setFont(Font.font("Century Gothic", 32));
    titleLabel.setAlignment(Pos.CENTER);

    HBox gameSelectionBox = createGameSelectionBox();

    VBox content = new VBox(20, titleLabel, gameSelectionBox);
    content.setAlignment(Pos.CENTER);
    content.setPadding(new Insets(10));

    mainContainer.setCenter(content);

    root = mainContainer;
  }

  @Override
  protected void setupEventHandlers() {
    // Event handlers are set up in the createGameIcon method
  }

  @Override
  protected void applyInitialUIState() {
    // No initial state to apply
  }

  private HBox createGameSelectionBox() {
    Image snakesAndLaddersImage = new Image("images/snakesnladders.png");
    ImageView snakesAndLaddersView = createGameIcon(snakesAndLaddersImage, "SNAKES_AND_LADDERS");

    Image placeholderImage1 = new Image("images/black.png");
    ImageView placeholderView1 = createGameIcon(placeholderImage1, null);

    Image placeholderImage2 = new Image("images/black.png");
    ImageView placeholderView2 = createGameIcon(placeholderImage2, null);

    HBox gameSelectionBox = new HBox(20, snakesAndLaddersView, placeholderView1, placeholderView2);
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
      imageView.setOnMouseClicked(e -> controller.startGame(gameType));
      imageView.setStyle("-fx-cursor: hand;");
    }

    return imageView;
  }

}
