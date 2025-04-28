package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snakesladders.SNLBoard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class StarGame extends BoardGame {

  public StarGame(SNLBoard board) {
    super(board);
  }

  @Override
  public void initialize(List<Player> players){
    super.initialize(players);
  }

  @Override
  public void playTurn() {
    Player currentPlayer = getCurrentPlayer();
    int roll = dice.roll();
    currentPlayer.move(roll,board);
    notifyObservers(currentPlayer,roll);
  }

  public int loadPlayersFromCsv(String path) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(path));
    int count = 0;

    for (String line : lines) {
      String[] parts = line.split(",");
      if (parts.length != 3) {
        continue;
      }

      String name = parts[0];
      String character = parts[1];
      int position = Integer.parseInt(parts[2]);
      int points = Integer.parseInt(parts[3]);

      Player player = new StarPlayer(name, character,position,points);
      this.addPlayer(player);
      count++;
    }

    return count;
  }



}







