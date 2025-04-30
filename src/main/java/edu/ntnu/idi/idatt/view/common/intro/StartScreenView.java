package edu.ntnu.idi.idatt.view.common.intro;

import edu.ntnu.idi.idatt.filehandling.DialogJsonHandler;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.DialogConfig;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.InfoDialog;
import java.util.Map;
import java.util.Objects;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

public class StartScreenView {

  private final StackPane root;
  private final ImageView backgroundImage;

  private ImageView cowImage;
  private ImageView pigImage;
  private ImageView sheepImage;
  private ImageView duckImage;
  private ImageView henImage;
  private ImageView moleImage;
  private ImageView starImage;
  private ImageView paintImage;
  private ImageView snlImage;
  private ImageView farmerImage;

  private final Map<String, DialogConfig> dialogs;

  public StartScreenView() {
    // root pane
    root = new StackPane();

    // apply only this view’s stylesheet
    root.getStylesheets().add(
        Objects.requireNonNull(
            getClass().getResource("/css/StartScreenStyleSheet.css")
        ).toExternalForm()
    );

    // background
    backgroundImage = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/home_screen/farm.jpg"))
    ));
    dialogs = DialogJsonHandler.loadDialogs();
    initialize();
  }

  private void initialize() {
    Rectangle2D bounds = Screen.getPrimary().getBounds();
    backgroundImage.setFitWidth(bounds.getWidth());
    backgroundImage.setFitHeight(bounds.getHeight());
    backgroundImage.setPreserveRatio(true);
    backgroundImage.setSmooth(true);

    cowImage = createClickableImage("/home_screen/cow.png", 200, 200);
    pigImage = createClickableImage("/home_screen/pig.png", 120, 120);
    sheepImage = createClickableImage("/home_screen/sheep.png", 120, 120);
    duckImage = createClickableImage("/home_screen/duck.png", 100, 100);
    henImage = createClickableImage("/home_screen/hen.png", 100, 100);
    moleImage = createClickableImage("/home_screen/mole.png", 80, 80);
    starImage = createClickableImage("/home_screen/star.png", 80, 80);
    paintImage = createClickableImage("/home_screen/paint.png", 80, 80);
    snlImage = createClickableImage("/home_screen/snake.png", 80, 80);
    farmerImage = createClickableImage("/home_screen/farmer.png", 140, 140);

    positionIcons();
    setupHandlers();

    root.getChildren().addAll(
        backgroundImage,
        cowImage, pigImage, sheepImage,
        duckImage, henImage, moleImage,
        starImage, paintImage, snlImage,
        farmerImage
    );
  }

  private void positionIcons() {
    cowImage.setTranslateX(-400);
    cowImage.setTranslateY(150);
    pigImage.setTranslateX(-250);
    pigImage.setTranslateY(240);
    sheepImage.setTranslateX(-100);
    sheepImage.setTranslateY(150);
    duckImage.setTranslateX(470);
    duckImage.setTranslateY(150);
    henImage.setTranslateX(380);
    henImage.setTranslateY(210);
    moleImage.setTranslateX(-200);
    moleImage.setTranslateY(110);
    starImage.setTranslateX(-140);
    starImage.setTranslateY(-300);
    paintImage.setTranslateX(300);
    paintImage.setTranslateY(90);
    snlImage.setTranslateX(0);
    snlImage.setTranslateY(-150);
    farmerImage.setTranslateX(100);
    farmerImage.setTranslateY(50);
  }

  private void setupHandlers() {
    cowImage.setOnMouseClicked(this::onClicked);
    pigImage.setOnMouseClicked(this::onClicked);
    sheepImage.setOnMouseClicked(this::onClicked);
    duckImage.setOnMouseClicked(this::onClicked);
    henImage.setOnMouseClicked(this::onClicked);
    moleImage.setOnMouseClicked(this::onClicked);
    starImage.setOnMouseClicked(this::onClicked);
    paintImage.setOnMouseClicked(this::onClicked);
    snlImage.setOnMouseClicked(this::onClicked);
    farmerImage.setOnMouseClicked(this::onClicked);
  }

  private void onClicked(MouseEvent ev) {
    ImageView src = (ImageView) ev.getSource();
    String id = null;

    if (src == cowImage) {
      id = "cow";
    } else if (src == pigImage) {
      id = "pig";
    } else if (src == sheepImage) {
      id = "sheep";
    } else if (src == duckImage) {
      id = "duck";
    } else if (src == henImage) {
      id = "hen";
    } else if (src == moleImage) {
      id = "mole";
    } else if (src == starImage) {
      id = "starGame";
    } else if (src == paintImage) {
      id = "paintCanvas";
    } else if (src == snlImage) {
      id = "snakeGame";
    } else if (src == farmerImage) {
      id = "memoryGame";
    }

    if (id != null && dialogs.containsKey(id)) {
      new InfoDialog(dialogs.get(id)).showAndWait();
    }
  }

  private ImageView createClickableImage(String path, double w, double h) {
    ImageView iv = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream(path))
    ));
    iv.setFitWidth(w);
    iv.setFitHeight(h);
    iv.setPreserveRatio(true);
    iv.setPickOnBounds(true);
    iv.getStyleClass().add("clickable-image");
    return iv;
  }

  public StackPane getRoot() {
    return root;
  }
}
