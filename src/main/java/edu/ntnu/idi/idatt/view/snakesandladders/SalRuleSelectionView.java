//package edu.ntnu.idi.idatt.view.snakesandladders;
//
//import edu.ntnu.idi.idatt.controller.snakesandladders.SalRuleSelectionController;
//import edu.ntnu.idi.idatt.model.model_observers.SalRuleSelectionViewObserver;
//import edu.ntnu.idi.idatt.view.common.AbstractRuleSelectionView;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.control.*;
//import javafx.scene.layout.GridPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class SalRuleSelectionView extends AbstractRuleSelectionView
//    implements SalRuleSelectionViewObserver {
//
//  private static final Logger logger = LoggerFactory.getLogger(SalRuleSelectionView.class);
//
//  private final SalRuleSelectionController controller;
//
//  private RadioButton easyButton;
//  private RadioButton normalButton;
//  private RadioButton hardButton;
//  private Button randomButton;
//  private TextField diceField;
//  private Label laddersValueLabel;
//  private Label snakesValueLabel;
//
//  private static final String BACKGROUND_IMAGE_PATH = "/images/SALGameBack.png";
//  private static final String RANDOM_BUTTON_COLOR = "#9b59b6";
//
//  public SalRuleSelectionView(SalRuleSelectionController controller) {
//    super();
//    this.controller = controller;
//    controller.registerViewObserver(this);
//  }
//
//  @Override
//  protected String getBackgroundImagePath() {
//    return BACKGROUND_IMAGE_PATH;
//  }
//
//  @Override
//  protected String getViewTitle() {
//    return "Snakes and Ladders - Game Rules";
//  }
//
//  @Override
//  protected String getViewDescription() {
//    return "Customize your game settings before starting";
//  }
//
//  @Override
//  protected void setupGameSpecificControls() {
//    setupDifficultyControls();
//    setupDiceControls();
//  }
//
//  public void show() {
//    super.show();
//    controller.displayRuleSelection(this);
//  }
//
//  private void setupDifficultyControls() {
//    ToggleGroup difficultyGroup = new ToggleGroup();
//
//    easyButton = createRadioButton("Easy", "easy", difficultyGroup);
//    normalButton = createRadioButton("Normal", "default", difficultyGroup);
//    hardButton = createRadioButton("Hard", "hard", difficultyGroup);
//
//    randomButton = createStyledButton("Random Board", RANDOM_BUTTON_COLOR, "white");
//    normalButton.setSelected(true);
//
//    difficultyGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
//      if (newValue != null) {
//        String difficulty = (String) newValue.getUserData();
//        controller.setDifficulty(difficulty);
//        logger.info("Selected difficulty: {}", difficulty);
//      }
//    });
//  }
//
//  private void setupDiceControls() {
//    diceField = new TextField("1");
//    diceField.setPrefWidth(60);
//
//    diceField.textProperty().addListener((observable, oldValue, newValue) -> {
//      try {
//        if (!newValue.isEmpty()) {
//          int diceCount = Integer.parseInt(newValue);
//          if (diceCount >= 1 && diceCount <= 3) {
//            controller.setDiceCount(diceCount);
//          } else {
//            diceField.setText(oldValue);
//          }
//        }
//      } catch (NumberFormatException ex) {
//        diceField.setText(oldValue);
//      }
//    });
//  }
//
//  @Override
//  protected VBox createGameSettingsSection() {
//    VBox gameSettingsSection = new VBox(20);
//    Label difficultyLabel = new Label("Game Difficulty");
//    difficultyLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
//
//    HBox difficultyBox = new HBox(20, easyButton, normalButton, hardButton);
//    difficultyBox.setAlignment(Pos.CENTER);
//
//    HBox randomBox = new HBox(randomButton);
//    randomBox.setAlignment(Pos.CENTER);
//    randomBox.setPadding(new Insets(5, 0, 0, 0));
//
//    VBox difficultySection = new VBox(10, difficultyLabel, difficultyBox, randomBox);
//
//    Label boardLabel = new Label("Board Configuration");
//    boardLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
//
//    GridPane boardGrid = new GridPane();
//    boardGrid.setHgap(15);
//    boardGrid.setVgap(10);
//    boardGrid.setAlignment(Pos.CENTER);
//
//    laddersValueLabel = new Label(String.valueOf(controller.getLadderCount()));
//    snakesValueLabel = new Label(String.valueOf(controller.getSnakeCount()));
//
//    Label laddersLabel = new Label("Number of Ladders:");
//    boardGrid.add(laddersLabel, 0, 0);
//    boardGrid.add(laddersValueLabel, 1, 0);
//
//    Label snakesLabel = new Label("Number of Snakes:");
//    boardGrid.add(snakesLabel, 0, 1);
//    boardGrid.add(snakesValueLabel, 1, 1);
//    Label diceLabel = new Label("Game Settings");
//    diceLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
//
//    GridPane diceGrid = new GridPane();
//    diceGrid.setHgap(15);
//    diceGrid.setVgap(10);
//    diceGrid.setAlignment(Pos.CENTER);
//
//    Label diceCountLabel = new Label("Number of Dice:");
//    diceGrid.add(diceCountLabel, 0, 0);
//    diceGrid.add(diceField, 1, 0);
//    gameSettingsSection.getChildren().addAll(
//        difficultySection,
//        new Separator(),
//        boardLabel,
//        boardGrid,
//        new Separator(),
//        diceLabel,
//        diceGrid
//    );
//
//    return gameSettingsSection;
//  }
//
//  @Override
//  protected void setupEventHandlers() {
//    startGameButton.setOnAction(e -> controller.startGame());
//    backButton.setOnAction(e -> controller.navigateBack());
//    randomButton.setOnAction(e -> controller.selectRandomBoard());
//  }
//
//  @Override
//  protected void applyInitialUIState() {
//    controller.initializeDefaultSettings();
//  }
//
//  @Override
//  public void onDifficultyChanged(String difficulty) {
//    if (!uiInitialized) return;
//
//    switch (difficulty.toLowerCase()) {
//      case "easy":
//        easyButton.setSelected(true);
//        break;
//      case "default":
//        normalButton.setSelected(true);
//        break;
//      case "hard":
//        hardButton.setSelected(true);
//        break;
//      default:
//        break;
//    }
//  }
//
//  @Override
//  public void onLadderCountChanged(int count) {
//    if (!uiInitialized) return;
//    laddersValueLabel.setText(String.valueOf(count));
//  }
//
//  @Override
//  public void onSnakeCountChanged(int count) {
//    if (!uiInitialized) return;
//    snakesValueLabel.setText(String.valueOf(count));
//  }
//
//  @Override
//  public void onRandomBoardSelected(int boardNumber) {
//    if (!uiInitialized) return;
//
//    easyButton.setSelected(false);
//    normalButton.setSelected(false);
//    hardButton.setSelected(false);
//
//    laddersValueLabel.setText(controller.getLadderCount() + " (Board " + boardNumber + ")");
//    snakesValueLabel.setText(controller.getSnakeCount() + " (Board " + boardNumber + ")");
//  }
//
//  @Override
//  public void onDiceCountChanged(int count) {
//    if (!uiInitialized) return;
//    diceField.setText(String.valueOf(count));
//  }
//
//}
