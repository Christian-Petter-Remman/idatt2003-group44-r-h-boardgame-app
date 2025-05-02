package edu.ntnu.idi.idatt.model.common.factory;

import edu.ntnu.idi.idatt.filehandling.handlers.MemoryJsonHandler;
import edu.ntnu.idi.idatt.model.memorygame.MemoryCard;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings.BoardSize;
import java.util.Collections;
import java.util.List;

public class MemoryCardFactory {

  private static final MemoryJsonHandler handler = new MemoryJsonHandler();

  public static List<MemoryCard> loadAndShuffle(BoardSize size) {
    String fileName = size.getFileName();
    List<MemoryCard> cards;
    try {
      cards = handler.loadFromFile(fileName);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load memory JSON: " + fileName, e); //TODO: Handle this better maybe new exception
    }
    Collections.shuffle(cards);
    return cards;
  }
}
