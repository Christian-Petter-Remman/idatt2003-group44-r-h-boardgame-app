package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
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

public class IntroScreenView {
  private final Stage primaryStage;

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
    ImageView imageView1 = new ImageView(image1);
    imageView1.setFitHeight(150);
    imageView1.setFitWidth(200);
    imageView1.setPreserveRatio(false);
    imageView1.setOnMouseClicked(e -> startSnakesAndLadders());

    Image image2 = new Image("images/black.png");
    ImageView imageView2 = new ImageView(image2);
    imageView2.setFitHeight(150);
    imageView2.setFitWidth(200);
    imageView2.setPreserveRatio(false);

    Image image3 = new Image("images/black.png");
    ImageView imageView3 = new ImageView(image3);
    imageView3.setFitHeight(150);
    imageView3.setFitWidth(200);
    imageView3.setPreserveRatio(false);

    HBox imageBox = new HBox(20, imageView1, imageView2, imageView3);
    imageBox.setAlignment(Pos.CENTER);
    imageBox.setPadding(new Insets(10));
    return imageBox;
  }

  public void startSnakesAndLadders() {
    SnakesAndLaddersCharacterSelectionView characterSelectionView = new SnakesAndLaddersCharacterSelectionView(primaryStage);
    characterSelectionView.show();
  }
}
