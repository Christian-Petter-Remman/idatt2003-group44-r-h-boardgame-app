package edu.ntnu.idi.idatt.view.common;

import javafx.scene.Parent;

public interface IntroView {
  Parent getRoot();
  void setStartGameListener(Runnable listener);
  void setLoadGameListener(Runnable listener);
}
