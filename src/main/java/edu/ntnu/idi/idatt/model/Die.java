package edu.ntnu.idi.idatt.model;

import java.util.Random;

public class Die {
  private int eyes;

  public Die(int eyes) {
    this.eyes = eyes;
  }

  public int getNumEyes() {
    Random random = new Random();
    eyes = random.nextInt(eyes) + 1;
    return eyes;
  }
}