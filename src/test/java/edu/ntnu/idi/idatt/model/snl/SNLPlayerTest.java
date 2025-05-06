package edu.ntnu.idi.idatt.model.snl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SNLPlayerTest {

  private SNLPlayer player;

  @BeforeEach
  void setUp() {
    player = new SNLPlayer("olli", "bowser", 10);
  }

  @Test
  void testConstructorInitializesCorrectly() {
    assertEquals("olli", player.getName());
    assertEquals("bowser", player.getCharacter());
    assertEquals(10, player.getPosition());
  }

  @Test
  void testGetAndSetPosition() {
    player.setPosition(55);
    assertEquals(55, player.getPosition());

    player.setPosition(99);
    assertEquals(99, player.getPosition());
  }

  @Test
  void testHasWonReturnsFalseIfPositionBelow100() {
    player.setPosition(99);
    assertFalse(player.hasWon());
  }

  @Test
  void testHasWonReturnsTrueIfPositionIs100OrMore() {
    player.setPosition(100);
    assertTrue(player.hasWon());

    player.setPosition(101);
    assertTrue(player.hasWon());
  }

  @Test
  void testGetStartPositionReturnsOne() {
    assertEquals(1, player.getStartPosition());
  }
}