package edu.ntnu.idi.idatt.view.common.intro.dialogs;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.memorygame.MemoryPlayer;
import edu.ntnu.idi.idatt.model.snl.SNLPlayer;
import edu.ntnu.idi.idatt.model.stargame.StarPlayer;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Objects;

public class WinnerDialogs {

  public void showWinnerDialog(Player winner) {
    if (winner instanceof StarPlayer starPlayer) {
      showWinnerScreen("Star Game", starPlayer.getName(), "They earned " + starPlayer.getPoints() + " stars!", starPlayer.getCharacterIcon());
    } else if (winner instanceof SNLPlayer snlPlayer) {
      showWinnerScreen("Snakes and Ladders", snlPlayer.getName(), " Reached the top!", snlPlayer.getCharacterIcon());
    }
  }

  public void showMemoryWinnerDialog(List<MemoryPlayer> winners) {
    StringBuilder names = new StringBuilder();
    winners.forEach(p -> names.append(p.getName()).append(", "));
    String nameList = names.substring(0, names.length() - 2);
    showWinnerScreen("Memory Game", "Winners!", nameList, "null.png");
  }

  private void showWinnerScreen(String gameTitle, String winnerName, String message, String characterIcon) {
    Stage dialogStage = new Stage(StageStyle.UNDECORATED);
    dialogStage.initModality(Modality.APPLICATION_MODAL);

    VBox container = new VBox(30);
    container.setAlignment(Pos.CENTER);
    container.setPadding(new Insets(40));
    container.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #fefefe, #d9e6f2);" +
                    "-fx-border-color: #333333;" +
                    "-fx-border-width: 3;" +
                    "-fx-border-radius: 15;" +
                    "-fx-background-radius: 15;"
    );

    Label title = new Label(gameTitle);
    title.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 28));
    title.setTextFill(Color.DARKSLATEBLUE);

    Label header = new Label(winnerName);
    header.setFont(Font.font("Arial", FontWeight.BOLD, 22));
    header.setTextFill(Color.BLACK);

    Label subText = new Label(message);
    subText.setFont(Font.font("Arial", 18));
    subText.setTextFill(Color.DIMGRAY);

    ImageView imageView = new ImageView();
    try {
      Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player_icons/" + characterIcon.toLowerCase()+ ".png")));
      imageView.setImage(image);
      imageView.setFitHeight(180);
      imageView.setPreserveRatio(true);
    } catch (Exception e) {
      System.err.println("Could not load character icon: " + characterIcon);
    }

    Button homeButton = new Button("Home Page");
    homeButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
    homeButton.setStyle(
            "-fx-background-color: #4CAF50;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-radius: 10;" +
                    "-fx-padding: 12 28;"
    );
    homeButton.setOnAction(e -> {
      dialogStage.close();
      NavigationManager.getInstance().navigateToStartScreen();
    });

    container.getChildren().addAll(title, header, subText, imageView, homeButton);
    Scene scene = new Scene(container, 600, 500);
    dialogStage.setScene(scene);
    dialogStage.setOnCloseRequest(e -> e.consume());
    dialogStage.showAndWait();
  }
}