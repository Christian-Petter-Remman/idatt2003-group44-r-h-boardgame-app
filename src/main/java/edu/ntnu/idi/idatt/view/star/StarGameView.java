package edu.ntnu.idi.idatt.view.star;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.controller.star.StarGameController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.List;

  public class StarGameView extends GameScreen {

    private final StarGameController controller;

    public StarGameView(StarGameController controller) {
      this.controller = controller;

      controller.registerObserver(new GameScreenObserver() {
        @Override
        public void onPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
          renderBoardGrid();
        }

        @Override
        public void onDiceRolled(int result) {
          diceResultLabel.setText("Roll result: " + result);
        }

        @Override
        public void onPlayerTurnChanged(Player currentPlayer) {
          currentPlayerLabel.setText("Current turn: " + currentPlayer.getName());
          positionLabel.setText("Position: " + currentPlayer.getPosition());
        }

        @Override
        public void onGameOver(Player winner) {
        }

        @Override
        public void onGameSaved(String filePath) {
        }
      });

      createUI();
    }

    public void initializeUI() {
      createUI();
    }
    @Override
    public void renderBoardGrid() {
      boardGrid.getChildren().clear();

      for (int i = 0; i < 100; i++) {
        int tileNum = i + 1;
        StackPane cell = createTile(tileNum);

        int row = 9 - (i / BOARD_SIZE);
        int col = (row % 2 == 0) ? i % BOARD_SIZE : (BOARD_SIZE - 1 - i % BOARD_SIZE);

        boardGrid.add(cell, col, row);
      }
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
