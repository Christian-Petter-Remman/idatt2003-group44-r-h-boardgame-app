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

  public MemoryGameController(MemoryGameSettings settings) {
    model = new MemoryBoardGame(settings);
    view = new MemoryGameView();
    view.initialize(settings);
    model.addObserver(view);
    view.setOnCardClick(model::flipCard);
    view.setOnRestart(model::reset);
    view.setOnQuit(this::navigateBack);
    view.render(model);
  }

  @Override
  public void navigateTo(String destination) {
    NavigationManager.getInstance().navigateTo(NavigationTarget.valueOf(destination));
  }

  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
  }

  @Override
  public void setRoot(Parent root) {
    NavigationManager.getInstance().setRoot(root);
  }

  public MemoryGameView getView() {
    return view;
  }
}
