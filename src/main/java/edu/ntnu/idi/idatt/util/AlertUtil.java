package edu.ntnu.idi.idatt.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class AlertUtil {
  public static void showAlert(String title, String message) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle(title);
    alert.setContentText(message);

    Window mainWindow = findMainWindow();

    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    stage.initModality(Modality.APPLICATION_MODAL);
    if (mainWindow != null) {
      stage.initOwner(mainWindow);
    }
    alert.showAndWait();
  }

  private static Window findMainWindow() {
    return Stage.getWindows().stream()
        .filter(Window::isShowing)
        .findFirst()
        .orElse(null);

  }
}
