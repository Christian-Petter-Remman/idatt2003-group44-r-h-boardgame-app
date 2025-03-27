package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.FileWriteException;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.view.IntroScreenView;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainGUI extends Application {

  @Override
  public void start(Stage primaryStage) {
    try {
      FileManager.ensureApplicationDirectoriesExist();
      FileManager.saveDefaultBoard(new Board());
      SnakesAndLadders game = new SnakesAndLadders();
      FileManager.loadOrCreateDefaultPlayers(game);

      IntroScreenView introScreenView = new IntroScreenView(primaryStage);
      introScreenView.setGame(game);
      introScreenView.show();

    } catch (FileReadException | FileWriteException | CsvFormatException e) {
      showStartupError("Startup Error", e.getMessage());
      e.printStackTrace();
    }
  }

  private void showStartupError(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText("Could not start the game");
    alert.setContentText(message);
    alert.showAndWait();
  }

  public static void main(String[] args) {
    launch(args);
  }
}