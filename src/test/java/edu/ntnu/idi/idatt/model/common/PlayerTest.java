package edu.ntnu.idi.idatt.model.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

  static class DummyBoard extends AbstractBoard {

    public DummyBoard(int size) {
      super(size);
    }

    @Override
    public Tile getTile(int i) {
      return new DummyTile(i);
    }

    @Override
    public int getSize() {
      return 10;
    }
  }

  static class DummyTile extends Tile {

    public DummyTile(int n) {
      super(n);
    }

    @Override
    public void onPlayerLanded(Player player, AbstractBoard board) {
      player.addPoints(1);
    }
  }

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

  @Test
  void testConstructorAndGetters() {
    DummyPlayer p = new DummyPlayer("Ola", "bilde.png");
    assertEquals("Ola", p.getName());
    assertEquals("bilde.png", p.getCharacterIcon());
    assertEquals(1, p.getPosition());
    assertEquals(0, p.getPoints());
    assertFalse(p.isJailed());
  }

  @Test
  void testSetAndGetPosition() {
    DummyPlayer p = new DummyPlayer("A", "icon.png");
    p.setPosition(5);
    assertEquals(5, p.getPosition());
  }

  @Test
  void testSetAndGetCharacterIcon() {
    DummyPlayer p = new DummyPlayer("A", "oldIcon");
    p.setCharacter("newIcon");
    assertEquals("newIcon", p.getCharacterIcon());
  }

  @Test
  void testAddPoints() {
    DummyPlayer p = new DummyPlayer("A", "icon.png");
    assertEquals(0, p.getPoints());
    p.addPoints(3);
    assertEquals(3, p.getPoints());
  }

  @Test
  void testMoveOnBoard() {
    DummyPlayer p = new DummyPlayer("B", "icon.png");
    DummyBoard board = new DummyBoard(10);
    p.setPosition(2);
    p.move(4, board);
    assertEquals(6, p.getPosition());
    p.move(10, board);
    assertEquals(10, p.getPosition());
  }

  @Test
  void testJailAndReleaseLogic() {
    DummyPlayer p = new DummyPlayer("X", "xicon");
    assertFalse(p.isJailed());
    p.setJailed(2);
    assertTrue(p.isJailed());
    assertEquals(2, p.getJailTurnsLeft());
    p.decreaseJailTurns();
    assertTrue(p.isJailed());
    assertEquals(1, p.getJailTurnsLeft());
    p.decreaseJailTurns();
    assertFalse(p.isJailed());
    assertEquals(0, p.getJailTurnsLeft());
    p.setJailed(3);
    assertTrue(p.isJailed());
    p.releaseFromJail();
    assertFalse(p.isJailed());
    assertEquals(0, p.getJailTurnsLeft());
  }
}
