package edu.ntnu.idi.idatt.model.memorygame;

import edu.ntnu.idi.idatt.model.common.factory.MemoryCardFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryBoardGame {

  private final List<MemoryCard> cards;
  private final List<MemoryPlayer> players;
  private int currentPlayerIndex;
  private MemoryCard firstSelected;
  private final List<MemoryGameObserver> observers = new ArrayList<>();

  public MemoryBoardGame(MemoryGameSettings settings) {
    this.cards = MemoryCardFactory.loadAndShuffle(settings.getBoardSize());
    this.players = new ArrayList<>(settings.getPlayers());
    this.currentPlayerIndex = 0;
    this.firstSelected = null;
  }

  public void addObserver(MemoryGameObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(MemoryGameObserver observer) {
    observers.remove(observer);
  }

  private void notifyBoardUpdated() {
    for (MemoryGameObserver o : observers) {
      o.onBoardUpdated(this);
    }
  }

  private void notifyGameOver() {
    for (MemoryGameObserver o : observers) {
      o.onGameOver(getWinners());
    }
  }

  public void flipCard(int index) {
    MemoryCard card = cards.get(index);
    if (card.isFaceUp() || card.isMatched()) {
      return;
    }
    card.setFaceUp(true);
    if (firstSelected == null) {
      firstSelected = card;
      notifyBoardUpdated();
      return;
    }
    if (firstSelected.getId().equals(card.getId())) {
      firstSelected.setMatched(true);
      card.setMatched(true);
      players.get(currentPlayerIndex).incrementScore();
    } else {
      firstSelected.setFaceUp(false);
      card.setFaceUp(false);
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
    firstSelected = null;
    notifyBoardUpdated();
    if (isGameOver()) {
      notifyGameOver();
    }
  }

  public boolean isGameOver() {
    return cards.stream().allMatch(MemoryCard::isMatched);
  }

  public List<MemoryPlayer> getWinners() {
    int max = players.stream().mapToInt(MemoryPlayer::getScore).max().orElse(0);
    List<MemoryPlayer> winners = new ArrayList<>();
    for (MemoryPlayer p : players) {
      if (p.getScore() == max) {
        winners.add(p);
      }
    }
    return winners;
  }

  public List<MemoryCard> getCards() {
    return Collections.unmodifiableList(cards);
  }

  public List<MemoryPlayer> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }
}