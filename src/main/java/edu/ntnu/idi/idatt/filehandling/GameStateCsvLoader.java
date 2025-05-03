package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snl.SNLPlayer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.ntnu.idi.idatt.model.stargame.StarPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameStateCsvLoader {

  public static class GameState {
    public String boardFile;
    public int diceCount;
    public int currentTurnIndex;
    public List<Player> players = new ArrayList<>();
    public int points;


    private static final Logger logger = LoggerFactory.getLogger(GameStateCsvLoader.class);


    public String getBoardFile() {
      logger.info("Loading game state from {}", boardFile);  // Log statement
      return boardFile;
    }

    public void setBoardFile(String boardFile) {
      this.boardFile = boardFile;  // Ensure no recursive calls here
      logger.info("Board file set to {}", boardFile);
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

  public static GameState SNLLoad(String filePath) throws IOException {
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

  public static GameState StarLoad(String filePath) throws IOException {
    GameState state = new GameState();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean readingPlayers = false;

      while ((line = reader.readLine()) != null) {
        if (line.startsWith("Board,")) {
          state.boardFile = line.split(",")[1];
        } else if (line.startsWith("DiceCount,")) {
          state.diceCount = Integer.parseInt(line.split(",")[1]);
        } else if (line.startsWith("Points")) {
          state.points = Integer.parseInt(line.split(",")[1]);
        } else if (line.startsWith("CurrentTurnIndex,")) {
          state.currentTurnIndex = Integer.parseInt(line.split(",")[1]);
        } else if (line.equals("Players:")) {
          readingPlayers = true;
        } else if (readingPlayers && !line.isBlank()) {
          String[] parts = line.split(",");
          String name = parts[0];
          String character = parts[1];
          int position = Integer.parseInt(parts[2]);
          int points = Integer.parseInt(parts[3]);
          state.players.add(new StarPlayer(name, character, position,points));
        }
      }
    }

    return state;
  }


}