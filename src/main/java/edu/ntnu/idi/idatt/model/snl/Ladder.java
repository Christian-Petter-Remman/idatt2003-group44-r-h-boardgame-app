package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

public class Ladder implements TileAttribute {
  private final int start;
  private final int end;

  public Ladder(int start, int end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public void onLand(Player player, AbstractBoard board) {
    player.move(end - player.getPosition(), board);
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }
}