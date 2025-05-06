package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.Tile;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StarBoardTest {

  private StarBoard board;

  @BeforeEach
  void setUp() {
    board = new StarBoard(100);
  }

  @Test
  void testAddBridge() {
    board.addBridge(5, 10);
    Bridge bridge = board.getBridgeAt(5);
    assertNotNull(bridge);
    assertEquals(5, bridge.getStart());
    assertEquals(10, bridge.getEnd());
  }

  @Test
  void testAddTunnel() {
    board.addTunnel(3, 7);
    Tunnel tunnel = board.getTunnelAt(3);
    assertNotNull(tunnel);
    assertEquals(3, tunnel.getStart());
    assertEquals(7, tunnel.getEnd());
  }

  @Test
  void testAddPath() {
    board.addPath(4, "right", 6, 8);
    Path path = board.getPathAt(4);
    assertNotNull(path);
    assertEquals(4, path.getStart());
    assertEquals("right", path.getDirection());
    assertEquals(6, path.getEndStatic());
    assertEquals(8, path.getEndDynamic());
  }

  /**
   * <H3>testAddJail</H3>
   *
   * Jail position is modifeid one tile ahed therefore, int 13 in {@code board.getJailAt}
   *
   */
  @Test
  void testAddJail() {
    board.addJail(12);
    Jail jail = board.getJailAt(13);
    assertNotNull(jail);
    assertEquals(12, jail.getStart());
  }

  @Test
  void testAddStar() {
    Star star = board.addStar();
    assertNotNull(star);
    assertTrue(board.getStars().contains(star));
    Tile tile = board.getTile(star.getStart());
    assertTrue(tile.getAttributes().contains(star));
  }

  @Test
  void testRemoveStar() {
    Star star = board.addStar();
    assertNotNull(star);
    board.removeStar(star);
    assertFalse(board.getStars().contains(star));
    Tile tile = board.getTile(star.getStart());
    assertFalse(tile.getAttributes().contains(star));
  }

  @Test
  void testRespawnStar() {
    Star original = board.addStar();
    int originalPos = original.getStart();
    int newPos = board.respawnStar(original);
    assertNotEquals(originalPos, newPos);
    assertNull(board.getStarAt(originalPos));
    assertNotNull(board.getStarAt(newPos));
  }

  @Test
  void testGettersReturnSafeCopies() {
    board.addBridge(1, 2);
    board.addJail(3);
    board.addTunnel(4, 5);
    board.addPath(6, "left", 7, 8);
    board.addStar();

    List<Bridge> bridges = board.getBridges();
    List<Jail> jails = board.getJailTiles();
    List<Tunnel> tunnels = board.getTunnels();
    List<Path> paths = board.getPaths();
    List<Star> stars = board.getStars();

    assertEquals(1, bridges.size());
    assertEquals(1, jails.size());
    assertEquals(1, tunnels.size());
    assertEquals(1, paths.size());
    assertEquals(1, stars.size());

    bridges.clear();
    jails.clear();
    tunnels.clear();
    paths.clear();
    stars.clear();


    assertEquals(1, board.getBridges().size());
    assertEquals(1, board.getJailTiles().size());
    assertEquals(1, board.getTunnels().size());
    assertEquals(1, board.getPaths().size());
    assertEquals(1, board.getStars().size());
  }

  @Test
  void testSetAndGetJailTile() {
    board.setJailTile(15);
    assertEquals(15, board.getJailTile());
  }
}