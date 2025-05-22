package edu.ntnu.idi.idatt.view.star;

import edu.ntnu.idi.idatt.controller.star.StarGameController;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import edu.ntnu.idi.idatt.model.stargame.StarPlayer;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.GameScreen;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.WinnerDialogs;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>StarGameView</h1>
 * JavaFX implementation of the game screen for the Star game.
 * Handles rendering of the board, displaying player positions,
 * and observing game state changes via the GameScreenObserver interface.
 */

public class StarGameView extends GameScreen {

  private final int TILE_SIZE = 75;
  private final int rows = 10;
  private final int cols = 13;
  private final List<Integer> blankTiles = new ArrayList<>(List.of(0));


  private final StarGameController controller;


  /**
   * <h2>Constructor</h2>
   * Initializes the game screen, sets up listeners, and creates the UI.
   *
   * @param controller the controller handling game logic and player state
   */
  public StarGameView(StarGameController controller) {
    this.controller = controller;
    File tempFile = controller.getCsvFile();

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
          Platform.runLater(() -> showWinner(starPlayer));
          controller.deleteGame(tempFile);
        }
      }

      @Override
      public void onGameSaved(String filePath) {}
    });

    createUI();
    setBackListener(() -> NavigationManager.getInstance().navigateToStartScreen());
    setSaveListener(() -> showSaveGamePopup());
  }

  /**
   * <h2>showSaveGamePopup</h2>
   * Displays a popup allowing the player to name and save the current game state.
   */
  private void showSaveGamePopup() {
    javafx.stage.Popup popup = new javafx.stage.Popup();

    VBox content = new VBox(10);
    content.setStyle("-fx-background-color: #e0f7fa; -fx-padding: 15; -fx-border-color: #00acc1; -fx-border-width: 2;");
    Label header = new Label("Save Game");
    header.setStyle("-fx-font-weight: bold; -fx-text-fill: #006064;");

    TextField filenameField = new TextField("star_save_" + System.currentTimeMillis());
    filenameField.setPrefWidth(220);

    Label feedbackLabel = new Label();
    feedbackLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
    feedbackLabel.setVisible(false);

    Button confirm = new Button("Save");
    confirm.setStyle("-fx-cursor: hand;");
    confirm.setOnAction(e -> {
      String filename = filenameField.getText().trim();
      if (!filename.isEmpty()) {
        FileManager.writeStarGameStateToCSV(
                controller.getCsvFile(),
                controller.getPlayers(),
                "default.json",
                controller.getDiceCount(),
                controller.getCurrentTurn()
        );
        controller.saveGame(controller.getCsvFile(), filename + ".csv");
        feedbackLabel.setText("Game saved");
        feedbackLabel.setVisible(true);
        new Thread(() -> {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException ignored) {}
          javafx.application.Platform.runLater(popup::hide);
        }).start();
      }
    });

    content.getChildren().addAll(header, filenameField, confirm, feedbackLabel);
    popup.getContent().add(content);
    popup.setAutoHide(true);
    popup.setHideOnEscape(true);

    javafx.stage.Window window = root.getScene().getWindow();
    popup.show(window, window.getX() + window.getWidth() / 2 - 150, window.getY() + window.getHeight() / 2 - 70);
  }


  public void initializeUI() {
    createUI();
  }

  /**
   * <h2>showWinner</h2>
   * Displays the winner dialog.
   *
   * @param winner the player who won the game
   */
  private void showWinner(StarPlayer winner) {
    WinnerDialogs dialogs = new WinnerDialogs();
    dialogs.showWinnerDialog(winner);
  }


  /**
   * <h2>getCurrentPlayerImage</h2>
   * Returns the current player's character icon as an Image.
   *
   * @return Image of current player's icon or null if not available
   */
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

  /**
   * <h2>renderBoardGrid</h2>
   * Draws the board grid using the tile mapping from BoardCreator.
   */
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
      int tileNum = BoardCreator.StarGameCreator(i);
      StackPane cell = createTile(tileNum);

      int row = rows - 1 - (i / cols);
      int col = (row % 2 == 0) ? (i % cols) : (cols - 1 - (i % cols));

      boardGrid.add(cell, col, row);
    }
  }

  /**
   * <h2>createTile</h2>
   * Creates a tile on the board with styling and player icons.
   *
   * @param tileNum logical number of the tile
   * @return configured StackPane representing the tile
   */

  @Override
  public StackPane createTile(int tileNum) {
    StackPane cell = new StackPane();
    cell.setPrefSize(TILE_SIZE, TILE_SIZE);

    if (tileNum == 100) {
      var url = getClass().getResource("/images/jailTile.png");
      if (url != null) {
        cell.setStyle("-fx-border-color: black; -fx-background-image: url('" + url.toExternalForm() + "'); " +
                "-fx-background-size: cover;");
      } else {
        cell.setStyle("-fx-border-color: black; -fx-background-color: black;");
      }
    } else {
      String borderColor = isBlank(tileNum) ? "white" : "black";
      cell.setStyle("-fx-border-color: " + borderColor + ";" +
              "-fx-background-color:" + getTileColor(tileNum) + ";");

      if (tileNum != 0) {
        Text tileNumber = new Text(String.valueOf(tileNum));
        tileNumber.setStyle("-fx-fill: #555;");
        cell.getChildren().add(tileNumber);
      }
    }

    addOverlayImagesToCell(cell, tileNum);

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

  /**
   * <h2>addOverlayImagesToCell</h2>
   * Adds stars, tunnels, and bridges on top of the tile if present.
   *
   * @param cell     the tile pane
   * @param tileNum  the tile number
   */
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

  /**
   * <h2>addImageToCell</h2>
   * Adds an image overlay to a tile.
   *
   * @param cell          tile container
   * @param imageFileName name of the image file to add
   */

  private void addImageToCell(StackPane cell, String imageFileName) {
    try {
      var url = getClass().getResource("/images/" + imageFileName);
      if (url == null) {
        logger.warn("Image not found: {}", imageFileName);
        return;
      }

      Image image = new Image(url.toExternalForm(), TILE_SIZE * 0.8, TILE_SIZE * 0.8, true, true);
      ImageView icon = new ImageView(image);
      cell.getChildren().add(icon);
    } catch (Exception e) {
      logger.error("Failed to load image: {}", imageFileName, e);
    }
  }

  /**
   * <h2>isBlank</h2>
   * Checks if a tile is considered blank.
   *
   * @param tileNum tile number
   * @return true if blank
   */

  private boolean isBlank(int tileNum) {
    return blankTiles.contains(tileNum);
  }

  @Override
  protected List<Player> getAllPlayers() {
    return controller.getPlayers();
  }

  @Override
  public void initializeOverlay() {
    // No longer needed since overlays are added per cell
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
    return new Pane(); // Overlay no longer used
  }
}
