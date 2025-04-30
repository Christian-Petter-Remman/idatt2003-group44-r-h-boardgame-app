package edu.ntnu.idi.idatt.model.common.memorygame;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionData;

public class MemoryCard {

  private final int pairId;
  private final CharacterSelectionData icon;
  private CardState state = CardState.FACE_DOWN;


  public MemoryCard(int pairId, CharacterSelectionData icon) {
    this.pairId = pairId;
    this.icon = icon;
  }

  public int getPairId() {
    return pairId;
  }

  public CharacterSelectionData getIcon() {
    return icon;
  }

  public CardState getState() {
    return state;
  }

  public void flipUp() {
    if (state == CardState.FACE_DOWN) {
      state = CardState.FACE_UP;
    }
  }

  public void flipDown() {
    if (state == CardState.FACE_UP) {
      state = CardState.FACE_DOWN;
    }
  }

  public void markMatched() {
    if (state == CardState.FACE_UP) {
      state = CardState.MATCHED;
    }
  }

  public boolean isMatchable() {
    return state == CardState.FACE_DOWN;
  }


}

