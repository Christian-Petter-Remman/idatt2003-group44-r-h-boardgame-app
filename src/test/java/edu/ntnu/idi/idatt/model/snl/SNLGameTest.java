package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.Tile;
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
    player1 = new SNLPlayer("Olli","bowser",1);
    player2 = new SNLPlayer("Chris","peach",2);
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
    board.addLadder(2,20);
    board.addLadder(3,20);
    board.addLadder(4,20);
    board.addLadder(5,20);
    board.addLadder(6,20);

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
    board.addSnake(10,2);
    board.addSnake(11,2);

    player1.setPosition(5);

    {
      game.playTurn();
    }

    assertTrue(player1.getPosition() <= 2);
  }

}