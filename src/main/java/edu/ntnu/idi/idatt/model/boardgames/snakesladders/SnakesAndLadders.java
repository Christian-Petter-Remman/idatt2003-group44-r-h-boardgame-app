package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import edu.ntnu.idi.idatt.model.common.game.BoardGame;
import edu.ntnu.idi.idatt.model.common.player.Player;
import edu.ntnu.idi.idatt.model.common.dice.Dice;

public class SnakesAndLadders extends BoardGame {
  private Board board;
  private Dice dice;
  private int currentPlayerIndex;

  public SnakesAndLadders() {
    super();
    currentPlayerIndex = 0;
  }

  @Override
  public void initialize() {
    board = new Board();
    dice = new Dice(1);
  }

  @Override
  public void makeMove(Player player) {
    if (!(player instanceof SnakesAndLaddersPlayer)) {
      throw new IllegalArgumentException("Player must be a SnakesAndLaddersPlayer");
    }

    int roll = dice.roll();
    player.move(roll, board);
    if (player.hasWon()) {
      gameOver = true;
    } else {
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
  }

  @Override
  public boolean isValidMove(Player player, int roll) {
    return player.getPosition() + roll <= 100;
  }

  @Override
  public boolean checkWinCondition() {
    return players.stream().anyMatch(Player::hasWon);
  }

  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  public int rollDice() {
    return dice.roll();
  }

  public Player getWinner() {
    return players.stream()
        .filter(Player::hasWon)
        .findFirst()
        .orElse(null);
  }

  public void addPlayer(String name) {
    addPlayer(new SnakesAndLaddersPlayer(name));
  }
}
