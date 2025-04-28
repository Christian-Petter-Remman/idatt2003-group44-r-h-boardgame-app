package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.Player;

public class SNLPlayer extends Player {

  public SNLPlayer(String name, String characterIcon, int position) {
    super(name, characterIcon);
  }

  @Override
  public boolean hasWon() {
    return false;
  }
}

