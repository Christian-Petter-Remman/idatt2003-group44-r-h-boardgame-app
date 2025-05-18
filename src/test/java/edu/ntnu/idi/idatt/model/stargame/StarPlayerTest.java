package edu.ntnu.idi.idatt.model.stargame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StarPlayerTest {

  private StarPlayer player;

  @BeforeEach
  void setUp() {
    player = new StarPlayer("olli", "mario", 3, 2);
  }

  @Test
  void testConstructorInitializesCorrectly() {
    assertEquals("olli", player.getName());
    assertEquals("mario", player.getCharacter());
    assertEquals(3, player.getPosition());
    assertEquals(2, player.getPoints());
  }

  @Test
  void testGetAndSetPosition() {
    player.setPosition(10);
    assertEquals(10, player.getPosition());
  }

  @Test
  void testGetAndSetPoints() {
    player.setPoints(4);
    assertEquals(4, player.getPoints());
  }

  @Test
  void testAddPointsIncreasesCorrectly() {
    player.addPoints(3);
    assertEquals(5, player.getPoints());
  }

  @Test
  void testHasWonReturnsTrueAtFivePointsOrMore() {
    player.setPoints(4);
    assertFalse(player.hasWon());

    player.addPoints(1);
    assertTrue(player.hasWon());

    player.addPoints(2);
    assertTrue(player.hasWon());
  }

  @Test
  void testStartPositionIsZero() {
    assertEquals(0, player.getStartPosition());
  }
}