package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.Player;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>StarGame</h1>
 *
 * <p>Represents the main logic for a Star-themed board game. Handles game flow, including turns,
 * dice
 * rolls, movement, star collection, jail rules, and path decisions.
 */
public class StarGame extends BoardGame {

  private static final Logger logger = LoggerFactory.getLogger(StarGame.class);

  private boolean pendingPathDecision = false;
  private Path pendingPath;
  private boolean gameOver;

  /**
   * <h2>Constructor</h2>
   *
   * <p>Initializes the StarGame with board, players, and current player index.
   *
   * @param board              StarBoard instance.
   * @param players            List of participating players.
   * @param currentPlayerIndex Index of the current player.
   */
  public StarGame(StarBoard board, List<Player> players, int currentPlayerIndex) {
    super(board);
    this.players = players;
    this.currentPlayerIndex = currentPlayerIndex;
    this.dice = new Dice(1);
    initializePlayer(players);
    logger.info("StarGameCreator created with board size {}", board.getSize());
  }

  /**
   * <h2>initialize</h2>
   * Sets the board and reinitializes dice and turn index.
   *
   * @param board The game board to use.
   */
  public void initialize(StarBoard board) {
    this.board = board;
    this.dice = new Dice(1);
    this.currentPlayerIndex = 0;
    logger.info("Game initialized with board and dice");
  }

  /**
   * <h2>initializePlayer</h2>
   * Sets up players for the game.
   *
   * @param players List of players.
   */
  public void initializePlayer(List<Player> players) {
    super.initializePlayer(players);
    logger.info("{} players initialized", players.size());
  }

  /**
   * <h2>playTurn</h2>
   * Executes a full turn for the current player.
   */
  public void playTurn() {
    if (gameOver) {
      return;
    }

    StarPlayer player = (StarPlayer) getCurrentPlayer();
    logger.info("It's {}'s turn (pos: {}, points: {}, jailed: {})",
        player.getName(), player.getPosition(), player.getPoints(), player.isJailed());

    if (player.isJailed()) {
      handleJailTurn(player);
    } else {
      int roll = dice.roll();
      logger.info("{} rolled a {}", player.getName(), roll);
      int newPosition = handleMovementAndSpecialTiles(player, roll);
      if (newPosition != -1) {
        movePlayerAndHandleTile(player, newPosition, roll);
      }
    }
  }

  /**
   * <h2>handleJailTurn</h2>
   * Manages player behavior while in jail.
   *
   * @param player The player in jail.
   */
  private void handleJailTurn(StarPlayer player) {
    int roll = dice.roll();
    logger.info("{} is jailed and rolled {}", player.getName(), roll);

    if (roll == 6) {
      logger.info("{} is freed by rolling a 6!", player.getName());
      player.releaseFromJail();
      player.setPosition(1);
    } else {
      player.decreaseJailTurns();
      if (!player.isJailed()) {
        logger.info("{} finished jail term", player.getName());
        player.setPosition(1);
      } else {
        logger.info("{} remains jailed ({} turns left)", player.getName(),
            player.getJailTurnsLeft());
        advanceTurn();
        return;
      }
    }
    notifyMoveObservers(player, 0);
    advanceTurn();
  }

  /**
   * <h2>handleMovementAndSpecialTiles</h2>
   * Applies star collection, bridge jumps, and path checks during movement.
   *
   * @param player The current player.
   * @param roll   Dice roll result.
   * @return New tile position or -1 if paused for path decision.
   */
  private int handleMovementAndSpecialTiles(StarPlayer player, int roll) {
    int oldPosition = player.getPosition();

    for (int i = oldPosition + 1; i <= oldPosition + roll; i++) {
      if (checkForBridge(player, i, roll)) {
        return player.getPosition();
      }
      checkForStar(player, i);
      if (checkForPathDuringPass(player, i)) {
        return -1;
      }
    }

    int newPosition = oldPosition + roll;
    return Math.min(newPosition, board.getSize());
  }

  private void checkForStar(StarPlayer player, int tile) {
    Star star = ((StarBoard) board).getStarAt(tile);
    if (star != null) {
      player.addPoints(1);
      notifyScoreObservers(player, 1);
      int newStarPos = ((StarBoard) board).respawnStar(star);
      notifyStarObservers(player, newStarPos);
      logger.info("{} passed a STAR at {}. Points: {}, New star: {}", player.getName(), tile,
          player.getPoints(), newStarPos);
    }
  }

  private boolean checkForBridge(StarPlayer player, int tile, int originalRoll) {
    Bridge bridge = ((StarBoard) board).getBridgeAt(tile);
    if (bridge != null) {
      int stepsTaken = tile - player.getPosition();
      int remainingRoll = originalRoll - stepsTaken;
      player.setPosition(bridge.end() + remainingRoll);
      logger.info("{} took bridge from {} to {}", player.getName(), tile, bridge.end());
      return true;
    }
    return false;
  }

  private boolean checkForPathDuringPass(StarPlayer player, int tile) {
    Path path = ((StarBoard) board).getPathAt(tile);
    if (path != null) {
      pendingPathDecision = true;
      pendingPath = path;
      notifyPathDecisionRequested(player, path);
      return true;
    }
    return false;
  }

  /**
   * <h2>movePlayerAndHandleTile</h2>
   * Moves the player and handles tunnel or jail tile effects.
   *
   * @param player      The current player.
   * @param newPosition Destination tile.
   * @param roll        Dice roll result.
   */
  private void movePlayerAndHandleTile(StarPlayer player, int newPosition, int roll) {
    player.setPosition(newPosition);
    notifyMoveObservers(player, roll);

    if (handleJail(player)) {
      return;
    }

    Tunnel tunnel = ((StarBoard) board).getTunnelAt(player.getPosition());
    if (tunnel != null) {
      player.setPosition(tunnel.end());
      notifyMoveObservers(player, 0);
      if (handleJail(player)) {
        return;
      }
    }

    if (player.hasWon()) {
      gameOver = true;
      notifyWinnerObservers(player);
    } else {
      advanceTurn();
    }
  }

  private boolean handleJail(StarPlayer player) {
    Jail jail = ((StarBoard) board).getJailAt(player.getPosition());
    if (jail != null) {
      player.setJailed(jail.jailTurns());
      player.setPosition(100);
      notifyMoveObservers(player, 0);
      advanceTurn();
      return true;
    }
    return false;
  }

  /**
   * <h2>continuePathDecision</h2>
   * Resolves player's decision when encountering a path tile.
   *
   * @param usePath Whether the player chooses the dynamic path.
   */
  public void continuePathDecision(boolean usePath) {
    StarPlayer player = (StarPlayer) getCurrentPlayer();
    int targetTile = usePath ? pendingPath.endDynamic() : pendingPath.endStatic();
    pendingPathDecision = false;
    pendingPath = null;
    movePlayerAndHandleTile(player, targetTile, 0);
  }

  /**
   * <h2>advanceTurn</h2>
   * Moves the turn to the next player.
   */
  public void advanceTurn() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    logger.info("Turn moved to player index {}", currentPlayerIndex);
  }

  /**
   * <h2>isGameOver.</h2>
   *
   * @return true if the game has ended.
   */
  public boolean isGameOver() {
    return gameOver;
  }

  /**
   * <h2>isAwaitingPathDecision.</h2>
   *
   * @return true if awaiting user input for a path decision.
   */
  public boolean isAwaitingPathDecision() {
    return pendingPathDecision;
  }

  /**
   * <h2>getStarPlayers.</h2>
   *
   * @return List of all StarPlayer instances.
   */
  public List<StarPlayer> getStarPlayers() {
    return players.stream()
        .filter(p -> p instanceof StarPlayer)
        .map(p -> (StarPlayer) p)
        .collect(Collectors.toList());
  }
}