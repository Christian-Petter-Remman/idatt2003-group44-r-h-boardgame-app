package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Tile;

import edu.ntnu.idi.idatt.model.tile.Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StarBoard extends AbstractBoard {

  public StarBoard(int size) {
    super(size);
    initializeBoard();
  }

  public void initializeBoard() {
    for (int i = 1; i <= getSize(); i++){
      addTile(new Tile(1));
    }
  }

  public void addBridge(int start, int end) {
    getTile(start).addAttribute(new Bridge(start,end));
  }

  public void addTunnel(int start, int end) {
    getTile(start).addAttribute(new Tunnel(start, end));
  }

  public void addJail(int position) {
    getTile(position).addAttribute(new Jail());
  }

  public void addPath(int position, String direction) {
    getTile(position).addAttribute(new Path(direction));
  }
}


