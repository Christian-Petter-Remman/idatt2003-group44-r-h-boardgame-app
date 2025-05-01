package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.model.common.intro.StartScreenModel;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.InfoDialog;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Map;

public class StartScreenController {
  private final StartScreenModel model;

  public StartScreenController(StartScreenModel model) {
    this.model = model;
  }

  public void onIconClicked(ImageView icon, String id) {
    Map<String, ?> dialogs = model.getDialogs();
    if (!dialogs.containsKey(id)) return;

    // play audio
    var cfg = model.getDialogs().get(id);
    if (cfg.getAudio() != null && !cfg.getAudio().isBlank()) {
      MediaPlayer player = new MediaPlayer(
          new Media(getClass().getResource(cfg.getAudio()).toExternalForm())
      );
      player.play();
    }

    new InfoDialog(cfg).showAndWait();
  }
}
