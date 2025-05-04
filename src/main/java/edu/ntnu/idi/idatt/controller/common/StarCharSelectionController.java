package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.filehandling.SaveFileNameGenerator;
import edu.ntnu.idi.idatt.filehandling.StarGameStateExporter;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionData;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import edu.ntnu.idi.idatt.model.model_observers.CsvExportObserver;
import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import edu.ntnu.idi.idatt.navigation.NavigationTarget;
import edu.ntnu.idi.idatt.view.common.character.StarCharSelectionScreen;
import javafx.scene.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StarCharSelectionController implements NavigationHandler {

  private final CharacterSelectionManager manager;
  private final List<CsvExportObserver> observers = new ArrayList<>();
  private String savePath = "";

  private static final Logger logger = LoggerFactory.getLogger(StarCharSelectionController.class);

  public StarCharSelectionController(CharacterSelectionManager manager, StarCharSelectionScreen screen) {
    this.manager = manager;
    this.savePath = savePath;
    screen.setHandler(this);
  }

  public void handleCharacterSelection(int playerId, CharacterSelectionData character) {
    PlayerData player = manager.getPlayerById(playerId);
    if (player != null && player.isActive() &&
            (!character.isSelected() || character.getSelectedBy() == player)) {
      manager.selectCharacter(player, character);
    }
  }

  public String getSavePath(){
    return savePath;
  }

  private List<PlayerData> getPlayrDataList() {
    return new ArrayList<>(manager.getPlayers());
  }

  public void addObserver(CsvExportObserver observer) {
    observers.add(observer);
  }

  public void notifyObservers() {
    for (CsvExportObserver o : observers) {
      o.onExportRequested();
    }
  }

  public void onStart(){
    String saveFileName = SaveFileNameGenerator.StargenerateSaveFileName();
    savePath = "saves/temp/" + saveFileName;
    List<PlayerData> players = getPlayrDataList();
    StarGameStateExporter exporter = new StarGameStateExporter(players, savePath);

    addObserver(exporter);
    notifyObservers();

    NavigationManager.getInstance().navigateTo(NavigationTarget.STAR_GAME);

  }

  public void activatePlayer(int playerId) {
    manager.activatePlayer(playerId);
  }

  public void deactivatePlayer(int playerId) {
    manager.deactivatePlayer(playerId);
  }

  @Override
  public void navigateTo(String destination) {
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
