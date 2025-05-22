package edu.ntnu.idi.idatt.model.memorygame;

import java.util.Objects;

/**
 * <h1>MemoryCard</h1>
 *
 * <p>Represents a single memory card used in the Memory game. Each card has a unique identifier, an
 * associated image, and state information such as whether it is face-up or matched.
 */
public class MemoryCard {

  private final String id;
  private final String imagePath;
  private boolean faceUp;
  private boolean matched;
  private int matchedBy = -1;

  /**
   * <h2>Constructor</h2>
   * Creates a new MemoryCard with the given ID and image path.
   *
   * @param id        the unique identifier for the card
   * @param imagePath the path to the card's image
   */
  public MemoryCard(String id, String imagePath) {
    this.id = id;
    this.imagePath = imagePath;
    this.faceUp = false;
    this.matched = false;
  }

  /**
   * <h2>getId</h2>
   * Returns the unique identifier of the card.
   *
   * @return card ID
   */
  public String getId() {
    return id;
  }

  /**
   * <h2>getImagePath</h2>
   * Returns the image path associated with this card.
   *
   * @return path to image
   */
  public String getImagePath() {
    return imagePath;
  }

  /**
   * <h2>isFaceUp</h2>
   * Indicates whether the card is currently face-up.
   *
   * @return true if face-up, false otherwise
   */
  public boolean isFaceUp() {
    return faceUp;
  }

  /**
   * <h2>setFaceUp</h2>
   * Sets the face-up state of the card.
   *
   * @param faceUp true to set the card face-up, false otherwise
   */
  public void setFaceUp(boolean faceUp) {
    this.faceUp = faceUp;
  }

  /**
   * <h2>isMatched</h2>
   * Indicates whether the card has been matched with its pair.
   *
   * @return true if matched, false otherwise
   */
  public boolean isMatched() {
    return matched;
  }

  /**
   * <h2>setMatched</h2>
   * Marks the card as matched and records which player matched it.
   *
   * @param playerIndex the index of the player who matched the card
   */
  public void setMatched(int playerIndex) {
    this.matched = true;
    this.matchedBy = playerIndex;
  }

  /**
   * <h2>getMatchedBy</h2>
   * Returns the index of the player who matched the card.
   *
   * @return player index, or -1 if not matched
   */
  public int getMatchedBy() {
    return matchedBy;
  }

  /**
   * <h2>equals</h2>
   * Checks equality based on card ID.
   *
   * @param o the object to compare
   * @return true if IDs are equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MemoryCard that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  /**
   * <h2>hashCode</h2>
   * Computes hash code based on the card ID.
   *
   * @return hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}