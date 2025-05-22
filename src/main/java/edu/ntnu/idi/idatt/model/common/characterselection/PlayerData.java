package edu.ntnu.idi.idatt.model.common.characterselection;

/**
 * <h1>PlayerData</h1>
 *
 * <p>Represents a player participating in a game. Stores the player's ID, name, active status,
 * selected character, points, and character icon used for display.
 */
public class PlayerData {

  private int id;
  private String name;
  private CharacterSelectionData selectedCharacter;
  private boolean active;
  private String characterIcon;
  private int points = 0;

  /**
   * <h2>Constructor</h2>
   *
   * <p>Initializes a player with a given ID. Players with ID 1 or 2 are active by default.
   *
   * @param id The ID of the player.
   */
  public PlayerData(int id) {
    this.id = id;
    this.name = "Player " + id;
    this.active = (id <= 2);
  }

  /**
   * <h2>getId.</h2>
   *
   * @return The player's ID.
   */
  public int getId() {
    return id;
  }

  /**
   * <h2>setId.</h2>
   *
   * @param id The player's new ID.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * <h2>getName.</h2>
   *
   * @return The name of the player.
   */
  public String getName() {
    return name;
  }

  /**
   * <h2>setName.</h2>
   *
   * @param name The new name for the player.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * <h2>getSelectedCharacter.</h2>
   *
   * @return The character currently selected by the player.
   */
  public CharacterSelectionData getSelectedCharacter() {
    return selectedCharacter;
  }

  /**
   * <h2>setSelectedCharacter.</h2>
   *
   * @param selectedCharacter The character to assign to the player.
   */
  public void setSelectedCharacter(CharacterSelectionData selectedCharacter) {
    this.selectedCharacter = selectedCharacter;
  }

  /**
   * <h2>isActive.</h2>
   *
   * @return Whether the player is active in the current game.
   */
  public boolean isActive() {
    return active;
  }

  /**
   * <h2>setActive.</h2>
   *
   * @param active Sets the player's active status.
   */
  public void setActive(boolean active) {
    this.active = active;
  }

  /**
   * <h2>getCharacterIcon.</h2>
   *
   * @return The name of the selected character icon.
   */
  public String getCharacterIcon() {
    return characterIcon;
  }

  /**
   * <h2>setCharacterIcon.</h2>
   *
   * @param characterIcon The name of the character icon to assign.
   */
  public void setCharacterIcon(String characterIcon) {
    this.characterIcon = characterIcon;
  }

  /**
   * <h2>getPoints.</h2>
   *
   * @return The number of points this player has accumulated.
   */
  public int getPoints() {
    return points;
  }

  /**
   * <h2>setPoints.</h2>
   *
   * @param points The number of points to assign to the player.
   */
  public void setPoints(int points) {
    this.points = points;
  }
}