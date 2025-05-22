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

/**
 * <h1>StarCharSelectionController</h1>
 * Controller responsible for managing character selection logic in the Star game mode.
 * Handles character assignment, observer notification for CSV export, and transitions to the game screen.
 * Implements {@link NavigationHandler} to enable UI transitions.
 *
 * @author Oliver
 */
public class StarCharSelectionController implements NavigationHandler {

  private final CharacterSelectionManager manager;
  private final List<CsvExportObserver> observers = new ArrayList<>();
  private String savePath = "";

  private static final Logger logger = LoggerFactory.getLogger(StarCharSelectionController.class);

  /**
   * <h2>Constructor</h2>
   * Sets up the controller and binds the handler to the character selection screen.
   *
   * @param manager the shared character selection manager
   * @param screen the UI screen for Star character selection
   */
  public StarCharSelectionController(CharacterSelectionManager manager, StarCharSelectionScreen screen) {
    this.manager = manager;
    this.savePath = savePath;
    screen.setHandler(this);
  }

  /**
   * <h2>handleCharacterSelection</h2>
   * Assigns a character to a player if the character is not taken or is already selected by the same player.
   *
   * @param playerId the ID of the player making the selection
   * @param character the selected character data
   */
  public void handleCharacterSelection(int playerId, CharacterSelectionData character) {
    PlayerData player = manager.getPlayerById(playerId);
    if (player != null && player.isActive() &&
        (!character.isSelected() || character.getSelectedBy() == player)) {
      manager.selectCharacter(player, character);
    }
  }

  /**
   * <h2>getSavePath</h2>
   * Returns the current path where the game save is stored temporarily.
   *
   * @return the save file path
   */
  public String getSavePath() {
    return savePath;
  }

  /**
   * <h2>getPlayerDataList</h2>
   * Retrieves a copy of the list of all players from the manager.
   *
   * @return a list of {@link PlayerData}
   */
  private List<PlayerData> getPlayerDataList() {
    return new ArrayList<>(manager.getPlayers());
  }

  /**
   * <h2>addObserver</h2>
   * Registers a new observer for CSV export events.
   *
   * @param observer an implementation of {@link CsvExportObserver}
   */
  public void addObserver(CsvExportObserver observer) {
    observers.add(observer);
  }

  /**
   * <h2>notifyObservers</h2>
   * Notifies all registered observers to trigger their export routines.
   */
  public void notifyObservers() {
    observers.forEach(CsvExportObserver::onExportRequested);
  }

  /**
   * <h2>onStart</h2>
   * Begins the game after generating a save file and notifying the exporter.
   */
  public void onStart() {
    String saveFileName = SaveFileNameGenerator.StargenerateSaveFileName();
    savePath = "saves/temp/" + saveFileName;

    List<PlayerData> players = getPlayerDataList();
    StarGameStateExporter exporter = new StarGameStateExporter(players, savePath);

    addObserver(exporter);
    notifyObservers();

    NavigationManager.getInstance().navigateTo(NavigationTarget.STAR_GAME);

  }

  /**
   * <h2>activatePlayer</h2>
   * Marks the specified player as active for character selection.
   *
   * @param playerId the ID of the player to activate
   */
  public void activatePlayer(int playerId) {
    manager.activatePlayer(playerId);
  }

  /**
   * <h2>deactivatePlayer</h2>
   * Marks the specified player as inactive and clears their selection.
   *
   * @param playerId the ID of the player to deactivate
   */
  public void deactivatePlayer(int playerId) {
    manager.deactivatePlayer(playerId);
  }

  /**
   * <h2>navigateTo</h2>
   * Empty implementation; overridden from {@link NavigationHandler}.
   *
   * @param destination the target screen name
   */
  @Override
  public void navigateTo(String destination) {
  }

  /**
   * <h2>navigateBack</h2>
   * Returns to the previous screen in the navigation stack.
   */
  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
  }

  /**
   * <h2>setRoot</h2>
   * Replaces the application's root node with the given view.
   *
   * @param root the new root node
   */
  @Override
  public void setRoot(Parent root) {
    NavigationManager.getInstance().setRoot(root);
  }
}
