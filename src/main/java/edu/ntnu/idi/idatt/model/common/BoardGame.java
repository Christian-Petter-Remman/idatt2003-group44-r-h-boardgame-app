package edu.ntnu.idi.idatt.model.common;

import edu.ntnu.idi.idatt.model.model_observers.GameObserver;
import edu.ntnu.idi.idatt.model.model_observers.PathDecisionObserver;
import edu.ntnu.idi.idatt.model.stargame.Path;

import java.util.ArrayList;
import java.util.List;

public abstract class BoardGame {
  protected List<Player> players;
  protected AbstractBoard board;
  protected Dice dice;
  protected int currentPlayerIndex;
  protected List<GameObserver> observers = new ArrayList<>();
  protected List<PathDecisionObserver> pathDecisionObservers = new ArrayList<>();

  public BoardGame(AbstractBoard board) {
    this.board = board;
    this.players = new ArrayList<>();
    this.dice = new Dice(1);
    this.currentPlayerIndex = 0;
  }

  public void initializePlayer(List<Player> players) {
    this.players = players;
    this.currentPlayerIndex = 0;
  }

  public void addPlayer(Player player) {
    this.players.add(player);
  }

  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  public void nextTurn() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  // Observer pattern methods (we will improve later)
  public void registerObserver(GameObserver observer) {
    observers.add(observer);
  }

  public void notifyMoveObservers(Player player, int roll) {
    for (GameObserver observer : observers) {
      observer.onPlayerMoved(player, player.getPosition(),player.getPosition()+roll);
    }
  }
  public void notifyWinnerObservers(Player player) {
    for (GameObserver observer : observers) {
      observer.onPlayerWon(player);
    }
  }

  public void notifyPathDecisionRequested(Player player, Path path) {
    for (GameObserver observer : observers) {
        observer.onPathDecisionRequested(player, path);
    }
  }
  public void notifyStarObservers(Player player, int newTile) {
    for (GameObserver observer : observers) {
      observer.onStarRespawned(player, newTile);
    }
  }

  public void notifyScoreObservers(Player player, int score) {
    for (GameObserver observer : observers) {
      observer.onScoreChanged(player, score);
    }
  }
  public AbstractBoard getBoard() {
    return board;
  }
}
