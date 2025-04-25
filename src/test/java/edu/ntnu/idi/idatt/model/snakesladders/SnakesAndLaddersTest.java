package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.testutil.FixedDice;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SnakesAndLaddersTest {

  SnakesAndLadders game;

  @BeforeEach
  void setUp() {
    game = new SnakesAndLadders();
    game.addPlayer("A", "toad");
    game.addPlayer("B", "peach");
    game.initialize(new SNLBoard());
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
    game.addPlayer("Tester","bowser");
    game.initialize(new SNLBoard());

    Player p = game.getCurrentPlayer();
    p.setPosition(97);

    game.setDice(new FixedDice(3));

    game.makeMove(p);

    assertTrue(p.hasWon());
    assertEquals(p, game.getWinner());
  }
}