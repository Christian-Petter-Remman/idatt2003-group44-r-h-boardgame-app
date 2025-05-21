package edu.ntnu.idi.idatt.model.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class DiceTest {

  @Test
  void testConstructorThrowsOnInvalidNumber() {
    assertThrows(IllegalArgumentException.class, () -> new Dice(0));
    assertThrows(IllegalArgumentException.class, () -> new Dice(-5));
  }

  @Test
  void testConstructorAndGetDiceCount() {
    Dice dice = new Dice(3);
    assertEquals(3, dice.getDiceCount());
  }

  @Test
  void testRollReturnsValidSumForOneDie() {
    Dice dice = new Dice(1);
    for (int i = 0; i < 100; i++) {
      int val = dice.roll();
      assertTrue(val >= 1 && val <= 6, "Value: " + val);
    }
  }

  @Test
  void testRollReturnsValidSumForMultipleDice() {
    Dice dice = new Dice(3);
    for (int i = 0; i < 100; i++) {
      int val = dice.roll();
      assertTrue(val >= 3 && val <= 18, "Value: " + val);
    }
  }

  @Test
  void testGetLastRollsReflectsLastRoll() {
    Dice dice = new Dice(2);
    dice.roll();
    List<Integer> rolls = dice.getLastRolls();
    assertEquals(2, rolls.size());
    for (int val : rolls) {
      assertTrue(val >= 1 && val <= 6, "Die face value: " + val);
    }

    dice.roll();
    List<Integer> rolls2 = dice.getLastRolls();
    assertEquals(2, rolls2.size());
    assertNotEquals(rolls, rolls2);
  }
}
