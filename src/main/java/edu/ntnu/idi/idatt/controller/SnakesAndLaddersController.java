
package edu.ntnu.idi.idatt.controller;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersFactory;
import edu.ntnu.idi.idatt.model.common.dice.Dice;
import edu.ntnu.idi.idatt.model.common.player.Player;
import java.util.List;

public class SnakesAndLaddersController {
  private final SnakesAndLaddersFactory factory;

  public SnakesAndLaddersController() {
    this.factory = new SnakesAndLaddersFactory();
  }

  public SnakesAndLadders createGame(String difficulty, int diceCount, int ladderCount, int penaltyCount, List<Player> players) {
    SnakesAndLadders game = (SnakesAndLadders) factory.createBoardGameFromConfiguration(difficulty);
    game.setDice(new Dice(diceCount));

    if (!difficulty.equals("default")) {
      adjustBoard(game.getBoard(), ladderCount, penaltyCount);
    }

    for (Player player : players) {
      game.addPlayer(player);
    }

    game.initialize();
    return game;
  }

  private void adjustBoard(Board board, int ladderCount, int penaltyCount) {
    board.initializeEmptyBoard();
    addRandomLadders(board, ladderCount);
    addRandomPenalties(board, penaltyCount);
  }

  private void addRandomLadders(Board board, int count) {
    for (int i = 0; i < count; i++) {
      board.addRandomLadder();
    }
  }

  private void addRandomPenalties(Board board, int count) {
    for (int i = 0; i < count; i++) {
      board.addRandomSnake();
    }
  }

  public boolean validateInputs(int diceCount, int ladderCount, int penaltyCount) {
    return diceCount >= 1 && ladderCount >= 1 && penaltyCount >= 0;
  }
}
