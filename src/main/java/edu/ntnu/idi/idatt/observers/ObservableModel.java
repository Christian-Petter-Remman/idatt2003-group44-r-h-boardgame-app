package edu.ntnu.idi.idatt.observers;

/**
 * <h1>ObservableModel</h1>
 *
 * Interface representing a model that can be observed using the Observer pattern.
 * Classes implementing this interface can register observers and notify them
 * when relevant events occur.
 */
public interface ObservableModel {

  /**
   * <h2>addObserver</h2>
   *
   * Adds an observer to the list of listeners for this model.
   *
   * @param observer The observer to add.
   */
  void addObserver(ModelObserver observer);

  /**
   * <h2>removeObserver</h2>
   *
   * Removes an observer from the list of listeners.
   *
   * @param observer The observer to remove.
   */
  void removeObserver(ModelObserver observer);

  /**
   * <h2>notifyObservers</h2>
   *
   * Notifies all registered observers of a specific event.
   *
   * @param eventType A string describing the type of event.
   * @param data Additional data associated with the event.
   */
  void notifyObservers(String eventType, Object data);
}