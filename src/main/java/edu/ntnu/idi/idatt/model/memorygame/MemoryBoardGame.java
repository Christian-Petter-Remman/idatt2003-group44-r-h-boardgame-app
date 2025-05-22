package edu.ntnu.idi.idatt.model.memorygame;

import edu.ntnu.idi.idatt.model.common.factory.MemoryCardFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>MemoryBoardGame</h1>
 * Manages the state and logic of a memory card matching game.
 * It tracks player turns, card flips, scorekeeping, and notifies observers of game events.
 */
public class MemoryBoardGame {

  private final MemoryGameSettings settings;
  private final List<MemoryCard> cards;
  private final List<MemoryPlayer> players;
  private final List<MemoryGameObserver> observers = new ArrayList<>();
  private int currentPlayerIndex;
  private MemoryCard firstSelected;

  /**
   * <h2>Constructor</h2>
   * Initializes a new memory game using the provided settings.
   *
   * @param settings the game settings including board size and player list
   */
  public MemoryBoardGame(MemoryGameSettings settings) {
    this.settings = settings;
    this.cards = MemoryCardFactory.loadAndShuffle(settings.getBoardSize());
    this.players = new ArrayList<>(settings.getPlayers());
    this.currentPlayerIndex = 0;
  }

  /**
   * <h2>addObserver</h2>
   * Adds an observer to receive updates on game state changes.
   *
   * @param observer the observer to register
   */
  public void addObserver(MemoryGameObserver observer) {
    observers.add(observer);
  }

  /**
   * <h2>removeObserver</h2>
   * Removes an observer from the list.
   *
   * @param observer the observer to unregister
   */
  public void removeObserver(MemoryGameObserver observer) {
    observers.remove(observer);
  }

  /**
   * <h2>notifyBoardUpdated</h2>
   * Notifies all observers that the board state has changed.
   */
  private void notifyBoardUpdated() {
    observers.forEach(observer -> observer.onBoardUpdated(this));
  }

  /**
   * <h2>notifyGameOver</h2>
   * Notifies all observers that the game is over.
   */
  private void notifyGameOver() {
    observers.forEach(observer -> observer.onGameOver(getWinners()));
  }

  /**
   * <h2>flipCard</h2>
   * Handles logic for flipping a card at a given index.
   *
   * @param index the index of the card to flip
   */
  public void flipCard(int index) {
    if (index < 0 || index >= cards.size()) {
      throw new IndexOutOfBoundsException("Invalid card index: " + index);
    }

    MemoryCard card = cards.get(index);
    if (card.isFaceUp() || card.isMatched()) return;

    card.setFaceUp(true);
    if (firstSelected == null) {
      firstSelected = card;
      notifyBoardUpdated();
      return;
    }

    notifyBoardUpdated();
    MemoryCard second = card;

    if (firstSelected.getId().equals(second.getId())) {
      firstSelected.setMatched(currentPlayerIndex);
      second.setMatched(currentPlayerIndex);
      players.get(currentPlayerIndex).incrementScore();
      firstSelected = null;
      notifyBoardUpdated();
      if (isGameOver()) notifyGameOver();
    } else {
      MemoryCard first = firstSelected;
      int prevPlayer = currentPlayerIndex;
      firstSelected = null;
      new Thread(() -> {
        try {
          Thread.sleep(500);
        } catch (InterruptedException ignored) {}
        first.setFaceUp(false);
        second.setFaceUp(false);
        currentPlayerIndex = (prevPlayer + 1) % players.size();
        notifyBoardUpdated();
      }).start();
    }
  }

  /**
   * <h2>isGameOver</h2>
   * Checks if all cards have been matched.
   *
   * @return true if game is over, false otherwise
   */
  public boolean isGameOver() {
    return cards.stream().allMatch(MemoryCard::isMatched);
  }

  /**
   * <h2>getWinners</h2>
   * Determines the player(s) with the highest score.
   *
   * @return list of players who won
   */
  public List<MemoryPlayer> getWinners() {
    int maxScore = players.stream()
            .mapToInt(MemoryPlayer::getScore)
            .max()
            .orElse(0);

    return players.stream()
            .filter(p -> p.getScore() == maxScore)
            .collect(Collectors.toList());
  }

  /**
   * <h2>getCards</h2>
   * Returns an unmodifiable view of the card list.
   *
   * @return the list of memory cards
   */
  public List<MemoryCard> getCards() {
    return Collections.unmodifiableList(cards);
  }

  /**
   * <h2>getPlayers</h2>
   * Returns an unmodifiable view of the player list.
   *
   * @return the list of players
   */
  public List<MemoryPlayer> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  /**
   * <h2>getCurrentPlayerIndex</h2>
   * Returns the index of the player whose turn it is.
   *
   * @return current player index
   */
  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  /**
   * <h2>reset</h2>
   * Resets the game to initial state, shuffling cards and resetting scores.
   */
  public void reset() {
    cards.clear();
    cards.addAll(MemoryCardFactory.loadAndShuffle(settings.getBoardSize()));
    players.forEach(MemoryPlayer::resetScore);
    currentPlayerIndex = 0;
    firstSelected = null;
    notifyBoardUpdated();
  }
}