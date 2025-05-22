package edu.ntnu.idi.idatt.model.common.factory;

import edu.ntnu.idi.idatt.filehandling.handlers.MemoryJsonHandler;
import edu.ntnu.idi.idatt.model.memorygame.MemoryCard;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings.BoardSize;
import java.util.Collections;
import java.util.List;

/**
 * <h1>MemoryCardFactory</h1>
 *
 * <p>Factory class responsible for loading and shuffling memory cards based on the selected board
 * size.
 */
public class MemoryCardFactory {

  private static final MemoryJsonHandler handler = new MemoryJsonHandler();

  /**
   * <h2>loadAndShuffle</h2>
   *
   * <p>Loads memory cards from a JSON file corresponding to the specified board size and shuffles
   * them.
   *
   * @param size The board size configuration that determines which file to load.
   * @return A shuffled list of {@link MemoryCard} objects.
   * @throws RuntimeException if the card file fails to load.
   */
  public static List<MemoryCard> loadAndShuffle(BoardSize size) {
    String fileName = size.getFileName();
    List<MemoryCard> cards;
    try {
      cards = handler.loadFromFile(fileName);
    } catch (Exception e) {
      throw new RuntimeException("Failed to load memory JSON: " + fileName, e);
    }
    Collections.shuffle(cards);
    return cards;
  }
}