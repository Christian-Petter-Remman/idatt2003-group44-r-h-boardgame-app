package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;

public class StarPlayer extends Player {

  private int position;
  private int points;

  public StarPlayer(String name, String character, int position, int points) {
    super(name, character);
    this.position = position;
    this.points = points;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  @Override
  public boolean hasWon() {
    return points >= 5;
  }

  @Override
  public void move(int roll, AbstractBoard board) {
    int tentativePosition = position + roll;
    if (tentativePosition <= board.getSize()) {
      this.position = tentativePosition;
      board.getTile(tentativePosition).onPlayerLanded(this, board);
    }
  }

  public void addPoints(int pointsToAdd) {
    this.points += pointsToAdd;
  }
}