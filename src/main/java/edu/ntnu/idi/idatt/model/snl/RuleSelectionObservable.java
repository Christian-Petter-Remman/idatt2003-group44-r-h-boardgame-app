package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.view.snl.SNLRuleSelectionViewObserver;

/**
 * <h1>RuleSelectionObservable</h1>
 *
 * Interface for classes that support observer registration related to rule selection events
 * in the Snakes and Ladders rule selection view.
 */
public interface RuleSelectionObservable {

  /**
   * <h2>addObserver</h2>
   *
   * Registers an observer to be notified of rule selection changes.
   *
   * @param observer the observer to add
   */
  void addObserver(SNLRuleSelectionViewObserver observer);

  /**
   * <h2>removeObserver</h2>
   *
   * Removes a previously registered observer.
   *
   * @param observer the observer to remove
   */
  void removeObserver(SNLRuleSelectionViewObserver observer);

  /**
   * <h2>notifyObservers</h2>
   *
   * Notifies all registered observers about a change in rule selection.
   */
  void notifyObservers();
}