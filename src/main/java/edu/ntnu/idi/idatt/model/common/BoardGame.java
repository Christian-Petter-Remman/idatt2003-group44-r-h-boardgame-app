package edu.ntnu.idi.idatt.model.common;

import java.util.List;

public abstract class BoardGame {
  protected List<Player> players;
  protected AbstractBoard board;
  protected Dice dice;
  protected int currentPlayerIndex;

  public BoardGame(AbstractBoard board, List<Player> players) {
    this.board = board;
    this.players = players;
    this.dice = new Dice(1);
  }

  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  public void makeMove(Player player) {
    int roll = dice.roll();
    player.move(roll, board);
  }

  public void nextTurn() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  public boolean isGameOver() {
    return getCurrentPlayer().getPosition() >= board.getSize();
  }

  public List<Player> getPlayers(){
    return players;
  }
  public AbstractBoard getBoard() {
    return board;
  }
}
