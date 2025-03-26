package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.view.IntroScreenView;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainGUI extends Application {

  @Override
  public void start(Stage primaryStage) {
    IntroScreenView introScreenView = new IntroScreenView(primaryStage);
    introScreenView.show();
  }
  public static void main(String[] args) {
    launch(args);
  }
}


