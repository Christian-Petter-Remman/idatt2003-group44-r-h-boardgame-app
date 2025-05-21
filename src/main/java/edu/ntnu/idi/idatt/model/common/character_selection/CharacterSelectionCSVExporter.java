package edu.ntnu.idi.idatt.model.common.character_selection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>CharacterSelectionCSVExporter</h1>
 *
 * Observes the character selection process and prepares player-character mappings
 * for export as CSV-compatible string arrays.
 */
public class CharacterSelectionCSVExporter implements CharacterSelectionObserver {

  private final CharacterSelectionManager manager;
  private final List<String[]> playerCharacterData;

  /**
   * <h2>Constructor</h2>
   *
   * Registers this exporter as an observer to the character selection manager.
   *
   * @param manager The manager controlling the character selection.
   */
  public CharacterSelectionCSVExporter(CharacterSelectionManager manager) {
    this.manager = manager;
    this.playerCharacterData = new ArrayList<>();
    manager.addObserver(this);
  }

  /**
   * <h2>getPlayerCharacterData</h2>
   *
   * Retrieves the list of player-character pairs in CSV-compatible format.
   *
   * @return A list of string arrays representing selected characters.
   */
  public List<String[]> getPlayerCharacterData() {
    return playerCharacterData;
  }

  /**
   * <h2>update</h2>
   *
   * Updates the internal list of selected characters when notified of changes.
   */
  @Override
  public void update() {
    playerCharacterData.clear();
    playerCharacterData.addAll(
            manager.getPlayers().stream()
                    .filter(p -> p.isActive() && p.getSelectedCharacter() != null)
                    .map(p -> new String[]{
                            "Player " + p.getId(),
                            p.getSelectedCharacter().getName()
                    })
                    .collect(Collectors.toList())
    );
  }
}