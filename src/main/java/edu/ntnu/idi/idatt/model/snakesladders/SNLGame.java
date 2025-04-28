package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Player;

import java.util.List;

public class SNLGame extends BoardGame {

  public SNLGame(SNLBoard board) {
    super(board);
  }

  public void initializePlayer(List<Player> players) {
    super.initializePlayer(players);
  }

  public void playTurn() {
    Player currentPlayer = getCurrentPlayer();
    int roll = dice.roll();
    currentPlayer.move(roll, board);
    notifyMoveObservers(currentPlayer, roll);
    nextTurn();
  }
}