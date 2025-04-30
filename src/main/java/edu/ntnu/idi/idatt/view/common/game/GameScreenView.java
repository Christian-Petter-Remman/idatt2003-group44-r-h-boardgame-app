package edu.ntnu.idi.idatt.view.common.game;

import edu.ntnu.idi.idatt.controller.snl.SNLGameScreenController;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.view.AbstractView;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

public class GameScreenView extends AbstractView implements GameScreenObserver {

  private SNLGameScreenController controller;
  private VBox root;
  private Label currentPlayerLabel;
  private Label positionLabel;
  private Label diceResultLabel;
  private Button rollButton;

  public GameScreenView() {
    // Empty constructor; initialization will be done later through the setter
  }

  /**
   * This method should be called to set the controller after instantiating the view.
   * It will initialize the view with the provided controller.
   *
   * @param controller the controller to be used by this view
   */
  public void initializeWithController(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this);  // Registering the observer to handle updates
    initializeUI();  // Initialize the UI with the controller
  }

  @Override
  protected void createUI() {
    root = new VBox(20);
    root.setStyle("-fx-padding: 30; -fx-alignment: center;");

    currentPlayerLabel = new Label("Current turn:");
    positionLabel = new Label("Position:");
    diceResultLabel = new Label("Roll result:");

    rollButton = new Button("Roll Dice");
    rollButton.setOnAction(e -> controller.handleRoll());

    root.getChildren().addAll(currentPlayerLabel, positionLabel, diceResultLabel, rollButton);
  }

  @Override
  protected void setupEventHandlers() {
    // Roll button handled inside createUI()
  }

  @Override
  protected void applyInitialUIState() {
    // After initializing UI, set the initial state for the player and dice result
    updateCurrentPlayerView(controller.getCurrentPlayer());
  }

  @Override
  public void onPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
    updateCurrentPlayerView(player);  // Update UI when the player's position changes
  }

  @Override
  public void onDiceRolled(int result) {
    diceResultLabel.setText("Roll result: " + result);
    rollButton.setDisable(true);  // Disable roll button until next turn
  }

  @Override
  public void onPlayerTurnChanged(Player currentPlayer) {
    updateCurrentPlayerView(currentPlayer);  // Update the UI for the current player
    rollButton.setDisable(false);  // Enable the roll button for the next turn
  }

  @Override
  public void onGameOver(Player winner) {
    // Show an alert when the game is over with the winner's details
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Over");
    alert.setHeaderText("🎉 " + winner.getName() + " has won the game!");
    alert.setContentText("Final position: " + winner.getPosition());

    if (winner.getName() != null) {
      ImageView image = new ImageView(new Image("player_icons/" + winner.getName() + ".png", 100, 100, true, true));
      alert.setGraphic(image);
    }

    alert.initModality(Modality.APPLICATION_MODAL);
    alert.initOwner(root.getScene().getWindow());
    alert.showAndWait();

    rollButton.setDisable(true);  // Disable roll button once the game is over
  }

  @Override
  public void onGameSaved(String filePath) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Saved");
    alert.setHeaderText("Game saved successfully");
    alert.setContentText("Saved to: " + filePath);
    alert.showAndWait();  // Show the alert once the game has been saved
  }

  private void updateCurrentPlayerView(Player player) {
    currentPlayerLabel.setText("Current turn: " + player.getName());
    positionLabel.setText("Position: " + player.getPosition());
  }

  @Override
  public Parent getRoot() {
    return root;  // Return the root node of the view
  }
}