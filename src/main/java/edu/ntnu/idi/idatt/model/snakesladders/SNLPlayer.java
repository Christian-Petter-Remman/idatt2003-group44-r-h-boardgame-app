package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.Player;

public class SNLPlayer extends Player {

  private int position;

  public SNLPlayer(String name, String characterIcon, int position) {
    super(name, characterIcon);
    this.position = position;
  }

  public int getPosition() {
    return position;
  }
  public void setPosition(int position) {
    this.position = position;
  }

  @Override
  public boolean hasWon() {
    return position > 100;
  }
}

