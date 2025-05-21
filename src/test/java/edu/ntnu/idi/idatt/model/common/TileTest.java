package edu.ntnu.idi.idatt.model.common;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

  static class DummyAttribute implements TileAttribute {

    boolean triggered = false;

    @Override
    public void onLand(Player player, AbstractBoard board) {
      triggered = true;
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

  static class DummyBoard extends AbstractBoard {

    public DummyBoard(int size) {
      super(size);
    }

    @Override
    public int getSize() {
      return 5;
    }
  }

  @Test
  void testConstructorAndGetNumber() {
    Tile tile = new Tile(7);
    assertEquals(7, tile.getNumber());
  }

  @Test
  void testGetAttributesIsInitiallyEmpty() {
    Tile tile = new Tile(1);
    assertTrue(tile.getAttributes().isEmpty());
  }

  @Test
  void testAddAttributeAndGetAttributes() {
    Tile tile = new Tile(2);
    DummyAttribute attr = new DummyAttribute();
    tile.addAttribute(attr);
    List<TileAttribute> attributes = tile.getAttributes();
    assertEquals(1, attributes.size());
    assertTrue(attributes.contains(attr));
  }


  @Test
  void testAddMultipleAttributes() {
    Tile tile = new Tile(4);
    DummyAttribute attr1 = new DummyAttribute();
    DummyAttribute attr2 = new DummyAttribute();
    tile.addAttribute(attr1);
    tile.addAttribute(attr2);
    assertEquals(2, tile.getAttributes().size());
  }

  @Test
  void testOnPlayerLandedCallsOnLandForEachAttribute() {
    Tile tile = new Tile(5);
    DummyAttribute attr1 = new DummyAttribute();
    DummyAttribute attr2 = new DummyAttribute();
    tile.addAttribute(attr1);
    tile.addAttribute(attr2);
    DummyPlayer player = new DummyPlayer("Player", "icon");
    DummyBoard board = new DummyBoard(5);

    tile.onPlayerLanded(player, board);

    assertTrue(attr1.triggered);
    assertTrue(attr2.triggered);
  }
}
