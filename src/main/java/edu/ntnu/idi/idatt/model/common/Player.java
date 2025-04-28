package edu.ntnu.idi.idatt.model.common;

public abstract class Player {
  private final String name;
  private int position = 1;
  private String characterIcon;
  private int points;

  public Player(String name, String characterIcon) {
    this.name = name;
    this.characterIcon = characterIcon;
  }

  public String getName() {
    return name;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public int getPosition() {
    return position;
  }

  public void move(int steps, AbstractBoard board) {
    int newPosition = position + steps;
    if (newPosition > board.getSize()) {
      newPosition = board.getSize();
    }
    position = newPosition;
    board.getTile(position).onPlayerLanded(this, board);
  }

  public void setCharacter(String character) {
    this.characterIcon = character;
  }

  public String getCharacterIcon() {
    return characterIcon;
  }

  public int getPoints() {
    return points;
  }

  public void addPoints(int points) {
    this.points += points;
  }
}

