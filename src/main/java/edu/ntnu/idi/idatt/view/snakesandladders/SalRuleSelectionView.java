package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.controller.snakesandladders.SalRuleSelectionController;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.model_observers.SalRuleSelectionViewObserver;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.common.AbstractRuleSelectionView;
import edu.ntnu.idi.idatt.view.common.GameScreenView;
import edu.ntnu.idi.idatt.view.common.IntroScreenView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalRuleSelectionView extends AbstractRuleSelectionView
    implements SalRuleSelectionViewObserver {

  private static final Logger logger = LoggerFactory.getLogger(SalRuleSelectionView.class);

  // Controller
  private final SalRuleSelectionController controller;

  private RadioButton easyButton;
  private RadioButton normalButton;
  private RadioButton hardButton;
  private Button randomButton;
  private TextField diceField;
  private Slider ladderSlider;
  private Slider snakeSlider;
  private Label laddersValueLabel;
  private Label snakesValueLabel;

  private static final String BACKGROUND_IMAGE_PATH = "/images/snakes_ladders_bg.png";
  private static final String RANDOM_BUTTON_COLOR = "#9b59b6";

  public SalRuleSelectionView(SalRuleSelectionController controller) {
    super();
    this.controller = controller;
    controller.registerViewObserver(this);
    controller.setNavigationHandler(this);
  }

  @Override
  protected String getBackgroundImagePath() {
    return BACKGROUND_IMAGE_PATH;
  }

  @Override
  protected String getViewTitle() {
    return "Snakes and Ladders - Game Rules";
  }

  @Override
  protected String getViewDescription() {
    return "Customize your game settings before starting";
  }

  @Override
  protected void setupGameSpecificControls() {
    setupDifficultyControls();
    setupBoardControls();
    setupDiceControls();
  }

  private void setupDifficultyControls() {
    ToggleGroup difficultyGroup = new ToggleGroup();

    easyButton = createRadioButton("Easy", "easy", difficultyGroup);
    normalButton = createRadioButton("Normal", "default", difficultyGroup);
    hardButton = createRadioButton("Hard", "hard", difficultyGroup);

    randomButton = createRandomButton();

    // Default selection
    normalButton.setSelected(true);

    // Event handling
    difficultyGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        String difficulty = (String) newValue.getUserData();
        controller.setDifficulty(difficulty);
        logger.info("Selected difficulty: {}", difficulty);
      }
    });
  }

  private void setupBoardControls() {
    // Sliders for snake and ladder counts
    ladderSlider = new Slider(2, 15, 8);
    ladderSlider.setShowTickMarks(true);
    ladderSlider.setShowTickLabels(true);
    ladderSlider.setMajorTickUnit(1);
    ladderSlider.setMinorTickCount(0);
    ladderSlider.setSnapToTicks(true);

    snakeSlider = new Slider(2, 15, 8);
    snakeSlider.setShowTickMarks(true);
    snakeSlider.setShowTickLabels(true);
    snakeSlider.setMajorTickUnit(1);
    snakeSlider.setMinorTickCount(0);
    snakeSlider.setSnapToTicks(true);

    laddersValueLabel = new Label("8");
    snakesValueLabel = new Label("8");

    // Event handling
    ladderSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
      int value = newVal.intValue();
      controller.setLadderCount(value);
      laddersValueLabel.setText(String.valueOf(value));
    });

    snakeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
      int value = newVal.intValue();
      controller.setSnakeCount(value);
      snakesValueLabel.setText(String.valueOf(value));
    });
  }

  private void setupDiceControls() {
    diceField = new TextField("1");
    diceField.setPrefWidth(60);

    // Event handling
    diceField.textProperty().addListener((observable, oldValue, newValue) -> {
      try {
        if (!newValue.isEmpty()) {
          int diceCount = Integer.parseInt(newValue);
          if (diceCount >= 1 && diceCount <= 3) {
            controller.setDiceCount(diceCount);
          } else {
            diceField.setText(oldValue);
          }
        }
      } catch (NumberFormatException ex) {
        diceField.setText(oldValue);
      }
    });
  }

  private RadioButton createRadioButton(String text, String userData, ToggleGroup group) {
    RadioButton button = new RadioButton(text);
    button.setToggleGroup(group);
    button.setUserData(userData);
    return button;
  }

  private Button createRandomButton() {
    Button btn = new Button("Random Board");
    btn.setStyle("-fx-background-color: " + RANDOM_BUTTON_COLOR + "; -fx-text-fill: white;");
    return btn;
  }

  @Override
  protected VBox createGameSettingsSection() {
    VBox gameSettingsSection = new VBox(20);

    // Difficulty section
    Label difficultyLabel = new Label("Game Difficulty");
    difficultyLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

    HBox difficultyBox = new HBox(20, easyButton, normalButton, hardButton);
    difficultyBox.setAlignment(Pos.CENTER);

    HBox randomBox = new HBox(randomButton);
    randomBox.setAlignment(Pos.CENTER);
    randomBox.setPadding(new Insets(5, 0, 0, 0));

    VBox difficultySection = new VBox(10, difficultyLabel, difficultyBox, randomBox);

    // Board settings section
    Label boardLabel = new Label("Board Configuration");
    boardLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

    GridPane boardGrid = new GridPane();
    boardGrid.setHgap(15);
    boardGrid.setVgap(10);
    boardGrid.setAlignment(Pos.CENTER);

    Label laddersLabel = new Label("Number of Ladders:");
    boardGrid.add(laddersLabel, 0, 0);
    boardGrid.add(ladderSlider, 1, 0);
    boardGrid.add(laddersValueLabel, 2, 0);

    Label snakesLabel = new Label("Number of Snakes:");
    boardGrid.add(snakesLabel, 0, 1);
    boardGrid.add(snakeSlider, 1, 1);
    boardGrid.add(snakesValueLabel, 2, 1);

    // Dice settings section
    Label diceLabel = new Label("Game Settings");
    diceLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

    GridPane diceGrid = new GridPane();
    diceGrid.setHgap(15);
    diceGrid.setVgap(10);
    diceGrid.setAlignment(Pos.CENTER);

    Label diceCountLabel = new Label("Number of Dice:");
    diceGrid.add(diceCountLabel, 0, 0);
    diceGrid.add(diceField, 1, 0);

    // Add all sections to the main container
    gameSettingsSection.getChildren().addAll(
        difficultySection,
        new Separator(),
        boardLabel,
        boardGrid,
        new Separator(),
        diceLabel,
        diceGrid
    );

    return gameSettingsSection;
  }

  @Override
  protected void setupEventHandlers() {
    startGameButton.setOnAction(e -> controller.startGame());
    backButton.setOnAction(e -> navigateBack());
    randomButton.setOnAction(e -> controller.selectRandomBoard());
  }

  @Override
  protected void initializeDefaultSettings() {
    controller.initializeDefaultSettings();
  }

  // RuleSelectionViewObserver implementation
  @Override
  public void onDifficultyChanged(String difficulty) {
    if (!uiInitialized) return;

    switch (difficulty.toLowerCase()) {
      case "easy":
        easyButton.setSelected(true);
        break;
      case "default":
        normalButton.setSelected(true);
        break;
      case "hard":
        hardButton.setSelected(true);
        break;
      default:
        break;
    }
  }

  @Override
  public void onLadderCountChanged(int count) {
    if (!uiInitialized) return;
    ladderSlider.setValue(count);
    laddersValueLabel.setText(String.valueOf(count));
  }

  @Override
  public void onSnakeCountChanged(int count) {
    if (!uiInitialized) return;
    snakeSlider.setValue(count);
    snakesValueLabel.setText(String.valueOf(count));
  }

  @Override
  public void onRandomBoardSelected(int boardNumber) {
    if (!uiInitialized) return;

    easyButton.setSelected(false);
    normalButton.setSelected(false);
    hardButton.setSelected(false);

    laddersValueLabel.setText(controller.getLadderCount() + " (Board " + boardNumber + ")");
    snakesValueLabel.setText(controller.getSnakeCount() + " (Board " + boardNumber + ")");
  }

  @Override
  public void onDiceCountChanged(int count) {
    if (!uiInitialized) return;
    diceField.setText(String.valueOf(count));
  }

  @Override
  public void navigateTo(String destination) {
    switch (destination) {
      case "GAME_SCREEN":
        SnakesAndLadders game = controller.createGame();
        String boardFile = controller.getBoardFile();

        GameScreenView gameView = new GameScreenView(game, boardFile);
        NavigationManager.getInstance().setRoot(gameView.getRoot());
        logger.info("Navigated to Game Screen");
        break;

      case "INTRO_SCREEN":
        IntroScreenView introView = new IntroScreenView();
        NavigationManager.getInstance().setRoot(introView.getRoot());
        logger.info("Navigated to Intro Screen");
        break;

      default:
        logger.warn("Unknown destination: {}", destination);
        break;
    }
  }
}
