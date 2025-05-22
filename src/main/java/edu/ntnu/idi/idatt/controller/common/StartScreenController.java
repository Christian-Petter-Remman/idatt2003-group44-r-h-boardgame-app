package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.model.common.intro.StartScreenModel;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.InfoDialog;
import java.util.Map;
import java.util.Objects;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


/**
 * <h1>StartScreenController</h1>
 *
 * <p>Controller for handling interactions on the start screen, including displaying information
 * dialogs and playing associated audio when icons are clicked.
 *
 * @author Oliver, Christian
 */
public class StartScreenController {

  private final StartScreenModel model;

  /**
   * <h2>Constructor</h2>
   *
   * <p>Initializes the controller with a given {@link StartScreenModel}.
   *
   * @param model the start screen model containing dialog configurations
   */
  public StartScreenController(StartScreenModel model) {
    this.model = model;
  }

  /**
   * <h2>onIconClicked</h2>
   *
   * <p>Called when an icon is clicked. If the corresponding ID is registered in the model's dialog
   * map, this method plays an associated audio clip (if available) and shows a dialog.
   *
   * @param id   the identifier for the dialog and audio mapping
   */
  public void onIconClicked(String id) {
    Map<String, ?> dialogs = model.getDialogs();
    if (!dialogs.containsKey(id)) {
      return;
    }

    var cfg = model.getDialogs().get(id);
    if (cfg.getAudio() != null && !cfg.getAudio().isBlank()) {
      MediaPlayer player = new MediaPlayer(
          new Media(Objects.requireNonNull(getClass().getResource(cfg.getAudio())).toExternalForm())
      );
      player.play();
    }

    new InfoDialog(cfg).showAndWait();
  }
}