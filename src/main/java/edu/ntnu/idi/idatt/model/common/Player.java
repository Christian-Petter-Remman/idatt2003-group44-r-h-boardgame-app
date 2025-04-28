package edu.ntnu.idi.idatt.model.common;

public abstract class Player {
  private final String name;
  private int position = 1;
  private String characterIcon;
  private int points;
  private int jailTurnsLeft = 0;
  private boolean jailed = false;

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

  public boolean isJailed() {
    return jailed;
  }

  public void setJailed(int turns) {
    this.jailed = true;
    this.jailTurnsLeft = turns;
    this.position = -1;
  }

  public void decreaseJailTurns() {
    if (jailTurnsLeft > 0) {
      jailTurnsLeft--;
    }
    if (jailTurnsLeft == 0) {
      jailed = false;
    }
  }

  public int getJailTurnsLeft() {
    return jailTurnsLeft;
  }

  public void releaseFromJail() {
    this.jailed = false;
    this.jailTurnsLeft = 0;
  }

  public int getPoints() {
    return points;
  }

  public void addPoints(int points) {
    this.points += points;
  }

  public abstract boolean hasWon();
}

