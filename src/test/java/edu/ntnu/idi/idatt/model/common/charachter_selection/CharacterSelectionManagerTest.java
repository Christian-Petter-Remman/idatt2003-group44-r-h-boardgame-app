package edu.ntnu.idi.idatt.model.common.charachter_selection;

import edu.ntnu.idi.idatt.model.common.characterselection.CharacterSelectionData;
import edu.ntnu.idi.idatt.model.common.characterselection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.characterselection.CharacterSelectionObserver;
import edu.ntnu.idi.idatt.model.common.characterselection.PlayerData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterSelectionManagerTest {

  private CharacterSelectionManager manager;

  @BeforeEach
  public void setUp() {
    manager = CharacterSelectionManager.getInstance();
    for (PlayerData player : manager.getPlayers()) {
      player.setActive(false);
      player.setSelectedCharacter(null);
    }
    for (CharacterSelectionData character : manager.getAvailableCharacters()) {
      character.setSelected(false);
      character.setSelectedBy(null);
    }
  }

  @Test
  public void testActivatePlayer_setsPlayerActiveAndNotifies() {
    TestObserver observer = new TestObserver();
    manager.addObserver(observer);

    manager.activatePlayer(1);
    assertTrue(manager.getPlayerById(1).isActive());
    assertTrue(observer.wasUpdated);

    manager.removeObserver(observer);
  }

  @Test
  public void testDeactivatePlayer_deactivatesOnlyPlayersAbove2() {
    manager.activatePlayer(3);
    CharacterSelectionData character = manager.getAvailableCharacters().getFirst();
    manager.selectCharacter(manager.getPlayerById(3), character);

    manager.deactivatePlayer(3);
    PlayerData player3 = manager.getPlayerById(3);

    assertFalse(player3.isActive());
    assertNull(player3.getSelectedCharacter());
    assertFalse(character.isSelected());
    assertNull(character.getSelectedBy());
  }

  @Test
  public void testSelectCharacter_assignsCorrectCharacterToPlayer() {
    PlayerData player = manager.getPlayerById(1);
    CharacterSelectionData character = manager.getAvailableCharacters().getFirst();

    manager.selectCharacter(player, character);

    assertEquals(character, player.getSelectedCharacter());
    assertTrue(character.isSelected());
    assertEquals(player, character.getSelectedBy());
    assertEquals(character.getName(), player.getCharacterIcon());
  }

  @Test
  public void testIsCharacterTaken_returnsTrueIfAssigned() {
    CharacterSelectionData character = manager.getAvailableCharacters().getFirst();
    manager.selectCharacter(manager.getPlayerById(1), character);

    assertTrue(manager.isCharacterTaken(character));
  }

  private static class TestObserver implements CharacterSelectionObserver {

    boolean wasUpdated = false;

    @Override
    public void update() {
      wasUpdated = true;
    }
  }
}