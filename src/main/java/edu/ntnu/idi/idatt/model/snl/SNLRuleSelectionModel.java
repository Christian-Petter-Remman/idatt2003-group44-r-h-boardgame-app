package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.filehandling.handlers.SNLBoardJsonHandler;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>SNLRuleSelectionModel</h1>
 *
 * Handles logic related to rule selection for Snakes and Ladders,
 * including available boards, dice count, and observer notification.
 */
public class SNLRuleSelectionModel {

  /**
   * <h2>Observer</h2>
   *
   * Interface for views or controllers that need to react to changes in rule selection.
   */
  public interface Observer {
    void onRuleSelectionChanged();
  }

  private final List<Observer> observers = new ArrayList<>();
  private final List<String> availableBoards;
  private final SNLBoardJsonHandler boardHandler = new SNLBoardJsonHandler();

  private String selectedBoardFile;
  private int diceCount = 1;
  private String savePath;

  /**
   * <h2>Constructor</h2>
   *
   * Initializes the model and loads available board files.
   */
  public SNLRuleSelectionModel() {
    this.availableBoards = loadAvailableBoards();
    if (!availableBoards.isEmpty()) {
      this.selectedBoardFile = availableBoards.getFirst();
    }
  }

  /**
   * <h2>getSavePath</h2>
   *
   * @return Path where game state should be saved.
   */
  public String getSavePath() {
    return savePath;
  }

  /**
   * <h2>setSavePath</h2>
   *
   * @param savePath The path to save game state.
   */
  public void setSavePath(String savePath) {
    this.savePath = savePath;
  }

  /**
   * <h2>loadAvailableBoards</h2>
   *
   * Scans the board directory and returns a sorted list of JSON board filenames.
   *
   * @return List of board filenames.
   */
  private List<String> loadAvailableBoards() {
    File dir = new File(FileManager.SNAKES_LADDERS_BOARDS_DIR);
    File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".json"));
    if (files == null) return new ArrayList<>();

    return Arrays.stream(files)
            .map(File::getName)
            .sorted(String::compareToIgnoreCase)
            .collect(Collectors.toList());
  }

  /**
   * <h2>getAvailableBoards</h2>
   *
   * @return List of available board filenames.
   */
  public List<String> getAvailableBoards() {
    return availableBoards;
  }

  /**
   * <h2>getSelectedBoardFile</h2>
   *
   * @return The currently selected board filename.
   */
  public String getSelectedBoardFile() {
    return selectedBoardFile;
  }

  /**
   * <h2>setSelectedBoardFile</h2>
   *
   * Sets the currently selected board and notifies observers.
   *
   * @param boardFile Filename to set.
   */
  public void setSelectedBoardFile(String boardFile) {
    this.selectedBoardFile = boardFile;
    notifyObservers();
  }

  /**
   * <h2>getDiceCount</h2>
   *
   * @return The number of dice set.
   */
  public int getDiceCount() {
    return diceCount;
  }

  /**
   * <h2>setDiceCount</h2>
   *
   * Sets the dice count (limited to 1–2) and notifies observers.
   *
   * @param diceCount Dice count value.
   */
  public void setDiceCount(int diceCount) {
    this.diceCount = Math.max(1, Math.min(2, diceCount));
    notifyObservers();
  }

  /**
   * <h2>addObserver</h2>
   *
   * Registers an observer to be notified on rule changes.
   *
   * @param observer The observer to add.
   */
  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  /**
   * <h2>notifyObservers</h2>
   *
   * Notifies all registered observers about rule changes.
   */
  private void notifyObservers() {
    observers.forEach(Observer::onRuleSelectionChanged);
  }

  /**
   * <h2>getDisplayName</h2>
   *
   * Converts board filename to a user-friendly name.
   *
   * @param boardFile The filename.
   * @return Display name for UI.
   */
  public static String getDisplayName(String boardFile) {
    if (boardFile == null) return "";
    if (boardFile.equalsIgnoreCase("default.json")) return "Default";
    if (boardFile.equalsIgnoreCase("easy.json")) return "Easy";
    if (boardFile.equalsIgnoreCase("hard.json")) return "Hard";
    if (boardFile.toLowerCase().startsWith("random")) {
      String num = boardFile.replaceAll("[^0-9]", "");
      return "Random " + num;
    }
    return boardFile.replace(".json", "");
  }

  /**
   * <h2>getLadderCountFromJSON</h2>
   *
   * @return Number of ladders in the selected board.
   */
  public int getLadderCountFromJSON() {
    try {
      String path = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + selectedBoardFile;
      return boardHandler.loadFromFile(path).getLadders().size();
    } catch (FileReadException | JsonParsingException e) {
      return 0;
    }
  }

  /**
   * <h2>getSnakeCountFromJSON</h2>
   *
   * @return Number of snakes in the selected board.
   */
  public int getSnakeCountFromJSON() {
    try {
      String path = FileManager.SNAKES_LADDERS_BOARDS_DIR + "/" + selectedBoardFile;
      return boardHandler.loadFromFile(path).getSnakes().size();
    } catch (FileReadException | JsonParsingException e) {
      return 0;
    }
  }
}