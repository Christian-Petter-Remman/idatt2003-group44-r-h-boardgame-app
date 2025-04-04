package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.common.player.Player;
import edu.ntnu.idi.idatt.view.common.AbstractRuleSelectionView;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    Label titleLabel = new Label("Customize game");
    titleLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 36));
    layout.add(titleLabel, 0, 0, 2, 1);

    Label difficultyLabel = new Label("Difficulty:");
    difficultyLabel.setFont(Font.font("Monospace", FontWeight.BOLD, 16));

    ToggleGroup difficultyGroup = new ToggleGroup();
    RadioButton easyButton = new RadioButton("Easy");
    RadioButton normalButton = new RadioButton("Default");
    RadioButton hardButton = new RadioButton("Hard");

    easyButton.setToggleGroup(difficultyGroup);
    normalButton.setToggleGroup(difficultyGroup);
    hardButton.setToggleGroup(difficultyGroup);
    normalButton.setSelected(true);

    easyButton.setUserData("easy");
    normalButton.setUserData("default");
    hardButton.setUserData("hard");

    difficultyGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null) {
        selectedDifficulty = (String) newValue.getUserData();
        logger.info("Selected difficulty: {}", selectedDifficulty);
      }
    });
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
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
    Scene scene = new Scene(layout, 600, 500);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Snakes and Ladders - Game Rules");
    primaryStage.show();
  }
}
