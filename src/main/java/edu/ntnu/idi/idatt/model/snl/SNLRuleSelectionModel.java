package edu.ntnu.idi.idatt.model.snl;


import java.util.ArrayList;
import java.util.List;

import edu.ntnu.idi.idatt.filehandling.FileManager;

import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.model.model_observers.CsvExportObserver;

import java.io.File;

public class SNLRuleSelectionModel {

  public interface Observer {
    void onRuleSelectionChanged();
  }

  private final List<Observer> observers = new ArrayList<>();
  private final List<CsvExportObserver> exportObservers = new ArrayList<>();

  private final List<String> availableBoards;
  private String selectedBoardFile;
  private int diceCount = 1;

  public SNLRuleSelectionModel() {
    this.availableBoards = loadAvailableBoards();
    if (!availableBoards.isEmpty()) {
      this.selectedBoardFile = availableBoards.getFirst();
    }
  }

  public void addExportObserver(CsvExportObserver observer) {
    exportObservers.add(observer);
  }

  public void notifyExportObservers() {
    for (CsvExportObserver observer : exportObservers) {
      observer.onExportRequested();
    }
  }

  private List<String> loadAvailableBoards() {
    List<String> boards = new ArrayList<>();
    File dir = new File(FileManager.SNAKES_LADDERS_BOARDS_DIR);
    File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".json"));
    if (files != null) {
      for (File file : files) {
        boards.add(file.getName());
      }
    }
    boards.sort(String::compareToIgnoreCase);
    return boards;
  }

  public List<String> getAvailableBoards() {
    return availableBoards;
  }

  public String getSelectedBoardFile() {
    return selectedBoardFile;
  }

  public void setSelectedBoardFile(String boardFile) {
    this.selectedBoardFile = boardFile;
    notifyObservers();
  }

  public int getDiceCount() {
    return diceCount;
  }

  public void setDiceCount(int diceCount) {
    if (diceCount < 1) diceCount = 1;
    if (diceCount > 2) diceCount = 2;
    this.diceCount = diceCount;
    notifyObservers();
  }

  public void addObserver(Observer observer) {
    observers.add(observer);
  }

  public void removeObserver(Observer observer) {
    observers.remove(observer);
  }

  private void notifyObservers() {
    for (Observer observer : observers) {
      observer.onRuleSelectionChanged();
    }
  }

//  public Board loadSelectedBoard() throws FileReadException, JsonParsingException {
//    String path = FileManager.SNAKES_LADDERS_BOARDS_DIR + File.separator + selectedBoardFile;
//    BoardJsonHandler handler = new BoardJsonHandler();
//    return handler.loadBoardFromFile(path);
//  }

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
}
