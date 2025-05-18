//package edu.ntnu.idi.idatt.model.common;
//
//import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
//import edu.ntnu.idi.idatt.observers.ModelObserver;
//import edu.ntnu.idi.idatt.observers.ObservableModel;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GameState implements ObservableModel {
//
//  private final List<ModelObserver> observers = new ArrayList<>();
//
//  private String boardFile;
//  private int diceCount;
//  private int currentTurnIndex;
//  private List<PlayerData> players;
//
//  public GameState(String boardFile, int diceCount, int currentTurnIndex, List<PlayerData> players) {
//    this.boardFile = boardFile;
//    this.diceCount = diceCount;
//    this.currentTurnIndex = currentTurnIndex;
//    this.players = players;
//  }
//
//  public String getBoardFile() {
//    return boardFile;
//  }
//
//  public int getDiceCount() {
//    return diceCount;
//  }
//
//  public int getCurrentTurnIndex() {
//    return currentTurnIndex;
//  }
//
//  public List<PlayerData> getPlayers() {
//    return players;
//  }
//
//  // Observer logic (optional)
//  public void setGameOver(boolean gameOver) {
//    notifyObservers("GAME_OVER", gameOver);
//  }
//
//  @Override
//  public void addObserver(ModelObserver observer) {
//    observers.add(observer);
//  }
//
//  @Override
//  public void removeObserver(ModelObserver observer) {
//    observers.remove(observer);
//  }
//
//  @Override
//  public void notifyObservers(String eventType, Object data) {
//    new ArrayList<>(observers).forEach(o -> o.update(eventType, data));
//  }
//}