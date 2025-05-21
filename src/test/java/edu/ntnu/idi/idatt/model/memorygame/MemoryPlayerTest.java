package edu.ntnu.idi.idatt.model.memorygame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryPlayerTest {

  @Test
  void testConstructorAndGetters() {
    MemoryPlayer player = new MemoryPlayer("Test");
    assertEquals("Test", player.getName());
    assertEquals(0, player.getScore());
  }

  @Test
  void testIncrementScore() {
    MemoryPlayer player = new MemoryPlayer("Player");
    assertEquals(0, player.getScore());
    player.incrementScore();
    assertEquals(1, player.getScore());
    player.incrementScore();
    assertEquals(2, player.getScore());
  }

  @Test
  void testResetScore() {
    MemoryPlayer player = new MemoryPlayer("Player");
    player.incrementScore();
    player.incrementScore();
    assertTrue(player.getScore() > 0);
    player.resetScore();
    assertEquals(0, player.getScore());
  }
}
