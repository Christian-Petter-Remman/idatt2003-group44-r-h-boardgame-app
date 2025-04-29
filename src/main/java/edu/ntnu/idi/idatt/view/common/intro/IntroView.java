package edu.ntnu.idi.idatt.view.common.intro;

import javafx.scene.Parent;

public interface IntroView {
  Parent getRoot();
  void setStartGameListener(Runnable listener);
  void setLoadGameListener(Runnable listener);
}
