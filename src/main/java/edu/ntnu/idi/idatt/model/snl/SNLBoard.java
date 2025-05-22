package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.factory.SNLFactory;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <h1>SNLBoard</h1>
 *
 * <p>Represents the board for the Snakes and Ladders game. Manages the addition and retrieval of
 * ladders and snakes, and provides utility methods for loading a board from a file.
 */
public class SNLBoard extends AbstractBoard {

  private static final Logger logger = LoggerFactory.getLogger(SNLBoard.class);
  private final List<Ladder> ladders = new ArrayList<>();
  private final List<Snake> snakes = new ArrayList<>();

  /**
   * <h2>SNLBoard</h2>
   *
   * <p>Constructs a new board of the given size.
   *
   * @param size the number of tiles on the board
   */
  public SNLBoard(int size) {
    super(size);
  }

  /**
   * <h2>addLadder</h2>
   *
   * <p>Adds a ladder from the specified start to end tile.
   *
   * @param start the starting tile of the ladder
   * @param end   the ending tile of the ladder
   */
  public void addLadder(int start, int end) {
    Ladder ladder = new Ladder(start, end);
    ladders.add(ladder);
    tiles.get(start).addAttribute(ladder);
    logger.info("Added Ladder from {} to {}", start, end);
  }

  /**
   * <h2>addSnake</h2>
   *
   * <p>Adds a snake from the specified start to end tile.
   *
   * @param start the starting tile of the snake
   * @param end   the ending tile of the snake
   */
  public void addSnake(int start, int end) {
    Snake snake = new Snake(start, end);
    snakes.add(snake);
    tiles.get(start).addAttribute(snake);
    logger.info("Added Snake from {} to {}", start, end);
  }

  /**
   * <h2>getLadderEnd</h2>
   *
   * <p>Gets the end tile of a ladder starting at the specified tile, if one exists.
   *
   * @param tileNumber the tile to check
   * @return the end tile number of the ladder, or null if none found
   */
  public Integer getLadderEnd(int tileNumber) {
    return ladders.stream()
        .filter(l -> l.start() == tileNumber)
        .map(Ladder::end)
        .findFirst()
        .orElse(null);
  }

  /**
   * <h2>getSnakeEnd</h2>
   *
   * <p>Gets the end tile of a snake starting at the specified tile, if one exists.
   *
   * @param tileNumber the tile to check
   * @return the end tile number of the snake, or null if none found
   */
  public Integer getSnakeEnd(int tileNumber) {
    return snakes.stream()
        .filter(s -> s.start() == tileNumber)
        .map(Snake::end)
        .findFirst()
        .orElse(null);
  }

  /**
   * <h2>initializeBoardFromFile</h2>
   *
   * <p>Loads a board configuration from a JSON file using {@link SNLFactory}.
   *
   * @param initFilename the name of the file to load
   * @return the loaded SNLBoard instance
   */
  public static SNLBoard initializeBoardFromFile(String initFilename) {
    SNLFactory snlFactory = new SNLFactory();
    SNLBoard board = snlFactory.loadBoardFromFile(initFilename);
    logger.info("Board initialized from {}", initFilename);
    return board;
  }

  /**
   * <h2>getLadders.</h2>
   *
   * @return a copy of the list of all ladders on the board
   */
  public List<Ladder> getLadders() {
    return new ArrayList<>(ladders);
  }

  /**
   * <h2>getSnakes.</h2>
   *
   * @return a copy of the list of all snakes on the board
   */
  public List<Snake> getSnakes() {
    return new ArrayList<>(snakes);
  }

  /**
   * <h2>getSize.</h2>
   *
   * @return the total number of tiles on the board
   */
  @Override
  public int getSize() {
    return tiles.size();
  }
}