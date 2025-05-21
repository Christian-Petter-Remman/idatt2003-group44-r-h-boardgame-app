package edu.ntnu.idi.idatt.model.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {

  @Test
  void testConstructorAndGetSides() {
    Die die = new Die(6);
    assertEquals(6, die.getSides());
  }

  @Test
  void testConstructorThrowsOnInvalidSides() {
    assertThrows(IllegalArgumentException.class, () -> new Die(0));
    assertThrows(IllegalArgumentException.class, () -> new Die(-2));
    assertThrows(IllegalArgumentException.class, () -> new Die(1));
  }

  @Test
  void testRollReturnsInRange() {
    Die die = new Die(8);
    for (int i = 0; i < 100; i++) {
      int val = die.roll();
      assertTrue(val >= 1 && val <= 8, "Value: " + val);
    }
  }

  @Test
  void testFaceValueUpdatesOnRoll() {
    Die die = new Die(6);
    int previous = die.getFaceValue();
    die.roll();
    int newVal = die.getFaceValue();
    assertTrue(newVal >= 1 && newVal <= 6);
  }
}
