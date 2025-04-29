package edu.ntnu.idi.idatt.controller.snl;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.model_observers.BoardObserver;
import edu.ntnu.idi.idatt.model.snl.Ladder;
import edu.ntnu.idi.idatt.model.snl.Snake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SNLBoardController {
  private static final Logger logger = LoggerFactory.getLogger(SNLBoardController.class);

  private final AbstractBoard board;
  private final List<Player> players;
  private final List<BoardObserver> observers = new ArrayList<>();
  private final Map<Integer, List<Player>> playerPositions = new HashMap<>();

  public SNLBoardController(AbstractBoard board, List<Player> players) {
    this.board = board;
    this.players = new ArrayList<>(players);
    updatePlayerPositions();
  }

  //Observer Registration
  public void registerObserver(BoardObserver observer) {
    observers.add(observer);
  }

  //Board Queries

  public AbstractBoard getBoard() {
    return board;
  }

//  public List<Ladder> getLadders() {
//    return board.getLadders();
//  }
//
//  public List<Snake> getSnakes() {
//    return board.getSnakes();
//  }

  public int getBoardSize() {
    return board.getSize();
  }

  //Player Queries

  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public List<Player> getPlayersAtPosition(int position) {
    return playerPositions.getOrDefault(position, new ArrayList<>());
  }

  //Actions

//  public void movePlayer(Player player, int toPosition) {
//    int fromPosition = player.getPosition();
//    player.setPosition(toPosition);
//
//    updatePlayerPositions();
//    notifyPlayerMoved(player, fromPosition, toPosition);
//
//    int finalPosition = board.getFinalPosition(toPosition);
//    if (finalPosition != toPosition) {
//      boolean isLadder = finalPosition > toPosition;
//      notifySpecialTileActivated(toPosition, finalPosition, isLadder);
//
//      player.setPosition(finalPosition);
//      updatePlayerPositions();
//      notifyPlayerMoved(player, toPosition, finalPosition);
//    }
//  }

  public void render() {
    updatePlayerPositions();
    notifyBoardRendered();
    logger.debug("Board rendered and updated.");
  }


  public String getTileColor(int tileNum) {
    return (tileNum % 2 == 0) ? "#f0f0f0" : "#d0d0d0";
  }

  //Internal Helpers

  private void updatePlayerPositions() {
    playerPositions.clear();
    for (Player player : players) {
      int position = player.getPosition();
      playerPositions.computeIfAbsent(position, k -> new ArrayList<>()).add(player);
    }
  }

  //Notify Observers

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
