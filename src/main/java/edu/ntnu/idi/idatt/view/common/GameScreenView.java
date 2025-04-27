package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.controller.common.GameScreenController;
import edu.ntnu.idi.idatt.controller.snakesandladders.SalBoardController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.view.AbstractView;
import edu.ntnu.idi.idatt.view.snakesandladders.SalBoardView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;


import java.util.List;

public class GameScreenView extends AbstractView implements GameScreenObserver {

  private final GameScreenController controller;

  private SalBoardView salBoardView;
  private Label currentPlayerLabel;
  private ImageView currentPlayerImageView;
  private Label diceResultLabel;
  private Label positionLabel;
  private Button rollButton;
  private Button backButton;
  private Button saveButton;
  private Label player1TurnLabel;
  private Label player2TurnLabel;
  private Label player3TurnLabel;
  private Label player4TurnLabel;

  public GameScreenView(GameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this);
  }

  @Override
  protected void createUI() {
    BorderPane mainContainer = new BorderPane();

    // Initialize player position labels
    initializePlayerLabels();

    // Create the game board view
    createBoardView();

    // Create the right panel with player info and controls
    BorderPane rightPanel = createRightPanel();

    // Combine board and right panel in main content
    HBox mainContent = createMainContent(rightPanel);

    mainContainer.setCenter(mainContent);
    root = mainContainer;
  }

  private void initializePlayerLabels() {
    player1TurnLabel = new Label();
    player2TurnLabel = new Label();
    player3TurnLabel = new Label();
    player4TurnLabel = new Label();
  }

  private void createBoardView() {
    List<Player> players = controller.getPlayers();
    SalBoardController boardController = new SalBoardController(controller.getGame().getBoard(), controller.getPlayers());
    salBoardView = new SalBoardView(boardController);
  }

  private BorderPane createRightPanel() {
    // Create the top section with current player info
    HBox topRightInfo = createCurrentPlayerInfoSection();

    // Create the center section with player grid
    VBox playerSection = createPlayerSection();

    // Create the bottom section with control buttons
    HBox buttonBox = createControlButtonsSection();

    // Combine all sections in the right panel
    BorderPane rightPanel = new BorderPane();
    rightPanel.setTop(topRightInfo);
    rightPanel.setCenter(playerSection);
    rightPanel.setBottom(buttonBox);
    rightPanel.setPrefWidth(300);

    return rightPanel;
  }

  private HBox createCurrentPlayerInfoSection() {
    // Create current player label
    currentPlayerLabel = new Label("Current turn: ");
    currentPlayerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    currentPlayerLabel.setMinWidth(150);

    // Create dice result and position labels
    diceResultLabel = new Label("Roll result: -");
    positionLabel = new Label("Position: -");

    // Create current player image
    String characterName = controller.getCurrentPlayer().getCharacter();
    if (characterName == null || characterName.isEmpty()) {
      characterName = "Unknown-Player";
    }
    Image currentPlayerImage = new Image("player_icons/" + characterName + ".png", 150, 150, true, true);
    currentPlayerImageView = new ImageView(currentPlayerImage);

    // Combine elements in a hbox
    HBox topRightInfo = new HBox(60, currentPlayerLabel, currentPlayerImageView);
    topRightInfo.setAlignment(Pos.CENTER_LEFT);
    topRightInfo.setPadding(new Insets(20, 10, 0, 10));

    return topRightInfo;
  }

  private VBox createPlayerSection() {
    // Create player section header
    Label playerLabel = new Label("Players");
    playerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    playerLabel.setAlignment(Pos.CENTER);
    playerLabel.setMaxWidth(Double.MAX_VALUE);

    // Create player grid with all players
    GridPane playerGrid = createPlayerGrid(controller.getPlayers(), controller.getCharacterNames());

    // Combine elements in a vbox
    VBox playerSection = new VBox(20, playerLabel, playerGrid);
    playerSection.setAlignment(Pos.TOP_CENTER);
    playerSection.setPadding(new Insets(150, 0, 0, 0));

    return playerSection;
  }

  private HBox createControlButtonsSection() {
    // Create back button
    backButton = new Button("Back");
    backButton.setStyle("-fx-font-size: 12px;");

    // Create roll dice button
    rollButton = new Button("Roll Dice");
    rollButton.setStyle("-fx-font-size: 12px;");

    // Create save game button
    saveButton = new Button("Save Game");
    saveButton.setStyle("-fx-font-size: 12px;");

    // Combine buttons in a hbox
    HBox buttonBox = new HBox(20, backButton, rollButton, saveButton);
    buttonBox.setAlignment(Pos.BOTTOM_CENTER);
    buttonBox.setPadding(new Insets(10));

    return buttonBox;
  }

  private HBox createMainContent(BorderPane rightPanel) {
    HBox mainContent = new HBox(40, salBoardView.getRoot(), rightPanel);
    mainContent.setAlignment(Pos.CENTER_LEFT);
    mainContent.setPadding(new Insets(20));
    return mainContent;
  }

  @Override
  protected void setupEventHandlers() {
    rollButton.setOnAction(e -> controller.handleRoll());
    backButton.setOnAction(e -> controller.navigateBack());
    saveButton.setOnAction(e -> controller.saveGame());
  }

  @Override
  protected void applyInitialUIState() {
    updatePlayerTurnLabels();
    updateCurrentPlayerView(controller.getCurrentPlayer());
    //salBoardView.show(); TODO: FIX THIS HERE --- !!!!!!!!!!!!!!!!!!
  }

  private GridPane createPlayerGrid(List<Player> players, List<String> characterNames) {
    GridPane playerGrid = new GridPane();
    playerGrid.setHgap(60);
    playerGrid.setVgap(60);
    playerGrid.setAlignment(Pos.CENTER);

    addPlayersToGrid(playerGrid, players, characterNames);

    return playerGrid;
  }

  private void addPlayersToGrid(GridPane playerGrid, List<Player> players, List<String> characterNames) {
    if (!players.isEmpty()) {
      Player player1 = players.getFirst();
      String player1Character = !characterNames.isEmpty() ? characterNames.getFirst() : "default";
      VBox player1Box = createPlayerBox(player1, player1Character, player1TurnLabel);
      playerGrid.add(player1Box, 0, 0);
    }

    if (players.size() >= 2) {
      Player player2 = players.get(1);
      String player2Character = characterNames.size() > 1 ? characterNames.get(1) : "default";
      VBox player2Box = createPlayerBox(player2, player2Character, player2TurnLabel);
      playerGrid.add(player2Box, 1, 0);
    }

    if (players.size() >= 3) {
      Player player3 = players.get(2);
      String player3Character = characterNames.size() > 2 ? characterNames.get(2) : "default";
      VBox player3Box = createPlayerBox(player3, player3Character, player3TurnLabel);
      playerGrid.add(player3Box, 0, 1);
    }

    if (players.size() >= 4) {
      Player player4 = players.get(3);
      String player4Character = characterNames.size() > 3 ? characterNames.get(3) : "default";
      VBox player4Box = createPlayerBox(player4, player4Character, player4TurnLabel);
      playerGrid.add(player4Box, 1, 1);
    }
  }

  private VBox createPlayerBox(Player player, String characterName, Label positionLabel) {
    Image playerImage = new Image("player_icons/" + characterName + ".png", 75, 75, true, true);
    ImageView playerImageView = new ImageView(playerImage);
    Label playerNameLabel = new Label(player.getName());
    positionLabel.setText(String.valueOf(player.getPosition()));

    VBox playerBox = new VBox(5, playerImageView, playerNameLabel, positionLabel);
    playerBox.setAlignment(Pos.CENTER);
    return playerBox;
  }

  private void updatePlayerTurnLabels() {
    List<Player> players = controller.getPlayers();

    if (!players.isEmpty()) {
      player1TurnLabel.setText(String.valueOf(players.getFirst().getPosition()));
    }

    if (players.size() >= 2) {
      player2TurnLabel.setText(String.valueOf(players.get(1).getPosition()));
    }

    if (players.size() >= 3) {
      player3TurnLabel.setText(String.valueOf(players.get(2).getPosition()));
    }

    if (players.size() >= 4) {
      player4TurnLabel.setText(String.valueOf(players.get(3).getPosition()));
    }
  }

  private void updateCurrentPlayerView(Player currentPlayer) {
    currentPlayerLabel.setText("Current turn: " + currentPlayer.getName());
    positionLabel.setText("Position: " + currentPlayer.getPosition());

    String characterName = currentPlayer.getCharacter();
    if (characterName == null || characterName.isEmpty()) {
      characterName = "default";
    }
    Image currentPlayerImage = new Image("player_icons/" + characterName + ".png", 150, 150, true, true);
    currentPlayerImageView.setImage(currentPlayerImage);
  }

  @Override
  public void onPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
    salBoardView.render();
    updatePlayerTurnLabels();

    if (player.equals(controller.getCurrentPlayer())) {
      positionLabel.setText("Position: " + newPosition);
    }
  }

  @Override
  public void onDiceRolled(int result) {
    diceResultLabel.setText("Roll result: " + result);
    rollButton.setDisable(true);
  }

  @Override
  public void onPlayerTurnChanged(Player currentPlayer) {
    updateCurrentPlayerView(currentPlayer);
    rollButton.setDisable(false);
  }

  @Override
  public void onGameOver(Player winner) {
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Game Over");
    alert.setHeaderText("🎉 " + winner.getName() + " has won the game!");
    alert.setContentText("Final position: " + winner.getPosition());
    alert.setGraphic(new ImageView(new Image("player_icons/" + winner.getCharacter() + ".png", 100, 100, true, true)));

    alert.initOwner(root.getScene().getWindow());
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.showAndWait();
    rollButton.setDisable(true);
  }

  @Override
  public void onGameSaved(String filePath) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Saved");
    alert.setHeaderText("Game saved successfully");
    alert.setContentText("Game saved to: " + filePath);
    alert.showAndWait();
  }
}
