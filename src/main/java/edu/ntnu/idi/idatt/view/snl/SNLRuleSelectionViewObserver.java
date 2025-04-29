package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;

public interface SNLRuleSelectionViewObserver {
  void onRuleSelectionChanged(SNLRuleSelectionModel model);
}
