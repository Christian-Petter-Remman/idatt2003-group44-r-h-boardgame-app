package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.Player;

public class SNLPlayer extends Player {

  public SNLPlayer(String name, String character, int position) {
    super(name,character,position);
  }

  @Override
  public int getStartPosition() {
    return 0;
  }

  @Override
  public boolean hasWon() {
    return getPosition() == 100;
  }

  @Override
  public <T> void move(int steps, T gameContext) {
    if (!(gameContext instanceof SNLBoard board)) {
      throw new IllegalArgumentException("Game context must be a SNLBoard for SnakesAndLaddersPlayer");
    }

    int newPosition = getPosition() + steps;
    if (newPosition > 100) return;

    setPosition(board.getFinalPosition(newPosition));
  }
}
