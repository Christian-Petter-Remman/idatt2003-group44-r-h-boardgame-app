package edu.ntnu.idi.idatt.controller.snl;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationManager.NavigationTarget;

/**
 * Controller for the Snakes and Ladders rule selection screen.
 * Handles user interactions and updates the model accordingly.
 */
public class SNLRuleSelectionController {

  private final SNLRuleSelectionModel model;

  public SNLRuleSelectionController(SNLRuleSelectionModel model) {
    this.model = model;
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
    NavigationManager.getInstance().navigateTo(NavigationTarget.SAL_GAME_SCREEN);
  }

  public void onBackPressed() {
    NavigationManager.getInstance().navigateBack();
  }

}
