package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snl.SNLPlayer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameStateCsvLoader {

  public static class GameState {
    public String boardFile;
    public int diceCount;
    public int currentTurnIndex;
    public List<Player> players = new ArrayList<>();

    public String getBoardFile() {
      return boardFile;
    }

    public int getDiceCount() {
      return diceCount;
    }

    public int getCurrentTurnIndex() {
      return currentTurnIndex;
    }

    public List<Player> getPlayers() {
      return players;
    }
  }

  public static GameState load(String filePath) throws IOException {
    GameState state = new GameState();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean readingPlayers = false;

      while ((line = reader.readLine()) != null) {
        if (line.startsWith("Board,")) {
          state.boardFile = line.split(",")[1];
        } else if (line.startsWith("DiceCount,")) {
          state.diceCount = Integer.parseInt(line.split(",")[1]);
        } else if (line.startsWith("CurrentTurnIndex,")) {
          state.currentTurnIndex = Integer.parseInt(line.split(",")[1]);
        } else if (line.equals("Players:")) {
          readingPlayers = true;
        } else if (readingPlayers && !line.isBlank()) {
          String[] parts = line.split(",");
          String name = parts[0];
          String character = parts[1];
          int position = Integer.parseInt(parts[2]);
          state.players.add(new SNLPlayer(name, character, position));
        }
      }
    }

    return state;
  }
}