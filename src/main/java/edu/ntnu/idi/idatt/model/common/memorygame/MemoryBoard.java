package edu.ntnu.idi.idatt.model.common.memorygame;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionData;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MemoryBoard {

  private final MemoryCard[][] grid;
  private final int rows;
  private final int cols;

  public MemoryBoard(int rows, int cols, List<CharacterSelectionData> icons) {
    this.rows = rows;
    this.cols = cols;
    if ((rows * cols) % 2 != 0) {
      throw new IllegalCardAmountException("The number of cards must be even.");
    }

    List<MemoryCard> cards = IntStream.range(0, icons.size())
        .boxed()
        .flatMap(pairId -> Stream.of(
            new MemoryCard(pairId, icons.get(pairId)),
            new MemoryCard(pairId, icons.get(pairId))
        ))
        .collect(Collectors.toList());
    Collections.shuffle(cards);

    grid = new MemoryCard[rows][cols];
    Iterator<MemoryCard> iterator = cards.iterator();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (iterator.hasNext()) {
          grid[i][j] = iterator.next();
        }
      }
    }
  }

  public MemoryCard getCard(int row, int col) {
    return grid[row][col];
  }

  public int getRowCount() {
    return rows;
  }

  public int getColCount() {
    return cols;
  }
}