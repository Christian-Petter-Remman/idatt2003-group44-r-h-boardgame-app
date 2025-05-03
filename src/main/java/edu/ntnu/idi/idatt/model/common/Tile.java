package edu.ntnu.idi.idatt.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single tile in any game board.
 */
public class Tile {
  private final int number;
  private final List<TileAttribute> attributes = new ArrayList<>();

  /**
   * Constructor for a normal tile
   *
   * @param number the number of this tile on the board
   */
  public Tile(int number) {
    this.number = number;
  }

  public int getNumber(){
    return number;
  }

  public boolean hasAttribute(Class<? extends TileAttribute> clazz) {
    for (TileAttribute attribute : attributes) {
      if (clazz.isInstance(attribute)) {
        return true;
      }
    }
    return false;
  }


  public List<TileAttribute> getAttributes(){
    return attributes;
  }

 public void addAttribute(TileAttribute attribute) {
    attributes.add(attribute);
 }

 public void onPlayerLanded(Player player, AbstractBoard board) {
    for (TileAttribute attribute : attributes){
    attribute.onLand(player,board);
    }
 }
}
