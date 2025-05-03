// LoadGameView.java
package edu.ntnu.idi.idatt.view.star;

import edu.ntnu.idi.idatt.controller.common.load.LoadGameController;
import edu.ntnu.idi.idatt.filehandling.GameStateCsvLoader;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import edu.ntnu.idi.idatt.model.stargame.StarGame;
import edu.ntnu.idi.idatt.controller.star.StarGameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Parent;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LoadGameView {
  private final LoadGameController controller;
  private final VBox root;

  public LoadGameView(LoadGameController controller) {
    this.controller = controller;
    this.root = new VBox(15);
    root.setPadding(new Insets(30));
    root.setAlignment(Pos.CENTER);

    Label header = new Label("Select a save file to load:");
    header.setStyle("-fx-font-size: 20px;");
    root.getChildren().add(header);

    File[] recentFiles = controller.getRecentSaveFiles(10);
    for (File file : recentFiles) {
      Button fileButton = new Button(file.getName());
      fileButton.setPrefWidth(300);
      fileButton.setOnAction(e -> controller.loadStarGame(file));
      root.getChildren().add(fileButton);
    }
  }

  public VBox getRoot() {
    return root;
  }
}