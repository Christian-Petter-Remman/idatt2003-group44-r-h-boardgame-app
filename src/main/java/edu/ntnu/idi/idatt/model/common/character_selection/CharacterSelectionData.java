package edu.ntnu.idi.idatt.model.common.character_selection;

public class CharacterSelectionData {
  private String name;
  private final String imagePath;
  private boolean selected;
  private PlayerData selectedBy;

  public CharacterSelectionData(String name, String imagePath, boolean selected, PlayerData selectedBy) {
    this.name = name;
    this.imagePath = imagePath;
    this.selected = selected;
    this.selectedBy = selectedBy;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImagePath() {
    return imagePath;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  public PlayerData getSelectedBy() {
    return selectedBy;
  }

  public void setSelectedBy(PlayerData selectedBy) {
    this.selectedBy = selectedBy;
  }
}