package edu.ntnu.idi.idatt.view;

import javafx.scene.Parent;

public abstract class AbstractView {

  public AbstractView() {

  }

  protected abstract void createUI();

  protected abstract void setupEventHandlers();

  protected abstract void applyInitialUIState();

  public void initializeUI() {
    createUI();
    setupEventHandlers();
    applyInitialUIState();
  }

  public abstract Parent getRoot();
}