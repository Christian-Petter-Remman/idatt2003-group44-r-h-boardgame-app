package edu.ntnu.idi.idatt.view.star;

import edu.ntnu.idi.idatt.controller.star.StarGameController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.stargame.Bridge;
import edu.ntnu.idi.idatt.model.stargame.Tunnel;
import edu.ntnu.idi.idatt.view.GameScreen;
import edu.ntnu.idi.idatt.view.snl.BoardCreator;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class StarGameView extends GameScreen {

    private final Pane attributeOverlay = new Pane();
    private final int tileSize = 75;
    private final int rows = 10;
    private final int cols = 13;
    List<Integer> blankTiles = new ArrayList<>(List.of(0));

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

        int row = rows - 1 - (i / cols);
        int col = (row % 2 == 0) ? (i % cols) : (cols - 1 - (i % cols));

        boardGrid.add(cell, col, row);
      }
    }

    private String getTileBorder(int tileNum) {
      if (isBlank(tileNum)) return "white";
      return "black";
    }

    private boolean isBlank(int tileNum) {
      return blankTiles.contains(tileNum);
    }

  @Override
  public StackPane createTile(int tileNum) {
    StackPane cell = new StackPane();
    cell.setPrefSize(TILE_SIZE, TILE_SIZE);

    String borderColor = getTileBorder(tileNum);
    cell.setStyle("-fx-border-color: " + borderColor + "; -fx-background-color: white;");

    if (tileNum != 0) {
      Text tileNumber = new Text(String.valueOf(tileNum));
      tileNumber.setStyle("-fx-fill: #555;");
      cell.getChildren().add(tileNumber);
    }

    List<Player> playersOnTile = getPlayersAtPosition(tileNum);
    for (Player player : playersOnTile) {
      String characterName = (player.getCharacter() != null) ? player.getCharacter().toLowerCase() : "default";
      try {
        var url = getClass().getResource("/player_icons/" + characterName + ".png");
        if (url == null) {
          logger.warn("Image not found for character: {}", characterName);
          continue;
        }

        Image image = new Image(url.toExternalForm(), TILE_SIZE * 0.5, TILE_SIZE * 0.5, true, true);
        ImageView icon = new ImageView(image);
        icon.setTranslateY(TILE_SIZE * 0.15 * playersOnTile.indexOf(player));
        cell.getChildren().add(icon);
      } catch (Exception e) {
        logger.error("Error loading image for character: {}", characterName, e);
      }
    }

    return cell;
  }

  private void renderBridges(int tileStart, int tileEnd) {
    double[] startPos = getTileCenter(tileStart);
    double[] endPos = getTileCenter(tileEnd);
    addImageToOverlay("bridge.png", startPos[0], startPos[1]);
    addImageToOverlay("bridge.png", endPos[0], endPos[1]);
  }

  private void renderTunnels(int tileStart, int tileEnd) {
    double[] startPos = getTileCenter(tileStart);
    double[] endPos = getTileCenter(tileEnd);
    addImageToOverlay("tunnel.png", startPos[0], startPos[1]);
    addImageToOverlay("tunnel.png", endPos[0], endPos[1]);
  }

  private void addImageToOverlay(String imageFileName, double x, double y) {
    try {
      var url = getClass().getResource("/images/" + imageFileName);
      if (url == null) {
        logger.warn("Image not found: {}", imageFileName);
        return;
      }

      Image image = new Image(url.toExternalForm(), TILE_SIZE * 0.8, TILE_SIZE * 0.8, true, true);
      ImageView icon = new ImageView(image);
      icon.setLayoutX(x - TILE_SIZE * 0.4); // center image
      icon.setLayoutY(y - TILE_SIZE * 0.4);
      attributeOverlay.getChildren().add(icon);
    } catch (Exception e) {
      logger.error("Failed to load image: {}", imageFileName, e);
    }
  }
  @Override
  public void initializeOverlay() {
    for (Tunnel tunnel : ) {
      renderTunnels(tunnel.getStart(), tunnel.getEnd());
    }

    for (Bridge bridge : controller.getBridges()) {
      renderBridges(bridge.getStart(), bridge.getEnd());
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
