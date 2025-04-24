package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.controller.snakesandladders.SalRuleSelectionController;
import edu.ntnu.idi.idatt.model.model_observers.SalRuleSelectionViewObserver;
import edu.ntnu.idi.idatt.view.AbstractView;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SalRuleSelectionView extends AbstractView
    implements SalRuleSelectionViewObserver {

  private static final Logger logger = LoggerFactory.getLogger(SalRuleSelectionView.class);

  private final SalRuleSelectionController controller;

  private RadioButton easyButton;
  private RadioButton normalButton;
  private RadioButton hardButton;
  private Button randomButton;
  private TextField diceField;
  private Label laddersValueLabel;
  private Label snakesValueLabel;
  private Button startGameButton;
  private Button backButton;

  private static final String BACKGROUND_IMAGE_PATH = "/images/SALGameBack.png";
  private static final String RANDOM_BUTTON_COLOR = "#9b59b6";

  public SalRuleSelectionView(SalRuleSelectionController controller) {
    this.controller = controller;
    controller.registerViewObserver(this);
  }

  @Override
  protected void createUI() {
    VBox mainContainer = new VBox(20);
    mainContainer.setAlignment(Pos.CENTER);
    mainContainer.setPadding(new Insets(30));

    try {
      Image backgroundImage = new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(BACKGROUND_IMAGE_PATH)));
      BackgroundImage background = new BackgroundImage(
          backgroundImage,
          BackgroundRepeat.NO_REPEAT,
          BackgroundRepeat.NO_REPEAT,
          BackgroundPosition.CENTER,
          new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
      );
      mainContainer.setBackground(new Background(background));
    } catch (Exception e) {
      logger.error("Failed to load background image: {}", e.getMessage());
      mainContainer.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    // Title
    Text titleText = getTitleText();

    // Description
    Text descriptionText = getDescriptionText();

    // Game settings section
    VBox gameSettingsSection = getGameSettingsSection();

    // Navigation buttons
    HBox navigationBox = getNavigationBox();

    // Add all components to main container
    mainContainer.getChildren().addAll(
        titleText,
        descriptionText,
        new Separator(),
        gameSettingsSection,
        new Separator(),
        navigationBox
    );

    root = mainContainer;
    setupEventHandlers();
  }

  private HBox getNavigationBox() {
    HBox navigationBox = new HBox(20);
    navigationBox.setAlignment(Pos.CENTER);

    backButton = new Button("Back");
    backButton.setPrefWidth(100);

    startGameButton = new Button("Start Game");
    startGameButton.setPrefWidth(100);
    startGameButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

    navigationBox.getChildren().addAll(backButton, startGameButton);
    return navigationBox;
  }

  private VBox getGameSettingsSection() {
    return createGameSettingsSection();
  }

  private Text getDescriptionText() {
    Text descriptionText = new Text("Customize your game settings before starting");
    descriptionText.setFont(Font.font("System", FontWeight.NORMAL, 16));
    return descriptionText;
  }

  private Text getTitleText() {
    Text titleText = new Text("Snakes and Ladders - Game Rules");
    titleText.setFont(Font.font("System", FontWeight.BOLD, 24));
    return titleText;
  }

  private RadioButton createRadioButton(String text, String userData, ToggleGroup group) {
    RadioButton button = new RadioButton(text);
    button.setUserData(userData);
    button.setToggleGroup(group);
    return button;
  }

  private Button createStyledButton() {
    Button button = new Button("Random Board");
    button.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: %s;",
        SalRuleSelectionView.RANDOM_BUTTON_COLOR, "white"));
    return button;
  }

  private void setupDifficultyControls() {
    ToggleGroup difficultyGroup = new ToggleGroup();

    easyButton = createRadioButton("Easy", "easy", difficultyGroup);
    normalButton = createRadioButton("Normal", "default", difficultyGroup);
    hardButton = createRadioButton("Hard", "hard", difficultyGroup);

    randomButton = createStyledButton();
    normalButton.setSelected(true);

    difficultyGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        String difficulty = (String) newValue.getUserData();
        controller.setDifficulty(difficulty);
        logger.info("Selected difficulty: {}", difficulty);
      }
    });
  }

  private void setupDiceControls() {
    diceField = new TextField("1");
    diceField.setPrefWidth(60);

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

  private VBox createGameSettingsSection() {
    setupDifficultyControls();
    setupDiceControls();

    VBox gameSettingsSection = new VBox(20);
    Label difficultyLabel = new Label("Game Difficulty");
    difficultyLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

    HBox difficultyBox = new HBox(20, easyButton, normalButton, hardButton);
    difficultyBox.setAlignment(Pos.CENTER);

    HBox randomBox = new HBox(randomButton);
    randomBox.setAlignment(Pos.CENTER);
    randomBox.setPadding(new Insets(5, 0, 0, 0));

    VBox difficultySection = new VBox(10, difficultyLabel, difficultyBox, randomBox);

    Label boardLabel = new Label("Board Configuration");
    boardLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

    GridPane boardGrid = new GridPane();
    boardGrid.setHgap(15);
    boardGrid.setVgap(10);
    boardGrid.setAlignment(Pos.CENTER);

    laddersValueLabel = new Label(String.valueOf(controller.getLadderCount()));
    snakesValueLabel = new Label(String.valueOf(controller.getSnakeCount()));

    Label laddersLabel = new Label("Number of Ladders:");
    boardGrid.add(laddersLabel, 0, 0);
    boardGrid.add(laddersValueLabel, 1, 0);

    Label snakesLabel = new Label("Number of Snakes:");
    boardGrid.add(snakesLabel, 0, 1);
    boardGrid.add(snakesValueLabel, 1, 1);

    Label diceLabel = new Label("Game Settings");
    diceLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

    GridPane diceGrid = new GridPane();
    diceGrid.setHgap(15);
    diceGrid.setVgap(10);
    diceGrid.setAlignment(Pos.CENTER);

    Label diceCountLabel = new Label("Number of Dice:");
    diceGrid.add(diceCountLabel, 0, 0);
    diceGrid.add(diceField, 1, 0);

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
    backButton.setOnAction(e -> controller.navigateBack());
    randomButton.setOnAction(e -> controller.selectRandomBoard());
  }

  @Override
  protected void applyInitialUIState() {
    controller.initializeDefaultSettings();
  }

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
    laddersValueLabel.setText(String.valueOf(count));
  }

  @Override
  public void onSnakeCountChanged(int count) {
    if (!uiInitialized) return;
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
}
