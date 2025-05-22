package edu.ntnu.idi.idatt.view;

import edu.ntnu.idi.idatt.model.common.Player;
import java.net.URL;
import java.util.List;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>GameScreen</h1>
 * GameScreen.java an abstract class that provides a base for rendering a tile-based game screen
 * in JavaFX.
 *
 * <p>Abstract base class for rendering a tile-based game screen in JavaFX. It defines shared UI
 * layout, player info, board rendering, and control buttons.
 * </p>
 */
public abstract class GameScreen {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected static final int TILE_SIZE = 60;
  protected static final int BOARD_SIZE = 10;

  protected Runnable saveListener;
  protected Runnable backListener;

  protected BorderPane root;
  protected GridPane boardGrid;
  protected Label currentPlayerLabel;
  protected Label positionLabel;
  protected Label diceResultLabel;
  protected Button rollButton;
  protected ImageView playerImage;
  protected VBox playerInfoList;

  /**
   * Constructs the GameScreen base.
   */
  public GameScreen() {
  }

  /**
   * <h2>createUI</h2>
   * Initializes the main game UI layout, board grid, control buttons, and player panel.
   */
  protected void createUI() {
    root = new BorderPane();
    root.setStyle("-fx-padding: 30;");

    initializeBoardGrid();
    initializeOverlay();

    StackPane boardWithOverlay = new StackPane(boardGrid, getOverlay());
    boardGrid.toBack();
    if (getOverlay() != null) {
      getOverlay().toFront();
    }
    root.setLeft(boardWithOverlay);

    VBox currentPlayerBox = new VBox(5);
    currentPlayerBox.setAlignment(Pos.CENTER);
    currentPlayerLabel = new Label("Current turn:");
    playerImage = new ImageView();
    playerImage.setFitWidth(70);
    playerImage.setFitHeight(70);
    playerImage.setPreserveRatio(true);
    currentPlayerBox.getChildren().addAll(currentPlayerLabel, playerImage);

    playerInfoList = new VBox(10);
    playerInfoList.setAlignment(Pos.CENTER_LEFT);

    VBox bottomBox = new VBox(10);
    positionLabel = new Label("Position:");
    diceResultLabel = new Label("Roll result:");
    rollButton = new Button("Roll Dice");
    rollButton.setOnAction(e -> handleRoll());

    Button saveButton = new Button("Save Game");
    saveButton.setOnAction(e -> {
      if (saveListener != null) {
        saveListener.run();
      }
    });

    Button homeButton = new Button("Home");
    homeButton.setStyle(
        "-fx-font-size: 14px;"
            + "-fx-background-color: #cccccc;"
            + "-fx-text-fill: black;"
            + "-fx-background-radius: 12;"
            + "-fx-padding: 6 14;"
            + "-fx-cursor: hand;"
    );
    homeButton.setOnAction(e -> {
      if (backListener != null) {
        backListener.run();
      }
    });

    bottomBox.setAlignment(Pos.CENTER);
    bottomBox.getChildren()
        .addAll(positionLabel, diceResultLabel, rollButton, saveButton, homeButton);

    VBox infoPanel = new VBox(30, currentPlayerBox, playerInfoList, bottomBox);
    infoPanel.setAlignment(Pos.TOP_CENTER);
    root.setRight(infoPanel);

    updatePlayerImages();
  }

  /**
   * <h2>updatePlayerImages</h2>
   * Updates the right-side player panel with their name, position, and icon.
   */
  protected void updatePlayerImages() {
    playerInfoList.getChildren().clear();

    getAllPlayers().forEach(player -> {
      String characterName =
          player.getCharacter() != null ? player.getCharacter().toLowerCase() : "default";
      try {
        URL url = getClass().getResource("/player_icons/" + characterName + ".png");
        Image image = url != null ? new Image(url.toExternalForm(), 50, 50, true, true) : null;
        ImageView imageView = image != null ? new ImageView(image) : new ImageView();

        Label name = new Label(player.getName());
        Label pos = new Label("Pos: " + player.getPosition());
        Label points = new Label("Pts: " + player.getPoints());

        HBox row = new HBox(10, imageView, name, pos, points);
        row.setAlignment(Pos.CENTER_LEFT);
        playerInfoList.getChildren().add(row);
      } catch (Exception e) {
        logger.error("Error loading image for character: {}", characterName, e);
      }
    });
  }

  /**
   * <h2>initializeBoardGrid</h2>
   * Sets up the GridPane for tile-based board layout and calls {@code renderBoardGrid()}.
   */
  protected void initializeBoardGrid() {
    boardGrid = new GridPane();
    boardGrid.setHgap(2);
    boardGrid.setVgap(2);
    boardGrid.setAlignment(Pos.CENTER);
    boardGrid.setPrefSize(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);

    for (int i = 0; i < BOARD_SIZE; i++) {
      ColumnConstraints colConst = new ColumnConstraints(TILE_SIZE);
      colConst.setHalignment(HPos.CENTER);
      boardGrid.getColumnConstraints().add(colConst);

      RowConstraints rowConst = new RowConstraints(TILE_SIZE);
      rowConst.setValignment(VPos.CENTER);
      boardGrid.getRowConstraints().add(rowConst);
    }

    renderBoardGrid();
  }

  /**
   * <h2>renderBoardGrid</h2>
   * Populates the board grid with styled tiles based on player positions.
   */
  protected void renderBoardGrid() {
    boardGrid.getChildren().clear();

    for (int i = 0; i < 100; i++) {
      int tileNum = i + 1;
      StackPane cell = createTile(tileNum);

      int row = 9 - (i / BOARD_SIZE);
      int col = (row % 2 == 0) ? i % BOARD_SIZE : (BOARD_SIZE - 1 - i % BOARD_SIZE);

      boardGrid.add(cell, col, row);
    }
  }

  /**
   * <h2>createTile</h2>
   * Constructs a visual tile node with tile number and any players currently on it.
   *
   * @param tileNum The tile number to create.
   * @return a styled StackPane representing the tile.
   */
  protected StackPane createTile(int tileNum) {
    StackPane cell = new StackPane();
    cell.setPrefSize(TILE_SIZE, TILE_SIZE);
    cell.setStyle("-fx-border-color: black; -fx-background-color: " + getTileColor(tileNum) + ";");

    Text tileNumber = new Text(String.valueOf(tileNum));
    tileNumber.setStyle("-fx-fill: #555;");
    cell.getChildren().add(tileNumber);

    List<Player> playersOnTile = getPlayersAtPosition(tileNum);
    for (int i = 0; i < playersOnTile.size(); i++) {
      Player player = playersOnTile.get(i);
      String characterName =
          player.getCharacter() != null ? player.getCharacter().toLowerCase() : "default";
      try {
        URL url = getClass().getResource("/player_icons/" + characterName + ".png");
        if (url == null) {
          continue;
        }

        Image image = new Image(url.toExternalForm(), TILE_SIZE * 0.5, TILE_SIZE * 0.5, true, true);
        ImageView icon = new ImageView(image);
        icon.setTranslateY(TILE_SIZE * 0.15 * i);
        cell.getChildren().add(icon);
      } catch (Exception e) {
        logger.error("Error loading image for character: {}", characterName, e);
      }
    }

    return cell;
  }

  /**
   * <h2>setSaveListener</h2>
   * Sets the runnable to be triggered on save button click.
   *
   * @param listener Runnable for save action
   */
  public void setSaveListener(Runnable listener) {
    this.saveListener = listener;
  }

  /**
   * <h2>setBackListener</h2>
   * Sets the runnable to be triggered on home button click.
   *
   * @param listener Runnable for back action
   */
  public void setBackListener(Runnable listener) {
    this.backListener = listener;
  }

  /**
   * <h2>initializeOverlay</h2>
   * Initializes the overlay for the game screen. Can be overridden by subclasses.
   */
  protected void initializeOverlay() {
  }

  /**
   * <h2>getRoot</h2>
   * Returns the root Parent node of this view.
   *
   * @return Parent node for scene graph
   */
  public Parent getRoot() {
    return root;
  }

  /**
   * <h2>getCurrentPlayerImage</h2>
   * Absract method to get the current player's image. Must be implemented by subclasses.
   */
  protected abstract Image getCurrentPlayerImage();

  /**
   * <h2>handleRoll</h2>
   * Abstract method to handle the dice roll action. Must be implemented by subclasses.
   */
  protected abstract void handleRoll();

  /**
   * <h2>getTileColor</h2>
   * Abstract method to get the color of a tile based on its number. Must be implemented by subclasses.
   *
   * @param tileNumber The tile number
   */
  protected abstract String getTileColor(int tileNumber);

  /**
   * <h2>getPlayersAtPosition</h2>
   * Abstract method to get the list of players at a specific tile position. Must be implemented by
   * subclasses.
   *
   * @param tileNumber The tile number
   */
  protected abstract List<Player> getPlayersAtPosition(int tileNumber);

  /**
   * <h2>getOverlay</h2>
   * Abstract method to get the overlay pane. Must be implemented by subclasses.
   *
   * @return Pane representing the overlay
   */
  protected abstract Pane getOverlay();

  /**
   * <h2>getAllPlayers</h2>
   * Abstract method to get the list of all players. Must be implemented by subclasses.
   *
   * @return List of all players
   */
  protected abstract List<Player> getAllPlayers();
}