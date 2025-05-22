package edu.ntnu.idi.idatt.model.memorygame;

import java.util.List;

/**
 * <h1>MemoryGameSettings</h1>
 *
 * <p>Represents configuration settings for a Memory game, including board size and participating
 * players.
 */
public class MemoryGameSettings {

  /**
   * <h2>BoardSize</h2>
   *
   * <p>Enum representing the available board dimensions for the Memory game.
   */
  public enum BoardSize {
    FOUR_BY_FOUR(4, 4),
    FOUR_BY_FIVE(4, 5),
    SIX_BY_SIX(6, 6);

    private final int rows;
    private final int cols;

    BoardSize(int rows, int cols) {
      this.rows = rows;
      this.cols = cols;
    }

    /**
     * <h3>getFileName</h3>
     *
     * <p>Generates the corresponding file name based on board dimensions.
     *
     * @return the JSON filename for the board configuration.
     */
    public String getFileName() {
      return String.format("memory_%dx%d.json", rows, cols);
    }

    /**
     * <h3>getRows.</h3>
     *
     * @return the number of rows on the board.
     */
    public int getRows() {
      return rows;
    }

    /**
     * <h3>getCols.</h3>
     *
     * @return the number of columns on the board.
     */
    public int getCols() {
      return cols;
    }
  }

  private BoardSize boardSize;
  private List<MemoryPlayer> players;

  /**
   * <h2>Constructor</h2>
   *
   * <p>Constructs a game settings instance with a board size and player list.
   *
   * @param boardSize the board size (e.g. 4x4, 6x6)
   * @param players   the list of participating players
   */
  public MemoryGameSettings(BoardSize boardSize, List<MemoryPlayer> players) {
    this.boardSize = boardSize;
    this.players = players;
  }

  /**
   * <h2>getBoardSize.</h2>
   *
   * @return the selected board size
   */
  public BoardSize getBoardSize() {
    return boardSize;
  }

  /**
   * <h2>setBoardSize</h2>
   *
   * <p>Sets the board size for the game.
   *
   * @param boardSize the new board size
   */
  public void setBoardSize(BoardSize boardSize) {
    this.boardSize = boardSize;
  }

  /**
   * <h2>getPlayers.</h2>
   *
   * @return the list of players participating in the game
   */
  public List<MemoryPlayer> getPlayers() {
    return players;
  }

  /**
   * <h2>setPlayers</h2>
   *
   * <p>Sets the list of players for the game.
   *
   * @param players the players to assign to the game
   */
  public void setPlayers(List<MemoryPlayer> players) {
    this.players = players;
  }
}