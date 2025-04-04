package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.model.common.player.Player;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public abstract class AbstractRuleSelectionView {
  protected Stage primaryStage;
  protected GridPane layout;
  protected Button startGameButton;

  public AbstractRuleSelectionView(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.layout = new GridPane();
    this.startGameButton = new Button("Start Game");

    initializeLayout();
  }

  private void initializeLayout() {
    layout.setHgap(10);
    layout.setVgap(10);
    layout.setPadding(new javafx.geometry.Insets(10));

    Label titleLabel = new Label("Customize Game Rules");
    titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

    layout.add(titleLabel, 0, 0);
    layout.add(startGameButton, 0, 4);
  }

  protected abstract void addRuleOptions();
  protected abstract void onStart(List<Player> players);
  protected abstract void onBack();

  public void show(){
    Scene scene = new Scene(layout);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Game Rules");
    primaryStage.show();
  }

}
