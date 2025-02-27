package edu.ntnu.idi.idatt.model;

import edu.ntnu.idi.idatt.model.Tile.Tile;

/**
 * Represents a player in the snakes and ladders game.
 */
public class Player {
  private final String name;
  private int position;

  public Player(String name) {
    this.name = name;
    this.position = 1;
  }

  public String getName() {
    return name;
  }

  public int getPosition() {
    return position;
  }

  public void move(int roll, Board board) {
    position += roll;

    if (RollIsTooHigh(roll)) return;

    System.out.println(name + " moved to tile " + position);

    CheckTileForBoogie(board);
  }

  private boolean RollIsTooHigh(int roll) {
    if (position > 100) {
      position -= roll;
      System.out.println(name + " rolled too high and stays at tile " + position);
      return true;
    }
    return false;
  }

  private void CheckTileForBoogie(Board board) {
    Tile tile = board.getTile(position);
    if (tile.hasSnakeOrLadder()) {
      System.out.println(name + " encountered a " + (tile.getDestination() > position ? "ladder!" : "snake!"));
      position = tile.getDestination();
      System.out.println(name + " moved to tile " + position);
    }
  }

  public boolean hasWon() {
    return position == 100; // Player wins if they reach tile 100
  }
}
