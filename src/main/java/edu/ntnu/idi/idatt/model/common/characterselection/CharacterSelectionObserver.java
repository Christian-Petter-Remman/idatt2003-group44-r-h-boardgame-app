package edu.ntnu.idi.idatt.model.common.characterselection;

/**
 * <h1>CharacterSelectionObserver</h1>
 *
 * <p>Interface for observers that wish to be notified when changes occur in the character
 * selection
 * process. Typically implemented by UI components that need to update when players select or
 * deselect characters.
 */
public interface CharacterSelectionObserver {

  /**
   * <h2>update</h2>
   *
   * <p>Called whenever the character selection state changes. Implementing classes should define
   * how
   * they respond to selection updates.
   */
  void update();
}