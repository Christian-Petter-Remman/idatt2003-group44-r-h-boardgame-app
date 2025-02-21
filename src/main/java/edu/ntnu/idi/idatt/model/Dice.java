package edu.ntnu.idi.idatt.model;

public class Dice {
  private int numberOfDice;
  private Die die;

  public Dice(int numberOfDice) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("The number of dice must be at least 1.");
    }
    this.numberOfDice = numberOfDice;
    this.die = new Die(6);
  }

  public int roll() {
    return java.util.stream.IntStream.range(0, numberOfDice)
            .map(i -> die.getNumEyes())
            .sum();
  }
}