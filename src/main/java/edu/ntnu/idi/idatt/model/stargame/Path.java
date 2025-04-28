package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

public class Path implements TileAttribute {
  private final String direction;

  public Path(String direction) {
    this.direction = direction;
  }

  @Override
  public void onLand(Player player, AbstractBoard board) {
    // TODO: Define behavior for "Path" if needed (move player? bonus?)
  }

  public String getDirection() { return direction; }
}