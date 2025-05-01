package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.BoardObserver;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SNLGame extends BoardGame {

  private static final Logger logger = LoggerFactory.getLogger(SNLGame.class.getName());

  private boolean gameOver = false;

  private final List<GameScreenObserver> turnObservers = new ArrayList<>();
  private final List<GameScreenObserver> moveObservers = new ArrayList<>();
  private final List<GameScreenObserver> winnerObservers = new ArrayList<>();
  private final List<BoardObserver> boardObservers = new ArrayList<>();

  public SNLGame(SNLBoard board, List<Player> players, int diceCount, int currentTurnIndex) {
    super(board);
    this.players = players;
    this.board = board;
    this.dice = new Dice(diceCount);
    this.currentPlayerIndex = currentTurnIndex;
    initializePlayer(players);
    logger.info("Snakes and Ladders created with board size {} and turn index {}", board.getSize(), currentTurnIndex);
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
    for (BoardObserver observer : boardObservers) {
      observer.onPlayerMoved(player, oldPosition, newPosition);
    }

    Integer ladderEnd = ((SNLBoard) board).getLadderEnd(newPosition);
    Integer snakeEnd = ((SNLBoard) board).getSnakeEnd(newPosition);

    if (ladderEnd != null) {
      logger.info("{} landed on a ladder at {}! Climbing to {}...", player.getName(), newPosition, ladderEnd);
      delay(500);
      player.setPosition(ladderEnd);
      logger.info("{} climbed to {}", player.getName(), ladderEnd);
      for (BoardObserver observer : boardObservers) {
        observer.onSpecialTileActivated(newPosition, ladderEnd, true);
        observer.onPlayerMoved(player, newPosition, ladderEnd);
      }
      notifyMoveObservers(player, 0);
    } else if (snakeEnd != null) {
      logger.info("{} landed on a snake at {}! Sliding to {}...", player.getName(), newPosition, snakeEnd);
      delay(500);
      player.setPosition(snakeEnd);
      logger.info("{} slid down to {}", player.getName(), snakeEnd);
      for (BoardObserver observer : boardObservers) {
        observer.onSpecialTileActivated(newPosition, snakeEnd, false);
        observer.onPlayerMoved(player, newPosition, snakeEnd);
      }
      notifyMoveObservers(player, 0);
    }

    if (player.hasWon()) {
      logger.info(" {} has won the game!", player.getName());
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

  public void addTurnObserver(GameScreenObserver observer) {
    turnObservers.add(observer);
  }

  public void addMoveObserver(GameScreenObserver observer) {
    moveObservers.add(observer);
  }

  public void addWinnerObserver(GameScreenObserver observer) {
    winnerObservers.add(observer);
  }

  public void addMoveObserver(BoardObserver observer) {
    boardObservers.add(observer);
  }
 @Override
  public void notifyMoveObservers(Player player, int roll) {
    for (GameScreenObserver observer : moveObservers) {
      observer.onDiceRolled(roll);
      observer.onPlayerPositionChanged(player, -1, player.getPosition());
    }
  }
  @Override
  public void notifyWinnerObservers(Player winner) {
    for (GameScreenObserver observer : winnerObservers) {
      observer.onGameOver(winner);
    }
  }

  public boolean isGameOver() {
    return gameOver;
  }
}
