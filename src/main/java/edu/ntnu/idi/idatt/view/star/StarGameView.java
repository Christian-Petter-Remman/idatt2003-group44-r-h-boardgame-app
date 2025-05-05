package edu.ntnu.idi.idatt.view.star;

import edu.ntnu.idi.idatt.controller.star.StarGameController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import edu.ntnu.idi.idatt.model.stargame.StarPlayer;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.GameScreen;
import edu.ntnu.idi.idatt.view.snl.BoardCreator;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
      public void onGameOver(Player winner) {
        if (winner instanceof StarPlayer starPlayer) {
          Platform.runLater(() -> showWinnerDialog(starPlayer));
        }
      }

      @Override
      public void onGameSaved(String filePath) {}
    });

    createUI();
  }

  public void initializeUI() {
    createUI();
  }

  private void showWinnerDialog(StarPlayer winner) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Over!");
    alert.setHeaderText(winner.getName() + " has won the game, getting " + winner.getPoints() + " stars!");
    alert.setContentText("Press home to continue");

    ButtonType okButton = new ButtonType("Home Page", ButtonBar.ButtonData.OK_DONE);
    alert.getButtonTypes().setAll(okButton);

    alert.showAndWait().ifPresent(response -> {
      if (response == okButton) {
        NavigationManager.getInstance().navigateToStartScreen();
      }
    });
  }

  protected Image getCurrentPlayerImage() {
    Player currentPlayer = controller.getCurrentPlayer();
    if (currentPlayer != null && currentPlayer.getCharacter() != null) {
      String characterName = currentPlayer.getCharacter().toLowerCase();
      URL url = getClass().getResource("/player_icons/" + characterName + ".png");
      if (url != null) {
        return new Image(url.toExternalForm());
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
  }

  public StackPane createTile(int tileNum) {
    StackPane cell = new StackPane();
    cell.setPrefSize(TILE_SIZE, TILE_SIZE);

    if (tileNum == 100) {
      var url = getClass().getResource("/images/jailTile.png");
      if (url != null) {
        cell.setStyle("-fx-border-color: black; -fx-background-image: url('" + url.toExternalForm() + "'); -fx-background-size: cover;");
      } else {
        cell.setStyle("-fx-border-color: black; -fx-background-color: black;");
      }
    } else {
      String borderColor = isBlank(tileNum) ? "white" : "black";
      cell.setStyle("-fx-border-color: " + borderColor + "; -fx-background-color:" + getTileColor(tileNum) + ";");

      if (tileNum != 0) {
        Text tileNumber = new Text(String.valueOf(tileNum));
        tileNumber.setStyle("-fx-fill: #555;");
        cell.getChildren().add(tileNumber);
      }
    }

    addOverlayImagesToCell(cell, tileNum);

    List<Player> playersOnTile = getPlayersAtPosition(tileNum);
    for (Player player : playersOnTile) {
      String characterName = player.getCharacter() != null ? player.getCharacter().toLowerCase() : "default";
      try {
        var url = getClass().getResource("/player_icons/" + characterName + ".png");
        if (url == null) continue;

        Image image = new Image(url.toExternalForm(), TILE_SIZE * 0.5, TILE_SIZE * 0.5, true, true);
        ImageView icon = new ImageView(image);
        icon.setTranslateY(TILE_SIZE * 0.15 * playersOnTile.indexOf(player));
        cell.getChildren().add(icon);
      } catch (Exception e) {
        //logger.error("Error loading image for character: {}", characterName, e);
      }
    }

    return cell;
  }

  private void addOverlayImagesToCell(StackPane cell, int tileNum) {
    StarBoard board = (StarBoard) controller.getBoard();
    board.getBridges().forEach(bridge -> {
      if (bridge.getStart() == tileNum || bridge.getEnd() == tileNum) {
        addImageToCell(cell, "bridge.png");
      }
    });
    board.getTunnels().forEach(tunnel -> {
      if (tunnel.getStart() == tileNum || tunnel.getEnd() == tileNum) {
        addImageToCell(cell, "tunnel.png");
      }
    });
    board.getStars().forEach(star -> {
      if (star.getStart() == tileNum) {
        addImageToCell(cell, "star.png");
      }
    });
  }

  private void addImageToCell(StackPane cell, String imageFileName) {
    try {
      var url = getClass().getResource("/images/" + imageFileName);
      if (url == null) {
        //logger.warn("Image not found: {}", imageFileName);
        return;
      }

      Image image = new Image(url.toExternalForm(), TILE_SIZE * 0.8, TILE_SIZE * 0.8, true, true);
      ImageView icon = new ImageView(image);
      cell.getChildren().add(icon);
    } catch (Exception e) {
      //logger.error("Failed to load image: {}", imageFileName, e);
    }
  }

  private boolean isBlank(int tileNum) {
    return blankTiles.contains(tileNum);
  }

  @Override
  protected List<Player> getAllPlayers() {
    return controller.getPlayers();
  }

  @Override
  protected Image getPlayerImage(Player player) {
    return null;
  }

  public void initializeOverlay() {}

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

  protected Pane getOverlay() {
    return new Pane();
  }
}
