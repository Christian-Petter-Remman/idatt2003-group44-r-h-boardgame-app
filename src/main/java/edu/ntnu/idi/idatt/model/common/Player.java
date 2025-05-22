package edu.ntnu.idi.idatt.model.common;

/**
 * <h1>Player</h1>
 *
 * <p>Abstract representation of a player in a board game. Stores position, character icon, points,
 * and
 * jail status. Concrete implementations must define victory condition and starting position.
 */
public abstract class Player {

  private final String name;
  private int position = 1;
  private String characterIcon;
  private int points;
  private int jailTurnsLeft = 0;
  private boolean jailed = false;

  /**
   * <h2>Constructor.</h2>
   *
   * @param name          Player's display name.
   * @param characterIcon Identifier or path for the player's character icon.
   */
  public Player(String name, String characterIcon) {
    this.name = name;
    this.characterIcon = characterIcon;
  }

  /**
   * <h2>getName.</h2>
   *
   * @return Player name.
   */
  public String getName() {
    return name;
  }

  /**
   * <h2>getCharacter.</h2>
   *
   * @return Character icon reference.
   */
  public String getCharacter() {
    return characterIcon;
  }

  /**
   * <h2>setPosition.</h2>
   *
   * @param position New board position.
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * <h2>getPosition.</h2>
   *
   * @return Current board position.
   */
  public int getPosition() {
    return position;
  }

  /**
   * <h2>move</h2>
   *
   * <p>Moves the player a number of steps and triggers any tile effects.
   *
   * @param steps Number of steps to move.
   * @param board Reference to the board.
   */
  public void move(int steps, AbstractBoard board) {
    int newPosition = position + steps;
    if (newPosition > board.getSize()) {
      newPosition = board.getSize();
    }
    position = newPosition;
    board.getTile(position).onPlayerLanded(this, board);
  }

  /**
   * <h2>setCharacter.</h2>
   *
   * @param character New character icon identifier.
   */
  public void setCharacter(String character) {
    this.characterIcon = character;
  }

  /**
   * <h2>getCharacterIcon.</h2>
   *
   * @return Character icon name or path.
   */
  public String getCharacterIcon() {
    return characterIcon;
  }

  /**
   * <h2>isJailed.</h2>
   *
   * @return True if the player is in jail.
   */
  public boolean isJailed() {
    return jailed;
  }

  /**
   * <h2>setJailed</h2>
   *
   * <p>Sends the player to jail for a given number of turns.
   *
   * @param turns Number of turns to remain in jail.
   */
  public void setJailed(int turns) {
    this.jailed = true;
    this.jailTurnsLeft = turns;
    this.position = -1;
  }

  /**
   * <h2>decreaseJailTurns</h2>
   *
   * <p>Reduces the jail duration by one turn. Automatically releases the player if no turns remain.
   */
  public void decreaseJailTurns() {
    if (jailTurnsLeft > 0) {
      jailTurnsLeft--;
    }
    if (jailTurnsLeft == 0) {
      jailed = false;
    }
  }

  /**
   * <h2>getJailTurnsLeft.</h2>
   *
   * @return Remaining jail turns.
   */
  public int getJailTurnsLeft() {
    return jailTurnsLeft;
  }

  /**
   * <h2>releaseFromJail</h2>
   *
   * <p>Releases the player from jail immediately.
   */
  public void releaseFromJail() {
    this.jailed = false;
    this.jailTurnsLeft = 0;
  }

  /**
   * <h2>getPoints.</h2>
   *
   * @return Current score or star count.
   */
  public int getPoints() {
    return points;
  }

  /**
   * <h2>addPoints.</h2>
   *
   * @param points Points to add.
   */
  public void addPoints(int points) {
    this.points += points;
  }

  /**
   * <h2>hasWon.</h2>
   *
   * @return True if this player has met the winning condition.
   */
  public abstract boolean hasWon();

  /**
   * <h2>getStartPosition.</h2>
   *
   * @return Starting position for this player.
   */
  public abstract int getStartPosition();
}