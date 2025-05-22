package edu.ntnu.idi.idatt.model.memorygame;

/**
 * <h1>MemoryPlayer</h1>
 *
 * <p>Represents a player in the Memory game, tracking their name and score.
 */
public class MemoryPlayer {

  private final String name;
  private int score;

  /**
   * <h2>Constructor</h2>
   *
   * <p>Initializes a player with the given name and a starting score of zero.
   *
   * @param name the name of the player
   */
  public MemoryPlayer(String name) {
    this.name = name;
    this.score = 0;
  }

  /**
   * <h2>getName</h2>
   *
   * <p>Returns the name of the player.
   *
   * @return the player's name
   */
  public String getName() {
    return name;
  }

  /**
   * <h2>getScore</h2>
   *
   * <p>Returns the current score of the player.
   *
   * @return the player's score
   */
  public int getScore() {
    return score;
  }

  /**
   * <h2>incrementScore</h2>'
   *
   * <p>Increments the player's score by 1.
   */
  public void incrementScore() {
    score++;
  }

  /**
   * <h2>resetScore</h2>
   *
   * <p>Resets the player's score to zero.
   */
  public void resetScore() {
    this.score = 0;
  }
}