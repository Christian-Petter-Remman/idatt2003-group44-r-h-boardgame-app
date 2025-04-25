package edu.ntnu.idi.idatt.model.common;

import edu.ntnu.idi.idatt.model.snakesladders.SNLBoard;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;

import java.util.ArrayList;
import java.util.List;

public abstract class BoardGame {
  protected List<Player> players;
  protected boolean gameOver;

  public BoardGame() {
    players = new ArrayList<>();
    gameOver = false;
  }

  public abstract void makeMove(Player player);

  public abstract boolean isValidMove(Player player, int move);

  public abstract boolean checkWinCondition();

  public abstract void initialize(SNLBoard board);

  public abstract void initialize(StarBoard board);

  public abstract void setBoard(SNLBoard board);


  public void addPlayer(Player player) {
    players.add(player);
  }

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }

}
