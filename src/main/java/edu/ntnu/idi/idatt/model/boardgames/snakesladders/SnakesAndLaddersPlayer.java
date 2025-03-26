package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import edu.ntnu.idi.idatt.model.common.player.Player;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Tile;

public class SnakesAndLaddersPlayer extends Player {

  public SnakesAndLaddersPlayer(String name) {
    super(name);
  }

  @Override
  public int getStartPosition() {
    return 1;
  }

  @Override
  public boolean hasWon() {
    return getPosition() == 100;
  }

  @Override
  public void move(int steps, Object gameContext) {
    if (!(gameContext instanceof Board board)) {
      throw new IllegalArgumentException("Game context must be a Board for SnakesAndLaddersPlayer");
    }

    int newPosition = getPosition() + steps;

    if (newPosition > 100) {
      return;
    }

    setPosition(newPosition);

    Tile currentTile = board.getTile(getPosition());
    if (currentTile.hasSnakeOrLadder()) {
      setPosition(currentTile.getDestination());
    }

  }
}
