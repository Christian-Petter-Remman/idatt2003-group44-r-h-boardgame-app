package edu.ntnu.idi.idatt.model.common.player;

public abstract class Player {
  private final String name;
  private int position;

  public Player(String name) {
    this.name = name;
    this.position = getStartPosition();
  }

  public String getName() {
    return name;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public abstract int getStartPosition();

  public abstract boolean hasWon();

  public abstract <T> void move(int steps, T gameContext);

  @Override
  public String toString() {
    return name + "at position " + position;
  }
}