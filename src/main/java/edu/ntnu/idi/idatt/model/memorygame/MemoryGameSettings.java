package edu.ntnu.idi.idatt.model.memorygame;

import java.util.List;

public class MemoryGameSettings {

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

    public String getFileName() {
      return String.format("memory_%dx%d.json", rows, cols);
    }

    public int getRows() {
      return rows;
    }

    public int getCols() {
      return cols;
    }
  }

  private BoardSize boardSize;
  private List<MemoryPlayer> players;

  public MemoryGameSettings(BoardSize boardSize, List<MemoryPlayer> players) {
    this.boardSize = boardSize;
    this.players = players;
  }

  public BoardSize getBoardSize() {
    return boardSize;
  }

  public void setBoardSize(BoardSize boardSize) {
    this.boardSize = boardSize;
  }

  public List<MemoryPlayer> getPlayers() {
    return players;
  }

  public void setPlayers(List<MemoryPlayer> players) {
    this.players = players;
  }

}