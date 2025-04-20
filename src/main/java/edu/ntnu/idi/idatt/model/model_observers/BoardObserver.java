package edu.ntnu.idi.idatt.model.model_observers;

import edu.ntnu.idi.idatt.model.common.Player;

public interface BoardObserver {
  void onBoardRendered();
  void onPlayerMoved(Player player, int fromPosition, int toPosition);
  void onSpecialTileActivated(int tileNumber, int destination, boolean isLadder);
}
