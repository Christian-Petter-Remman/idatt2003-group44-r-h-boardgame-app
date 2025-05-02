package edu.ntnu.idi.idatt.controller.memorygame;

import edu.ntnu.idi.idatt.model.memorygame.MemoryBoardGame;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import edu.ntnu.idi.idatt.view.memorygame.MemoryGameView;
import javafx.scene.Parent;

public class MemoryGameController implements NavigationHandler {
  private final MemoryBoardGame model;
  private final MemoryGameView view;
  private final NavigationManager nav = NavigationManager.getInstance();

  public MemoryGameController(MemoryGameSettings settings) {
    this.model = new MemoryBoardGame(settings);
    this.view = new MemoryGameView(settings);
    model.addObserver(view);
    view.setOnCardClick(model::flipCard);
    view.setOnQuit(this::navigateBack);
    view.setOnRestart(this::onRestart);
    view.render(model);
  }

  public MemoryGameView getView() {
    return view;
  }

  private void onRestart() {
    model.reset();
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
