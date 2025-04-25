package edu.ntnu.idi.idatt.view;

import static edu.ntnu.idi.idatt.util.AlertUtil.showAlert;

import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractView {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  protected boolean uiInitialized = false;
  protected Parent root;

  public AbstractView() {
    initializeUI();
  }

  private void initializeUI(){
    try {
      createUI();
      setupEventHandlers();
      applyInitialUIState();
      uiInitialized = true;
      logger.info("UI initialized successfully.");

    } catch (Exception e) {
      logger.error("Failed to initialize UI: {}", e.getMessage());
      uiInitialized = false;
      showAlert("Critical Error", "Failed to initialize UI. Please restart the application.");
    }
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
