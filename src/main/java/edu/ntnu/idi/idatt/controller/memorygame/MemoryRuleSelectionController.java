package edu.ntnu.idi.idatt.controller.memorygame;

import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.model.memorygame.MemoryPlayer;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import java.util.List;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class MemoryRuleSelectionController implements NavigationHandler {
  private MemoryGameSettings settings;
  private MemoryGameSettings.BoardSize selectedSize = MemoryGameSettings.BoardSize.FOUR_BY_FOUR;
  private ToggleGroup sizeGroup;
  private TextField player1Field;
  private TextField player2Field;
  private final NavigationManager nav = NavigationManager.getInstance();

  public void setSizeGroup(ToggleGroup sizeGroup) {
    this.sizeGroup = sizeGroup;
    sizeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
      if (newToggle != null) {
        selectedSize = (MemoryGameSettings.BoardSize) newToggle.getUserData();
      }
    });
  }

  public void setPlayerFields(TextField player1Field, TextField player2Field) {
    this.player1Field = player1Field;
    this.player2Field = player2Field;
  }

  public void setBackButton(Button backBtn) {
    backBtn.setOnAction(e -> navigateBack());
  }

  public void setContinueButton(Button continueBtn) {
    continueBtn.setOnAction(e -> {
      settings = new MemoryGameSettings(
          selectedSize,
          List.of(
              new MemoryPlayer(player1Field.getText()),
              new MemoryPlayer(player2Field.getText())
          )
      );
      navigateTo(NavigationTarget.MEMORY_GAME_SCREEN.name());
    });
  }

  public MemoryGameSettings getSettings() {
    return settings;
  }

  @Override
  public void navigateTo(String destination) {
    nav.navigateTo(NavigationTarget.valueOf(destination));
  }

  @Override
  public void navigateBack() {
    nav.navigateBack();
  }

  @Override
  public void setRoot(Parent root) {
    nav.setRoot(root);
  }
}
