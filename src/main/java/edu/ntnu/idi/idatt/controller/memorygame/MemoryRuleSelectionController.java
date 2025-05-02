package edu.ntnu.idi.idatt.controller.memorygame;

import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.model.memorygame.MemoryPlayer;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.util.Arrays;
import java.util.List;

public class MemoryRuleSelectionController implements NavigationHandler {

  private final NavigationManager navManager = NavigationManager.getInstance();
  private MemoryGameSettings.BoardSize selectedSize = MemoryGameSettings.BoardSize.FOUR_BY_FOUR;
  private TextField player1Field, player2Field;
  private MemoryGameSettings settings;

  public void setSizeGroup(ToggleGroup sizeGroup) {
    sizeGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
      if (newT != null) {
        selectedSize = (MemoryGameSettings.BoardSize) newT.getUserData();
      }
    });
  }

  public void setPlayerFields(TextField p1, TextField p2) {
    this.player1Field = p1;
    this.player2Field = p2;
  }

  public void setBackButton(Button backBtn) {
    backBtn.setOnAction(e -> navigateBack());
  }

  public void setContinueButton(Button contBtn) {
    contBtn.setOnAction(e -> navigateTo(
        NavigationTarget.MEMORY_GAME_SCREEN.name()
    ));
  }

  public MemoryGameSettings getSettings() {
    return settings;
  }

  @Override
  public void navigateTo(String destination) {
    if (NavigationTarget.MEMORY_GAME_SCREEN.name().equals(destination)) {
      List<MemoryPlayer> players = Arrays.asList(
          new MemoryPlayer(player1Field.getText()),
          new MemoryPlayer(player2Field.getText())
      );
      settings = new MemoryGameSettings(selectedSize, players);
      navManager.navigateTo(NavigationTarget.MEMORY_GAME_SCREEN);
    } else {
      navManager.navigateTo(NavigationTarget.valueOf(destination));
    }
  }

  @Override
  public void navigateBack() {
    navManager.navigateBack();
  }

  @Override
  public void setRoot(Parent root) {
    navManager.setRoot(root);
  }
}
