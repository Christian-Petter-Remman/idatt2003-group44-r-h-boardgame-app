package edu.ntnu.idi.idatt.model.memorygame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryBoardGameTest {

  private MemoryBoardGame game;
  private MemoryPlayer player1;
  private MemoryPlayer player2;

  @BeforeEach
  void setUp() {
    player1 = new MemoryPlayer("A");
    player2 = new MemoryPlayer("B");
    MemoryGameSettings settings = new MemoryGameSettings(
        MemoryGameSettings.BoardSize.FOUR_BY_FOUR,
        List.of(player1, player2)
    );
    game = new MemoryBoardGame(settings);
  }

  @Test
  void testConstructorInitializesCorrectly() {
    assertEquals(16, game.getCards().size());
    assertEquals(2, game.getPlayers().size());
    assertEquals(player1, game.getPlayers().get(0));
    assertEquals(player2, game.getPlayers().get(1));
    assertEquals(0, game.getCurrentPlayerIndex());
  }

  @Test
  void testFlipTwoMatchingCardsGivesPoint() {
    List<MemoryCard> cards = game.getCards();
    int firstIdx = 0, secondIdx = -1;
    for (int i = 1; i < cards.size(); i++) {
      if (cards.getFirst().getId().equals(cards.get(i).getId())) {
        secondIdx = i;
        break;
      }
    }
    game.flipCard(firstIdx);
    game.flipCard(secondIdx);
    assertTrue(cards.get(firstIdx).isMatched());
    assertTrue(cards.get(secondIdx).isMatched());
    assertEquals(1, game.getPlayers().getFirst().getScore());
    assertEquals(0, game.getCurrentPlayerIndex());
  }

  @Test
  void testFlipTwoNonMatchingCardsSwitchesPlayer() throws InterruptedException {
    List<MemoryCard> cards = game.getCards();
    int firstIdx = 0, secondIdx = -1;
    for (int i = 1; i < cards.size(); i++) {
      if (!cards.getFirst().getId().equals(cards.get(i).getId())) {
        secondIdx = i;
        break;
      }
    }
    game.flipCard(firstIdx);
    game.flipCard(secondIdx);
    Thread.sleep(600);
    assertFalse(cards.get(firstIdx).isMatched());
    assertFalse(cards.get(secondIdx).isMatched());
    assertFalse(cards.get(firstIdx).isFaceUp());
    assertFalse(cards.get(secondIdx).isFaceUp());
    assertEquals(1, game.getCurrentPlayerIndex());
    assertEquals(0, game.getPlayers().get(0).getScore());
    assertEquals(0, game.getPlayers().get(1).getScore());
  }

  @Test
  void testFlipAlreadyFaceUpCardDoesNothing() {
    game.flipCard(0);
    assertTrue(game.getCards().getFirst().isFaceUp());
    game.flipCard(0);
    assertTrue(game.getCards().getFirst().isFaceUp());
  }

  @Test
  void testFlipMatchedCardDoesNothing() {
    List<MemoryCard> cards = game.getCards();
    int firstIdx = 0, secondIdx = -1;
    for (int i = 1; i < cards.size(); i++) {
      if (cards.getFirst().getId().equals(cards.get(i).getId())) {
        secondIdx = i;
        break;
      }
    }
    game.flipCard(firstIdx);
    game.flipCard(secondIdx);
    assertTrue(cards.get(firstIdx).isMatched());
    game.flipCard(firstIdx);
    assertTrue(cards.get(firstIdx).isMatched());
  }

  @Test
  void testIsGameOverWhenAllCardsMatched() {
    List<MemoryCard> cards = game.getCards();
    boolean[] matched = new boolean[cards.size()];
    for (int i = 0; i < cards.size(); i++) {
      if (matched[i]) {
        continue;
      }
      for (int j = i + 1; j < cards.size(); j++) {
        if (!matched[j] && cards.get(i).getId().equals(cards.get(j).getId())) {
          game.flipCard(i);
          game.flipCard(j);
          matched[i] = true;
          matched[j] = true;
          break;
        }
      }
    }
    assertTrue(game.isGameOver());
  }

  @Test
  void testGetWinnersReturnsHighestScore() throws InterruptedException {
    List<MemoryCard> cards = game.getCards();
    int[] p1 = {-1, -1}, p2 = {-1, -1};
    outer:
    for (int i = 0; i < cards.size(); i++) {
      for (int j = i + 1; j < cards.size(); j++) {
        if (cards.get(i).getId().equals(cards.get(j).getId())) {
          if (p1[0] == -1) {
            p1[0] = i;
            p1[1] = j;
          } else {
            p2[0] = i;
            p2[1] = j;
            break outer;
          }
        }
      }
    }
    int nonPair = -1;
    for (int k = 0; k < cards.size(); k++) {
      if (k != p1[0] && k != p1[1] && k != p2[0] && k != p2[1]) {
        nonPair = k;
        break;
      }
    }
    game.flipCard(p1[0]);
    game.flipCard(p1[1]);
    game.flipCard(nonPair);
    game.flipCard(p2[0]);
    Thread.sleep(600);
    game.flipCard(p2[0]);
    game.flipCard(p2[1]);
    assertTrue(game.getWinners().contains(player1));
    assertTrue(game.getWinners().contains(player2));
  }

  @Test
  void testFlipCardWithInvalidTooHighIndexThrowsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> game.flipCard(game.getCards().size() + 5));
  }

  @Test
  void testFlipSameCardTwiceInARowDoesNothing() {
    game.flipCard(0);
    assertTrue(game.getCards().getFirst().isFaceUp());
    game.flipCard(0);
    assertTrue(game.getCards().getFirst().isFaceUp());
    assertFalse(game.getCards().getFirst().isMatched());
  }

  @Test
  void testFlipSecondCardIsSameAsFirstDoesNothing() {
    game.flipCard(0);
    game.flipCard(0);
    assertTrue(game.getCards().getFirst().isFaceUp());
    assertFalse(game.getCards().getFirst().isMatched());
  }

  @Test
  void testFlipCardOnMatchedCardDoesNothing() {
    List<MemoryCard> cards = game.getCards();
    int matchA = 0, matchB = -1;
    for (int i = 1; i < cards.size(); i++) {
      if (cards.getFirst().getId().equals(cards.get(i).getId())) {
        matchB = i;
        break;
      }
    }
    game.flipCard(matchA);
    game.flipCard(matchB);
    assertTrue(cards.get(matchA).isMatched());
    int score = game.getPlayers().getFirst().getScore();
    game.flipCard(matchA);
    assertEquals(score, game.getPlayers().getFirst().getScore());
  }

  @Test
  void testGameOverTriggersNotifyGameOver() throws InterruptedException {
    TestObserver observer = new TestObserver();
    game.addObserver(observer);
    List<MemoryCard> cards = game.getCards();
    boolean[] matched = new boolean[cards.size()];
    for (int i = 0; i < cards.size(); i++) {
      if (matched[i]) {
        continue;
      }
      for (int j = i + 1; j < cards.size(); j++) {
        if (!matched[j] && cards.get(i).getId().equals(cards.get(j).getId())) {
          game.flipCard(i);
          game.flipCard(j);
          matched[i] = true;
          matched[j] = true;
          break;
        }
      }
    }
    Thread.sleep(50);
    assertTrue(observer.gameOverCalled);
  }

  @Test
  void testSinglePlayerGame() {
    MemoryPlayer solo = new MemoryPlayer("Solo");
    MemoryGameSettings s = new MemoryGameSettings(MemoryGameSettings.BoardSize.FOUR_BY_FOUR,
        List.of(solo));
    MemoryBoardGame soloGame = new MemoryBoardGame(s);
    assertEquals(1, soloGame.getPlayers().size());
    assertEquals(solo, soloGame.getPlayers().getFirst());
  }

  @Test
  void testCurrentPlayerIndexWrapsAround() throws InterruptedException {
    int playersCount = game.getPlayers().size();
    List<MemoryCard> cards = game.getCards();
    int a = 0, b = -1;
    for (int i = 1; i < cards.size(); i++) {
      if (!cards.getFirst().getId().equals(cards.get(i).getId())) {
        b = i;
        break;
      }
    }
    int startIndex = game.getCurrentPlayerIndex();
    for (int i = 0; i < playersCount * 2; i++) {
      game.flipCard(a);
      game.flipCard(b);
      Thread.sleep(600);
    }
    assertEquals(startIndex, game.getCurrentPlayerIndex());
  }

  @Test
  void testAddRemoveObserverDoesNotThrow() {
    TestObserver observer = new TestObserver();
    assertDoesNotThrow(() -> game.addObserver(observer));
    assertDoesNotThrow(() -> game.removeObserver(observer));
  }

  static class TestObserver implements MemoryGameObserver {

    boolean gameOverCalled = false;

    @Override
    public void onBoardUpdated(MemoryBoardGame game) {
    }

    @Override
    public void onGameOver(List<MemoryPlayer> winners) {
      gameOverCalled = true;
    }
  }

}
