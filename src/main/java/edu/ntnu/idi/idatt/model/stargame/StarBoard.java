package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SNLBoard;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StarBoard {
  private static final Logger logger = LoggerFactory.getLogger(SNLBoard.class);
  private List<Tile> tiles;
  private final int size;


  public StarBoard() {
    this(130);
  }

  public StarBoard(int size) {
    this.size = size;
    initializeEmptyBoard();
  }

  public void initializeEmptyBoard() {
    tiles = new ArrayList<>();
    for (int i = 1; i <= size; i++) {
      tiles.add(new Tile(i));
    }
    logger.debug("Initialized empty board with {} tiles", size);
  }

  private void addBridge(){}

  private void addTunnel(){}

  private void addJail(){}

  private void addPath(){}
}



