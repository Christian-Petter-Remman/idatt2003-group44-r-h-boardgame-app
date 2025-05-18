package edu.ntnu.idi.idatt.model.common.memory;

import edu.ntnu.idi.idatt.model.common.factory.MemoryCardFactory;
import edu.ntnu.idi.idatt.model.memorygame.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class MemoryBoardGameTest {

  private MemoryBoardGame game;
  private MemoryPlayer player1;
  private MemoryPlayer player2;

  @BeforeEach
  void setUp() {
    player1 = new MemoryPlayer("Alice");
    player2 = new MemoryPlayer("Bob");

    MemoryGameSettings settings = new MemoryGameSettings(MemoryGameSettings.BoardSize.FOUR_BY_FOUR, List.of(player1, player2));
    game = new MemoryBoardGame(settings);
  }

  @Test
  void testGameInitializesCorrectly() {
    assertEquals(2, game.getPlayers().size());
    assertEquals(16, game.getCards().size());
    assertEquals(0, game.getCurrentPlayerIndex());
  }

  @Test
  void testFlippingTwoMatchingCardsScoresAndStaysFaceUp() {
    List<MemoryCard> cards = game.getCards();


    MemoryCard card1 = cards.getFirst();
    MemoryCard card2 = cards.stream()
            .filter(c -> !c.equals(card1) && c.getId().equals(card1.getId()))
            .findFirst()
            .orElseThrow();

    int index1 = cards.indexOf(card1);
    int index2 = cards.indexOf(card2);

    game.flipCard(index1);
    game.flipCard(index2);

    assertTrue(card1.isFaceUp());
    assertTrue(card2.isFaceUp());
    assertTrue(card1.isMatched());
    assertEquals(1, game.getPlayers().getFirst().getScore());
  }

  @Test
  void testFlippingTwoNonMatchingCardsChangesTurn() throws InterruptedException {
    List<MemoryCard> cards = game.getCards();
    MemoryCard card1 = cards.get(0);
    MemoryCard card2 = cards.stream()
            .filter(c -> !c.getId().equals(card1.getId()))
            .findFirst()
            .orElseThrow();

    int index1 = cards.indexOf(card1);
    int index2 = cards.indexOf(card2);

    game.flipCard(index1);
    game.flipCard(index2);

    // Wait for delayed flip-back to finish
    Thread.sleep(600);

    assertFalse(card1.isFaceUp());
    assertFalse(card2.isFaceUp());
    assertEquals(1, game.getCurrentPlayerIndex()); // player switched
  }

  @Test
  void testGameOverAndWinner() {
    List<MemoryCard> cards = game.getCards();
    for (int i = 0; i < cards.size(); i += 2) {
      MemoryCard c1 = cards.get(i);
      MemoryCard c2 = cards.get(i + 1);
      c1.setMatched(0);
      c2.setMatched(0);
    }

    assertTrue(game.isGameOver());
    List<MemoryPlayer> winnersEqOne = game.getWinners();
    assertEquals(1, winnersEqOne.size());
    assertEquals(player1, winnersEqOne.get(0));
  }

  @Test
  void testObserversAreNotified() {
    AtomicBoolean updated = new AtomicBoolean(false);
    AtomicBoolean over = new AtomicBoolean(false);

    game.addObserver(new MemoryGameObserver() {
      @Override
      public void onBoardUpdated(MemoryBoardGame g) {
        updated.set(true);
      }

      @Override
      public void onGameOver(List<MemoryPlayer> winners) {
        over.set(true);
      }
    });

    game.flipCard(0);
    assertTrue(updated.get());

    // Match all cards to trigger game over
    for (int i = 0; i < game.getCards().size(); i += 2) {
      game.getCards().get(i).setMatched(0);
      game.getCards().get(i + 1).setMatched(0);
    }
    game.flipCard(1); // trigger game over notification
    assertTrue(over.get());
  }

  @Test
  void testResetRestoresState() {
    // Force a score and matched state
    MemoryCard c1 = game.getCards().get(0);
    MemoryCard c2 = game.getCards().stream()
            .filter(c -> !c.equals(c1) && c.getId().equals(c1.getId()))
            .findFirst().orElseThrow();

    c1.setMatched(0);
    c2.setMatched(0);
    player1.incrementScore();
    game.flipCard(0);
    game.reset();

    assertEquals(0, player1.getScore());
    assertEquals(0, game.getCurrentPlayerIndex());
    assertTrue(game.getCards().stream().noneMatch(MemoryCard::isMatched));
  }
}