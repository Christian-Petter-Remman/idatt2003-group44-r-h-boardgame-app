package edu.ntnu.idi.idatt.view.common.intro;

import javafx.scene.Scene;

import edu.ntnu.idi.idatt.controller.common.StartScreenController;
import edu.ntnu.idi.idatt.model.common.intro.StartScreenModel;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.DialogConfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Objects;

public class StartScreenView implements PropertyChangeListener {

  private final StackPane root = new StackPane();
  private final ImageView backgroundImage;
  private final StartScreenModel model;
  private final StartScreenController controller;

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

    model = new StartScreenModel();
    controller = new StartScreenController(model);
    model.addListener(this);


    root.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/css/StartScreenStyleSheet.css"))
            .toExternalForm()
    );

    backgroundImage = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/home_screen/farm.jpg"))
    ));

    initializeUI();
    loadIconsFromModel();
  }

  private void initializeUI() {
    Rectangle2D bounds = Screen.getPrimary().getBounds();
    backgroundImage.setFitWidth(bounds.getWidth());
    backgroundImage.setFitHeight(bounds.getHeight());
    backgroundImage.setPreserveRatio(true);
    backgroundImage.setSmooth(true);
    root.getChildren().add(backgroundImage);
  }

  private void loadIconsFromModel() {
    root.getChildren().removeIf(node -> node instanceof ImageView && node != backgroundImage);

    Map<String, DialogConfig> dialogs = model.getDialogs();
    dialogs.forEach((id, cfg) -> {
      ImageView iv = new ImageView(new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(cfg.getImagePath()))
      ));
      iv.setFitWidth(cfg.getId().equals("cow") ? 200 : 100);
      iv.setPreserveRatio(true);
      iv.setPickOnBounds(true);
      iv.getStyleClass().add("clickable-image");
      iv.setId(id);
      positionIcon(id, iv);
      iv.setOnMouseClicked(this::handleClick);
      root.getChildren().add(iv);
    });

    ImageView fredrik = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/home_screen/farmerfredrik.png"))
    ));
    fredrik.setFitWidth(300);
    fredrik.setPreserveRatio(true);
    StackPane.setAlignment(fredrik, Pos.BOTTOM_LEFT);
    StackPane.setMargin(fredrik, new Insets(0, 0, 20, 20));
    root.getChildren().add(fredrik);
  }

  private void handleClick(MouseEvent ev) {
    ImageView src = (ImageView) ev.getSource();
    controller.onIconClicked(src, src.getId());
  }

  private void positionIcon(String id, ImageView iv) {
    switch (id) {
      case "cow" -> { iv.setTranslateX(-50); iv.setTranslateY(30); }
      case "pig" -> { iv.setTranslateX(-250); iv.setTranslateY(200); }
      case "sheep" -> { iv.setTranslateX(-50); iv.setTranslateY(220); }
      case "duck" -> { iv.setTranslateX(470); iv.setTranslateY(150); }
      case "hen" -> { iv.setTranslateX(380); iv.setTranslateY(210); }
      case "mole" -> { iv.setTranslateX(-250); iv.setTranslateY(60); }
      case "starGame" -> { iv.setTranslateX(-140); iv.setTranslateY(-300); }
      case "paintCanvas" -> { iv.setTranslateX(300); iv.setTranslateY(90); }
      case "snakeGame" -> { iv.setTranslateX(0); iv.setTranslateY(-150); }
      case "memoryGame" -> { iv.setTranslateX(150); iv.setTranslateY(50); }
      default -> {}
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if ("dialogs".equals(evt.getPropertyName())) {
      loadIconsFromModel();
    }
  }

  public StackPane getRoot() {
    return root;
  }
}
