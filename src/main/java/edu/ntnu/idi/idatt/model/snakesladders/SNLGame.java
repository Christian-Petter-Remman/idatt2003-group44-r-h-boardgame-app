package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.Dice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SNLGame extends BoardGame {

  public SNLGame(SNLBoard board, List<Player> players) {
    super(board, List.copyOf(players));
  }

  @Override
  public void makeMove(Player player){
    int roll = dice.roll();
    player.move(roll,board);
  }

  @Override
public boolean isGameOver() {
  return getCurrentPlayer().getPosition() >= board.getSize();
  }
}