package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.tile.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SnakesAndLaddersPlayerTest {

  @Test
  public void testMoveWithCustomLadder() {
    SNLBoard board = new SNLBoard();

    board.setTile(4,new Tile(4,12));

    Player player = new SnakesAndLaddersPlayer("TestPlayer", "bowser",0);

    player.move(4, board);

    assertEquals(12, player.getPosition(), "Player should land on tile 12 via ladder");
  }

  @Test
  public void testMoveWithoutLadderNegative() {
    SNLBoard board = new SNLBoard();
    board.setTile(4, new Tile(4, 12));
    Player player = new SnakesAndLaddersPlayer("TestPlayer","bowser",0);
    player.move(4, board);
    assertNotEquals(4, player.getPosition(), "Player should land on tile 12 via ladder");
  }

  @Test
  public void testMoveWithoutLadder() {
    SNLBoard board = new SNLBoard();
    Player player = new SnakesAndLaddersPlayer("TestPlayer", "bowser",0);

    player.move(3, board);

    assertEquals(3, player.getPosition(), "Player should land directly on tile 3");
  }
}