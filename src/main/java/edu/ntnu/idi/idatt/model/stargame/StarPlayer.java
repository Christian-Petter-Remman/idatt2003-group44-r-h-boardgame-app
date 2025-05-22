package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.Player;

/**
 * <h1>StarPlayer</h1>
 *
 * <p>Represents a player in the Star board game. Holds information such as name, character, current
 * position on the board, and collected points.
 */
public class StarPlayer extends Player {

  private int position;
  private int points;

  /**
   * <h2>Constructor.</h2>
   * Creates a new StarPlayer instance with initial attributes.
   *
   * @param name      The name of the player.
   * @param character The selected character icon.
   * @param position  The starting tile position.
   * @param points    The number of points the player starts with.
   */
  public StarPlayer(String name, String character, int position, int points) {
    super(name, character);
    this.position = position;
    this.points = points;
  }

  /**
   * <h2>getPosition.</h2>
   *
   * @return Current tile position of the player.
   */
  public int getPosition() {
    return position;
  }

  /**
   * <h2>setPosition.</h2>
   *
   * @param position New tile position to assign to the player.
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * <h2>getPoints.</h2>
   *
   * @return Current points the player has.
   */
  public int getPoints() {
    return points;
  }

  /**
   * <h2>setPoints.</h2>
   *
   * @param points The new points value to set.
   */
  public void setPoints(int points) {
    this.points = points;
  }

  /**
   * <h2>addPoints</h2>
   * Adds a specified number of points to the player's current score.
   *
   * @param pointsToAdd Points to add.
   */
  public void addPoints(int pointsToAdd) {
    this.points += pointsToAdd;
  }

  /**
   * <h2>hasWon</h2>
   * Checks if the player has won the game (by reaching at least 5 points).
   *
   * @return true if the player has 5 or more points.
   */
  @Override
  public boolean hasWon() {
    return points >= 5;
  }

  /**
   * <h2>getStartPosition.</h2>
   *
   * @return The default starting position for the player.
   */
  @Override
  public int getStartPosition() {
    return 0;
  }
}