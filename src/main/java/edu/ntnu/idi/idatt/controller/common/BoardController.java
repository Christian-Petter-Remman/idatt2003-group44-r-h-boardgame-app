package edu.ntnu.idi.idatt.controller.common;

import edu.ntnu.idi.idatt.model.snakesladders.Ladder;
import edu.ntnu.idi.idatt.model.snakesladders.Snake;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.BoardObserver;
import edu.ntnu.idi.idatt.model.snakesladders.SNLBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardController {
  private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

  private final SNLBoard board;
  private final List<Player> players;
  private final List<BoardObserver> observers = new ArrayList<>();
  private final Map<Integer, List<Player>> playerPositions = new HashMap<>();

  public BoardController(SNLBoard board, List<Player> players) {
    this.board = board;
    this.players = new ArrayList<>(players);
    updatePlayerPositions();
  }

  public void registerObserver(BoardObserver observer) {
    observers.add(observer);
  }

  private void updatePlayerPositions() {
    playerPositions.clear();
    for (Player player : players) {
      int position = player.getPosition();
      if (!playerPositions.containsKey(position)) {
        playerPositions.put(position, new ArrayList<>());
      }
      playerPositions.get(position).add(player);
    }
  }

  public SNLBoard getBoard() {
    return board;
  }

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public List<Player> getPlayersAtPosition(int position) {
    return playerPositions.getOrDefault(position, new ArrayList<>());
  }

  public List<Ladder> getLadders() {
    return board.getLadders();
  }

  public List<Snake> getSnakes() {
    return board.getSnakes();
  }

  public int getBoardSize() {
    return board.getSize();
  }

  public void render() {
    updatePlayerPositions();
    notifyBoardRendered();
    logger.debug("Board rendered");
  }

  public void movePlayer(Player player, int toPosition) {
    int fromPosition = player.getPosition();
    player.setPosition(toPosition);
    updatePlayerPositions();
    notifyPlayerMoved(player, fromPosition, toPosition);

    int finalPosition = board.getFinalPosition(toPosition);
    if (finalPosition != toPosition) {
      boolean isLadder = finalPosition > toPosition;
      notifySpecialTileActivated(toPosition, finalPosition, isLadder);

      player.setPosition(finalPosition);
      updatePlayerPositions();
      notifyPlayerMoved(player, toPosition, finalPosition);
    }
  }

  public String getTileColor(int tileNum) {
    if (isSnakeStart(tileNum)) return "red";
    if (isSnakeEnd(tileNum)) return "pink";
    if (isLadderStart(tileNum)) return "darkgreen";
    if (isLadderEnd(tileNum)) return "lightgreen";
    return "white";
  }

  private boolean isSnakeStart(int tileNum) {
    return board.getSnakes().stream()
            .anyMatch(snake -> snake.start() == tileNum);
  }

  private boolean isSnakeEnd(int tileNum) {
    return board.getSnakes().stream()
            .anyMatch(snake -> snake.end() == tileNum);
  }

  private boolean isLadderStart(int tileNum) {
    return board.getLadders().stream()
            .anyMatch(ladder -> ladder.start() == tileNum);
  }

  private boolean isLadderEnd(int tileNum) {
    return board.getLadders().stream()
            .anyMatch(ladder -> ladder.end() == tileNum);
  }

  private void notifyBoardRendered() {
    for (BoardObserver observer : observers) {
      observer.onBoardRendered();
    }
  }

  private void notifyPlayerMoved(Player player, int fromPosition, int toPosition) {
    for (BoardObserver observer : observers) {
      observer.onPlayerMoved(player, fromPosition, toPosition);
    }
  }

  private void notifySpecialTileActivated(int tileNumber, int destination, boolean isLadder) {
    for (BoardObserver observer : observers) {
      observer.onSpecialTileActivated(tileNumber, destination, isLadder);
    }
  }
}
