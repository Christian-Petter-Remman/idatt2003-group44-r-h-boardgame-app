package edu.ntnu.idi.idatt.model.common.memorygame;


import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionData;
import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemoryGame {
  private final List<MemoryPlayer> players;
  private final MemoryBoard board;
  private int currentPlayerIdx = 0;
  private final List<MemoryGameObserver> observers = new ArrayList<>();

  private MemoryCard firstFlipped = null;
  private GameState state = GameState.NOT_STARTED;

  public MemoryGame(List<PlayerData> playerDataList,
      int rows, int cols,
      List<CharacterSelectionData> icons) {

    this.players = playerDataList.stream()
        .map(MemoryPlayer::new)
        .collect(Collectors.toList());

    this.board = new MemoryBoard(rows, cols, icons);
  }

  public void addObserver(MemoryGameObserver observer) {
    observers.add(observer);
  }
  public void removeObserver(MemoryGameObserver observer) {
    observers.remove(observer);
  }

  private void fireCardFlipped(int row, int col, CardState state) {
    for (MemoryGameObserver observer : observers) {
      observer.onCardFlipped(row, col, state);
    }
  }

  private void fireMatchFound(MemoryPlayer player, int newScore) {
    for (MemoryGameObserver observer : observers) {
      observer.onMatchFound(player, newScore);
    }
  }

  private void fireTurnChanged(MemoryPlayer nextPlayer) {
    for (MemoryGameObserver observer : observers) {
      observer.onTurnChanged(nextPlayer);
    }
  }

  private void fireGameStateChanged(GameState newState) {
    for (MemoryGameObserver observer : observers) {
      observer.onGameStateChanged(newState);
    }
  }

  private void fireGameFinished(List<MemoryPlayer> winners) {
    for (MemoryGameObserver observer : observers) {
      observer.onGameFinished(winners);
    }
  }




  public MemoryBoard getBoard() {
    return board;
  }

  public void start() {
    state = GameState.IN_PROGRESS;
  }

  public boolean flipCard(int r, int c) {
    if (state != GameState.IN_PROGRESS) {
      throw new IllegalStateException("Game not in progress");
    }

    MemoryCard card = board.getCard(r, c);
    if (!card.isMatchable()) {
      throw new IllegalArgumentException("Cannot flip this card");
    }

    card.flipUp();

    if (firstFlipped == null) {
      firstFlipped = card;
      return false;
    } else {
      boolean match = firstFlipped.getPairId() == card.getPairId();
      if (match) {
        firstFlipped.markMatched();
        card.markMatched();
        currentPlayer().incrementMatches();
      } else {
        firstFlipped.flipDown();
        card.flipDown();
        advanceTurn();
      }
      firstFlipped = null;
      checkForEnd();
      return match;
    }
  }

  private void advanceTurn() {
    currentPlayerIdx = (currentPlayerIdx + 1) % players.size();
  }

  private void checkForEnd() {
    boolean allMatched = true;
    for (int r = 0; r < board.getRowCount(); r++) {
      for (int c = 0; c < board.getColCount(); c++) {
        if (board.getCard(r, c).getState() != CardState.MATCHED) {
          allMatched = false;
        }
      }
    }
    if (allMatched) {
      state = GameState.FINISHED;
    }
  }

  public MemoryPlayer currentPlayer() {
    return players.get(currentPlayerIdx);
  }

  public GameState getState() {
    return state;
  }

  public List<MemoryPlayer> getWinners() {
    if (state != GameState.FINISHED) {
      throw new IllegalStateException("Game not over");
    }
    int max = players.stream().mapToInt(MemoryPlayer::getMatchesFound).max().orElse(0);
    return players.stream()
        .filter(p -> p.getMatchesFound() == max)
        .collect(Collectors.toList());
  }
}
