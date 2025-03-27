package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Tile;
import edu.ntnu.idi.idatt.model.common.player.Player;
import edu.ntnu.idi.idatt.testutil.FixedDice;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SnakesAndLaddersTest {

  SnakesAndLadders game;

  @BeforeEach
  void setUp() {
    game = new SnakesAndLadders();
    game.addPlayer("A");
    game.addPlayer("B");
    game.initialize(); // init board & dice
  }

  @Test
  void testAddPlayers() {
    assertEquals(2, game.getPlayers().size());
    assertEquals("A", game.getPlayers().get(0).getName());
  }

  @Test
  void testGetCurrentPlayer() {
    Player current = game.getCurrentPlayer();
    assertEquals("A", current.getName());
  }

  @Test
  void testMakeMoveChangesPlayerTurn() {
    Player first = game.getCurrentPlayer();
    game.makeMove(first);
    Player second = game.getCurrentPlayer();
    assertNotEquals(first, second, "Player turn should change after move");
  }

  @Test
  void testPlayerWinsWithExactRoll() {
    game = new SnakesAndLadders();
    game.addPlayer("Tester");
    game.initialize();

    Player p = game.getCurrentPlayer();
    p.setPosition(97);

    game.setDice(new FixedDice(3));

    game.makeMove(p);

    assertTrue(p.hasWon());
    assertEquals(p, game.getWinner());
  }
}