package edu.ntnu.idi.idatt.view.common.intro;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StartScreenView {

  private final StackPane root;
  private final ImageView backgroundImage;

  private ImageView cowImage;
  private ImageView pigImage;
  private ImageView sheepImage;
  private ImageView duckImage;
  private ImageView starImage;
  private ImageView moleImage;
  private ImageView paintImage;
  private ImageView SNLImage;

  public StartScreenView() {
    root = new StackPane();
    backgroundImage = new ImageView(new Image(getClass().getResourceAsStream(
        "/home_screen/farmy.jpg")));
    initialize();
  }

  private void initialize() {
    // Get screen size dynamically
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double screenHeight = Screen.getPrimary().getBounds().getHeight();


    backgroundImage.setFitWidth(screenWidth);
    backgroundImage.setFitHeight(screenHeight);
    backgroundImage.setPreserveRatio(true);
    backgroundImage.setSmooth(true);


    cowImage = createClickableImage("/home_screen/cow.png", 200, 200);
    pigImage = createClickableImage("/home_screen/pig.png", 100, 100);
    sheepImage = createClickableImage("/home_screen/sheep.png", 100, 100);
//    duckImage = createClickableImage("/HomeScreen/duck.png", 100, 100);
    starImage = createClickableImage("/home_screen/star.png", 80, 80);
    moleImage = createClickableImage("/home_screen/mole.png", 80, 80);
    paintImage = createClickableImage("/home_screen/paint.png", 80, 80);
    SNLImage = createClickableImage("/home_screen/snake.png", 80, 80);


    positionIcons();
    setupHandlers();

    // Add everything to root
    root.getChildren().addAll(backgroundImage, cowImage, pigImage, sheepImage, starImage, moleImage, paintImage, SNLImage);
  }

  private void positionIcons() {
    cowImage.setTranslateX(-400);
    cowImage.setTranslateY(200);

    pigImage.setTranslateX(-250);
    pigImage.setTranslateY(300);

    sheepImage.setTranslateX(100);
    sheepImage.setTranslateY(200);

//    duckImage.setTranslateX(350);
//    duckImage.setTranslateY(300);

    starImage.setTranslateX(0);
    starImage.setTranslateY(-250);

    moleImage.setTranslateX(-150);
    moleImage.setTranslateY(150);

    paintImage.setTranslateX(250);
    paintImage.setTranslateY(250);

    SNLImage.setTranslateX(0);
    SNLImage.setTranslateY(-150);
  }

  private void setupHandlers() {
    cowImage.setOnMouseClicked(this::onCowClicked);
    pigImage.setOnMouseClicked(this::onPigClicked);
    sheepImage.setOnMouseClicked(this::onSheepClicked);

    starImage.setOnMouseClicked(this::onStarClicked);
    moleImage.setOnMouseClicked(this::onMoleClicked);
    paintImage.setOnMouseClicked(this::onPaintClicked);
  }

  private ImageView createClickableImage(String resourcePath, double width, double height) {
    Image image = new Image(getClass().getResourceAsStream(resourcePath));
    ImageView imageView = new ImageView(image);
    imageView.setFitWidth(width);
    imageView.setFitHeight(height);
    imageView.setPreserveRatio(true);
    imageView.setPickOnBounds(true);
    return imageView;
  }

  private void onCowClicked(MouseEvent event) {
    System.out.println("Cow clicked!");
  }

  private void onPigClicked(MouseEvent event) {
    System.out.println("Pig clicked!");
  }

  private void onSheepClicked(MouseEvent event) {
    System.out.println("Sheep clicked!");
  }

  private void onDuckClicked(MouseEvent event) {
    System.out.println("Duck clicked!");
  }

  private void onStarClicked(MouseEvent event) {
    System.out.println("Star clicked - Start Star Game!");
  }

  private void onMoleClicked(MouseEvent event) {
    System.out.println("Mole clicked!");
  }

  private void onPaintClicked(MouseEvent event) {
    System.out.println("Paint clicked!");
  }

  public StackPane getRoot() {
    return root;
  }

  public static class TestApp extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) {
      StartScreenView startScreenView = new StartScreenView();
      Scene scene = new Scene(startScreenView.getRoot(), 800, 600, Color.LIGHTBLUE);

      primaryStage.setTitle("Farm Home Screen");
      primaryStage.setScene(scene);
      primaryStage.show();
    }

    public static void main(String[] args) {
      launch(args);
    }
  }
}
