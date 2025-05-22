package edu.ntnu.idi.idatt.model.common;

import java.util.Random;

/**
 * <h1>Die</h1>
 *
 * <p>Represents a single die with a configurable number of sides. Provides functionality for
 * rolling
 * the die and retrieving its face value.
 */
public class Die {

  private final int sides;
  private int faceValue;
  private final Random random;

  /**
   * <h2>Constructor</h2>
   *
   * <p>Constructs a die with a specified number of sides.
   *
   * @param sides The number of sides of the die. Must be at least 2.
   * @throws IllegalArgumentException if the number of sides is lower than 2.
   */
  public Die(int sides) {
    if (sides < 2) {
      throw new IllegalArgumentException("A die must have at least 2 sides.");
    }
    this.sides = sides;
    this.faceValue = 1;
    this.random = new Random();
  }

  /**
   * <h2>getFaceValue</h2>
   *
   * <p>Retrieves the current face value of the die.
   *
   * @return The value currently facing up.
   */
  public int getFaceValue() {
    return faceValue;
  }

  /**
   * <h2>roll</h2>
   *
   * <p>Rolls the die to produce a new random face value.
   *
   * @return The result of the die roll.
   */
  public int roll() {
    faceValue = random.nextInt(sides) + 1;
    return faceValue;
  }

  /**
   * <h2>getSides</h2>
   *
   * <p>Retrieves the number of sides on the die.
   *
   * @return The number of sides.
   */
  public int getSides() {
    return sides;
  }
}