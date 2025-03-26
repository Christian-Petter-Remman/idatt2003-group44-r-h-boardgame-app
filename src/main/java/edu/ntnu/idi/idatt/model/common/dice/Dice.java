package edu.ntnu.idi.idatt.model.common.dice;

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

  public int roll() {
    return dice.stream()
        .mapToInt(Die::roll)
        .sum();
  }

}
