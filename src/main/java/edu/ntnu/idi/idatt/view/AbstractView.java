package edu.ntnu.idi.idatt.view;

import javafx.scene.Parent;

public abstract class AbstractView {

  public AbstractView() {
  }

  protected abstract void createUI();

  public abstract Parent getRoot();
}