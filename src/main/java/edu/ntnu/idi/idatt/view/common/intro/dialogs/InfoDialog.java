package edu.ntnu.idi.idatt.view.common.intro.dialogs;

import edu.ntnu.idi.idatt.navigation.NavigationManager;
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

public class InfoDialog extends Stage {

  private MediaPlayer player;

  private static final int MAX_BODY_LINES = 4;
  private static final double BODY_WRAP_WIDTH = 360;

  public InfoDialog(DialogConfig cfg) {
    initOwner(NavigationManager.getInstance().getPrimaryStage());
    initModality(Modality.WINDOW_MODAL);
    setResizable(false);
    setTitle(cfg.getTitle());
    getIcons().add(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png"))
    ));

    // --- Build UI ---
    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setPrefWidth(BODY_WRAP_WIDTH + 40);

    // Top: title + logo
    Label lblTitle = new Label(cfg.getTitle());
    lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    ImageView logo = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png"))
    ));
    logo.setFitWidth(30);
    logo.setFitHeight(30);
    HBox topBar = new HBox(lblTitle, new Region(), logo);
    HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
    topBar.setAlignment(Pos.CENTER_LEFT);
    root.setTop(topBar);

    // Body: scrollable, max 4 lines
    Label lblBody = new Label(cfg.getBody());
    lblBody.setWrapText(true);
    lblBody.setMaxWidth(BODY_WRAP_WIDTH);
    lblBody.setPrefWidth(BODY_WRAP_WIDTH);

    Text helper = new Text("A");
    helper.setFont(lblBody.getFont());
    helper.applyCss();
    double lineHeight = helper.getLayoutBounds().getHeight();
    double maxBodyHeight = lineHeight * MAX_BODY_LINES;

    ScrollPane bodyScroll = new ScrollPane(lblBody);
    bodyScroll.setFitToWidth(true);
    bodyScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    bodyScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    bodyScroll.setPrefViewportHeight(maxBodyHeight);
    bodyScroll.setMinViewportHeight(maxBodyHeight);
    bodyScroll.getStyleClass().add("dialog-scroll-pane");

    VBox center = new VBox(10, bodyScroll);
    center.setPadding(new Insets(10, 0, 10, 0));

    if (cfg.getFunFact() != null) {
      Label lblFun = new Label("Fun fact: " + cfg.getFunFact());
      lblFun.setWrapText(true);
      lblFun.setMaxWidth(BODY_WRAP_WIDTH);
      center.getChildren().add(lblFun);
    }

    ImageView mainImg = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream(cfg.getImagePath()))
    ));
    mainImg.setFitWidth(150);
    mainImg.setPreserveRatio(true);
    center.getChildren().add(mainImg);

    root.setCenter(center);

    // Bottom: buttons
    HBox buttons = new HBox(10);
    buttons.setAlignment(Pos.CENTER_RIGHT);

    if (cfg.getAudio() != null) {
      Button btnListen = new Button("🔊 Listen");
      btnListen.getStyleClass().add("dialog-button");
      btnListen.setOnAction(e -> playSound(cfg.getAudio()));
      buttons.getChildren().add(btnListen);
    }

    if (cfg.getCtaLabel() != null) {
      Button btnPlay = new Button(cfg.getCtaLabel());
      btnPlay.getStyleClass().add("dialog-button");
      btnPlay.setOnAction(e -> {
        close();
        NavigationManager.getInstance()
            .navigateTo(
                NavigationManager.NavigationTarget
                    .valueOf(cfg.getCtaAction())
            );
      });
      buttons.getChildren().add(btnPlay);
    }

    Button btnClose = new Button("Close");
    btnClose.getStyleClass().add("dialog-button");
    btnClose.setOnAction(e -> close());
    buttons.getChildren().add(btnClose);

    root.setBottom(buttons);

    // --- Apply stylesheet and show ---
    Scene scene = new Scene(root);
    scene.getStylesheets().add(
        Objects.requireNonNull(
            getClass().getResource("/css/StartScreenStyleSheet.css")
        ).toExternalForm()
    );
    setScene(scene);
  }

  private void playSound(String audioRes) {
    if (player == null) {
      Media media = new Media(
          Objects.requireNonNull(getClass().getResource(audioRes)).toExternalForm()
      );
      player = new MediaPlayer(media);
    }
    player.stop();
    player.play();
  }
}
