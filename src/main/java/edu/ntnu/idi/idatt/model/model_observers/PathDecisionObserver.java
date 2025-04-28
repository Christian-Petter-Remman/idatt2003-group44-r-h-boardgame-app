package edu.ntnu.idi.idatt.model.model_observers;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.stargame.Path;
import edu.ntnu.idi.idatt.model.stargame.StarPlayer;

public interface PathDecisionObserver {
  void onPathDecisionRequested(Player player, Path path);
}