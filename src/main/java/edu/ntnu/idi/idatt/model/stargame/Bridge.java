package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

public class Bridge implements TileAttribute {
  private final int start;
  private final int end;

  public Bridge(int start, int end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public void onLand(Player player, AbstractBoard board) {
    player.setPosition(end);
  }

  public int getStart() { return start; }
  public int getEnd() { return end; }
}