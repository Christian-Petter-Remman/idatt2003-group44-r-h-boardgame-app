package edu.ntnu.idi.idatt.view.star;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.controller.star.StarGameController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.view.GameScreen;
import edu.ntnu.idi.idatt.view.snl.BoardCreator;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.*;

import java.util.List;

  public class StarGameView extends GameScreen {

    private final Pane attributeOverlay = new Pane();
    private final int tileSize = 75;
    private final int rows = 10;
    private final int cols = 13;

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
      boardGrid.getColumnConstraints().clear();
      boardGrid.getRowConstraints().clear();

      boardGrid.setPrefSize(tileSize * cols, tileSize * rows);
      boardGrid.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
      boardGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
      attributeOverlay.setPrefSize(tileSize * cols, tileSize * rows);

      for (int i = 0; i < cols; i++) {
        ColumnConstraints colConst = new ColumnConstraints(tileSize);
        colConst.setHalignment(HPos.CENTER);
        boardGrid.getColumnConstraints().add(colConst);
      }

      for (int i = 0; i < rows; i++) {
        RowConstraints rowConst = new RowConstraints(tileSize);
        rowConst.setValignment(VPos.CENTER);
        boardGrid.getRowConstraints().add(rowConst);
      }

      for (int i = 0; i < rows * cols; i++) {
        int tileNum = BoardCreator.StarGame(i);
        StackPane cell = createTile(tileNum);

        int row = i / cols;
        int col = i % cols;

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
