package edu.ntnu.idi.idatt.model.common.game;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardGameTest {

  // Enkel implementasjon for å teste det abstrakte
  static class DummyGame extends BoardGame {
    @Override public void initialize(Board board) {}
    @Override public void makeMove(Player p) {}
    @Override public boolean isValidMove(Player p, int roll) { return true; }
    @Override public boolean checkWinCondition() { return false; }
    @Override public Board getBoard() { return null; }
  }

  /*
  @Test
  void testAddAndRetrievePlayers() {
    DummyGame game = new DummyGame();
    Player p = new Player("Tester") {
      public int getStartPosition() { return 1; }
      public boolean hasWon() { return false; }
      public <T> void move(int steps, T gameContext) {}
    };

    game.addPlayer(p);

    List<Player> players = game.getPlayers();
    assertEquals(1, players.size());
    assertEquals("Tester", players.get(0).getName());
  }
   */

  @Test
  void testGameOverState() {
    DummyGame game = new DummyGame();
    assertFalse(game.isGameOver());

    game.setGameOver(true);
    assertTrue(game.isGameOver());
  }
}