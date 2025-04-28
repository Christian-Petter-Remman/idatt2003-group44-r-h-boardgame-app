package edu.ntnu.idi.idatt.controller.snakesandladders;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.rule_selection.SalRuleSelectionModel;

/**
 * Controller for the Snakes and Ladders rule selection screen.
 * Handles user interactions and updates the model accordingly.
 */
public class SalRuleSelectionController {

  private final SalRuleSelectionModel model;

  public SalRuleSelectionController(SalRuleSelectionModel model) {
    this.model = model;
  }

  public void onBoardSelected(String boardFile) {
    model.setSelectedBoardFile(boardFile);
  }

  public void onDiceCountSelected(int diceCount) {
    model.setDiceCount(diceCount);
  }

  public Board loadSelectedBoardForGame() throws FileReadException, JsonParsingException {
    return model.loadSelectedBoard();
  }

  public int getSelectedDiceCount() {
    return model.getDiceCount();
  }
}
