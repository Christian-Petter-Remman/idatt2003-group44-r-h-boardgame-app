package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.Player;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SNLGame extends BoardGame {

  private static final Logger logger = LoggerFactory.getLogger(SNLGame.class.getName());

  private boolean gameOver = false;

  public SNLGame(SNLBoard board) {
    super(board);
    logger.info("Snakes and Ladders created with board size {}", board.getSize());
  }

  public void initialize(SNLBoard board) {
    this.board = board;
    this.dice = new Dice(1);
    this.currentPlayerIndex = 0;
    logger.info("Game initialized with board and dice");
  }

  public void initializePlayer(List<Player> players) {
    super.initializePlayer(players);
    logger.info("{} players initialized", players.size());
  }

  public void playTurn() {
    if (gameOver) {
      return;
    }

    SNLPlayer player = (SNLPlayer) getCurrentPlayer();
    logger.info("It's {}'s turn (position: {})", player.getName(), player.getPosition());

    int roll = dice.roll();
    logger.info("{} rolled a {}", player.getName(), roll);

    int oldPosition = player.getPosition();
    int newPosition = oldPosition + roll;

    if (newPosition > board.getSize()) {
      newPosition = board.getSize();
    }

    player.setPosition(newPosition);
    logger.info("{} moved to tile {}", player.getName(), newPosition);
    notifyMoveObservers(player, roll);

    Integer ladderEnd = ((SNLBoard) board).getLadderEnd(newPosition);
    Integer snakeEnd = ((SNLBoard) board).getSnakeEnd(newPosition);

    if (ladderEnd != null) {
      logger.info("{} landed on a ladder at {}! Climbing to {}...", player.getName(), newPosition, ladderEnd);
      delay(500); // 0.5 second
      player.setPosition(ladderEnd);
      logger.info("{} climbed to {}", player.getName(), ladderEnd);
      notifyMoveObservers(player, 0);
    } else if (snakeEnd != null) {
      logger.info("{} landed on a snake at {}! Sliding to {}...", player.getName(), newPosition, snakeEnd);
      delay(500); // 0.5 second
      player.setPosition(snakeEnd);
      logger.info("{} slid down to {}", player.getName(), snakeEnd);
      notifyMoveObservers(player, 0);
    }

    // Check if won
    if (player.hasWon()) {
      logger.info("🏆 {} has won the game!", player.getName());
      gameOver = true;
      notifyWinnerObservers(player);
    } else {
      nextTurn();
    }
  }

  private void delay(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public boolean isGameOver() {
    return gameOver;
  }

}