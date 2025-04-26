package edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile;

import edu.ntnu.idi.idatt.model.common.Player;

public class LadderTile extends Tile {

  public LadderTile(int numberOfTile, int destination) {
    super(numberOfTile, destination);
    if (destination <= numberOfTile) {
      throw new IllegalArgumentException("A ladder must lead to a higher tile.");
    }
  }

  @Override
  public void onPlayerLanded(Player player) {
    int oldPosition = player.getPosition();
    player.setPosition(getDestination());
    notifyObservers("LADDER_USED", new LadderEvent(player, oldPosition, getDestination()));
  }

  public record LadderEvent(Player player, int from, int to) {}
}
