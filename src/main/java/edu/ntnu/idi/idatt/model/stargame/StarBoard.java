package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <h1>StarBoard</h1>
 *
 * <p>Represents the board for the Star game, managing paths, tunnels, jails, bridges, and stars.
 */
public class StarBoard extends AbstractBoard {

  private static final Logger logger = LoggerFactory.getLogger(StarBoard.class);

  private final List<Bridge> bridges = new ArrayList<>();
  private final List<Tunnel> tunnels = new ArrayList<>();
  private final List<Path> paths = new ArrayList<>();
  private final List<Jail> jailTiles = new ArrayList<>();
  private final List<Star> stars = new ArrayList<>();

  /**
   * <h2>Constructor</h2>
   *
   * <p>Initializes the board with default paths, tunnels, jails, and bridges.
   */
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
      occupied.add(jail.start());
      occupied.add(jail.start() + 1);
      occupied.add(jail.start() - 1);
    });

    bridges.forEach(bridge -> {
      occupied.add(bridge.start());
      occupied.add(bridge.end());
    });

    tunnels.forEach(tunnel -> {
      occupied.add(tunnel.start());
      occupied.add(tunnel.end());
    });

    stars.forEach(star -> occupied.add(star.start()));

    return occupied;
  }

  /**
   * <h2>getStarPosition</h2>
   *
   * <p>Returns the position of the star on the board.
   *
   * @param star the star to get the position of.
   * @return the tile number of the star.
   */
  public int getStarPosition(Star star) {
    return star.start();
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

  /**
   * <h2>removeStar</h2>
   * Removes a star from the board.
   *
   * @param star the star to be removed.
   */
  public void removeStar(Star star) {
    Tile tile = getTile(star.start());
    tile.getAttributes().remove(star);
    stars.remove(star);
    logger.info("Star removed from tile {}", star.start());
  }

  /**
   * <h2>addBrigde</h2>
   *
   * <p>Creates a bridge between two tiles. If a bridge already exists at the start tile, it is not
   * added.
   *
   * @param start the starting tile index of the bridge.
   * @param end the ending tile index of the bridge.
   */
  public void addBridge(int start, int end) {
    if (getBridgeAt(start) == null) {
      bridges.add(new Bridge(start, end));
    }
    logger.info("Bridge placed from tile {} to tile {}", start, end);
  }

  /**
   * <h2>addTunnel</h2>
   *
   * <p>Creates a tunnel between two tiles.
   *
   * @param start the starting tile index of the tunnel.
   * @param end the ending tile index of the tunnel.
   */
  public void addTunnel(int start, int end) {
    Tunnel tunnel = new Tunnel(start, end);
    getTile(start).addAttribute(tunnel);
    tunnels.add(tunnel);
    logger.info("Tunnel placed from tile {} to tile {}", start, end);
  }

  /**
   * <h2>addPath</h2>
   *
   * <p>Gives a path choice (not implemented yet, but can be used for future development).
   *
   * @param position the tile index where the path starts.
   * @param direction the direction of the path (e.g., "up", "down", "left", "right").
   * @param endStatic the static end index of the path.
   * @param endDynamic the dynamic end index of the path.
   */
  public void addPath(int position, String direction, int endStatic, int endDynamic) {
    Path path = new Path(position, direction, endStatic, endDynamic);
    getTile(position).addAttribute(path);
    paths.add(path);
    logger.info("Path placed at tile {} (static end: {}, dynamic end: {})", position, endStatic,
        endDynamic);
  }

  /**
   * <h2>addJail</h2>
   *
   * <p>Adds a jail tile to the board at the specified position.
   *
   * @param position the tile index where the jail is located.
   */
  public void addJail(int position) {
    boolean alreadyExists = jailTiles.stream().anyMatch(j -> j.start() == position);
    if (!alreadyExists) {
      Jail jail = new Jail(position, 5);
      getTile(position).addAttribute(jail);
      jailTiles.add(jail);
      logger.info("Jail placed at tile {}", position);
    } else {
      logger.info("Jail already exists at tile {}", position);
    }
  }

  /**
   * <h2>getStarAt</h2>
   *
   * <p>Returns the star located at the specified tile number.
   *
   * @param tileNumber the tile number to check.
   * @return the star at the specified tile number, or null if none exists.
   */
  public Star getStarAt(int tileNumber) {
    return stars.stream()
        .filter(star -> star.start() == tileNumber)
        .findFirst()
        .orElse(null);
  }

  /**
   * <h2>getBridgeAt</h2>
   *
   * <p>Returns the bridge located at the specified tile number.
   *
   * @param tileNumber the tile number to check.
   * @return the bridge at the specified tile number, or null if none exists.
   */
  public Bridge getBridgeAt(int tileNumber) {
    return bridges.stream()
        .filter(bridge -> bridge.start() == tileNumber)
        .findFirst()
        .orElse(null);
  }

  /**
   * <h2>getPathAt</h2>
   *
   * <p>Returns the path located at the specified tile number.
   *
   * @param position the tile number to check.
   * @return the path at the specified tile number, or null if none exists.
   */
  public Path getPathAt(int position) {
    Tile tile = getTile(position);
    return tile.getAttributes().stream()
        .filter(attr -> attr instanceof Path)
        .map(attr -> (Path) attr)
        .findFirst()
        .orElse(null);
  }

  /**
   * <h2>getTunnelAt</h2>
   *
   * <p>Returns the tunnel located at the specified tile number.
   *
   * @param tileNumber the tile number to check.
   * @return the tunnel at the specified tile number, or null if none exists.
   */
  public Tunnel getTunnelAt(int tileNumber) {
    return tunnels.stream()
        .filter(tunnel -> tunnel.start() == tileNumber)
        .findFirst()
        .orElse(null);
  }

  /**
   * <h2>getJailAt</h2>
   *
   * <p>Returns the jail located at the specified tile number.
   *
   * @param position the tile number to check.
   * @return the jail at the specified tile number, or null if none exists.
   */
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