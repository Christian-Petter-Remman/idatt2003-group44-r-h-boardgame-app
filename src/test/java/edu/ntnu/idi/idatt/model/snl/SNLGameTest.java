package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.BoardObserver;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SNLGameTest {

  private SNLBoard board;
  private SNLPlayer player1;
  private SNLPlayer player2;
  private SNLGame game;

  @BeforeEach
  void setUp() {
    board = new SNLBoard(100);
    player1 = new SNLPlayer("Olli", "bowser", 1);
    player2 = new SNLPlayer("Chris", "peach", 2);
    game = new SNLGame(board, List.of(player1, player2), 1, 0);
  }

  @Test
  void testConstructorInitializesCorrectly() {
    assertEquals(player1, game.getCurrentPlayer());
    assertFalse(game.isGameOver());
  }

  @Test
  void testPlayerWinsWhenReachingEnd() {
    player1.setPosition(99);
    game.playTurn();
    assertTrue(player1.hasWon());
    assertTrue(game.isGameOver());
  }

  @Test
  void testLadderWorking() {
    board.addLadder(1, 20);
    board.addLadder(2, 20);
    board.addLadder(3, 20);
    board.addLadder(4, 20);
    board.addLadder(5, 20);
    board.addLadder(6, 20);
    player1.setPosition(0);

    while (player1.getPosition() < 5) {
      game.playTurn();
    }

    assertTrue(player1.getPosition() >= 20);
  }

  @Test
  void testSnakeMovesPlayerDown() {
    board.addSnake(6, 2);
    board.addSnake(7, 2);
    board.addSnake(8, 2);
    board.addSnake(9, 2);
    board.addSnake(10, 2);
    board.addSnake(11, 2);
    player1.setPosition(5);

    game.playTurn();

    assertTrue(player1.getPosition() <= 2);
  }

  @Test
  void testMoveObserversAreNotified() {
    TestGameScreenObserver observer = new TestGameScreenObserver();
    game.addMoveObserver(observer);

    player1.setPosition(3);
    game.notifyMoveObservers(player1, 4);

    assertEquals(4, observer.diceRolled);
    assertEquals(-1, observer.from);
    assertEquals(3, observer.to);
    assertEquals(player1, observer.turnChangedPlayer);
  }

  @Test
  void testWinnerObserverIsNotified() {
    TestGameScreenObserver observer = new TestGameScreenObserver();
    game.addWinnerObserver(observer);

    player1.setPosition(100);
    game.notifyWinnerObservers(player1);

    assertEquals(player1, observer.gameOverWinner);
  }

  static class TestGameScreenObserver implements GameScreenObserver {

    int diceRolled = -1;
    int from = -1;
    int to = -1;
    Player turnChangedPlayer = null;
    Player gameOverWinner = null;

    @Override
    public void onDiceRolled(int roll) {
      this.diceRolled = roll;
    }

    @Override
    public void onPlayerPositionChanged(Player player, int from, int to) {
      this.from = from;
      this.to = to;
    }

    @Override
    public void onPlayerTurnChanged(Player player) {
      this.turnChangedPlayer = player;
    }

    @Override
    public void onGameOver(Player winner) {
      this.gameOverWinner = winner;
    }

    @Override
    public void onGameSaved(String filePath) {

    }
  }

  static class TestBoardObserver implements BoardObserver {

    Player movedPlayer;
    int movedFrom = -1;
    int movedTo = -1;
    int activatedFrom = -1;
    int activatedTo = -1;
    boolean wasLadder = false;

    @Override
    public void onBoardRendered() {

    }

    @Override
    public void onPlayerMoved(Player player, int from, int to) {
      this.movedPlayer = player;
      this.movedFrom = from;
      this.movedTo = to;
    }

    @Override
    public void onSpecialTileActivated(int from, int to, boolean isLadder) {
      this.activatedFrom = from;
      this.activatedTo = to;
      this.wasLadder = isLadder;
    }
  }
}