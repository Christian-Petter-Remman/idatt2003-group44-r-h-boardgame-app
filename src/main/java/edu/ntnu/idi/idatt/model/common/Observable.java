package edu.ntnu.idi.idatt.model.common;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionObserver;

public interface Observable {
    void addObserver(CharacterSelectionObserver observer);
    void removeObserver(CharacterSelectionObserver observer);
    void notifyObservers();
  }

