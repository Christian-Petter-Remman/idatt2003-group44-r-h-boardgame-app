package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.view.IntroScreenView;
import edu.ntnu.idi.idatt.view.SnakesAndLaddersCharacterSelectionView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
    try {

      SnakesAndLaddersCharacterSelectionView snake = new SnakesAndLaddersCharacterSelectionView(primaryStage);
      snake.show();

    } catch (Exception e) {
      showStartupError("Startup Error", e.getMessage());
      e.printStackTrace();
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