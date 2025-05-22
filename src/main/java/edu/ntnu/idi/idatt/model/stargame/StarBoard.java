package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Tile;
import edu.ntnu.idi.idatt.model.common.TileAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <h1>StarBoard</h1>
 *
 * Represents the board for the Star game, managing paths, tunnels, jails, bridges, and stars.
 */
public class StarBoard extends AbstractBoard {

  private static final Logger logger = LoggerFactory.getLogger(StarBoard.class);

  private final List<Bridge> bridges = new ArrayList<>();
  private final List<Tunnel> tunnels = new ArrayList<>();
  private final List<Path> paths = new ArrayList<>();
  private final List<Jail> jailTiles = new ArrayList<>();
  private final List<Star> stars = new ArrayList<>();

  public StarBoard(int size) {
    super(size);
    initializeBoard();
  }

  /**
   * <h2>addStar</h2>
   * Adds a new star to a random unoccupied tile.
   *
   * @return the created Star instance, or null if no valid tile was found.
   */
  public Star addStar() {
    List<Integer> occupied = getOccupiedTiles();
    List<Integer> validTiles = new ArrayList<>();

    for (int i = 1; i <= 73; i++) {
      if (!occupied.contains(i)) {
        validTiles.add(i);
      }
    }

    if (validTiles.isEmpty()) {
      logger.warn("No valid tiles left to place a Star.");
      return null;
    }

    int selected = validTiles.get(new Random().nextInt(validTiles.size()));
    Star star = new Star(selected, selected);
    getTile(selected).addAttribute(star);
    stars.add(star);
    logger.info("Star placed at tile {}", selected);
    return star;
  }

  private List<Integer> getOccupiedTiles() {
    List<Integer> occupied = new ArrayList<>();

    jailTiles.forEach(jail -> {
      occupied.add(jail.getStart());
      occupied.add(jail.getStart() + 1);
      occupied.add(jail.getStart() - 1);
    });

    bridges.forEach(bridge -> {
      occupied.add(bridge.getStart());
      occupied.add(bridge.getEnd());
    });

    tunnels.forEach(tunnel -> {
      occupied.add(tunnel.getStart());
      occupied.add(tunnel.getEnd());
    });

    stars.forEach(star -> occupied.add(star.getStart()));

    return occupied;
  }

  public int getStarPosition(Star star) {
    return star.getStart();
  }

  /**
   * <h2>respawnStar</h2>
   * Removes an existing star and places a new one randomly.
   *
   * @param star the star to be removed.
   * @return the tile number of the newly placed star.
   */
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
    if (getBridgeAt(start) == null) {
      bridges.add(new Bridge(start, end));
    }
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
    boolean alreadyExists = jailTiles.stream().anyMatch(j -> j.getStart() == position);
    if (!alreadyExists) {
      Jail jail = new Jail(position, 5);
      getTile(position).addAttribute(jail);
      jailTiles.add(jail);
      logger.info("Jail placed at tile {}", position);
    } else {
      logger.info("Jail already exists at tile {}", position);
    }
  }

  public Star getStarAt(int tileNumber) {
    return stars.stream()
            .filter(star -> star.getStart() == tileNumber)
            .findFirst()
            .orElse(null);
  }

  public Bridge getBridgeAt(int tileNumber) {
    return bridges.stream()
            .filter(bridge -> bridge.getStart() == tileNumber)
            .findFirst()
            .orElse(null);
  }

  public Path getPathAt(int position) {
    Tile tile = getTile(position);
    return tile.getAttributes().stream()
            .filter(attr -> attr instanceof Path)
            .map(attr -> (Path) attr)
            .findFirst()
            .orElse(null);
  }

  public Tunnel getTunnelAt(int tileNumber) {
    return tunnels.stream()
            .filter(tunnel -> tunnel.getStart() == tileNumber)
            .findFirst()
            .orElse(null);
  }

  public Jail getJailAt(int position) {
    if (position <= 0 || position > tiles.size()) {
      return null;
    }
    return tiles.get(position - 1).getAttributes().stream()
            .filter(attr -> attr instanceof Jail)
            .map(attr -> (Jail) attr)
            .findFirst()
            .orElse(null);
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

  @Override
  public int getSize() {
    return tiles.size();
  }
}