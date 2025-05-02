package edu.ntnu.idi.idatt.controller.memorygame;

import edu.ntnu.idi.idatt.model.memorygame.MemoryBoardGame;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import edu.ntnu.idi.idatt.view.memorygame.MemoryGameView;
import javafx.scene.Parent;

public class MemoryGameController implements NavigationHandler {

  private final MemoryGameView view;
  private final NavigationManager navManager = NavigationManager.getInstance();

  public MemoryGameController(MemoryGameSettings settings) {
    MemoryBoardGame model = new MemoryBoardGame(settings);
    this.view = new MemoryGameView(settings);
    model.addObserver(view);
    view.setOnCardClick(model::flipCard);
    view.render(model);
  }

  public MemoryGameView getView() {
    return view;
  }


  @Override
  public void navigateTo(String destination) {
    navManager.navigateTo(NavigationTarget.valueOf(destination));
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