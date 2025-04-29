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

  private final SNLGameScreenController controller;

  private VBox root;
  private Label currentPlayerLabel;
  private Label positionLabel;
  private Label diceResultLabel;
  private Button rollButton;

  public GameScreenView(SNLGameScreenController controller) {
    this.controller = controller;
    controller.registerObserver(this);
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
    updateCurrentPlayerView(controller.getCurrentPlayer());
  }

  @Override
  public void onPlayerPositionChanged(Player player, int oldPosition, int newPosition) {
    updateCurrentPlayerView(player);
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

    rollButton.setDisable(true);
  }

  @Override
  public void onGameSaved(String filePath) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Saved");
    alert.setHeaderText("Game saved successfully");
    alert.setContentText("Saved to: " + filePath);
    alert.showAndWait();
  }

  private void updateCurrentPlayerView(Player player) {
    currentPlayerLabel.setText("Current turn: " + player.getName());
    positionLabel.setText("Position: " + player.getPosition());
  }

  @Override
  public Parent getRoot() {
    return root;
  }
}
