package edu.ntnu.idi.idatt.view.common;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.model.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.view.snakesandladders.SnakesAndLaddersCharacterSelectionView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    HBox imageBox = getHBox();

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
    // not needed right now
  }

  private HBox getHBox() {
    VBox box1 = createGameBox(
            new Image("images/snakesnladders.png"),
            this::startSnakesAndLadders,
            this::openLoadScreen
    );

    VBox box2 = createGameBox(
            new Image("images/black.png"),
            () -> showAlert("Coming Soon", "This game will be available soon."),
            () -> showAlert("Coming Soon", "Loading is not yet available.")
    );

    VBox box3 = createGameBox(
            new Image("images/black.png"),
            () -> showAlert("Coming Soon", "This game will be available soon."),
            () -> showAlert("Coming Soon", "Loading is not yet available.")
    );

    HBox imageBox = new HBox(20, box1, box2, box3);
    imageBox.setAlignment(Pos.CENTER);
    imageBox.setPadding(new Insets(10));
    return imageBox;
  }

  private VBox createGameBox(Image image, Runnable onImageClick, Runnable onLoadClick) {
    ImageView imageView = new ImageView(image);
    imageView.setFitHeight(150);
    imageView.setFitWidth(200);
    imageView.setPreserveRatio(false);
    imageView.setOnMouseClicked(e -> onImageClick.run());

    Button loadButton = new Button("Load Game");
    loadButton.setOnAction(e -> onLoadClick.run());
    loadButton.setStyle("-fx-font-size: 14px; -fx-padding: 8 16;");

    VBox gameBox = new VBox(10, imageView, loadButton);
    gameBox.setAlignment(Pos.CENTER);
    return gameBox;
  }

  public void startSnakesAndLadders() {
    try {
      SnakesAndLaddersCharacterSelectionView characterSelectionView =
              new SnakesAndLaddersCharacterSelectionView(primaryStage);
      characterSelectionView.show();
    } catch (Exception e) {
      logger.error("Error loading Character Selection Screen: {}", e.getMessage());
      showAlert("Error", "An error occurred while loading the Character Selection Screen for Snakes And Ladders");
    }
  }

  public void openLoadScreen() {
    try {
      LoadScreenView loadScreenView = new LoadScreenView(primaryStage);
      loadScreenView.show();
    } catch (Exception e) {
      logger.error("Error loading Load Screen: {}", e.getMessage());
      showAlert("Error", "An error occurred while opening the Load Screen");
    }
  }
}