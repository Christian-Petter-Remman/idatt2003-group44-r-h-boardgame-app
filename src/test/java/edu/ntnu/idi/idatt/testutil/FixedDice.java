package edu.ntnu.idi.idatt.testutil;

import edu.ntnu.idi.idatt.model.common.Dice;

public class FixedDice extends Dice {
  private final int fixedValue;

  public FixedDice(int fixedValue) {
    super(1); // én terning
    this.fixedValue = fixedValue;
  }

  @Override
  public int roll() {
    return fixedValue;
  }
}