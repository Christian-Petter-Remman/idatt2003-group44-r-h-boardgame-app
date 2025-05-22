package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.Player;

/**
 * <h1>SNLPlayer</h1>
 *
 * Represents a player specific to the Snakes and Ladders game.
 * Inherits common properties from {@link Player}.
 */
public class SNLPlayer extends Player {

  /**
   * <h2>Constructor</h2>
   *
   * Initializes a new SNL player.
   *
   * @param name          Player's name.
   * @param characterIcon Icon or character selected by the player.
   * @param position      Initial position on the board.
   */
  public SNLPlayer(String name, String characterIcon, int position) {
    super(name, characterIcon);
    setPosition(position);
  }

  /**
   * <h2>hasWon</h2>
   *
   * Determines whether the player has reached or passed tile 100.
   *
   * @return true if the player has won the game.
   */
  @Override
  public boolean hasWon() {
    return getPosition() >= 100;
  }

  /**
   * <h2>getStartPosition</h2>
   *
   * Returns the starting position for this player.
   *
   * @return the starting tile number (1).
   */
  @Override
  public int getStartPosition() {
    return 1;
  }
}