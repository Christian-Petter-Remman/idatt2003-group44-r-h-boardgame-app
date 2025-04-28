package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snakesladders.SNLBoard;

public class StarPlayer extends Player {

  public StarPlayer(String name, String character) {
    super(name,character);
  }

 @Override
public void move(int roll, AbstractBoard board) {
  int newPosition = getPosition() + roll;
  setPosition(newPosition);
  }
}

