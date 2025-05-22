package edu.ntnu.idi.idatt.model.common;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionObserver;

/**
 * <h1>Observable</h1>
 *
 * A generic interface for implementing the observer pattern.
 * Classes implementing this interface can register and notify {@link CharacterSelectionObserver} instances.
 */
public interface Observable {

  /**
   * <h2>addObserver</h2>
   *
   * Registers an observer that should be notified when changes occur.
   *
   * @param observer the observer to add.
   */
  void addObserver(CharacterSelectionObserver observer);

  /**
   * <h2>removeObserver</h2>
   *
   * Removes a previously registered observer.
   *
   * @param observer the observer to remove.
   */
  void removeObserver(CharacterSelectionObserver observer);

  /**
   * <h2>notifyObservers</h2>
   *
   * Notifies all registered observers about a change or event.
   */
  void notifyObservers();
}