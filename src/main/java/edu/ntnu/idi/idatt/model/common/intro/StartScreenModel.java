package edu.ntnu.idi.idatt.model.common.intro;

import edu.ntnu.idi.idatt.filehandling.handlers.DialogJsonHandler;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.DialogConfig;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;

public class StartScreenModel {
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  private Map<String, DialogConfig> dialogs;

  public StartScreenModel() {
    loadDialogs();
  }

  public void loadDialogs() {
    this.dialogs = DialogJsonHandler.loadDialogs();
    pcs.firePropertyChange("dialogs", null, dialogs);
  }

  public Map<String, DialogConfig> getDialogs() {
    return dialogs;
  }

  public void addListener(PropertyChangeListener l) {
    pcs.addPropertyChangeListener(l);
  }

  public void removeListener(PropertyChangeListener l) {
    pcs.removePropertyChangeListener(l);
  }
}
