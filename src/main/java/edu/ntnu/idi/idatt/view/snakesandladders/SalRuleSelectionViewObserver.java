package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.rule_selection.SalRuleSelectionModel;

public interface SalRuleSelectionViewObserver {
  void onRuleSelectionChanged(SalRuleSelectionModel model);
}
