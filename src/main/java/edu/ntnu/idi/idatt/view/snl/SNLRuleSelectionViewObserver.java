package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;

/**
 * <h1>SNLRuleSelectionViewObserver</h1>
 *
 * Interface used for observing changes in the Snakes and Ladders rule selection model.
 * Implementing classes will be notified when the selected game rules (e.g. board file or dice count)
 * are changed in the model.
 */
public interface SNLRuleSelectionViewObserver {

  /**
   * <h2>onRuleSelectionChanged</h2>
   *
   * Callback triggered whenever the rule selection state changes in the {@link SNLRuleSelectionModel}.
   *
   * @param model the model containing the updated rule selection state
   */
  void onRuleSelectionChanged(SNLRuleSelectionModel model);
}