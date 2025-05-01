package edu.ntnu.idi.idatt.controller.memorygame;

import edu.ntnu.idi.idatt.model.common.memorygame.GameState;
import edu.ntnu.idi.idatt.view.memorygame.MemoryGameView;
import javafx.scene.Node;
import javafx.stage.Stage;

public class MemoryGameController {
  private final MemoryGame model;
  private final MemoryGameView view;

  public MemoryGameController(MemoryGame model, Stage stage) {
    this.model = model;
    this.view = new MemoryGameView(model);

    // Register the view as an observer of the model
    model.addObserver(view);

    // Wire up the Start button to begin the game
    view.getStartButton().setOnAction(e -> model.start());

    // Wire up each card slot in the view to call model.flipCard
    Node[][] slots = view.getCardSlots();
    for (int r = 0; r < slots.length; r++) {
      for (int c = 0; c < slots[r].length; c++) {
        final int row = r, col = c;
        slots[r][c].setOnMouseClicked(e -> {
          if (model.getState() == GameState.IN_PROGRESS) {
            try {
              model.flipCard(row, col);
            } catch (IllegalArgumentException ex) {
              // invalid flip
            }
          }
        });
      }
    }

    //Finally, show the view in the given stage
    view.show(stage);
  }
}
