package edu.ntnu.idi.idatt.controller.memorygame;

import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.model.memorygame.MemoryPlayer;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;

/**
 * <h1>MemoryRuleSelectionController</h1>
 *
 * <p>Controller class for managing the rule selection screen in the memory game.
 * It handles user input for board size and player names, and prepares settings for launching the
 * game.
 *
 * @author Oliver, Christian
 */
public class MemoryRuleSelectionController implements NavigationHandler {

  private MemoryGameSettings settings;
  private MemoryGameSettings.BoardSize selectedSize = MemoryGameSettings.BoardSize.FOUR_BY_FOUR;
  private TextField player1Field;
  private TextField player2Field;
  private final NavigationManager nav = NavigationManager.getInstance();

  /**
   * <h2>setSizeGroup</h2>
   *
   * <p>Binds a toggle group to select the board size and listens for user changes.
   *
   * @param sizeGroup the toggle group used to select board size
   */
  public void setSizeGroup(ToggleGroup sizeGroup) {
    sizeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
      if (newToggle != null) {
        selectedSize = (MemoryGameSettings.BoardSize) newToggle.getUserData();
      }
    });
  }

  /**
   * <h2>setPlayerFields</h2>
   *
   * <p>Sets the text fields for the two player name inputs.
   *
   * @param player1Field text field for player 1
   * @param player2Field text field for player 2
   */
  public void setPlayerFields(TextField player1Field, TextField player2Field) {
    this.player1Field = player1Field;
    this.player2Field = player2Field;
  }

  /**
   * <h2>setBackButton</h2>
   *
   * <p>Assigns navigation logic to the back button.
   *
   * @param backBtn the JavaFX button that navigates back
   */
  public void setBackButton(Button backBtn) {
    backBtn.setOnAction(e -> navigateBack());
  }

  /**
   * <h2>setContinueButton</h2>
   *
   * <p>Assigns logic to the continue button. When pressed, it collects the board size and player
   * names, and navigates to the memory game screen.
   *
   * @param continueBtn the JavaFX button that continues to the game
   */
  public void setContinueButton(Button continueBtn) {
    continueBtn.setOnAction(e -> {
      String name1 = player1Field.getText().trim();
      String name2 = player2Field.getText().trim();

      if (name1.isEmpty() || name2.isEmpty()) {
        Label warning = new Label("Please enter names for both players!");
        warning.setStyle(
            "-fx-text-fill: red;"
                + "-fx-font-weight: bold;"
                + "-fx-font-size: 14px;"
                + "-fx-padding: 10;"
                + "-fx-background-color: #ffeeee;"
                + "-fx-border-color: red;"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5;"
        );
        StackPane.setAlignment(warning, Pos.TOP_CENTER);
        StackPane.setMargin(warning, new Insets(20));

        StackPane container = (StackPane) continueBtn.getScene().getRoot();
        if (!container.getChildren().contains(warning)) {
          container.getChildren().add(warning);
          new Thread(() -> {
            try {
              Thread.sleep(2000);
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
            }
            javafx.application.Platform.runLater(() -> container.getChildren().remove(warning));
          }).start();
        }
        return;
      }

      settings = new MemoryGameSettings(
          selectedSize,
          List.of(
              new MemoryPlayer(name1),
              new MemoryPlayer(name2)
          )
      );
      navigateTo(NavigationTarget.MEMORY_GAME_SCREEN.name());
    });
  }


  /**
   * <h2>getSettings</h2>
   *
   * <p>Returns the memory game settings selected by the user.
   *
   * @return configured {@link MemoryGameSettings}
   */
  public MemoryGameSettings getSettings() {
    return settings;
  }

  /**
   * <h2>navigateTo</h2>
   *
   * <p>Navigates to the specified screen.
   *
   * @param destination the name of the target screen
   */
  @Override
  public void navigateTo(String destination) {
    nav.navigateTo(NavigationTarget.valueOf(destination));
  }

  /**
   * <h2>navigateBack</h2>
   *
   * <p>Navigates back to the previous screen in the navigation stack.
   */
  @Override
  public void navigateBack() {
    nav.navigateBack();
  }

  /**
   * <h2>setRoot</h2>
   *
   * <p>Sets the specified node as the new root of the application.
   *
   * @param root the new root scene node
   */
  @Override
  public void setRoot(Parent root) {
    nav.setRoot(root);
  }
}