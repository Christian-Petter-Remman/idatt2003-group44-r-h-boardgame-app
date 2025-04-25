package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snakesladders.SNLBoard;

public class StarGame extends BoardGame {







  @Override
  public void initialize(SNLBoard board) {
  }

  @Override
  public void makeMove(Player player) {
  }

  @Override
  public boolean isValidMove(Player player, int move) {
    return false;
  }

  @Override
  public boolean checkWinCondition() {
    return false;
  }

  @Override
  public void setBoard(SNLBoard board) {

  }

  @Override
  public SNLBoard getBoard() {
    return null;
  }
}
