package edu.ntnu.idi.idatt.model.common.intro;

import edu.ntnu.idi.idatt.filehandling.handlers.DialogJsonHandler;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.DialogConfig;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;

/**
 * <h1>StartScreenModel</h1>
 *
 * Represents the model for the start screen. It is responsible for loading
 * dialog configurations and notifying listeners when changes occur.
 */
public class StartScreenModel {

  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  private Map<String, DialogConfig> dialogs;

  /**
   * <h2>Constructor</h2>
   *
   * Initializes the model and loads dialog configurations from file.
   */
  public StartScreenModel() {
    loadDialogs();
  }

  /**
   * <h2>loadDialogs</h2>
   *
   * Loads the dialog configurations from the JSON handler and notifies listeners.
   */
  public void loadDialogs() {
    this.dialogs = DialogJsonHandler.loadDialogs();
    pcs.firePropertyChange("dialogs", null, dialogs);
  }

  /**
   * <h2>getDialogs</h2>
   *
   * Returns the map of loaded dialog configurations.
   *
   * @return A map of dialog IDs to {@link DialogConfig} instances.
   */
  public Map<String, DialogConfig> getDialogs() {
    return dialogs;
  }

  /**
   * <h2>addListener</h2>
   *
   * Adds a property change listener to observe changes in this model.
   *
   * @param l The listener to add.
   */
  public void addListener(PropertyChangeListener l) {
    pcs.addPropertyChangeListener(l);
  }

  /**
   * <h2>removeListener</h2>
   *
   * Removes a previously added property change listener.
   *
   * @param l The listener to remove.
   */
  public void removeListener(PropertyChangeListener l) {
    pcs.removePropertyChangeListener(l);
  }
}