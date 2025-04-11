package edu.ntnu.idi.idatt.model.common;

import java.util.Random;

public class Die {
  private final int sides;
  private int faceValue;
  private final Random random;

  public Die(int sides) {
    if (sides < 1) {
      throw new IllegalArgumentException("A die must have at least 1 side");
    }
    this.sides = sides;
    this.faceValue = 1;
    this.random = new Random();
  }

  public int getFaceValue() {
    return faceValue;
  }

  public int roll() {
    faceValue = random.nextInt(sides) + 1;
    return faceValue;
  }

}
