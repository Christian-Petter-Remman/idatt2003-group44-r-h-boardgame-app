package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.model.common.player.Player;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.List;

public abstract class AbstractRuleSelectionView {
  protected Stage primaryStage;
  protected GridPane layout;
  protected Button startGameButton;
  protected Button backButton;
  protected Label titleLabel;
  protected Label descriptionLabel;

  public AbstractRuleSelectionView(Stage primaryStage) {
    this.primaryStage = primaryStage;
    this.layout = new GridPane();
    initializeBaseLayout();
    createCommonElements();
  }

  private void initializeBaseLayout() {
    layout.setHgap(20);
    layout.setVgap(20);
    layout.setPadding(new Insets(30, 40, 40, 40));
    layout.setStyle("-fx-background-color: #f8f9fa;");
  }

  private void createCommonElements() {
    // Title
    titleLabel = new Label("Game Rules Configuration");
    titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
    layout.add(titleLabel, 0, 0, 2, 1);

    // Description
    descriptionLabel = new Label("Customize your game settings below");
    descriptionLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
    layout.add(descriptionLabel, 0, 1, 2, 1);

    // Navigation buttons
    backButton = createNavigationButton("Back", "#bdc3c7", "#2c3e50");
    startGameButton = createNavigationButton("Start Game", "#27ae60", "white");

    GridPane buttonContainer = new GridPane();
    buttonContainer.setHgap(20);
    buttonContainer.add(backButton, 0, 0);
    buttonContainer.add(startGameButton, 1, 0);

    layout.add(buttonContainer, 0, 99, 2, 1);
    GridPane.setHgrow(buttonContainer, Priority.ALWAYS);
    GridPane.setMargin(buttonContainer, new Insets(40, 0, 0, 0));
  }

  protected Button createNavigationButton(String text, String bgColor, String textColor) {
    Button btn = new Button(text);
    btn.setStyle("-fx-font-size: 14px; -fx-padding: 10 20; -fx-min-width: 120px; "
        + "-fx-background-color: " + bgColor + "; "
        + "-fx-text-fill: " + textColor + "; "
        + "-fx-background-radius: 5px;");
    return btn;
  }

  protected abstract void initializeCustomComponents();
  protected abstract void layoutCustomComponents();
  protected abstract void setupEventHandlers();
  protected abstract void onStart(List<Player> players);
  protected abstract void onBack();

  public void show() {
    initializeCustomComponents();
    layoutCustomComponents();
    setupEventHandlers();

    Scene scene = new Scene(layout, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Game Rules Configuration");
    primaryStage.show();
  }
}
