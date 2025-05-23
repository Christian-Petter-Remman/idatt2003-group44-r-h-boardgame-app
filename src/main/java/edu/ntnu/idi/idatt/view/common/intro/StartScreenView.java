package edu.ntnu.idi.idatt.view.common.intro;

import edu.ntnu.idi.idatt.controller.common.StartScreenController;
import edu.ntnu.idi.idatt.model.common.intro.StartScreenModel;
import edu.ntnu.idi.idatt.model.common.DialogConfig;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;

/**
 * <h1>StartScreenView</h1>
 *
 * <p>JavaFX view for the animated and interactive start screen of the application. Displays
 * clickable
 * animal icons that trigger learning dialogs or game navigation. Reacts to model updates and
 * delegates click behavior to the controller.
 *
 * <P>AI: active involvement as sparring partner and created underlying frame for view development.
 * </P>
 */
public class StartScreenView implements PropertyChangeListener {

  private final StackPane root = new StackPane();
  private final ImageView backgroundImage;
  private final StartScreenModel model;
  private final StartScreenController controller;

  /**
   * <h2>Constructor</h2>
   *
   * <p>Sets up the model, controller, listeners, and UI components for the start screen.
   */
  public StartScreenView() {
    this.model = new StartScreenModel();
    this.controller = new StartScreenController(model);
    model.addListener(this);

    root.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/css/StartScreenStyleSheet.css"))
            .toExternalForm());

    this.backgroundImage = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/home_screen/farm.jpg"))));

    initializeUI();
    loadIconsFromModel();
  }

  /**
   * <h2>initializeUI</h2>
   *
   * <p>Sets up the root pane with background image fitted to screen size.
   */
  private void initializeUI() {
    Rectangle2D bounds = Screen.getPrimary().getBounds();
    backgroundImage.setFitWidth(bounds.getWidth());
    backgroundImage.setFitHeight(bounds.getHeight());
    backgroundImage.setPreserveRatio(true);
    backgroundImage.setSmooth(true);
    root.getChildren().add(backgroundImage);
  }

  /**
   * <h2>loadIconsFromModel</h2>
   *
   * <p>Clears and reloads all dialog icons from the model into the view, with specific sizing and
   * click handlers per ID.
   */
  private void loadIconsFromModel() {
    root.getChildren().removeIf(node -> node instanceof ImageView && node != backgroundImage);

    Map<String, DialogConfig> dialogs = model.getDialogs();
    dialogs.forEach((id, cfg) -> {
      ImageView iv = new ImageView(new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(cfg.getImagePath()))));
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
        Objects.requireNonNull(getClass().getResourceAsStream("/home_screen/farmerfredrik.png"))));
    fredrik.setFitWidth(300);
    fredrik.setPreserveRatio(true);
    StackPane.setAlignment(fredrik, Pos.BOTTOM_LEFT);
    StackPane.setMargin(fredrik, new Insets(0, 0, 20, 20));
    root.getChildren().add(fredrik);
  }

  /**
   * <h2>handleClick</h2>
   *
   * <p>Handles user interaction with clickable icons and delegates to controller.
   *
   * @param ev Mouse event triggered by clicking an image.
   */
  private void handleClick(MouseEvent ev) {
    ImageView src = (ImageView) ev.getSource();
    controller.onIconClicked(src.getId());
  }

  /**
   * <h2>positionIcon</h2>
   *
   * <p>Sets custom X/Y coordinates for each icon based on its ID.
   *
   * @param id the string ID of the icon (e.g. "cow", "pig")
   * @param iv the ImageView to be positioned
   */
  private void positionIcon(String id, ImageView iv) {
    switch (id) {
      case "cow" -> {
        iv.setTranslateX(-50);
        iv.setTranslateY(30);
      }
      case "pig" -> {
        iv.setTranslateX(-250);
        iv.setTranslateY(200);
      }
      case "sheep" -> {
        iv.setTranslateX(-50);
        iv.setTranslateY(220);
      }
      case "duck" -> {
        iv.setTranslateX(470);
        iv.setTranslateY(150);
      }
      case "hen" -> {
        iv.setTranslateX(380);
        iv.setTranslateY(210);
      }
      case "mole" -> {
        iv.setTranslateX(-250);
        iv.setTranslateY(60);
      }
      case "starGame" -> {
        iv.setTranslateX(-140);
        iv.setTranslateY(-300);
      }
      case "paintCanvas" -> {
        iv.setTranslateX(300);
        iv.setTranslateY(90);
      }
      case "snakeGame" -> {
        iv.setTranslateX(0);
        iv.setTranslateY(-150);
      }
      case "memoryGame" -> {
        iv.setTranslateX(150);
        iv.setTranslateY(50);
      }
      default -> {
      } // No position assigned
    }
  }

  /**
   * <h2>propertyChange</h2>
   *
   * <p>Reacts to model updates and reloads icons when dialog set changes.
   *
   * @param evt the property change event
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if ("dialogs".equals(evt.getPropertyName())) {
      loadIconsFromModel();
    }
  }

  /**
   * <h2>getRoot.</h2>
   *
   * @return the root layout for this screen.
   */
  public StackPane getRoot() {
    return root;
  }
}