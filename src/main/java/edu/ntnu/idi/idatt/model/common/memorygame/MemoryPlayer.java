package edu.ntnu.idi.idatt.model.common.memorygame;

public class MemoryPlayer {

  private final String name;
  private int score;

  public MemoryPlayer(String name) {
    this.name = name;
    this.score = 0;
  }

  public String getName() {
    return name;
  }

  public int getScore() {
    return score;
  }

  public void incrementScore() {
    score++;
  }
}