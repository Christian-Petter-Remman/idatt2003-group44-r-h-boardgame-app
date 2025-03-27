package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.view.IntroScreenView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
    try {
      // Start GUI på Intro-skjermen
      IntroScreenView introScreenView = new IntroScreenView(primaryStage);
      introScreenView.show();

    } catch (Exception e) {
      showStartupError("Startup Error", e.getMessage());
      e.printStackTrace(); // Du kan bruke logger her senere
    }
  }

  private void showStartupError(String title, String message) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.ERROR,
            message,
            javafx.scene.control.ButtonType.OK
    );
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.showAndWait();
  }

  public static void main(String[] args) {
    launch(args);
  }
}