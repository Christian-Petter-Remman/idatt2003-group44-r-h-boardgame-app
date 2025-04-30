package edu.ntnu.idi.idatt.controller.snl;

import edu.ntnu.idi.idatt.filehandling.GameStateCsvExporter;
import edu.ntnu.idi.idatt.filehandling.SaveFileNameGenerator;
import edu.ntnu.idi.idatt.model.model_observers.CsvExportObserver;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationManager.NavigationTarget;
import java.util.ArrayList;
import java.util.List;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;

public class SNLRuleSelectionController {

  private final SNLRuleSelectionModel model;
  private final CharacterSelectionManager characterSelectionManager;
  private final List<CsvExportObserver> observers = new ArrayList<>();

  public SNLRuleSelectionController(SNLRuleSelectionModel model, CharacterSelectionManager manager) {
    this.model = model;
    this.characterSelectionManager = manager;
  }

  public void notifyObservers() {
    for (CsvExportObserver o : observers) {
      o.onExportRequested();
    }
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
    // 1. Generate and store the save file path
    String saveFileName = SaveFileNameGenerator.generateSaveFileName();
    String savePath = "saves/" + saveFileName;
    model.setSavePath(savePath);

    // 2. Create and register the CSV exporter
    GameStateCsvExporter exporter = new GameStateCsvExporter(
            model,
            characterSelectionManager.getPlayers(),
            savePath
    );
    addObserver(exporter);

    notifyObservers();


    NavigationManager.getInstance().navigateToSNLGameScreen();
  }

  public void onBackPressed() {
    NavigationManager.getInstance().navigateBack();
  }
}
