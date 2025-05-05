package edu.ntnu.idi.idatt.controller.snl;

import edu.ntnu.idi.idatt.filehandling.SNLGameStateCsvExporter;
import edu.ntnu.idi.idatt.filehandling.SaveFileNameGenerator;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import edu.ntnu.idi.idatt.view.snl.SNLRuleSelectionView;

import javafx.scene.Parent;
import java.util.List;
import java.util.Random;

public class SNLRuleSelectionController implements NavigationHandler {
  private final SNLRuleSelectionModel model;
  private final SNLRuleSelectionView view;
  private final CharacterSelectionManager characterSelectionManager;

  public SNLRuleSelectionController(SNLRuleSelectionModel model, SNLRuleSelectionView view, CharacterSelectionManager manager) {
    this.model = model;
    this.view = view;
    this.characterSelectionManager = manager;
    attachListeners();
  }

  private void attachListeners() {
    view.getDifficultyGroup().selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
      if (newToggle != null) {
        model.setSelectedBoardFile(newToggle.getUserData().toString());
      }
    });
    view.getRandomBtn().setOnAction(e -> {
      List<String> rnd = model.getAvailableBoards().stream()
          .filter(f -> f.toLowerCase().startsWith("random"))
          .toList();
      if (!rnd.isEmpty()) {
        model.setSelectedBoardFile(rnd.get(new Random().nextInt(rnd.size())));
      }
    });
    view.getBackBtn().setOnAction(e -> navigateBack());
    view.getContinueBtn().setOnAction(e -> onContinue());
  }


  private void onContinue() {
    String savePath = "saves/" + SaveFileNameGenerator.SNLgenerateSaveFileName();

  public int getSelectedDiceCount() {
    return model.getDiceCount();
  }

  public void onContinuePressed() {
    String saveFileName = SaveFileNameGenerator.SNLgenerateSaveFileName();
    String savePath = "saves/temp/" + saveFileName;

    model.setSavePath(savePath);
    List<PlayerData> players = characterSelectionManager.getPlayers();
    SNLGameStateCsvExporter exporter = new SNLGameStateCsvExporter(model, players, savePath);
    model.addExportObserver(exporter);
    model.notifyExportObservers();
    navigateTo(NavigationTarget.SAL_GAME_SCREEN.name());
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
}