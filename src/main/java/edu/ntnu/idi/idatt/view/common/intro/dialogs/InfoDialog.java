package edu.ntnu.idi.idatt.view.common.intro.dialogs;

import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * <h1>InfoDialog</h1>
 *
 * <p>A reusable informational modal dialog for displaying messages, fun facts, images, audio, and
 * navigation options. It is used to enhance user interaction and learning in a game or
 * application.
 */
public class InfoDialog extends Stage {

  private static final int MAX_BODY_LINES = 4;
  private static final double BODY_WRAP_WIDTH = 360;

  private MediaPlayer player;

  /**
   * <h2>Constructor</h2>
   *
   * <p>Constructs the InfoDialog with layout and functionality based on the provided configuration.
   *
   * @param cfg a configuration object with all necessary dialog content and action handlers
   */
  public InfoDialog(DialogConfig cfg) {
    initOwner(NavigationManager.getInstance().getPrimaryStage());
    initModality(Modality.WINDOW_MODAL);
    setResizable(false);
    setTitle(cfg.getTitle());
    getIcons().add(
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png"))));

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setPrefWidth(BODY_WRAP_WIDTH + 40);

    root.setTop(createTopBar(cfg));
    root.setCenter(createCenterContent(cfg));
    root.setBottom(createBottomButtons(cfg));

    Scene scene = new Scene(root);
    scene.getStylesheets().add(Objects.requireNonNull(
        getClass().getResource("/css/StartScreenStyleSheet.css")).toExternalForm());
    setScene(scene);
  }

  /**
   * <h2>createTopBar</h2>
   *
   * <p>Constructs the top bar of the dialog including title and logo.
   *
   * @param cfg configuration with title
   * @return HBox node for top section
   */
  private HBox createTopBar(DialogConfig cfg) {
    Label titleLabel = new Label(cfg.getTitle());
    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    ImageView logo = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png"))));
    logo.setFitWidth(30);
    logo.setFitHeight(30);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    HBox topBar = new HBox(titleLabel, spacer, logo);
    topBar.setAlignment(Pos.CENTER_LEFT);
    return topBar;
  }

  /**
   * <h2>createCenterContent</h2>
   *
   * <p>Builds the scrollable center content including main body, fun fact and image.
   *
   * @param cfg the dialog configuration
   * @return VBox with content
   */
  private VBox createCenterContent(DialogConfig cfg) {
    Label bodyLabel = new Label(cfg.getBody());
    bodyLabel.setWrapText(true);
    bodyLabel.setMaxWidth(BODY_WRAP_WIDTH);

    double lineHeight = estimateLineHeight(bodyLabel);
    double maxBodyHeight = lineHeight * MAX_BODY_LINES;

    ScrollPane scrollPane = new ScrollPane(bodyLabel);
    scrollPane.setFitToWidth(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scrollPane.setPrefViewportHeight(maxBodyHeight);
    scrollPane.setMinViewportHeight(maxBodyHeight);
    scrollPane.getStyleClass().add("dialog-scroll-pane");

    VBox centerBox = new VBox(10, scrollPane);
    centerBox.setPadding(new Insets(10, 0, 10, 0));

    if (cfg.getFunFact() != null) {
      Label funFact = new Label("Fun fact: " + cfg.getFunFact());
      funFact.setWrapText(true);
      funFact.setMaxWidth(BODY_WRAP_WIDTH);
      centerBox.getChildren().add(funFact);
    }

    ImageView mainImage = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream(cfg.getImagePath()))));
    mainImage.setFitWidth(150);
    mainImage.setPreserveRatio(true);
    centerBox.getChildren().add(mainImage);

    return centerBox;
  }

  /**
   * <h2>createBottomButtons</h2>
   *
   * <p>Constructs the button row including optional "Listen", "CTA", and "Close" buttons.
   *
   * @param cfg the dialog configuration
   * @return HBox containing all buttons
   */
  private HBox createBottomButtons(DialogConfig cfg) {
    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER_RIGHT);

    if (cfg.getAudio() != null) {
      Button listenButton = new Button("🔊 Listen");
      listenButton.getStyleClass().add("dialog-button");
      listenButton.setOnAction(e -> playSound(cfg.getAudio()));
      buttonBox.getChildren().add(listenButton);
    }

    if (cfg.getCtaLabel() != null) {
      Button playButton = new Button(cfg.getCtaLabel());
      playButton.getStyleClass().add("dialog-button");
      playButton.setOnAction(e -> {
        close();
        NavigationManager.getInstance().navigateTo(NavigationTarget.valueOf(cfg.getCtaAction()));
      });
      buttonBox.getChildren().add(playButton);
    }

    Button closeButton = new Button("Close");
    closeButton.getStyleClass().add("dialog-button");
    closeButton.setOnAction(e -> close());
    buttonBox.getChildren().add(closeButton);

    return buttonBox;
  }

  /**
   * <h2>playSound</h2>
   *
   * <p>Plays the sound file associated with the dialog if not already loaded.
   *
   * @param audioRes path to the sound file resource
   */
  private void playSound(String audioRes) {
    if (player == null) {
      Media media = new Media(
          Objects.requireNonNull(getClass().getResource(audioRes)).toExternalForm());
      player = new MediaPlayer(media);
    }
    player.stop();
    player.play();
  }

  /**
   * <h2>estimateLineHeight</h2>
   *
   * <p>Approximates the height of a single line of text based on font metrics.
   *
   * @param label the label to base calculation on
   * @return the estimated line height
   */
  private double estimateLineHeight(Label label) {
    Text helper = new Text("A");
    helper.setFont(label.getFont());
    helper.applyCss();
    return helper.getLayoutBounds().getHeight();
  }
}