package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snakesladders.SNLBoard;

public class StarPlayer extends Player {

  private int score;

  public StarPlayer(String name, String character, int position, int score) {
    super(name,character,position);
    this.score = score;
  }

  public int getScore() {
    return score;
  }
  public void setScore(int score) {
    this.score = score;
  }

  @Override
  public int getStartPosition() {
    return 0;
  }

  @Override
  public boolean hasWon() {
    return false;
  }

  @Override
  public <T> void move(int steps, T gameContext) {
    if (!(gameContext instanceof StarBoard board)) {
      throw new IllegalArgumentException("Game context must be a StarBoard for Starplayer");
    }
    int newPosition = getPosition() + steps;
    if (newPosition > 100) return;
    setPosition(board.getFinalStarPosition(newPosition));
  }
}

