package edu.ntnu.idi.idatt.view.star;

import edu.ntnu.idi.idatt.controller.star.StarGameController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.scene.layout.Pane;

import java.util.List;

  public class StarGameView extends GameScreen {

    private final StarGameController controller;

    public StarGameView(StarGameController controller) {
      this.controller = controller;
    }

    public void initializeUI() {
      createUI();
    }

    @Override
    protected void handleRoll() {
      controller.handleRoll();
    }

    @Override
    protected String getTileColor(int tileNumber) {
      return controller.getTileColor(tileNumber);
    }

    @Override
    protected List<Player> getPlayersAtPosition(int tileNumber) {
      return controller.getPlayersAtPosition(tileNumber);
    }

    /**
     * StarGame currently doesn't use a custom overlay like SNL does (e.g., for ladders/snakes),
     * but this method must be implemented to satisfy the abstract method in GameScreen.
     * Returns an empty Pane by default.
     */
    @Override
    protected Pane getOverlay() {
      return new Pane(); // No overlay logic needed, but required by base class
    }
  }
