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
    player1 = new StarPlayer("olli","bowser",2,3);
    player2 = new StarPlayer("chris","peach",1,1);
    game = new StarGame(board, List.of(player1, player2), 0);
  }

  @Test
  void testConstructorInitializesCorrectly() {
    assertEquals(2, game.getStarPlayers().size());
    assertEquals(player1, game.getStarPlayers().getFirst());
    assertFalse(game.isGameOver());
  }

  @Test
  void testAdvanceTurnCyclesPlayers() {
    assertEquals(player1, game.getStarPlayers().get(game.getStarPlayers().indexOf(player1)));
    game.advanceTurn();
    assertEquals(player2, game.getCurrentPlayer());
    game.advanceTurn();
    assertEquals(player1, game.getCurrentPlayer());
  }


  @Test
  void testIsAwaitingPathDecision() {
    assertFalse(game.isAwaitingPathDecision());
  }

}