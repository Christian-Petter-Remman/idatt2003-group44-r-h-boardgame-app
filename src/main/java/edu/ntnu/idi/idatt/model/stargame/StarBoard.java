package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.snakesladders.SNLBoard;
import edu.ntnu.idi.idatt.model.tile.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StarBoard {
  private static final Logger logger = LoggerFactory.getLogger(SNLBoard.class);
  private final List<Tile> tiles = new ArrayList<>();
  private final int size;
  private final List<Jail> jailTiles = new ArrayList<>();
  private final List<Bridge> bridgeTiles = new ArrayList<>();
  private final List<Tunnel> tunnelTiles = new ArrayList<>();
  private final List<Path> pathTiles = new ArrayList<>();

  public StarBoard() {
    this(130);
  }

  public StarBoard(int size) {
    this.size = size;
    initializeEmptyBoard();
  }

  public int getSize() {
    return size;
  }

  public List<Tile> getTiles() {
    return tiles;
  }
  public List<Jail> getJailTiles() {
    return jailTiles;
  }
  public List<Bridge> getBridgeTiles() {
    return bridgeTiles;
  }
  public List<Tunnel> getTunnelTiles() {
    return tunnelTiles;
  }
  public List<Path> getPathTiles() {
    return pathTiles;
  }

  public void initializeEmptyBoard() {

    for (int i = 1; i <= size; i++) {
      tiles.add(new Tile(i));
    }
    logger.debug("Initialized empty board with {} tiles", size);
  }

  public Tile getStarTile(int number) {
    if (number < 1 || number > size) {
      logger.warn("Attempted to get invalid tile number: {}", number);
      throw new IllegalArgumentException("Invalid tile number: " + number);
    }
    return tiles.get(number - 1);
  }

  public void setStarTile(int tileNumber, Tile tile) {
    if (tileNumber < 1 || tileNumber > size) {
      throw new IllegalArgumentException("Invalid tile number: " + tileNumber);
    }
    tiles.set(tileNumber - 1, tile);
  }

  public int getFinalStarPosition(int position) {
    if (position < 1 || position > size) {
      return position;
    }
    Tile tile = getStarTile(position);
    if (tile.hasSpecialTile()) {
      logger.debug("Player landed on special tile at {}, moving to {}", position, tile.getDestination());
      return tile.getDestination();
    }
    return position;
  }

  private void addBridge(int start, int end) {
    if (start >= end || start > size || end < 1) {
      throw new IllegalArgumentException("Invalid bridge positions");
    }
    Bridge bridge = new Bridge(start, end);
    bridgeTiles.add(bridge);
  }

  private void addTunnel(int start, int end) {
    if (start >= end || start > size || end < 1) {
      throw new IllegalArgumentException("Invalid tunnel positions");
    }
    Tunnel tunnel = new Tunnel(start, end);
    tunnelTiles.add(tunnel);
  }

  private void addJail(int start) {
    Jail jail = new Jail(start);
    jailTiles.add(jail);
  }

  private void addPath(int start, int end) {
    if (start >= end || start > size || end < 1) {
      throw new IllegalArgumentException("Invalid path positions");
    }
    Path path = new Path(start, end);
    pathTiles.add(path);
  }
}



