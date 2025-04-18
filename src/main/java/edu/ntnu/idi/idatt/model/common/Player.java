package edu.ntnu.idi.idatt.model.common;

public abstract class Player {
  private final String name;
  private int position;
  private String character;

  public Player(String name, String character, int position) {
    this.name = name;
    this.position = position;
    this.character = character;
  }

  public void setCharacter(String character) {
    this.character = character;
  }

  public String getCharacter() {
    return character;
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