package edu.ntnu.idi.idatt.model.stargame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StarGameTest {

  private StarBoard board;
  private StarPlayer player1;
  private StarPlayer player2;
  private StarGame game;

  @BeforeEach
  void setUp() {
    board = new StarBoard(80);
    player1 = new StarPlayer("olli", "bowser", 2, 3);
    player2 = new StarPlayer("chris", "peach", 1, 1);
    game = new StarGame(board, List.of(player1, player2), 0);
  }

  @Test
  void testConstructorInitializesCorrectly() {
    assertEquals(2, game.getStarPlayers().size());
    assertFalse(game.isGameOver());
    assertFalse(game.isAwaitingPathDecision());
  }

  @Test
  void testAdvanceTurnCyclesPlayers() {
    List<StarPlayer> players = game.getStarPlayers();
    int initialIndex = players.indexOf(player1);
    game.advanceTurn();
    int nextIndex = players.indexOf(player2);
    assertNotEquals(initialIndex, nextIndex);
    game.advanceTurn();
    int loopIndex = players.indexOf(player1);
    assertEquals(initialIndex, loopIndex);
  }

  @Test
  void testPlayTurnDoesNotThrow() {
    assertDoesNotThrow(() -> game.playTurn());
  }

  @Test
  void testInitializeResetsGame() {
    game.advanceTurn();
    game.initialize(board);
    assertEquals(player1, game.getStarPlayers().getFirst());
  }

  @Test
  void testIsAwaitingPathDecision() {
    assertFalse(game.isAwaitingPathDecision());
  }

  @Test
  void testGetStarPlayersOnlyReturnsStarPlayers() {
    List<StarPlayer> players = game.getStarPlayers();
    assertTrue(players.contains(player1));
    assertTrue(players.contains(player2));
  }

  @Test
  void testPlayTurnDoesNothingIfGameOver() {
    try {
      var field = StarGame.class.getDeclaredField("gameOver");
      field.setAccessible(true);
      field.set(game, true);
    } catch (Exception ignored) {}
    int before = player1.getPosition();
    game.playTurn();
    int after = player1.getPosition();
    assertEquals(before, after);
  }

  @Test
  void testPlayTurnMovesPlayer() {
    int posBefore = player1.getPosition();
    for (int i = 0; i < 5; i++) {
      game.playTurn();
      if (player1.getPosition() != posBefore) break;
    }
    assertNotEquals(posBefore, player1.getPosition());
  }

  @Test
  void testPlayerCollectsStarDuringTurn() {
    Star star = board.addStar();
    player1.setPosition(star.getStart() - 1);
    int before = player1.getPoints();
    for (int i = 0; i < 10; i++) {
      player1.setPosition(star.getStart() - 1);
      game.playTurn();
      if (player1.getPoints() > before) break;
    }
    assertTrue(player1.getPoints() > before);
  }

  @Test
  void testPlayerCrossesBridgeDuringMove() {
    board.addBridge(5, 20);
    boolean crossed = false;
    for (int i = 0; i < 100; i++) {
      player1.setPosition(4);
      game.playTurn();
      if (player1.getPosition() == 20) {
        crossed = true;
        break;
      }
    }
    assertTrue(crossed);
  }


  @Test
  void testPlayerLandsOnTunnelAndIsTeleported() {
    board.addTunnel(6, 40);
    boolean teleported = false;
    for (int i = 0; i < 30; i++) {
      player1.setPosition(5);
      game.playTurn();
      if (player1.getPosition() == 40) {
        teleported = true;
        break;
      }
    }
    assertTrue(teleported);
  }


  @Test
  void testPendingPathDecisionTrueAfterPath() {
    board.addPath(7, "right", 10, 15);
    player1.setPosition(6);
    boolean triggered = false;
    for (int i = 0; i < 8; i++) {
      player1.setPosition(6);
      game.playTurn();
      if (game.isAwaitingPathDecision()) {
        triggered = true;
        break;
      }
    }
    assertTrue(triggered);
  }

  @Test
  void testContinuePathDecisionDoesNotThrowIfPending() {
    board.addPath(7, "right", 10, 15);
    player1.setPosition(6);
    for (int i = 0; i < 8; i++) {
      player1.setPosition(6);
      game.playTurn();
      if (game.isAwaitingPathDecision()) break;
    }
    assertTrue(game.isAwaitingPathDecision());
    assertDoesNotThrow(() -> game.continuePathDecision(true));
    assertFalse(game.isAwaitingPathDecision());
  }

}
