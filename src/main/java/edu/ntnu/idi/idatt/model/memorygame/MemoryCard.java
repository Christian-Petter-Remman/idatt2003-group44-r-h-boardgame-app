package edu.ntnu.idi.idatt.model.memorygame;

import java.util.Objects;

public class MemoryCard {

  private final String id;
  private final String imagePath;
  private boolean faceUp;
  private boolean matched;
  private int matchedBy = -1;

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

  public void setFaceUp(boolean faceUp) {
    this.faceUp = faceUp;
  }

  public boolean isMatched() {
    return matched;
  }

  public void setMatched(int playerIndex) {
    this.matched = true;
    this.matchedBy = playerIndex;
  }

  public int getMatchedBy() {
    return matchedBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MemoryCard)) {
      return false;
    }
    MemoryCard that = (MemoryCard) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
