package edu.ntnu.idi.idatt.model.common;

import edu.ntnu.idi.idatt.model.common.character_selection_screen.CharacterSelectionObserver;
import java.util.Observer;

public interface Observable {
    void addObserver(CharacterSelectionObserver observer);
    void removeObserver(CharacterSelectionObserver observer);
    void notifyObservers();
  }

