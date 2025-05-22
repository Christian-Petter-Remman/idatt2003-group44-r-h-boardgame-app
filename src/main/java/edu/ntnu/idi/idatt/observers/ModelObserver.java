package edu.ntnu.idi.idatt.observers;

/**
 * <h1>ModelObserver</h1>
 *
 * A generic observer interface used to receive updates from models.
 * It follows the Observer pattern, allowing models to notify interested
 * components (such as views or controllers) about changes.
 */
public interface ModelObserver {

  /**
   * <h2>update</h2>
   *
   * Called when the observed model emits a change or event.
   *
   * @param eventType A string identifier for the type of event.
   * @param data Optional data associated with the event, can be {@code null}.
   */
  void update(String eventType, Object data);
}