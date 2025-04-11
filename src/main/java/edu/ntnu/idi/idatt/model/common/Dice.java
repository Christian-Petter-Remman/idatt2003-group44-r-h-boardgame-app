package edu.ntnu.idi.idatt.model.common;

import java.util.ArrayList;
import java.util.List;

public class Dice {
  private final List<Die> dice;

  public Dice(int numberOfDice) {
    if (numberOfDice < 1) {
      throw new IllegalArgumentException("The number of dice must be at least 1.");
    }

    dice = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      dice.add(new Die(6));
    }
  }

  public List<Integer> getLastRolls() {
    List<Integer> rolls = new ArrayList<>();
    for (Die die : dice) {
      rolls.add(die.getFaceValue());
    }
    return rolls;
  }

  public int roll() {
    return dice.stream()
        .mapToInt(Die::roll)
        .sum();
  }

}
