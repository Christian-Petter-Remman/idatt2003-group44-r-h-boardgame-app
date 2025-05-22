package edu.ntnu.idi.idatt.model.memorygame;

import java.util.List;

/**
 * <h1>MemoryGameObserver</h1>
 *
 * <p>Interface for observing state changes in the Memory game. Implementers are notified of board
 * updates and game-over events.
 */
public interface MemoryGameObserver {

  /**
   * <h2>onBoardUpdated</h2>
   *
   * <p>Called when the game board has changed, such as after a card flip or reset.
   *
   * @param board the updated memory game board
   */
  void onBoardUpdated(MemoryBoardGame board);

  /**
   * <h2>onGameOver</h2>
   *
   * <p>Called when the game has ended and winners have been determined.
   *
   * @param winners a list of players who have the highest score
   */
  void onGameOver(List<MemoryPlayer> winners);
}