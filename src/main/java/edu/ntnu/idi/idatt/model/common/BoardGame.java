package edu.ntnu.idi.idatt.model.common;

import edu.ntnu.idi.idatt.model.model_observers.BoardObserver;
import edu.ntnu.idi.idatt.model.model_observers.GameObserver;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.model_observers.PathDecisionObserver;
import edu.ntnu.idi.idatt.model.stargame.Path;

import java.util.ArrayList;
import java.util.List;

public abstract class BoardGame {
  protected List<Player> players;
  protected AbstractBoard board;
  protected Dice dice;
  protected int currentPlayerIndex;

  // Observer lists
  protected final List<GameObserver> observers = new ArrayList<>();
  protected final List<PathDecisionObserver> pathDecisionObservers = new ArrayList<>();
  protected final List<GameScreenObserver> turnObservers = new ArrayList<>();
  protected final List<GameScreenObserver> moveObservers = new ArrayList<>();
  protected final List<GameScreenObserver> winnerObservers = new ArrayList<>();
  protected final List<BoardObserver> boardObservers = new ArrayList<>();


  public BoardGame(AbstractBoard board) {
    this.board = board;
    this.players = new ArrayList<>();
    this.dice = new Dice(1);
    this.currentPlayerIndex = 0;
  }

  public Player getWinner() {
    return players.stream()
            .filter(Player::hasWon)
            .findFirst()
            .orElse(null);
  }

  public void initializePlayer(List<Player> players) {
    this.players = players;
    this.currentPlayerIndex = 0;
  }

  public void addPlayer(Player player) {
    this.players.add(player);
  }

  public List<Player> getPlayers() {
    return players;
  }

  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  public void nextTurn() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  public AbstractBoard getBoard() {
    return board;
  }

  //=========== OBSERVER REGISTRATION ============

  public void registerObserver(GameObserver observer) {
    observers.add(observer);
  }

  public void registerPathDecisionObserver(PathDecisionObserver observer) {
    pathDecisionObservers.add(observer);
  }

  public void addTurnObserver(GameScreenObserver observer) {
    turnObservers.add(observer);
  }

  public void addMoveObserver(GameScreenObserver observer) {
    moveObservers.add(observer);
  }

  public void addWinnerObserver(GameScreenObserver observer) {
    winnerObservers.add(observer);
  }

  public void addBoardObserver(BoardObserver observer) {
    boardObservers.add(observer);
  }

  //=========== OBSERVER NOTIFICATIONS ============

  public void notifyMoveObservers(Player player, int roll) {
    for (GameScreenObserver observer : moveObservers) {
      observer.onDiceRolled(roll);
      observer.onPlayerPositionChanged(player, -1, player.getPosition());
      observer.onPlayerTurnChanged(player);
    }
    for (BoardObserver observer : boardObservers) {
      observer.onPlayerMoved(player, -1, player.getPosition());
    }
  }

  public void notifyWinnerObservers(Player player) {
    for (GameScreenObserver observer : winnerObservers) {
      observer.onGameOver(player);
    }
  }

  public void notifyPathDecisionRequested(Player player, Path path) {
    for (GameObserver observer : observers) {
      observer.onPathDecisionRequested(player, path);
    }
    for (PathDecisionObserver observer : pathDecisionObservers) {
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
}