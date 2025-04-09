package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersPlayer;
import edu.ntnu.idi.idatt.model.common.player.Player;
import edu.ntnu.idi.idatt.view.common.AbstractCharacterSelectionView;
import edu.ntnu.idi.idatt.view.common.GameScreenView;
import javafx.stage.Stage;

import java.util.List;

public class SnakesAndLaddersCharacterSelectionView extends AbstractCharacterSelectionView {

  public SnakesAndLaddersCharacterSelectionView(Stage stage) {
    super(stage);
  }

  @Override
  protected String getBackgroundStyle() {

     return "-fx-background-image: url('images/bakgrun snake.jpg'); -fx-background-size: cover;";
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
    SnakesAndLaddersFactory factory = new SnakesAndLaddersFactory();
    SnakesAndLadders game = (SnakesAndLadders) factory.createBoardGameFromConfiguration("default");

    for (Player player : players) {
      if (player instanceof SnakesAndLaddersPlayer snakePlayer) {
        game.addPlayer(snakePlayer);
      }
    }

    new GameScreenView(stage, game).show();
  }

  @Override
  protected Player createPlayer(String name, String character) {
    return new SnakesAndLaddersPlayer(name, character);
  }

  @Override
  protected void onBack() {
    // TODO: Implement back navigation (e.g. return to main menu)
  }
}