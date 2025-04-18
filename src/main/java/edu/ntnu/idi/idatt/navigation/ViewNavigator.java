package edu.ntnu.idi.idatt.navigation;

import javafx.stage.Stage;

public interface ViewNavigator {
  void navigateTo(String viewName);
  Stage getStage();
}
