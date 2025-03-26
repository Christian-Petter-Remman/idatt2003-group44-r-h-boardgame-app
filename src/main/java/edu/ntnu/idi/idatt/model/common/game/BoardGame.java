package edu.ntnu.idi.idatt.model.common.game;

import edu.ntnu.idi.idatt.model.common.player.Player;
import java.util.ArrayList;
import java.util.List;

public abstract class BoardGame {
  protected List<Player> players;
  protected boolean gameOver;

  public BoardGame() {
    players = new ArrayList<>();
    gameOver = false;
  }

  public abstract void initialize();
  public abstract void makeMove(Player player);
  public abstract boolean isValidMove(Player player, int move);
  public abstract boolean checkWinCondition();

  public void addPlayer(Player player) {
    players.add(player);
  }

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public boolean isGameOver() {
    return gameOver;
  }
}

