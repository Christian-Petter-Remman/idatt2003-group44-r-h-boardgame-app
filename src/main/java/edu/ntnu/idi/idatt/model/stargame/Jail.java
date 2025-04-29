package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

public class Jail implements TileAttribute {
  private final int start;
  private final int jailTurns;

  public Jail(int start, int jailTurns) {
    this.start = start;
    this.jailTurns = jailTurns;
  }

  @Override
  public void onLand(Player player, AbstractBoard board) {
    player.setJailed(jailTurns);
  }

  public int getStart() { return start; }
  public int getJailTurns() { return jailTurns; }
}