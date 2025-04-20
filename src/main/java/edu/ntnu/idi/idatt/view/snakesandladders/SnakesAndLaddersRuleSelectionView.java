package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.controller.snakesandladders.SnakesAndLaddersRuleSelectionController;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.RuleSelectionViewObserver;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.view.common.GameScreenView;
import edu.ntnu.idi.idatt.view.common.IntroScreenView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class SnakesAndLaddersRuleSelectionView implements RuleSelectionViewObserver, NavigationHandler {
  private static final Logger logger = LoggerFactory.getLogger(SnakesAndLaddersRuleSelectionView.class);

  private final SnakesAndLaddersRuleSelectionController controller;
  private final BorderPane root;

  private GridPane layout;
  private Label titleLabel;
  private Label descriptionLabel;
  private RadioButton easyButton;
  private RadioButton normalButton;
  private RadioButton hardButton;
  private Button randomButton;
  private TextField diceField;
  private Label laddersValueLabel;
  private Label penaltyValueLabel;
  private Button startGameButton;
  private Button backButton;

  private static final String SECONDARY_COLOR = "#2c3e50";
  private static final String BACKGROUND_COLOR = "#ecf0f1";
  private static final String RANDOM_BUTTON_COLOR = "#9b59b6";
  private static final String RANDOM_BUTTON_HOVER = "#8e44ad";
  private static final String BACKGROUND_IMAGE_PATH = "/images/SALGameBack.png";

  private boolean uiInitialized = false;

  public SnakesAndLaddersRuleSelectionView(SnakesAndLaddersRuleSelectionController controller) {
    this.controller = controller;
    this.root = new BorderPane();
    controller.registerViewObserver(this);
    controller.setNavigationHandler(this);
  }

  public void show() {
    initializeLayout();
    setupBackgroundImage();
    setupTitleAndDescription();
    setupDifficultyControls();
    setupGameSettingsControls();
    setupButtons();
    layoutComponents();
    setupEventHandlers();

    uiInitialized = true;

    controller.initializeDefaultSettings();

    NavigationManager.getInstance().setRoot(root);
  }

  private void initializeLayout() {
    layout = new GridPane();
    layout.setHgap(20);
    layout.setVgap(20);
    layout.setPadding(new Insets(30, 40, 40, 40));
    layout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
    root.setCenter(layout);
  }

  private void setupBackgroundImage() {
    try {
      Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(BACKGROUND_IMAGE_PATH)));

      BackgroundImage background = new BackgroundImage(
          backgroundImage,
          BackgroundRepeat.NO_REPEAT,
          BackgroundRepeat.NO_REPEAT,
          BackgroundPosition.CENTER,
          new BackgroundSize(1.0, 1.0, true, true, false, false)
      );

      StackPane backgroundPane = new StackPane();
      backgroundPane.setBackground(new Background(background));
      backgroundPane.setOpacity(0.3);

      layout.getChildren().addFirst(backgroundPane);
      GridPane.setRowSpan(backgroundPane, Integer.MAX_VALUE);
      GridPane.setColumnSpan(backgroundPane, Integer.MAX_VALUE);

      logger.info("Background image loaded successfully");
    } catch (Exception e) {
      logger.error("Failed to load background image: {}", e.getMessage());
    }
  }

  private void setupTitleAndDescription() {
    titleLabel = new Label("Game Rules Configuration");
    titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
    titleLabel.setTextFill(Color.web(SECONDARY_COLOR));

    descriptionLabel = new Label("Customize your game settings below");
    descriptionLabel.setFont(Font.font("System", FontWeight.NORMAL, 16));
    descriptionLabel.setTextFill(Color.web(SECONDARY_COLOR));
  }

  private void setupDifficultyControls() {
    ToggleGroup difficultyGroup = new ToggleGroup();

    easyButton = createRadioButton("Easy", "easy", difficultyGroup);
    normalButton = createRadioButton("Normal", "default", difficultyGroup);
    hardButton = createRadioButton("Hard", "hard", difficultyGroup);

    randomButton = createRandomButton();

    normalButton.setSelected(true);

    difficultyGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        String difficulty = (String) newValue.getUserData();
        controller.setDifficulty(difficulty);
        logger.info("Selected difficulty: {}", difficulty);
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

    try {
      ImageView diceIcon = new ImageView(new Image(Objects.requireNonNull(
          getClass().getResourceAsStream("/images/dice_icon.png"))));
      diceIcon.setFitHeight(16);
      diceIcon.setFitWidth(16);
      btn.setGraphic(diceIcon);
    } catch (Exception e) {
      logger.warn("Could not load dice icon: {}", e.getMessage());
    }

    btn.setStyle("-fx-background-color: " + RANDOM_BUTTON_COLOR + "; " +
        "-fx-text-fill: white; " +
        "-fx-padding: 8 15;");

    DropShadow shadow = new DropShadow();
    shadow.setColor(Color.color(0, 0, 0, 0.3));
    shadow.setOffsetX(1);
    shadow.setOffsetY(1);
    btn.setEffect(shadow);

    btn.setOnMouseEntered(e ->
        btn.setStyle("-fx-background-color: " + RANDOM_BUTTON_HOVER + "; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 8 15;"));

    btn.setOnMouseExited(e ->
        btn.setStyle("-fx-background-color: " + RANDOM_BUTTON_COLOR + "; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 8 15;"));

    return btn;
  }

  private void setupGameSettingsControls() {
    diceField = new TextField("1");
    diceField.setPrefWidth(80);
    diceField.setMaxWidth(80);

    laddersValueLabel = new Label("8");
    laddersValueLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

    penaltyValueLabel = new Label("8");
    penaltyValueLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
  }

  private void setupButtons() {
    startGameButton = createNavigationButton("Start Game", "#27ae60", "white");
    backButton = createNavigationButton("Back", "#bdc3c7", "#2c3e50");
  }

  private Button createNavigationButton(String text, String bgColor, String textColor) {
    Button btn = new Button(text);
    btn.setStyle("-fx-font-size: 14px; " +
        "-fx-padding: 10 20; " +
        "-fx-min-width: 120px; " +
        "-fx-background-color: " + bgColor + "; " +
        "-fx-text-fill: " + textColor + "; " +
        "-fx-background-radius: 5px;");
    return btn;
  }

  private void layoutComponents() {
    BorderPane mainContainer = new BorderPane();
    mainContainer.setPadding(new Insets(20));

    VBox contentBox = new VBox(20);
    contentBox.setMaxWidth(600);
    contentBox.setAlignment(Pos.CENTER);
    contentBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); " +
        "-fx-background-radius: 10; " +
        "-fx-padding: 20;");

    DropShadow shadow = new DropShadow();
    shadow.setColor(Color.color(0, 0, 0, 0.3));
    shadow.setRadius(10);
    shadow.setOffsetX(0);
    shadow.setOffsetY(2);
    contentBox.setEffect(shadow);

    VBox difficultySection = createDifficultySection();
    VBox gameSettingsSection = createGameSettingsSection();
    HBox buttonBox = createNavigationButtonBox();

    contentBox.getChildren().addAll(difficultySection, gameSettingsSection, buttonBox);

    mainContainer.setCenter(contentBox);

    layout.add(mainContainer, 0, 2);
    GridPane.setHgrow(mainContainer, Priority.ALWAYS);
    GridPane.setVgrow(mainContainer, Priority.ALWAYS);

    centerTitleAndDescription();
  }

  private VBox createDifficultySection() {
    VBox difficultySection = new VBox(10);
    Label difficultyLabel = new Label("Game Difficulty");
    difficultyLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

    HBox difficultyBox = new HBox(20, easyButton, normalButton, hardButton);
    difficultyBox.setAlignment(Pos.CENTER);

    HBox randomBox = new HBox(randomButton);
    randomBox.setAlignment(Pos.CENTER);
    randomBox.setPadding(new Insets(5, 0, 0, 0));

    difficultySection.getChildren().addAll(difficultyLabel, difficultyBox, randomBox);
    return difficultySection;
  }

  private VBox createGameSettingsSection() {
    VBox gameSettingsSection = new VBox(10);
    Label settingsLabel = new Label("Game Settings");
    settingsLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

    GridPane settingsGrid = new GridPane();
    settingsGrid.setHgap(15);
    settingsGrid.setVgap(10);
    settingsGrid.setAlignment(Pos.CENTER);

    Label diceLabel = new Label("Number of Dice:");
    settingsGrid.add(diceLabel, 0, 0);
    settingsGrid.add(diceField, 1, 0);

    Label laddersLabel = new Label("Number of Ladders:");
    settingsGrid.add(laddersLabel, 0, 1);
    settingsGrid.add(laddersValueLabel, 1, 1);

    Label penaltyLabel = new Label("Number of Penalty Fields:");
    settingsGrid.add(penaltyLabel, 0, 2);
    settingsGrid.add(penaltyValueLabel, 1, 2);

    gameSettingsSection.getChildren().addAll(settingsLabel, settingsGrid);
    return gameSettingsSection;
  }

  private HBox createNavigationButtonBox() {
    HBox buttonBox = new HBox(20, backButton, startGameButton);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setPadding(new Insets(20, 0, 0, 0));
    return buttonBox;
  }

  private void centerTitleAndDescription() {
    StackPane titlePane = new StackPane(titleLabel);
    titlePane.setAlignment(Pos.CENTER);
    layout.add(titlePane, 0, 0);
    GridPane.setHgrow(titlePane, Priority.ALWAYS);

    StackPane descPane = new StackPane(descriptionLabel);
    descPane.setAlignment(Pos.CENTER);
    layout.add(descPane, 0, 1);
    GridPane.setHgrow(descPane, Priority.ALWAYS);
  }

  private void setupEventHandlers() {
    startGameButton.setOnAction(e -> controller.startGame());
    backButton.setOnAction(e -> navigateBack());

    diceField.textProperty().addListener((observable, oldValue, newValue) -> {
      try {
        if (!newValue.isEmpty()) {
          int diceCount = Integer.parseInt(newValue);
          controller.setDiceCount(diceCount);
        }
      } catch (NumberFormatException ex) {
        diceField.setText(oldValue);
      }
    });

    randomButton.setOnAction(e -> controller.selectRandomBoard());
  }

  @Override
  public void onDifficultyChanged(String difficulty) {
    if (!uiInitialized) {
      logger.info("UI not yet initialized, skipping update");
      return;
    }

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
      case "random":
        easyButton.setSelected(false);
        normalButton.setSelected(false);
        hardButton.setSelected(false);
        break;
      default:
        logger.warn("Invalid difficulty: {}", difficulty);
        break;
    }
  }

  @Override
  public void onLadderCountChanged(int count) {
    if (uiInitialized) {
      laddersValueLabel.setText(String.valueOf(count));
    }
  }

  @Override
  public void onSnakeCountChanged(int count) {
    if (uiInitialized) {
      penaltyValueLabel.setText(String.valueOf(count));
    }
  }

  @Override
  public void onRandomBoardSelected(int boardNumber) {
    if (uiInitialized && boardNumber > 0) {
      laddersValueLabel.setText(controller.getLadderCount() + " (Board " + boardNumber + ")");
      penaltyValueLabel.setText(controller.getSnakeCount() + " (Board " + boardNumber + ")");
    }
  }

  @Override
  public void onDiceCountChanged(int count) {
    if (uiInitialized) {
      diceField.setText(String.valueOf(count));
    }
  }

  @Override
  public void navigateTo(String destination) {
    switch (destination) {
      case "GAME_SCREEN":
        SnakesAndLadders game = controller.createGame();
        String boardFile = controller.getBoardFile();
        String csvFileName = controller.getCsvFileName();

        GameScreenView gameView = new GameScreenView(game, boardFile, csvFileName);
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

  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
    logger.info("Navigated back");
  }

  public BorderPane getRoot() {
    return root;
  }

  public void setPlayers(List<Player> players) {
    controller.setPlayers(players);
  }

  public void setBaseName(String baseName) {
    controller.setBaseName(baseName);
  }
}
