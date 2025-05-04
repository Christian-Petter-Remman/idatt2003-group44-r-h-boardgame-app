package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.model_observers.BoardObserver;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SNLGame extends BoardGame {

  private static final Logger logger = LoggerFactory.getLogger(SNLGame.class);

  private boolean gameOver = false;

  private final List<GameScreenObserver> turnObservers = new ArrayList<>();
  private final List<GameScreenObserver> moveObservers = new ArrayList<>();
  private final List<GameScreenObserver> winnerObservers = new ArrayList<>();
  private final List<GameScreenObserver> saveObservers = new ArrayList<>();
  private final List<BoardObserver> boardObservers = new ArrayList<>();
  private int diceCount = 1;

  public SNLGame(SNLBoard board, List<Player> players, int diceCount, int currentTurnIndex) {
    super(board);
    this.players = players;
    this.board = board;
    this.dice = new Dice(diceCount);
    this.currentPlayerIndex = currentTurnIndex;
    initializePlayer(players);
    logger.info("Snakes and Ladders created with board size {} and turn index {}", board.getSize(),
        currentTurnIndex);
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
    int newPosition = Math.min(oldPosition + roll, board.getSize());
    player.setPosition(newPosition);

    notifyMoveObservers(player, roll);
    for (BoardObserver o : boardObservers) {
      o.onPlayerMoved(player, oldPosition, newPosition);
    }

    Integer ladderEnd = ((SNLBoard) board).getLadderEnd(newPosition);
    Integer snakeEnd = ((SNLBoard) board).getSnakeEnd(newPosition);

    if (ladderEnd != null) {
      logger.info("{} hit a ladder at {} → {}", player.getName(), newPosition, ladderEnd);
      delay();
      player.setPosition(ladderEnd);
      for (BoardObserver o : boardObservers) {
        o.onSpecialTileActivated(newPosition, ladderEnd, true);
        o.onPlayerMoved(player, newPosition, ladderEnd);
      }
      notifyMoveObservers(player, 0);
    } else if (snakeEnd != null) {
      logger.info("{} hit a snake at {} → {}", player.getName(), newPosition, snakeEnd);
      delay();
      player.setPosition(snakeEnd);
      for (BoardObserver o : boardObservers) {
        o.onSpecialTileActivated(newPosition, snakeEnd, false);
        o.onPlayerMoved(player, newPosition, snakeEnd);
      }
      notifyMoveObservers(player, 0);
    }

    if (player.hasWon()) {
      logger.info("{} has won!", player.getName());
      gameOver = true;
      notifyWinnerObservers(player);
    } else {
      nextTurn();
    }
  }

  private void delay() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void saveGame(String filePath) {
    notifySaveObservers(filePath);
  }

  @Override
  public void addTurnObserver(GameScreenObserver o) {
    turnObservers.add(o);
  }

  @Override
  public void addMoveObserver(GameScreenObserver o) {
    moveObservers.add(o);
  }

  @Override
  public void addWinnerObserver(GameScreenObserver o) {
    winnerObservers.add(o);
  }

  public void addSaveObserver(GameScreenObserver o) {
    saveObservers.add(o);
  }

  public void addMoveObserver(BoardObserver o) {
    boardObservers.add(o);
  }

  @Override
  public void notifyMoveObservers(Player p, int roll) {
    for (var o : moveObservers) {
      o.onDiceRolled(roll);
      o.onPlayerPositionChanged(p, -1, p.getPosition());
      o.onPlayerTurnChanged(p);
    }
  }

  @Override
  public void notifyWinnerObservers(Player w) {
    for (var o : winnerObservers) {
      o.onGameOver(w);
    }
  }

  private void notifySaveObservers(String path) {
    for (var o : saveObservers) {
      o.onGameSaved(path);
    }
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public void setDiceCount(int diceCount) {
    if (diceCount < 1) {
      diceCount = 1;
    }
    if (diceCount > 2) {
      diceCount = 2;
    }
    this.diceCount = diceCount;
  }

  public int rollDice() {
    int total = 0;
    for (int i = 0; i < diceCount; i++) {
      total += ThreadLocalRandom.current().nextInt(1, 7);
    }
    return total;
  }
}
