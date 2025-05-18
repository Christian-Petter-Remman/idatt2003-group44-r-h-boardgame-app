package edu.ntnu.idi.idatt.model.common.charachter_selection;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionCSVExporter;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionData;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterSelectionCSVExporterTest {

  private CharacterSelectionManager manager;
  private CharacterSelectionCSVExporter exporter;

  @BeforeEach
  public void setUp() {
    manager = CharacterSelectionManager.getInstance();
    for (PlayerData player : manager.getPlayers()) {
      player.setActive(false);
      player.setSelectedCharacter(null);
    }
    exporter = new CharacterSelectionCSVExporter(manager);
  }

  @Test
  public void testUpdateExportsCorrectPlayerCharacterData() {
    CharacterSelectionData character = manager.getAvailableCharacters().getFirst();

    PlayerData player1 = manager.getPlayerById(1);
    manager.activatePlayer(1);
    manager.selectCharacter(player1, character);

    PlayerData player2 = manager.getPlayerById(2);
    player2.setActive(false);

    PlayerData player3 = manager.getPlayerById(3);
    manager.activatePlayer(3);
    player3.setSelectedCharacter(null);

    exporter.update();

    List<String[]> data = exporter.getPlayerCharacterData();
    assertEquals(1, data.size());
    assertArrayEquals(new String[]{"Player 1", character.getName()}, data.get(0));
  }
}