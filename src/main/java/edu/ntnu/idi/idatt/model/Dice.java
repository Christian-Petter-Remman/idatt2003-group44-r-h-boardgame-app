package edu.ntnu.idi.idatt.model;

public class Dice {
  private final int numberOfDice;

  public Dice(int numberOfDice) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("The number of dice must be at least 1.");
    }
    this.numberOfDice = numberOfDice;
  }

  public int rollSingleDie(int sides) {
    Die die = new Die(sides);
    return die.getNumEyes();
  }

  public int roll() {
    return java.util.stream.IntStream.range(0, numberOfDice)
            .map(i -> rollSingleDie(6))
            .sum();
  }
}