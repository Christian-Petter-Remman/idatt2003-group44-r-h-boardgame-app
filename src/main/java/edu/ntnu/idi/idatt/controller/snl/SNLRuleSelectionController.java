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
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Parent;


/**
 * <h1>SNLRuleSelectionController</h1>
 * Controller responsible for handling the logic related to rule selection in the Snakes and Ladders
 * game. It manages communication between the rule selection view, model, and navigation manager.
 */
public class SNLRuleSelectionController implements NavigationHandler {

  private final SNLRuleSelectionModel model;
  private final List<CsvExportObserver> observers = new ArrayList<>();
  private final CharacterSelectionManager characterSelectionManager;

  /**
   * <h2>Constructor.</h2>
   *
   * @param model   The rule selection model.
   * @param manager The character selection manager.
   */
  public SNLRuleSelectionController(SNLRuleSelectionModel model,
      CharacterSelectionManager manager) {
    this.model = model;
    this.characterSelectionManager = manager;
  }

  /**
   * <h2>Adds an observer to be notified when export is triggered.</h2>
   *
   * @param observer the observer to add
   */
  public void addObserver(CsvExportObserver observer) {
    observers.add(observer);
  }

  /**
   * <h2>Notifies all registered observers to perform CSV export.</h2>
   */
  private void notifyObservers() {
    observers.forEach(CsvExportObserver::onExportRequested);
  }

  /**
   * <h2>Sets the selected board file in the model.</h2>
   *
   * @param boardFile the filename of the selected board
   */
  public void setSelectedBoardFile(String boardFile) {
    model.setSelectedBoardFile(boardFile);
  }

  /**
   * <h2>Sets the dice count to be used in the game.</h2>
   *
   * @param diceCount number of dice to use
   */
  public void setDiceCount(int diceCount) {
    model.setDiceCount(diceCount);
  }

  /**
   * <h2>Handles the logic when the "Continue" button is pressed.</h2>
   * Generates a save file name and triggers the CSV export logic.
   */
  public void onContinuePressed() {
    String saveFileName = SaveFileNameGenerator.snlGenerateSaveFileName();
    String savePath = "saves/temp/" + saveFileName;
    model.setSavePath(savePath);

    List<PlayerData> players = characterSelectionManager.getPlayers();
    SNLGameStateCsvExporter exporter = new SNLGameStateCsvExporter(model, players, savePath);

    addObserver(exporter);
    notifyObservers();

    NavigationManager.getInstance().navigateTo(NavigationTarget.SAL_GAME_SCREEN);
  }

  /**
   * <h2>Handles logic when "Back" is pressed.</h2>
   */
  public void onBackPressed() {
    NavigationManager.getInstance().navigateBack();
  }

  /**
   * <h2>Navigates to a specified destination screen.</h2>
   *
   * @param destination the name of the screen to navigate to
   */
  @Override
  public void navigateTo(String destination) {
    NavigationManager.getInstance().navigateTo(NavigationTarget.valueOf(destination));
  }

  /**
   * <h2>Navigates back to the previous screen.</h2>
   */
  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
  }

  /**
   * <h2>Sets the root node of the view.</h2>
   *
   * @param root the new root node
   */
  @Override
  public void setRoot(Parent root) {
    NavigationManager.getInstance().setRoot(root);
  }
}