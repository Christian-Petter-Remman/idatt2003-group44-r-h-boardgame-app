package edu.ntnu.idi.idatt.view.snakesandladders;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.controller.snakesandladders.SnakesAndLaddersRuleSelectionController;
import edu.ntnu.idi.idatt.model.model_observers.DifficultyObserver;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.common.Player;
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

public class SnakesAndLaddersRuleSelectionView extends AbstractRuleSelectionView implements DifficultyObserver {
  private static final Logger logger = LoggerFactory.getLogger(SnakesAndLaddersRuleSelectionView.class);

  private final SnakesAndLaddersRuleSelectionController controller;
  private List<Player> players;

  private String selectedDifficulty = "default";
  private TextField diceField;
  private TextField laddersField;
  private TextField penaltyField;

  private ToggleGroup difficultyGroup;

  public SnakesAndLaddersRuleSelectionView(Stage primaryStage) {
    super(primaryStage);
    controller = new SnakesAndLaddersRuleSelectionController(new SnakesAndLaddersFactory());
    controller.addObserver(this);
  }

  @Override
  protected void initializeCustomComponents() {
    diceField = createTextField("1");
    laddersField = createTextField("8");
    penaltyField = createTextField("8");

    difficultyGroup = new ToggleGroup();
    createRadioButton("Easy", "easy", difficultyGroup);
    createRadioButton("Normal", "default", difficultyGroup);
    createRadioButton("Hard", "hard", difficultyGroup);

    difficultyGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        String difficulty = (String) newValue.getUserData();
        controller.setDifficulty(difficulty);
        logger.info("Selected difficulty: {}", difficulty);
      }
    });
  }

  @Override
  public void onDifficultyChanged(String difficulty) {
    this.selectedDifficulty = difficulty; // Update selectedDifficulty
    switch (difficulty.toLowerCase()) {
      case "easy":
        laddersField.setText("10");
        penaltyField.setText("4");
        break;
      case "default":
        laddersField.setText("8");
        penaltyField.setText("8");
        break;
      case "hard":
        laddersField.setText("5");
        penaltyField.setText("5");
        break;
      default:
        logger.warn("Invalid difficulty: {}", difficulty);
        break;
    }
  }

  @Override
  protected void layoutCustomComponents() {
    GridPane settingsGrid = new GridPane();
    settingsGrid.setVgap(15);
    settingsGrid.setHgap(15);
    settingsGrid.setPadding(new Insets(20));

    Label difficultyLabel = new Label("Difficulty:");
    HBox difficultyBox = new HBox(20,
        createRadioButton("Easy", "easy", difficultyGroup),
        createRadioButton("Normal", "default", difficultyGroup),
        createRadioButton("Hard", "hard", difficultyGroup)
    );
    settingsGrid.addRow(0, difficultyLabel, difficultyBox);

    settingsGrid.addRow(1, new Label("Number of Dice:"), diceField);
    settingsGrid.addRow(2, new Label("Number of Ladders:"), laddersField);
    settingsGrid.addRow(3, new Label("Number of Penalty Fields:"), penaltyField);

    HBox buttonBox = new HBox(20, backButton, startGameButton);
    buttonBox.setAlignment(Pos.CENTER);

    layout.add(settingsGrid, 0, 2);
    layout.add(buttonBox, 0, 3);
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

      SnakesAndLadders game = controller.startGame(
          selectedDifficulty,
          diceCount,
          ladders,
          penalties,
          players);

      new GameScreenView(primaryStage, game).show();

    } catch (NumberFormatException e) {
      logger.error("Error with starting game: {}", e.getMessage());
      showAlert("Invalid Input", "Please enter valid numbers in all fields.");
    } catch (Exception e) {
      logger.error("Error starting game: {}", e.getMessage());
      showAlert("Game Error", "An error occurred while starting the game.");
    }
  }

  @Override
  protected void onBack() {
    logger.info("Navigating back from rules screen");
    primaryStage.close();
  }

  private RadioButton createRadioButton(String text, String difficulty, ToggleGroup group) {
    RadioButton button = new RadioButton(text);
    button.setToggleGroup(group);
    button.setUserData(difficulty);
    if (difficulty.equalsIgnoreCase(selectedDifficulty)) button.setSelected(true);
    return button;
  }

  private TextField createTextField(String defaultValue) {
    TextField field = new TextField(defaultValue);
    field.setPrefWidth(100);
    return field;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }
}
