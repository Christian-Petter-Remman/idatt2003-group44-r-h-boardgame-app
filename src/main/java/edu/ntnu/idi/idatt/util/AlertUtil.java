package edu.ntnu.idi.idatt.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * <h1>AlertUtil</h1>
 *
 * Utility class for displaying JavaFX alert dialogs in a consistent and modal way.
 * Provides simplified methods to show error alerts tied to the main application window.
 */
public class AlertUtil {

  /**
   * <h2>showAlert</h2>
   *
   * Displays a modal error alert dialog with the specified title and message.
   * The alert is attached to the currently active main window if available.
   *
   * @param title   the title of the alert dialog
   * @param message the content text shown in the alert dialog
   */
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

  /**
   * <h2>findMainWindow</h2>
   *
   * Searches for the first visible window in the JavaFX application.
   *
   * @return the first active {@link Window}, or {@code null} if none are showing
   */
  private static Window findMainWindow() {
    return Stage.getWindows().stream()
            .filter(Window::isShowing)
            .findFirst()
            .orElse(null);
  }
}