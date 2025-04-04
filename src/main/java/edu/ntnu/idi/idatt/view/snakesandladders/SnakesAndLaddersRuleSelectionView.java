package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.common.dice.Dice;
import edu.ntnu.idi.idatt.model.common.player.Player;
import edu.ntnu.idi.idatt.view.common.AbstractRuleSelectionView;
import edu.ntnu.idi.idatt.view.common.GameScreenView;
import java.io.IOException;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnakesAndLaddersRuleSelectionView extends AbstractRuleSelectionView {
  private static final Logger logger = LoggerFactory.getLogger(SnakesAndLaddersRuleSelectionView.class);

  private final SnakesAndLaddersFactory factory;
  private String selectedDifficulty = "default";
  private List<Player> players;
  private TextField diceField;
  private TextField laddersField;
  private TextField penaltyField;

  public SnakesAndLaddersRuleSelectionView(Stage primaryStage) {
    super(primaryStage);
    this.factory = new SnakesAndLaddersFactory();
    setupLayout();
    addRuleOptions();


  }

  private void setupLayout() {
    layout.setAlignment(Pos.CENTER);
    layout.setHgap(20);
    layout.setVgap(20);
    layout.setPadding(new Insets(30));
    layout.setStyle("-fx-background-color: #f0f0f0;");
  }

  @Override
  protected void addRuleOptions() {
    addTitleSection();
    addDifficultySection();
    addGameSettingsSection();
    addNavigationButtons();


  }


  @Override
  protected void onStart(List<Player> players ) {
  }

  @Override
  protected void onBack(){
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  private void addTitleSection() {
    Label titleLabel = new Label("Customize game");
    titleLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 36));
    layout.add(titleLabel, 0, 0, 2, 1);
  }

  private void addDifficultySection() {
      Label difficultyLabel = new Label("Difficulty:");
      difficultyLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 16));

      ToggleGroup difficultyGroup = new ToggleGroup();
      RadioButton easyButton = createRadioButton("Easy", "easy", difficultyGroup);
      RadioButton normalButton = createRadioButton("Default", "default", difficultyGroup);
      RadioButton hardButton = createRadioButton("Hard", "hard", difficultyGroup);
      normalButton.setSelected(true);

      HBox difficultyBox = new HBox(20, easyButton, normalButton, hardButton);
      difficultyBox.setAlignment(Pos.CENTER_LEFT);

      layout.add(difficultyLabel, 0, 1);
      layout.add(difficultyBox, 1, 1);
  }

  private RadioButton createRadioButton(String text, String userData, ToggleGroup group) {
    RadioButton button = new RadioButton(text);
    button.setToggleGroup(group);
    button.setUserData(userData);
    button.setOnAction(e -> selectedDifficulty = userData);
    return button;
  }

  private void addGameSettingsSection() {
    // Dice
    Label diceLabel = new Label("Number of Dice:");
    diceLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
    diceField = new TextField("1");
    diceField.setPrefWidth(100);

    // Ladders
    Label laddersLabel = new Label("Number of Ladders:");
    laddersLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
    laddersField = new TextField("8");
    laddersField.setPrefWidth(100);

    // Penalty Fields
    Label penaltyLabel = new Label("Number of Penalty Fields:");
    penaltyLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
    penaltyField = new TextField("8");
    penaltyField.setPrefWidth(100);

    GridPane settingsGrid = new GridPane();
    settingsGrid.setHgap(10);
    settingsGrid.setVgap(10);
    settingsGrid.addRow(0, diceLabel, diceField);
    settingsGrid.addRow(1, laddersLabel, laddersField);
    settingsGrid.addRow(2, penaltyLabel, penaltyField);

    layout.add(settingsGrid, 0, 2, 2, 1);
  }

  private void addNavigationButtons() {
    Button backButton = getBackButton();

    //startGameButton declared in super
    startGameButton.setText("Start Game");
    startGameButton.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
    startGameButton.setPrefWidth(200);
    startGameButton.setOnAction(e -> onStart(players));

    HBox buttonBox = new HBox(20, backButton, startGameButton);
    buttonBox.setAlignment(Pos.CENTER);
    layout.add(buttonBox, 0, 3, 2, 1);
  }

  private Button getBackButton() {
    Button backButton = new Button("Back");
    backButton.setFont(Font.font("Monospace", FontWeight.BOLD, 16));
    backButton.setOnAction(e -> onBack());
    return backButton;
  }

  private void setupStartButton() {
    startGameButton.setOnAction(e -> {
      try {
        validateInputs();

      } catch (NumberFormatException ex) {

      }
    });
  }

  private void validateInputs() throws NumberFormatException {
    int diceCount = Integer.parseInt(diceField.getText());
    int ladderCount = Integer.parseInt(laddersField.getText());
    int penaltyCount = Integer.parseInt(penaltyField.getText());

    if (diceCount < 1) {
      throw new NumberFormatException();
    }
    if (ladderCount < 1 || penaltyCount < 0) {
      throw new NumberFormatException();
    }
  }

  private void startGameWithSettings() {
    try {
      int diceCount = Integer.parseInt(diceField.getText());
      int ladderCount = Integer.parseInt(laddersField.getText());
      int penaltyCount = Integer.parseInt(penaltyField.getText());

      SnakesAndLadders game = (SnakesAndLadders) factory.createBoardGameFromConfiguration(
          selectedDifficulty);
      game.setDice(new Dice(diceCount));

      if (!selectedDifficulty.equals("default")) {
        adjustBoard();
      }

      for (Player player : players) {
        game.addPlayer(player);
      }

      game.initialize();
      new GameScreenView(primaryStage, game).show();
    } catch (Exception e) {
      logger.error("Error starting game: {}", e.getMessage());
      showError("Game Error", "Could not initialize game with selected settings");
    }
  }

    private void adjustBoard() {

    }



  private void showError(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  @Override
  public void show() {
    Scene scene = new Scene(layout, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Snakes and Ladders - Game Rules");
    primaryStage.show();
  }
}
