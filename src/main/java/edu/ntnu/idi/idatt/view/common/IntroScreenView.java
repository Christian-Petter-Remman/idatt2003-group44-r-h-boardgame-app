package edu.ntnu.idi.idatt.view.common;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.view.snakesandladders.SnakesAndLaddersCharacterSelectionView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntroScreenView {
  private final Stage primaryStage;
  private final Logger logger = LoggerFactory.getLogger(IntroScreenView.class);

  public IntroScreenView(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void prepareScene() {
      Label titleLabel = new Label("The BoardGame App");
      titleLabel.setFont(Font.font("Century Gothic", 32));
      titleLabel.setAlignment(Pos.CENTER);

      Image image1 = new Image("images/snakesnladders.png");
      HBox imageBox = getHBox(image1);

      VBox root = new VBox(20, titleLabel, imageBox);
      root.setAlignment(Pos.CENTER);
      root.setPadding(new Insets(10));

      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.setTitle("BoardGame App");

      primaryStage.setFullScreenExitHint("");
      primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }

  public void setGame(SnakesAndLadders game) {
  }

  private HBox getHBox(Image image1) {
    VBox box1 = createGameBox(image1, () -> startSnakesAndLadders());

    Image image2 = new Image("images/black.png");
    VBox box2 = createGameBox(image2, () -> {
      showAlert("Coming Soon", "This game will be available soon.");
    });

    Image image3 = new Image("images/black.png");
    VBox box3 = createGameBox(image3, () -> {
      showAlert("Coming Soon", "This game will be available soon.");
    });

    HBox imageBox = new HBox(20, box1, box2, box3);
    imageBox.setAlignment(Pos.CENTER);
    imageBox.setPadding(new Insets(10));
    return imageBox;
  }

  private VBox createGameBox(Image image, Runnable onClickAction) {
    ImageView imageView = new ImageView(image);
    imageView.setFitHeight(150);
    imageView.setFitWidth(200);
    imageView.setPreserveRatio(false);
    imageView.setOnMouseClicked(e -> onClickAction.run());

    javafx.scene.control.Button loadButton = new javafx.scene.control.Button("Load Game");
    loadButton.setOnAction(e -> onClickAction.run());
    loadButton.setStyle("-fx-font-size: 14px; -fx-padding: 8 16;");

    VBox gameBox = new VBox(10, imageView, loadButton);
    gameBox.setAlignment(Pos.CENTER);
    return gameBox;
  }

  public void startSnakesAndLadders() {
    try {
      SnakesAndLaddersCharacterSelectionView characterSelectionView = new SnakesAndLaddersCharacterSelectionView(primaryStage);
      characterSelectionView.show();
    } catch (Exception e) {
      logger.error("Error loading Character Selection Screen: {}", e.getMessage());
      showAlert("Error", "An error occurred while loading the Character Selection Screen for Snakes And Ladders");
    }
  }
}
