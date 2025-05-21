package edu.ntnu.idi.idatt.model.memorygame;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryGameSettingsTest {

  @Test
  void testConstructorAndGetters() {
    MemoryPlayer p1 = new MemoryPlayer("A");
    MemoryPlayer p2 = new MemoryPlayer("B");
    MemoryGameSettings.BoardSize size = MemoryGameSettings.BoardSize.SIX_BY_SIX;
    MemoryGameSettings settings = new MemoryGameSettings(size, List.of(p1, p2));

    assertEquals(size, settings.getBoardSize());
    assertEquals(2, settings.getPlayers().size());
    assertEquals(p1, settings.getPlayers().get(0));
    assertEquals(p2, settings.getPlayers().get(1));
  }

  @Test
  void testSetBoardSize() {
    MemoryGameSettings settings = new MemoryGameSettings(MemoryGameSettings.BoardSize.FOUR_BY_FOUR,
        List.of());
    assertEquals(MemoryGameSettings.BoardSize.FOUR_BY_FOUR, settings.getBoardSize());
    settings.setBoardSize(MemoryGameSettings.BoardSize.FOUR_BY_FIVE);
    assertEquals(MemoryGameSettings.BoardSize.FOUR_BY_FIVE, settings.getBoardSize());
  }

  @Test
  void testSetPlayers() {
    MemoryPlayer p = new MemoryPlayer("A");
    MemoryGameSettings settings = new MemoryGameSettings(MemoryGameSettings.BoardSize.FOUR_BY_FOUR,
        List.of());
    settings.setPlayers(List.of(p));
    assertEquals(1, settings.getPlayers().size());
    assertEquals(p, settings.getPlayers().getFirst());
  }

  @Test
  void testBoardSizeGetters() {
    assertEquals(4, MemoryGameSettings.BoardSize.FOUR_BY_FOUR.getRows());
    assertEquals(4, MemoryGameSettings.BoardSize.FOUR_BY_FOUR.getCols());
    assertEquals(4, MemoryGameSettings.BoardSize.FOUR_BY_FIVE.getRows());
    assertEquals(5, MemoryGameSettings.BoardSize.FOUR_BY_FIVE.getCols());
    assertEquals(6, MemoryGameSettings.BoardSize.SIX_BY_SIX.getRows());
    assertEquals(6, MemoryGameSettings.BoardSize.SIX_BY_SIX.getCols());
  }

  @Test
  void testBoardSizeGetFileName() {
    assertEquals("memory_4x4.json", MemoryGameSettings.BoardSize.FOUR_BY_FOUR.getFileName());
    assertEquals("memory_4x5.json", MemoryGameSettings.BoardSize.FOUR_BY_FIVE.getFileName());
    assertEquals("memory_6x6.json", MemoryGameSettings.BoardSize.SIX_BY_SIX.getFileName());
  }
}
