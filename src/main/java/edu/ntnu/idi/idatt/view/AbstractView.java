package edu.ntnu.idi.idatt.view;

import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractView {
  protected final Logger logger = LoggerFactory.getLogger(AbstractView.class);

  protected boolean uiInitialized = false;
  protected Parent root;

  public AbstractView() {
    try {
      createUI();
      setupEventHandlers();
      applyInitialUIState();
    } catch (Exception e) {
      logger.error("Failed to initialize UI: {}", e.getMessage());
    }
    uiInitialized = true;
  }

  protected abstract void createUI();
  protected abstract void setupEventHandlers();
  protected abstract void applyInitialUIState();

  public Parent getRoot() {
    return root;
  }

  public void show() {
    applyInitialUIState();
  }
}
