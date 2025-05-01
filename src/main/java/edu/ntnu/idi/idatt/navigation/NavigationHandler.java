package edu.ntnu.idi.idatt.navigation;

public interface NavigationHandler {
  void navigateTo(String destination);
  void navigateBack();
  void setRoot(javafx.scene.Parent root);
}
