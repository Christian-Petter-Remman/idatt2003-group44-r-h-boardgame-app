package edu.ntnu.idi.idatt.model.snl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.filehandling.handlers.SNLBoardJsonHandler;
import edu.ntnu.idi.idatt.model.model_observers.CsvExportObserver;

public class SNLRuleSelectionModel {

  public interface Observer {

    void onRuleSelectionChanged();
  }

  private final List<Observer> observers = new ArrayList<>();
  private final List<CsvExportObserver> exportObservers = new ArrayList<>();
  private final List<String> availableBoards;
  private String selectedBoardFile;
  private int diceCount = 1;
  private String savePath;
  private int snakeCount;
  private int ladderCount;
  private static final org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger(SNLRuleSelectionModel.class);

  public SNLRuleSelectionModel() {
    this.availableBoards = loadAvailableBoards();
    if (!availableBoards.isEmpty()) {
      setSelectedBoardFile(availableBoards.getFirst());
    }
  }

  public List<String> getAvailableBoards() {
    return availableBoards;
  }

  public String getSelectedBoardFile() {
    return selectedBoardFile;
  }

  public void setSelectedBoardFile(String boardFile) {
    this.selectedBoardFile = boardFile;
    updateCounts(boardFile);
    notifyObservers();
  }

  public int getDiceCount() {
    return diceCount;
  }

  public void setDiceCount(int diceCount) {
    if (diceCount < 1) {
      diceCount = 1;
    }
    if (diceCount > 2) {
      diceCount = 2;
    }
    this.diceCount = diceCount;
    logger.info("Dice count set to: {}", diceCount);
    notifyObservers();
  }

  public String getSavePath() {
    return savePath;
  }

  public void setSavePath(String savePath) {
    this.savePath = savePath;
  }

  public int getSnakeCount() {
    return snakeCount;
  }

  public int getLadderCount() {
    return ladderCount;
  }

  private void updateCounts(String boardFile) {
    try {
      String path = FileManager.SNAKES_LADDERS_BOARDS_DIR + File.separator + boardFile;
      var board = new SNLBoardJsonHandler().loadBoardFromFile(path);
      this.snakeCount = board.getSnakes().size();
      this.ladderCount = board.getLadders().size();
    } catch (Exception e) {
      this.snakeCount = 0;
      this.ladderCount = 0;
    }
  }

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  private void notifyObservers() {
    observers.forEach(Observer::onRuleSelectionChanged);
  }

  public void addExportObserver(CsvExportObserver observer) {
    exportObservers.add(observer);
  }

  public void notifyExportObservers() {
    exportObservers.forEach(CsvExportObserver::onExportRequested);
  }

  private List<String> loadAvailableBoards() {
    List<String> boards = new ArrayList<>();
    File dir = new File(FileManager.SNAKES_LADDERS_BOARDS_DIR);
    File[] files = dir.listFiles((d, n) -> n.toLowerCase().endsWith(".json"));
    if (files != null) {
      for (File f : files) {
        boards.add(f.getName());
      }
    }
    boards.sort(String::compareToIgnoreCase);
    return boards;
  }

  public static String getDisplayName(String boardFile) {
    if (boardFile == null) {
      return "";
    }
    if (boardFile.equalsIgnoreCase("default.json")) {
      return "Default";
    }
    if (boardFile.equalsIgnoreCase("easy.json")) {
      return "Easy";
    }
    if (boardFile.equalsIgnoreCase("hard.json")) {
      return "Hard";
    }
    if (boardFile.toLowerCase().startsWith("random")) {
      String num = boardFile.replaceAll("[^0-9]", "");
      return "Random " + num;
    }
    return boardFile.replace(".json", "");
  }
}