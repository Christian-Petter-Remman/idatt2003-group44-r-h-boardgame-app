package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StarBoardTest {

  private StarBoard board;

  @BeforeEach
  void setUp() {
    board = new StarBoard(100);
  }

  @Test
  void testBoardInitialization() {
    assertEquals(100, board.getSize(), "Board should report correct size");
    for (int i = 0; i < board.getSize(); i++) {
      assertNotNull(board.getTile(i), "Tile " + i + " should exist on the board");
    }
    assertTrue(board.getBridges().isEmpty());
    assertTrue(board.getTunnels().isEmpty());
    assertTrue(board.getPaths().isEmpty());
    assertTrue(board.getJailTiles().isEmpty());
    assertTrue(board.getStars().isEmpty());
  }

  @Test
  void testAddBridge() {
    board.addBridge(5, 10);
    Bridge bridge = board.getBridgeAt(5);
    assertNotNull(bridge);
    assertEquals(5, bridge.getStart());
    assertEquals(10, bridge.getEnd());
    assertTrue(board.getBridges().contains(bridge));
  }

  @Test
  void testAddTunnel() {
    board.addTunnel(3, 7);
    Tunnel tunnel = board.getTunnelAt(3);
    assertNotNull(tunnel);
    assertEquals(3, tunnel.getStart());
    assertEquals(7, tunnel.getEnd());
    assertTrue(board.getTunnels().contains(tunnel));
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
    assertTrue(board.getPaths().contains(path));
  }

  @Test
  void testAddJail() {
    board.addJail(12);
    Jail jail = board.getJailAt(13);
    assertNotNull(jail);
    assertEquals(12, jail.getStart());
    assertTrue(board.getJailTiles().contains(jail));
  }

  @Test
  void testAddStar() {
    Star star = board.addStar();
    assertNotNull(star, "Should add a new star");
    assertTrue(board.getStars().contains(star), "Star should be tracked by board");
    Tile tile = board.getTile(star.getStart());
    assertTrue(tile.getAttributes().contains(star), "Star should be an attribute of its tile");
  }

  @Test
  void testRemoveStar() {
    Star star = board.addStar();
    assertNotNull(star);
    board.removeStar(star);
    assertFalse(board.getStars().contains(star), "Star should be removed from board");
    Tile tile = board.getTile(star.getStart());
    assertFalse(tile.getAttributes().contains(star), "Star attribute should be removed from tile");
  }

  @Test
  void testRespawnStar() {
    Star oldStar = board.addStar();
    int oldPosition = oldStar.getStart();
    int newPosition = board.respawnStar(oldStar);
    assertNotEquals(oldPosition, newPosition, "Star should respawn at a new position");
    assertFalse(board.getStars().stream().anyMatch(s -> s.getStart() == oldPosition),
        "Old star should be gone");
    assertTrue(board.getStars().stream().anyMatch(s -> s.getStart() == newPosition),
        "New star should exist");
  }

  @Test
  void testGetStarPosition() {
    Star star = board.addStar();
    int pos = board.getStarPosition(star);
    assertEquals(star.getStart(), pos, "getStarPosition should return star's start");
  }

  @Test
  void testAddStarNoSpaceLeft() {
    for (int i = 1; i <= 73; i++) {
      board.addJail(i);
    }
    Star star = board.addStar();
    assertNull(star, "Should not be able to add star when all tiles are occupied");
  }

  @Test
  void testNoDuplicateBridgesOrTunnelsOrJails() {
    board.addBridge(10, 20);
    board.addBridge(10, 20);
    assertEquals(1, board.getBridges().stream().filter(b -> b.getStart() == 10).count(),
        "Duplicate bridges on same tile should not exist");

    board.addTunnel(15, 25);
    board.addTunnel(15, 25);
    assertEquals(2, board.getTunnels().stream().filter(t -> t.getStart() == 15).count(),
        "2 tunnels on same tile should exist");

    board.addJail(30);
    board.addJail(30);
    assertEquals(1, board.getJailTiles().stream().filter(j -> j.getStart() == 30).count(),
        "Duplicate jail on same tile should not exist");
  }

  @Test
  void testGetBridgeAtWithNoBridge() {
    assertNull(board.getBridgeAt(42));
  }

  @Test
  void testGetTunnelAtWithNoTunnel() {
    assertNull(board.getTunnelAt(42));
  }

  @Test
  void testGetPathAtWithNoPath() {
    assertNull(board.getPathAt(99));
  }

  @Test
  void testGetJailAtWithNoJail() {
    assertNull(board.getJailAt(77));
  }

  @Test
  void testRemoveStarOnNonexistentStarDoesNothing() {
    Star star = new Star(10, 10);
    assertDoesNotThrow(() -> board.removeStar(star));
  }

  @Test
  void testAddMultipleStars() {
    Star s1 = board.addStar();
    Star s2 = board.addStar();
    assertNotNull(s1);
    assertNotNull(s2);
    assertNotEquals(s1.getStart(), s2.getStart(), "Stars should not be placed on same tile");
    assertEquals(2, board.getStars().size());
  }

  @Test
  void testAddBridgeWithNegativeValues() {
    board.addBridge(-1, -5);
    Bridge bridge = board.getBridgeAt(-1);
    assertNotNull(bridge);
    assertEquals(-1, bridge.getStart());
    assertEquals(-5, bridge.getEnd());
  }

  @Test
  void testTileOutOfBoundsThrows() {
    int outOfBounds = 1001;
    Exception ex = assertThrows(IllegalArgumentException.class, () -> board.getTile(outOfBounds));
    assertTrue(ex.getMessage().contains("Tile number"));
  }

  @Test
  void testJailCanOnlyExistOncePerTile() {
    board.addJail(50);
    board.addJail(50);
    long count = board.getJailTiles().stream().filter(j -> j.getStart() == 50).count();
    assertEquals(1, count, "Only one jail should exist per tile");
  }

  @Test
  void testAddStarDoesNotOverlapWithBridgesOrTunnelsOrJails() {
    board.addBridge(60, 61);
    board.addTunnel(62, 63);
    board.addJail(64);
    Star star = board.addStar();
    assertNotEquals(60, star.getStart());
    assertNotEquals(61, star.getStart());
    assertNotEquals(62, star.getStart());
    assertNotEquals(63, star.getStart());
    assertNotEquals(64, star.getStart());
    assertNotEquals(65, star.getStart());
    assertNotEquals(63, star.getStart());
    assertNotEquals(65, star.getStart());
  }

  @Test
  void testLoggerWarnOnNoValidTilesLeftForStar() {
    for (int i = 1; i <= 73; i++) {
      board.addJail(i);
    }
    Star star = board.addStar();
    assertNull(star);
  }
}