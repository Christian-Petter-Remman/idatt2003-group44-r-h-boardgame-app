package edu.ntnu.idi.idatt.model.common.memorygame;

import java.util.Objects;

public class MemoryCard {

  private final String id;
  private final String imagePath;
  private boolean faceUp;
  private boolean matched;

  public MemoryCard(String id, String imagePath) {
    this.id = id;
    this.imagePath = imagePath;
    this.faceUp = false;
    this.matched = false;
  }

  public String getId() {
    return id;
  }

  public String getImagePath() {
    return imagePath;
  }

  public boolean isFaceUp() {
    return faceUp;
  }

  public boolean isMatched() {
    return matched;
  }

  public void setFaceUp(boolean faceUp) {
    this.faceUp = faceUp;
  }

  public void setMatched(boolean matched) {
    this.matched = matched;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MemoryCard card = (MemoryCard) o;
    return Objects.equals(id, card.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
