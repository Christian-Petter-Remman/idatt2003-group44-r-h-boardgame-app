package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SnakeTest {

  static class DummyPlayer extends Player {

    public DummyPlayer(String name, String character) {
      super(name, character);
    }

    @Override
    public boolean hasWon() {
      return false;
    }

    @Override
    public int getStartPosition() {
      return 1;
    }
  }

  static class DummyBoard extends AbstractBoard {

    public DummyBoard(int size) {
      super(size);
    }

    @Override
    public int getSize() {
      return 100;
    }
  }

  @Test
  void testConstructorAndGetters() {
    Snake snake = new Snake(16, 6);
    assertEquals(16, snake.start());
    assertEquals(6, snake.end());
  }

  @Test
  void testOnLandMovesPlayerToEnd() {
    Snake snake = new Snake(20, 4);
    DummyPlayer player = new DummyPlayer("Nils", "icon");
    DummyBoard board = new DummyBoard(100);
    player.setPosition(20);
    snake.onLand(player, board);
    assertEquals(4, player.getPosition());
  }
}
