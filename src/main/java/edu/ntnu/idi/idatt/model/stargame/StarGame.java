package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.BoardObserver;
import edu.ntnu.idi.idatt.model.model_observers.GameScreenObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class StarGame extends BoardGame {

  private static final Logger logger = LoggerFactory.getLogger(StarGame.class);


  private boolean pendingPathDecision = false;
  private Path pendingPath;
  private boolean gameOver;

  public StarGame(StarBoard board, List<Player> players, int currentPlayerIndex) {
    super(board);
    this.players = players;
    this.currentPlayerIndex = currentPlayerIndex;
    this.dice = new Dice(1);
    initializePlayer(players);

    logger.info("StarGame created with board size {}", board.getSize());
  }

  public void initialize(StarBoard board) {
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
    StarPlayer player = (StarPlayer) getCurrentPlayer();
    logger.info("It's {}'s turn (pos: {}, points: {}, jailed: {})",
            player.getName(), player.getPosition(), player.getPoints(), player.isJailed());

    if (player.isJailed()) {
      handleJailTurn(player);
      return;
    }

    int roll = dice.roll();
    logger.info("{} rolled {}", player.getName(), roll);

    int newPosition = handleMovementAndSpecialTiles(player, roll);

    if (newPosition == -1) {
      return;
    }

    movePlayerAndHandleTile(player, newPosition, roll);
  }

  private void handleJailTurn(StarPlayer player) {
    int roll = dice.roll();
    logger.info("{} is jailed and rolled {}", player.getName(), roll);

    if (roll == 6) {
      logger.info("{} is freed by rolling a 6!", player.getName());
      player.releaseFromJail();
      player.setPosition(1);
      notifyMoveObservers(player, 0);
      playTurn();
    } else {
      player.decreaseJailTurns();
      if (!player.isJailed()) {
        logger.info("{} finished jail term", player.getName());
        player.setPosition(1);
        notifyMoveObservers(player, 0);
        playTurn();
      } else {
        logger.info("{} remains jailed ({} turns left)", player.getName(), player.getJailTurnsLeft());
        advanceTurn();
      }
    }
  }

  private int handleMovementAndSpecialTiles(StarPlayer player, int roll) {
    int oldPosition = player.getPosition();

    for (int i = oldPosition + 1; i <= oldPosition + roll; i++) {
      if (checkForBridge(player, i, roll)) {
        return player.getPosition();
      }
      if (checkForStar(player, i)) {
      }
      if (checkForPathDuringPass(player, i)) {
        return -1;
      }
    }

    int newPosition = oldPosition + roll;
    if (newPosition > board.getSize()) {
      newPosition = board.getSize();
    }
    return newPosition;
  }

  private boolean checkForStar(StarPlayer player, int tile) {
    Star star = ((StarBoard) board).getStarAt(tile);
    if (star != null) {
      logger.info("{} passed a STAR at tile {} -> earning 1 star point!", player.getName(), tile);
      player.addPoints(1);
      notifyScoreObservers(player, 1);
      logger.info("{} now has {} star points.", player.getName(), player.getPoints());

      int newStarPos = ((StarBoard) board).respawnStar(star);
      notifyStarObservers(player, newStarPos);
      logger.info("observer notified new STAR position is {}", newStarPos);

      return true;
    }
    return false;
  }


  private boolean checkForBridge(StarPlayer player, int tile, int originalRoll) {
    Bridge bridge = ((StarBoard) board).getBridgeAt(tile);
    if (bridge != null) {
      logger.info("{} passed bridge at {} -> jumping to {}", player.getName(), tile, bridge.getEnd());

      int stepsTaken = tile - player.getPosition();
      int remainingRoll = originalRoll - stepsTaken;

      player.setPosition(bridge.getEnd() + remainingRoll);
      return true;
    }
    return false;
  }

  private boolean checkForPathDuringPass(StarPlayer player, int tile) {
    Path path = ((StarBoard) board).getPathAt(tile);
    if (path != null) {
      logger.info("{} passed a path at tile {}", player.getName(), tile);

      pendingPathDecision = true;
      pendingPath = path;
      notifyPathDecisionRequested(player, path);
      return true;
    }
    return false;
  }

  private void movePlayerAndHandleTile(StarPlayer player, int newPosition, int roll) {
    player.setPosition(newPosition);
    logger.info("{} moved to {}", player.getName(), newPosition);
    notifyMoveObservers(player, roll);

    if (handleTunnel(player)) return;
    if (handleJail(player)) return;

    if (player.hasWon()) {
      logger.info("🎉 {} has won!", player.getName());
      gameOver = true;
      notifyWinnerObservers(player);
    } else {
      advanceTurn();
    }
  }

  private boolean handleTunnel(StarPlayer player) {
    Tunnel tunnel = ((StarBoard) board).getTunnelAt(player.getPosition());
    if (tunnel != null) {
      logger.info("{} landed on tunnel at {} -> teleporting to {}", player.getName(), tunnel.getStart(), tunnel.getEnd());
      player.setPosition(tunnel.getEnd());
      notifyMoveObservers(player, 0);
      return true;
    }
    return false;
  }

  private boolean handleJail(StarPlayer player) {
    Jail jail = ((StarBoard) board).getJailAt(player.getPosition());
    if (jail != null) {
      logger.info("{} landed on jail at {} -> jailed for {} turns", player.getName(), jail.getStart(), jail.getJailTurns());
      player.setJailed(jail.getJailTurns());
      player.setPosition(-1);
      notifyMoveObservers(player, 0);
      advanceTurn();
      return true;
    }
    return false;
  }

  public void continuePathDecision(boolean usePath) {
    StarPlayer player = (StarPlayer) getCurrentPlayer();
    int targetTile = usePath ? pendingPath.getEndDynamic() : pendingPath.getEndStatic();

    logger.info("{} {} the path at {} -> moving to {}",
            player.getName(), usePath ? "took" : "ignored", pendingPath.getStart(), targetTile);

    pendingPathDecision = false;
    pendingPath = null;

    movePlayerAndHandleTile(player, targetTile, 0);
  }

  public void advanceTurn() {
    currentPlayerIndex = (currentPlayerIndex + 1) % getStarPlayers().size();
    logger.info("Turn moved to player index {}", currentPlayerIndex);
  }

  public boolean isGameOver() {
    return gameOver;
  }

  public boolean isAwaitingPathDecision() {
    return pendingPathDecision;
  }

  public List<StarPlayer> getStarPlayers() {
    List<StarPlayer> result = new ArrayList<>();
    for (Player p : players) {
      if (p instanceof StarPlayer sp) {
        result.add(sp);
      }
    }
    return result;
  }
}