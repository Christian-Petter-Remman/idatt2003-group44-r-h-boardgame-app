package edu.ntnu.idi.idatt.model.common;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <h1>Dice</h1>
 *
 * Represents a collection of dice used in a board game.
 * Provides functionality to roll multiple dice and retrieve their results.
 */
public class Dice {

  private final List<Die> dice;

  /**
   * <h2>Constructor</h2>
   *
   * Initializes the Dice object with a given number of six-sided dice.
   *
   * @param numberOfDice the number of dice to include.
   * @throws IllegalArgumentException if the number of dice is less than 1.
   */
  public Dice(int numberOfDice) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("The number of dice must be at least 1.");
    }

    this.dice = IntStream.range(0, numberOfDice)
            .mapToObj(i -> new Die(6))
            .collect(Collectors.toList());
  }

  /**
   * <h2>getDiceCount</h2>
   *
   * Returns the number of dice.
   *
   * @return the number of dice in this set.
   */
  public int getDiceCount() {
    return dice.size();
  }

  /**
   * <h2>getLastRolls</h2>
   *
   * Returns a list of the most recent face values for each die.
   *
   * @return list of integers representing the last rolled values.
   */
  public List<Integer> getLastRolls() {
    return dice.stream()
            .map(Die::getFaceValue)
            .collect(Collectors.toList());
  }

  /**
   * <h2>roll</h2>
   *
   * Rolls all dice and returns the total sum.
   *
   * @return the sum of all dice rolls.
   */
  public int roll() {
    return dice.stream()
            .mapToInt(Die::roll)
            .sum();
  }
}