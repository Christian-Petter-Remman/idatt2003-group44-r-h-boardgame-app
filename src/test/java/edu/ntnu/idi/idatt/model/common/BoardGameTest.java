package edu.ntnu.idi.idatt.model.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardGameTest {

  static class DummyBoard extends AbstractBoard {

    private final int size;

    public DummyBoard(int size) {
      super(size);
      this.size = size;
    }

    @Override
    public int getSize() {
      return size;
    }
  }

  static class DummyPlayer extends Player {

    private boolean won = false;

    public DummyPlayer(String name, String characterIcon) {
      super(name, characterIcon);
    }

    @Override
    public boolean hasWon() {
      return won;
    }

    public void setWon(boolean won) {
      this.won = won;
    }

    @Override
    public int getStartPosition() {
      return 1;
    }
  }

  static class DummyBoardGame extends BoardGame {

    public DummyBoardGame(AbstractBoard board) {
      super(board);
    }
  }

  private DummyBoardGame game;
  private DummyPlayer player1;
  private DummyPlayer player2;

  @BeforeEach
  void setUp() {
    game = new DummyBoardGame(new DummyBoard(10));
    player1 = new DummyPlayer("P1", "icon1");
    player2 = new DummyPlayer("P2", "icon2");
  }

  @Test
  void testConstructorAndBoard() {
    assertNotNull(game.board);
    assertEquals(0, game.players.size());
    assertNotNull(game.dice);
    assertEquals(0, game.currentPlayerIndex);
  }

  @Test
  void testInitializePlayerAndGetPlayers() {
    game.initializePlayer(List.of(player1, player2));
    List<Player> players = game.getPlayers();
    assertEquals(2, players.size());
    assertEquals(player1, players.get(0));
    assertEquals(player2, players.get(1));
    assertEquals(0, game.currentPlayerIndex);
  }

  @Test
  void testAddPlayer() {
    game.addPlayer(player1);
    game.addPlayer(player2);
    List<Player> players = game.getPlayers();
    assertEquals(2, players.size());
    assertEquals(player2, players.get(1));
  }

  @Test
  void testGetCurrentPlayer() {
    game.initializePlayer(List.of(player1, player2));
    assertEquals(player1, game.getCurrentPlayer());
    game.nextTurn();
    assertEquals(player2, game.getCurrentPlayer());
    game.nextTurn();
    assertEquals(player1, game.getCurrentPlayer());
  }

  @Test
  void testNextTurnWrapsAround() {
    game.initializePlayer(List.of(player1, player2));
    game.nextTurn();
    assertEquals(1, game.currentPlayerIndex);
    game.nextTurn();
    assertEquals(0, game.currentPlayerIndex);
  }

  @Test
  void testGetWinnerReturnsPlayerWhoHasWon() {
    game.initializePlayer(List.of(player1, player2));
    assertNull(game.getWinner());
    player2.setWon(true);
    assertEquals(player2, game.getWinner());
  }
}
