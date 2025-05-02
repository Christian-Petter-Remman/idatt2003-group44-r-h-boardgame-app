package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.TileAttribute;

public class Path implements TileAttribute {
  private final String direction;
  private final int start;
  private final int endStatic;
  private final int endDynamic;


  public Path(int start, String direction, int endStatic, int endDynamic) {
    this.start = start;
    this.direction = direction;
    this.endStatic = endStatic;
    this.endDynamic = endDynamic;
  }

  public String getDirection() {
    return direction;
  }

  public int getStart() {
    return start;
  }

  public int getEndStatic() {
    return endStatic;
  }
  public int getEndDynamic() {
    return endDynamic;
  }


  @Override
  public void onLand(Player player, AbstractBoard board) {
    if (board instanceof StarBoard starBoard) {
      PathDecisionNotifier notifier = (PathDecisionNotifier) starBoard;
      notifier.notifyPathDecision(player, this); // Use observer pattern
    }
  }
}

