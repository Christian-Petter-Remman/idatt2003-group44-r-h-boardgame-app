package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LadderTest {

  static class DummyPlayer extends Player {
    public DummyPlayer(String name, String character) { super(name, character); }
    @Override public boolean hasWon() { return false; }
    @Override public int getStartPosition() { return 1; }
  }

  static class DummyBoard extends AbstractBoard {
    public DummyBoard(int size) { super(size); }
    @Override public int getSize() { return 100; }
  }

  @Test
  void testConstructorAndGetters() {
    Ladder ladder = new Ladder(3, 17);
    assertEquals(3, ladder.start());
    assertEquals(17, ladder.end());
  }

  @Test
  void testOnLandMovesPlayerToEnd() {
    Ladder ladder = new Ladder(7, 28);
    DummyPlayer player = new DummyPlayer("Oda", "icon");
    DummyBoard board = new DummyBoard(100);
    player.setPosition(7);
    ladder.onLand(player, board);
    assertEquals(28, player.getPosition());
  }
}
