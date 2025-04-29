package edu.ntnu.idi.idatt.model.common.character_selection;

public class PlayerData {
  private int id;
  private String name; // <-- ADD THIS
  private CharacterSelectionData selectedCharacter;
  private boolean active;

  public PlayerData(int id) {
    this.id = id;
    this.name = "Player " + id; // default
    this.active = (id <= 2);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public CharacterSelectionData getSelectedCharacter() {
    return selectedCharacter;
  }
  public void setSelectedCharacter(CharacterSelectionData selectedCharacter) {
    this.selectedCharacter = selectedCharacter;
  }
  public boolean isActive() {
    return active;
  }
  public void setActive(boolean active) {
    this.active = active;
  }
}
