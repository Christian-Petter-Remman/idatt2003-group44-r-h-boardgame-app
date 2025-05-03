package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Tile;
import edu.ntnu.idi.idatt.model.common.TileAttribute;
import edu.ntnu.idi.idatt.model.snl.Snake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarBoard extends AbstractBoard {

  private static final Logger logger = LoggerFactory.getLogger(StarBoard.class);

  private final List<Bridge> bridges = new ArrayList<>();
  private final List<Tunnel> tunnels = new ArrayList<>();
  private final List<Path> paths = new ArrayList<>();
  private final List<Jail> jailTiles = new ArrayList<>();
  private final List<Star> stars = new ArrayList<>();
  private int jailTile = -1; // default no jail

  public StarBoard(int size) {
    super(size);
    initializeBoard();
  }

  public Star addStar() {
    int start = new Random().nextInt(73);
    Star star = new Star(start, start);
    getTile(start).addAttribute(star);
    stars.add(star);
    logger.info("Star placed at tile {}", start);
    return star;
  }

  public int getStarPosition(Star star) {
    return star.getStart();
  }

  public int respawnStar(Star star) {
    removeStar(star);
    Star newStar = addStar();
    return getStarPosition(newStar);
  }

  public void removeStar(Star star) {
    Tile tile = getTile(star.getStart());
    tile.getAttributes().remove(star);
    stars.remove(star);
    logger.info("Star removed from tile {}", star.getStart());
  }

  public void addBridge(int start, int end) {
    Bridge bridge = new Bridge(start, end);
    getTile(start).addAttribute(bridge);
    bridges.add(bridge);
    logger.info("Bridge placed from tile {} to tile {}", start, end);
  }

  public void addTunnel(int start, int end) {
    Tunnel tunnel = new Tunnel(start, end);
    getTile(start).addAttribute(tunnel);
    tunnels.add(tunnel);
    logger.info("Tunnel placed from tile {} to tile {}", start, end);
  }

  public void addPath(int position, String direction, int endStatic, int endDynamic) {
    Path path = new Path(position, direction, endStatic, endDynamic);
    getTile(position).addAttribute(path);
    paths.add(path);
    logger.info("Path placed at tile {} (static end: {}, dynamic end: {})", position, endStatic, endDynamic);
  }

  public void addJail(int position) {
    Jail jail = new Jail(position, 5);
    getTile(position).addAttribute(jail);
    jailTiles.add(jail);
    logger.info("Jail placed at tile {}", position);
  }

  public void setJailTile(int jailTile) {
    this.jailTile = jailTile;
  }

  public Star getStarAt(int tileNumber) {
    for (Star star : stars) {
      if (star.getStart() == tileNumber) {
        return star;
      }
    }
    return null;
  }

  public Bridge getBridgeAt(int tileNumber) {
    for (Bridge bridge : bridges) {
      if (bridge.getStart() == tileNumber) {
        return bridge;
      }
    }
    return null;
  }

  public Path getPathAt(int position) {
    Tile tile = getTile(position);
    for (TileAttribute attr : tile.getAttributes()) {
      if (attr instanceof Path) {
        return (Path) attr;
      }
    }
    return null;
  }

  public Tunnel getTunnelAt(int tileNumber) {
    for (Tunnel tunnel : tunnels) {
      if (tunnel.getStart() == tileNumber) {
        return tunnel;
      }
    }
    return null;
  }

  public Jail getJailAt(int position) {
    if (position <= 0 || position > tiles.size()) {
      return null;
    }
    for (TileAttribute attribute : tiles.get(position - 1).getAttributes()) {
      if (attribute instanceof Jail) {
        return (Jail) attribute;
      }
    }
    return null;
  }

  public List<Bridge> getBridges() {
    return new ArrayList<>(bridges);
  }

  public List<Star> getStars() {
    return new ArrayList<>(stars);
  }

  public List<Tunnel> getTunnels() {
    return new ArrayList<>(tunnels);
  }
  public List<Path> getPaths() {
    return new ArrayList<>(paths);
  }
  public List<Jail> getJailTiles() {
    return new ArrayList<>(jailTiles);
  }

  public int getJailTile() {
    return jailTile;
  }

  @Override
  public int getSize() {
    return tiles.size();
  }
}