package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.modelobservers.BoardObserver;
import edu.ntnu.idi.idatt.model.modelobservers.GameScreenObserver;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <h1>SNLGame</h1>
 *
 * <p>Represents the core logic of a Snakes and Ladders game, including turn management, dice
 * rolling,
 * ladder/snake tile interactions, and observer notifications.
 */
public class SNLGame extends BoardGame {

  private static final Logger logger = LoggerFactory.getLogger(SNLGame.class.getName());

  private boolean gameOver = false;


  private final List<GameScreenObserver> moveObservers = new ArrayList<>();
  private final List<GameScreenObserver> winnerObservers = new ArrayList<>();
  private final List<BoardObserver> boardObservers = new ArrayList<>();

  /**
   * <h2>SNLGame</h2>
   *
   * <p>Initializes a new Snakes and Ladders game instance.
   *
   * @param board            the game board
   * @param players          list of players
   * @param diceCount        number of dice to use
   * @param currentTurnIndex current turn index to start from
   */
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

  /**
   * <h2>playTurn</h2>
   *
   * <p>Executes the current player's turn including dice roll, movement, special tile handling,
   * and
   * turn progression.
   */
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
    logger.info("{} moved to tile {}", player.getName(), newPosition);

    notifyMoveObservers(player, roll);
    boardObservers.forEach(observer -> observer.onPlayerMoved(player, oldPosition, newPosition));

    Integer ladderEnd = ((SNLBoard) board).getLadderEnd(newPosition);
    Integer snakeEnd = ((SNLBoard) board).getSnakeEnd(newPosition);

    if (ladderEnd != null) {
      logger.info("{} landed on a ladder at {}! Climbing to {}...", player.getName(), newPosition,
          ladderEnd);
      delay();
      player.setPosition(ladderEnd);
      logger.info("{} climbed to {}", player.getName(), ladderEnd);
      boardObservers.forEach(observer -> {
        observer.onSpecialTileActivated(newPosition, ladderEnd, true);
        observer.onPlayerMoved(player, newPosition, ladderEnd);
      });
      notifyMoveObservers(player, 0);
    } else if (snakeEnd != null) {
      logger.info("{} landed on a snake at {}! Sliding to {}...", player.getName(), newPosition,
          snakeEnd);
      delay();
      player.setPosition(snakeEnd);
      logger.info("{} slid down to {}", player.getName(), snakeEnd);
      boardObservers.forEach(observer -> {
        observer.onSpecialTileActivated(newPosition, snakeEnd, false);
        observer.onPlayerMoved(player, newPosition, snakeEnd);
      });
      notifyMoveObservers(player, 0);
    }

    if (player.hasWon()) {
      logger.info("{} has won the game!", player.getName());
      gameOver = true;
      notifyWinnerObservers(player);
    } else {
      nextTurn();
    }
  }

  /**
   * <h2>delay</h2>
   *
   * <p>Introduces a pause in execution.
   */
  private void delay() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  /**
   * <h2>addTurnObserver</h2>
   *
   * <p>Adds a turn observer.
   *
   * @param observer observer to register
   */
  @Override
  public void addTurnObserver(GameScreenObserver observer) {
    turnObservers.add(observer);
  }

  /**
   * <h2>addMoveObserver</h2>
   *
   * <p>Adds a move observer.
   *
   * @param observer observer to register
   */
  @Override
  public void addMoveObserver(GameScreenObserver observer) {
    moveObservers.add(observer);
  }

  /**
   * <h2>addWinnerObserver</h2>
   *
   * <p>Adds a winner observer.
   *
   * @param observer observer to register
   */
  @Override
  public void addWinnerObserver(GameScreenObserver observer) {
    winnerObservers.add(observer);
  }

  /**
   * <h2>notifyMoveObservers</h2>
   *
   * <p>Notifies all move observers of a movement event.
   *
   * @param player the player who moved
   * @param roll   the dice roll result
   */
  @Override
  public void notifyMoveObservers(Player player, int roll) {
    moveObservers.forEach(observer -> {
      observer.onDiceRolled(roll);
      observer.onPlayerPositionChanged(player, -1, player.getPosition());
      observer.onPlayerTurnChanged(player);
    });
  }

  /**
   * <h2>notifyWinnerObservers</h2>
   *
   * <p>Notifies all observers that a player has won.
   *
   * @param winner the player who won
   */
  @Override
  public void notifyWinnerObservers(Player winner) {
    winnerObservers.forEach(observer -> observer.onGameOver(winner));
  }

  /**
   * <h2>isGameOver.</h2>
   *
   * @return true if the game has ended
   */
  public boolean isGameOver() {
    return gameOver;
  }
}