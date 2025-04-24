package edu.ntnu.idi.idatt.view;

import javafx.scene.Parent;

public abstract class AbstractView {
  protected boolean uiInitialized = false;
  protected Parent root;

  public AbstractView() {
    createUI();
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
