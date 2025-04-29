package edu.ntnu.idi.idatt.controller.snl;


import edu.ntnu.idi.idatt.model.model_observers.CsvExportObserver;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationManager.NavigationTarget;
import java.util.ArrayList;
import java.util.List;

public class SNLRuleSelectionController {

  private final SNLRuleSelectionModel model;
  private final List<CsvExportObserver> observers = new ArrayList<>();

  public SNLRuleSelectionController(SNLRuleSelectionModel model) {
    this.model = model;
  }

  public void addObserver(CsvExportObserver observer) {
    observers.add(observer);
  }

  public void onBoardSelected(String boardFile) {
    model.setSelectedBoardFile(boardFile);
  }

  public void onDiceCountSelected(int diceCount) {
    model.setDiceCount(diceCount);
  }

  public int getSelectedDiceCount() {
    return model.getDiceCount();
  }

  public void onContinuePressed() {
    for (CsvExportObserver observer : observers) {
      observer.onExportRequested();
    }
    NavigationManager.getInstance().navigateTo(NavigationTarget.SAL_GAME_SCREEN);
  }

  public void onBackPressed() {
    NavigationManager.getInstance().navigateBack();
  }
}