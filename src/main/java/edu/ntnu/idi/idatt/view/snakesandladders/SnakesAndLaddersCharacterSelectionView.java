package edu.ntnu.idi.idatt.view.snakesandladders;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersPlayer;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.view.common.AbstractCharacterSelectionView;
import javafx.stage.Stage;

import java.util.List;

public class SnakesAndLaddersCharacterSelectionView extends AbstractCharacterSelectionView {

  public SnakesAndLaddersCharacterSelectionView(Stage stage) {
    super(stage);
  }

  @Override
  protected String getBackgroundStyle() {
    return "-fx-background-image: url('/images/snakesbackground.jpg');";
  }

  @Override
  protected String getHeaderText() {
    return "Choose your Characters";
  }

  @Override
  protected String getGameTitle() {
    return "Snakes and Ladders";
  }

  @Override
  protected void onStart(List<Player> players) {
    try {
    SnakesAndLaddersRuleSelectionView ruleSelectionView = new SnakesAndLaddersRuleSelectionView(stage);
      ruleSelectionView.setPlayers(players);
      ruleSelectionView.show();

    } catch (Exception e) {
      logger.error("Error loading Rule Selection Screen: {}", e.getMessage());
      showAlert("Error", "An error occurred while loading the Rule Selection Screen");
    }
  }

  @Override
  protected Player createPlayer(String name, String character) {
    return new SnakesAndLaddersPlayer(name, character,0);
  }

  @Override
  protected void onBack() {
    // TODO: Implement back navigation (e.g. return to main menu)
  }
}