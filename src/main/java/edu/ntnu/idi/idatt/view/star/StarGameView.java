package edu.ntnu.idi.idatt.view.star;

import edu.ntnu.idi.idatt.controller.star.StarGameController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import edu.ntnu.idi.idatt.view.GameScreen;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StarGameView extends GameScreen {

  private final int TILE_SIZE = 75;
  private final int rows = 10;
  private final int cols = 13;
  private final List<Integer> blankTiles = new ArrayList<>(List.of(0));

  private final Pane tileOverlay = new Pane(); // initialized here directly
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
        updatePlayerImages();

        Image newImage = getCurrentPlayerImage();
        if (newImage != null) {
          playerImage.setImage(newImage);
        }
      }


      @Override
      public void onGameOver(Player winner) {}

      @Override
      public void onGameSaved(String filePath) {}
    });

    createUI();
  }

  public void initializeUI() {
    createUI();
  }

  @Override
  protected Image getCurrentPlayerImage() {
    Player currentPlayer = controller.getCurrentPlayer();
    if (currentPlayer != null && currentPlayer.getCharacter() != null) {
      String characterName = currentPlayer.getCharacter().toLowerCase();
      URL url = getClass().getResource("/player_icons/" + characterName + ".png");
      if (url != null) {
        return new Image(url.toExternalForm());
      } else {
        logger.warn("No image found for character: {}", characterName);
      }
    }
    return null;
  }

  @Override
  public void renderBoardGrid() {
    boardGrid.getChildren().clear();
    boardGrid.getColumnConstraints().clear();
    boardGrid.getRowConstraints().clear();

    boardGrid.setPrefSize(TILE_SIZE * cols, TILE_SIZE * rows);
    boardGrid.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
    boardGrid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

    tileOverlay.setPrefSize(TILE_SIZE * cols, TILE_SIZE * rows);
    tileOverlay.getChildren().clear();

    for (int i = 0; i < cols; i++) {
      ColumnConstraints colConst = new ColumnConstraints(TILE_SIZE);
      colConst.setHalignment(HPos.CENTER);
      boardGrid.getColumnConstraints().add(colConst);
    }

    for (int i = 0; i < rows; i++) {
      RowConstraints rowConst = new RowConstraints(TILE_SIZE);
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

    renderOverlay();
  }

  @Override
  public StackPane createTile(int tileNum) {
    StackPane cell = new StackPane();
    cell.setPrefSize(TILE_SIZE, TILE_SIZE);

    String borderColor = isBlank(tileNum) ? "white" : "black";
    cell.setStyle("-fx-border-color: " + borderColor+";" + "-fx-background-color:"+getTileColor(tileNum)+";" );


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
        if (url == null) continue;

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

  private boolean isBlank(int tileNum) {
    return blankTiles.contains(tileNum);
  }

  private void renderOverlay() {
    StarBoard board = (StarBoard) controller.getBoard();

    board.getBridges().forEach(bridge -> renderBridges(bridge.getStart(), bridge.getEnd()));
    board.getTunnels().forEach(tunnel -> renderTunnels(tunnel.getStart(), tunnel.getEnd()));
    board.getStars().forEach(star -> renderStars(star.getStart()));
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

  private void renderStars(int tileStart) {
    double[] startPos = getTileCenter(tileStart);
    addImageToOverlay("star.png", startPos[0], startPos[1]);
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
      icon.setLayoutX(x - TILE_SIZE * 0.4);
      icon.setLayoutY(y - TILE_SIZE * 0.4);
      tileOverlay.getChildren().add(icon);
    } catch (Exception e) {
      logger.error("Failed to load image: {}", imageFileName, e);
    }
  }

  public double[] getTileCenter(int tileNum) {
    for (int i = 0; i < rows * cols; i++) {
      if (BoardCreator.StarGame(i) == tileNum) {
        int row = rows - 1 - (i / cols);
        int col = (row % 2 == 0) ? (i % cols) : (cols - 1 - (i % cols));
        double x = col * TILE_SIZE + TILE_SIZE / 2.0;
        double y = row * TILE_SIZE + TILE_SIZE / 2.0;
        return new double[]{x, y};
      }
    }
    return new double[]{0, 0}; // Fallback
  }

  @Override
  protected List<Player> getAllPlayers() {
    return controller.getPlayers(); // Or however you store them
  }

  @Override
  public void initializeOverlay() {
    tileOverlay.setPickOnBounds(false);
    tileOverlay.setMouseTransparent(true);
    tileOverlay.setPrefSize(TILE_SIZE * cols, TILE_SIZE * rows);
    renderOverlay();
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

  @Override
  protected Pane getOverlay() {
    return tileOverlay;
  }
}