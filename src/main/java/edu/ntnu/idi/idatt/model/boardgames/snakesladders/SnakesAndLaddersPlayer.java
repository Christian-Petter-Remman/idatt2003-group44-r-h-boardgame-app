package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import edu.ntnu.idi.idatt.model.common.Player;

public class SnakesAndLaddersPlayer extends Player {

  public SnakesAndLaddersPlayer(String name, String character) {
    super(name,character);
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
    if (!(gameContext instanceof Board board)) {
      throw new IllegalArgumentException("Game context must be a Board for SnakesAndLaddersPlayer");
    }

    int newPosition = getPosition() + steps;
    if (newPosition > 100) return;

    setPosition(board.getFinalPosition(newPosition));
  }
}
