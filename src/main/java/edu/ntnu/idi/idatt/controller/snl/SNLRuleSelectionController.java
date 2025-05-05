package edu.ntnu.idi.idatt.controller.snl;

import edu.ntnu.idi.idatt.filehandling.SNLGameStateCsvExporter;
import edu.ntnu.idi.idatt.filehandling.SaveFileNameGenerator;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import edu.ntnu.idi.idatt.model.model_observers.CsvExportObserver;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;

import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import javafx.scene.Parent;

import java.util.ArrayList;
import java.util.List;

public class SNLRuleSelectionController implements NavigationHandler {

  private final SNLRuleSelectionModel model;
  private final List<CsvExportObserver> observers = new ArrayList<>();
  private final CharacterSelectionManager characterSelectionManager;

  public SNLRuleSelectionController(SNLRuleSelectionModel model, CharacterSelectionManager manager) {
    this.model = model;
    this.characterSelectionManager = manager;
  }

  public void addObserver(CsvExportObserver observer) {
    observers.add(observer);
  }

  public void notifyObservers() {
    for (CsvExportObserver o : observers) {
      o.onExportRequested();
    }
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
    String saveFileName = SaveFileNameGenerator.SNLgenerateSaveFileName();
    String savePath = "saves/temp/" + saveFileName;
    model.setSavePath(savePath);

    List<PlayerData> players = characterSelectionManager.getPlayers();
    SNLGameStateCsvExporter exporter = new SNLGameStateCsvExporter(model, players, savePath);

    addObserver(exporter);
    notifyObservers();

    NavigationManager.getInstance().navigateToSNLGameScreen();
  }

  public void onBackPressed() {
    NavigationManager.getInstance().navigateBack();
  }

  // === NavigationHandler IMPLEMENTATION ===

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
}