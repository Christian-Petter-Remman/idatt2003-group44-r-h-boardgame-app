package edu.ntnu.idi.idatt.view.common.intro.dialogs;


import edu.ntnu.idi.idatt.navigation.NavigationManager;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoDialog extends Stage {
  private final DialogConfig cfg;
  private MediaPlayer player;

  public InfoDialog(DialogConfig cfg) {
    this.cfg = cfg;
    initModality(Modality.APPLICATION_MODAL);
    setResizable(false);
    setTitle(cfg.getTitle());
    getIcons().add(new Image(getClass().getResourceAsStream("/logo.png")));

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(20));
    root.setPrefWidth(400);

    // Top: title + logo
    Label lblTitle = new Label(cfg.getTitle());
    lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    ImageView logo = new ImageView(new Image(
        getClass().getResourceAsStream("/logo.png")));
    logo.setFitWidth(30);
    logo.setFitHeight(30);
    HBox topBar = new HBox(lblTitle, new Region(), logo);
    HBox.setHgrow(topBar.getChildren().get(1), Priority.ALWAYS);
    topBar.setAlignment(Pos.CENTER_LEFT);
    root.setTop(topBar);

    // Center: body, funFact, image
    VBox center = new VBox(10);
    center.setPadding(new Insets(10, 0, 10, 0));
    Label lblBody = new Label(cfg.getBody());
    lblBody.setWrapText(true);
    center.getChildren().add(lblBody);

    if (cfg.getFunFact() != null) {
      Label lblFun = new Label("Fun fact: " + cfg.getFunFact());
      lblFun.setStyle("-fx-font-style: italic;");
      center.getChildren().add(lblFun);
    }

    String imgPath = cfg.getImagePath() != null
        ? cfg.getImagePath()
        : "/home_screen/" + cfg.getId() + ".png";
    ImageView mainImg = new ImageView(new Image(
        getClass().getResourceAsStream(imgPath)));
    mainImg.setFitWidth(150);
    mainImg.setPreserveRatio(true);
    center.getChildren().add(mainImg);

    root.setCenter(center);

    // Bottom: buttons
    HBox buttons = new HBox(10);
    buttons.setAlignment(Pos.CENTER_RIGHT);

    if (cfg.getAudio() != null) {
      Button btnListen = new Button("🔊 Listen");
      btnListen.setOnAction(e -> playSound(cfg.getAudio()));
      buttons.getChildren().add(btnListen);
    }

    if (cfg.getCtaLabel() != null) {
      Button btnPlay = new Button(cfg.getCtaLabel());
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
    btnClose.setOnAction(e -> close());
    buttons.getChildren().add(btnClose);

    root.setBottom(buttons);

    setScene(new Scene(root));
  }

  private void playSound(String audioRes) {
    if (player == null) {
      Media media = new Media(
          getClass().getResource(audioRes).toExternalForm()
      );
      player = new MediaPlayer(media);
    }
    player.stop();
    player.play();
  }
}
