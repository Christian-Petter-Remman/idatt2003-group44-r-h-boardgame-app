package edu.ntnu.idi.idatt.view;

import javafx.scene.Parent;

public abstract class GameScreen{

  public GameScreen() {
  }

  protected abstract void createUI();

  public abstract Parent getRoot();
}