package edu.ntnu.idi.idatt.model.common.characterselection;

/**
 * <h1>CharacterSelectionData</h1>
 *
 * <p>Represents a selectable character in the game, including its name, image path, selection
 * state,
 * and the player who selected it.
 */
public class CharacterSelectionData {

  private String name;
  private final String imagePath;
  private boolean selected;
  private PlayerData selectedBy;

  /**
   * <h2>Constructor</h2>
   * Initializes a new instance of CharacterSelectionData.
   *
   * @param name       the name of the character
   * @param imagePath  the path to the image representing the character
   * @param selected   whether the character is currently selected
   * @param selectedBy the player who selected the character
   */
  public CharacterSelectionData(String name, String imagePath, boolean selected,
      PlayerData selectedBy) {
    this.name = name;
    this.imagePath = imagePath;
    this.selected = selected;
    this.selectedBy = selectedBy;
  }

  /**
   * <h2>getName.</h2>
   *
   * @return the name of the character
   */
  public String getName() {
    return name;
  }

  /**
   * <h2>setName.</h2>
   *
   * @param name the new name of the character
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * <h2>getImagePath.</h2>
   *
   * @return the file path to the character's image
   */
  public String getImagePath() {
    return imagePath;
  }

  /**
   * <h2>isSelected.</h2>
   *
   * @return true if the character is selected, false otherwise
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * <h2>setSelected.</h2>
   *
   * @param selected true to mark the character as selected, false otherwise
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  /**
   * <h2>getSelectedBy.</h2>
   *
   * @return the player who selected this character
   */
  public PlayerData getSelectedBy() {
    return selectedBy;
  }

  /**
   * <h2>setSelectedBy.</h2>
   *
   * @param selectedBy the player who selected this character
   */
  public void setSelectedBy(PlayerData selectedBy) {
    this.selectedBy = selectedBy;
  }
}