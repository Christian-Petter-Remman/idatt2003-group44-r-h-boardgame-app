package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.controller.snakesandladders.SnakesAndLaddersRuleSelectionController;
import edu.ntnu.idi.idatt.exceptions.InvalidGameConfigurationException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.common.player.Player;
import edu.ntnu.idi.idatt.view.common.AbstractRuleSelectionView;
import edu.ntnu.idi.idatt.view.common.GameScreenView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SnakesAndLaddersRuleSelectionView extends AbstractRuleSelectionView {
  private static final Logger logger = LoggerFactory.getLogger(SnakesAndLaddersRuleSelectionView.class);

  private final SnakesAndLaddersRuleSelectionController controller;
  private final SnakesAndLaddersFactory factory;
  private String selectedDifficulty = "default";
  private List<Player> players;

  private ToggleGroup difficultyGroup;
  private TextField diceField;
  private TextField laddersField;
  private TextField penaltyField;

  public SnakesAndLaddersRuleSelectionView(Stage primaryStage) {
    super(primaryStage);
    this.factory = new SnakesAndLaddersFactory();
    this.controller = new SnakesAndLaddersRuleSelectionController(factory);
  }

  @Override
  protected void initializeCustomComponents() {
    difficultyGroup = new ToggleGroup();
    diceField = createTextField("1");
    laddersField = createTextField("8");
    penaltyField = createTextField("8");
  }

  @Override
  protected void layoutCustomComponents() {
    GridPane settingsGrid = new GridPane();
    settingsGrid.setVgap(15);
    settingsGrid.setHgap(15);
    settingsGrid.setPadding(new Insets(20, 0, 0, 0));

    // Difficulty section
    Label difficultyLabel = new Label("Difficulty:");
    difficultyLabel.setStyle("-fx-font-weight: bold;");
    HBox difficultyBox = new HBox(20,
        createRadioButton("Easy", "easy"),
        createRadioButton("Normal", "default"),
        createRadioButton("Hard", "hard")
    );
    settingsGrid.addRow(0, difficultyLabel, difficultyBox);

    // Game settings
    settingsGrid.addRow(1, new Label("Number of Dice:"), diceField);
    settingsGrid.addRow(2, new Label("Number of Ladders:"), laddersField);
    settingsGrid.addRow(3, new Label("Number of Penalty Fields:"), penaltyField);

    layout.add(settingsGrid, 0, 2, 2, 1);
  }

  @Override
  protected void setupEventHandlers() {
    startGameButton.setOnAction(e -> onStart(players));
    backButton.setOnAction(e -> onBack());
  }

  @Override
  protected void onStart(List<Player> players) {
    try {
      int diceCount = Integer.parseInt(diceField.getText());
      int ladders = Integer.parseInt(laddersField.getText());
      int penalties = Integer.parseInt(penaltyField.getText());

      if (!controller.validateInput(diceCount, ladders, penalties)) {
        logger.warn("Invalid input: diceCount={}, ladders={}, penalties={}", diceCount, ladders, penalties);
        showErrorDialog("Invalid Input", "Please check your input values.");
        throw new InvalidGameConfigurationException("Invalid Game Configuration");
      }


      SnakesAndLadders game = controller.startGame(
          selectedDifficulty,
          diceCount,
          ladders,
          penalties,
          players);

      new GameScreenView(primaryStage, game).show();

    } catch (NumberFormatException e) {
      showErrorDialog("Invalid Input", "Please enter valid numbers in all fields.");
    } catch (Exception e) {
      logger.error("Error starting game: {}", e.getMessage());
      showErrorDialog("Game Error", "An error occurred while starting the game.");
    }
  }

  @Override
  protected void onBack() {
    // Implement back navigation logic
    logger.info("Navigating back from rules screen");
    primaryStage.close();
  }

  private RadioButton createRadioButton(String text, String difficulty) {
    RadioButton button = new RadioButton(text);
    button.setToggleGroup(difficultyGroup);
    button.setUserData(difficulty);
    button.setOnAction(e -> selectedDifficulty = difficulty);
    if (difficulty.equals("default")) button.setSelected(true);
    return button;
  }

  private TextField createTextField(String defaultValue) {
    TextField field = new TextField(defaultValue);
    field.setPrefWidth(100);
    return field;
  }

  private void showErrorDialog(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }
}
