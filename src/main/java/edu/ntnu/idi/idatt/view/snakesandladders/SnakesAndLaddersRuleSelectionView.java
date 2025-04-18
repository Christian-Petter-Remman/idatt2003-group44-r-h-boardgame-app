package edu.ntnu.idi.idatt.view.snakesandladders;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.controller.snakesandladders.SnakesAndLaddersRuleSelectionController;
import edu.ntnu.idi.idatt.filehandling.BoardJsonHandler;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.model_observers.DifficultyObserver;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.view.common.AbstractRuleSelectionView;
import edu.ntnu.idi.idatt.view.common.GameScreenView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class SnakesAndLaddersRuleSelectionView extends AbstractRuleSelectionView implements DifficultyObserver {
  private static final Logger logger = LoggerFactory.getLogger(SnakesAndLaddersRuleSelectionView.class);
  BoardJsonHandler boardJsonHandler;

  private final SnakesAndLaddersRuleSelectionController controller;
  private List<Player> players;

  private String selectedDifficulty = "default";
  private TextField diceField;
  private Label laddersValueLabel;
  private Label penaltyValueLabel;

  private RadioButton easyButton;
  private RadioButton normalButton;
  private RadioButton hardButton;
  private Button randomButton;
  private String baseName;

  private boolean uiInitialized = false;

  private static final String SECONDARY_COLOR = "#2c3e50";
  private static final String BACKGROUND_COLOR = "#ecf0f1";
  private static final String RANDOM_BUTTON_COLOR = "#9b59b6";
  private static final String RANDOM_BUTTON_HOVER = "#8e44ad";

  private static final String BACKGROUND_IMAGE_PATH = "/images/SALGameBack.png";

  public SnakesAndLaddersRuleSelectionView(Stage primaryStage) {
    super(primaryStage);
    controller = new SnakesAndLaddersRuleSelectionController(new SnakesAndLaddersFactory());
    this.boardJsonHandler = new BoardJsonHandler();
    controller.addObserver(this);
  }

  public void setBaseName(String baseName) {
    this.baseName = baseName;
  }

  @Override
  protected void initializeCustomComponents() {
    setupBackgroundImage();
    setupTitleAndDescription();
    setupDifficultyControls();
    setupGameSettingsControls();

    uiInitialized = true;
  }

  private void setupBackgroundImage() {
    try {
      Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
          SnakesAndLaddersRuleSelectionView.BACKGROUND_IMAGE_PATH)));

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
    layout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");

    titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
    titleLabel.setTextFill(Color.web(SECONDARY_COLOR));

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
        selectedDifficulty = controller.setDifficulty(difficulty);
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

    btn.setOnAction(e -> {
      selectedDifficulty = controller.setDifficulty("random");

      easyButton.setSelected(false);
      normalButton.setSelected(false);
      hardButton.setSelected(false);

      logger.info("Selected random board");
    });

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

  @Override
  public void onDifficultyChanged(String difficulty) {
    this.selectedDifficulty = difficulty;

    if (!uiInitialized) {
      logger.info("UI not yet initialized, skipping update");
      return;
    }

    laddersValueLabel.setText(String.valueOf(controller.getCurrentLadderCount()));
    penaltyValueLabel.setText(String.valueOf(controller.getCurrentSnakeCount()));

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

        if (controller.getSelectedRandomBoard() > 0) {
          laddersValueLabel.setText(controller.getCurrentLadderCount() +
              " (Board " + controller.getSelectedRandomBoard() + ")");
          penaltyValueLabel.setText(controller.getCurrentSnakeCount() +
              " (Board " + controller.getSelectedRandomBoard() + ")");
        }
        break;
      default:
        logger.warn("Invalid difficulty: {}", difficulty);
        break;
    }
  }

  @Override
  protected void layoutCustomComponents() {
    BorderPane mainContainer = createMainContainer();

    VBox contentBox = createContentBox();

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

  private BorderPane createMainContainer() {
    BorderPane mainContainer = new BorderPane();
    mainContainer.setPadding(new Insets(20));
    return mainContainer;
  }

  private VBox createContentBox() {
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

    return contentBox;
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

  @Override
  protected Button createNavigationButton(String text, String bgColor, String textColor) {
    Button btn = new Button(text);
    btn.setStyle("-fx-font-size: 14px; " +
        "-fx-padding: 10 20; " +
        "-fx-min-width: 120px; " +
        "-fx-background-color: " + bgColor + "; " +
        "-fx-text-fill: " + textColor + "; " +
        "-fx-background-radius: 5px;");
    return btn;
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

      String gameToStart = controller.GetBoardFile(selectedDifficulty);
      SnakesAndLadders snakes = boardJsonHandler.loadGameFromFile(gameToStart, SnakesAndLadders::new);

      String csvPath = "data/user-data/player-files/" + baseName + ".csv";

      int playersLoaded = snakes.loadPlayersFromCsv(csvPath);
      logger.info("Loaded {} players from {}", playersLoaded, csvPath);

      snakes.setDice(new Dice(diceCount));

      new GameScreenView(primaryStage, snakes, gameToStart,csvPath).show();

    } catch (NumberFormatException e) {
      logger.error("Error with starting game: {}", e.getMessage());
      showAlert("Invalid Input", "Please enter a valid number for dice.");
    } catch (Exception e) {
      logger.error("Error starting game: {}", e.getMessage());
      showAlert("Game Error", "An error occurred while starting the game: " + e.getMessage());
    }
  }

  @Override
  protected void onBack() {
    logger.info("Navigating back from rules screen");
    primaryStage.close();
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  @Override
  public void show() {
    initializeCustomComponents();
    layoutCustomComponents();
    setupEventHandlers();

    Scene scene = new Scene(layout, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Game Rules Configuration");

    layout.prefWidthProperty().bind(scene.widthProperty());
    layout.prefHeightProperty().bind(scene.heightProperty());

    primaryStage.show();
    controller.setDifficulty(selectedDifficulty);
  }

  public String getDifficulty () {
    return selectedDifficulty;
  }
}
