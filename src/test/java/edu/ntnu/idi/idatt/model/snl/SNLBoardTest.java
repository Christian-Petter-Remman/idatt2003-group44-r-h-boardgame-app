package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SNLBoardTest {

  private SNLBoard board;

  @BeforeEach
  void setUp() {
    board = new SNLBoard(100);
  }

  @Test
  void testAddLadderAndGetLadderEnd() {
    board.addLadder(2, 22);
    assertEquals(22, board.getLadderEnd(2));
    assertNull(board.getLadderEnd(3));
  }

  @Test
  void testAddSnakeAndGetSnakeEnd() {
    board.addSnake(17, 7);
    assertEquals(7, board.getSnakeEnd(17));
    assertNull(board.getSnakeEnd(16));
  }

  @Test
  void testGetLaddersReturnsSafeCopy() {
    board.addLadder(5, 25);
    List<Ladder> ladders = board.getLadders();
    assertEquals(1, ladders.size());
    ladders.clear();
    assertEquals(1, board.getLadders().size());
  }

  @Test
  void testGetSnakesReturnsSafeCopy() {
    board.addSnake(30, 10);
    List<Snake> snakes = board.getSnakes();
    assertEquals(1, snakes.size());
    snakes.clear();
    assertEquals(1, board.getSnakes().size());
  }

  @Test
  void testBoardSize() {
    assertEquals(100, board.getSize());
    Tile tile = board.getTile(50);
    assertNotNull(tile);
    assertEquals(50, tile.getNumber());
  }

  @Test
  void testInitializeBoardFromFileCallsFactory() {
    SNLBoard loaded = SNLBoard.initializeBoardFromFile(
        "data/custom_boards/snakes_and_ladders/default.json");
    assertNotNull(loaded);
    assertInstanceOf(SNLBoard.class, loaded);
  }
}