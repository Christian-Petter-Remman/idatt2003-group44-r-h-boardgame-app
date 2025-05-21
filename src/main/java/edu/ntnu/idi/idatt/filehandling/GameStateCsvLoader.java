package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snl.SNLPlayer;
import edu.ntnu.idi.idatt.model.stargame.StarPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>GameStateCsvLoader</h1>
 * Utility class responsible for loading saved game state from CSV files
 * for both Snakes and Ladders and Star board games.
 */
public class GameStateCsvLoader {

  /**
   * <h1>GameState</h1>
   * Represents the data loaded from a CSV save file, including board file,
   * dice count, turn index, player list, and optionally points (for StarGame).
   */
  public static class GameState {
    private static final Logger logger = LoggerFactory.getLogger(GameStateCsvLoader.class);

    private String boardFile;
    private int diceCount;
    private int currentTurnIndex;
    private final List<Player> players = new ArrayList<>();
    private int points;

    /**
     * <h2>getBoardFile</h2>
     * @return the path to the board file
     */
    public String getBoardFile() {
      logger.info("Loading game state from {}", boardFile);
      return boardFile;
    }

    /**
     * <h2>setBoardFile</h2>
     * @param boardFile the path to the board file
     */
    public void setBoardFile(String boardFile) {
      this.boardFile = boardFile;
      logger.info("Board file set to {}", boardFile);
    }

    /**
     * <h2>getBoardFileName</h2>
     * @return the board file name (same as getBoardFile)
     */
    public String getBoardFileName() {
      return boardFile;
    }

    /**
     * <h2>getDiceCount</h2>
     * @return the number of dice used in the game
     */
    public int getDiceCount() {
      return diceCount;
    }

    /**
     * <h2>getCurrentTurnIndex</h2>
     * @return the index of the player whose turn it is
     */
    public int getCurrentTurnIndex() {
      return currentTurnIndex;
    }

    /**
     * <h2>getPlayers</h2>
     * @return a list of players loaded from file
     */
    public List<Player> getPlayers() {
      return players;
    }

    /**
     * <h2>getPoints</h2>
     * @return the total points (used only in StarGame)
     */
    public int getPoints() {
      return points;
    }

    /**
     * <h2>setPoints</h2>
     * @param points the number of points
     */
    public void setPoints(int points) {
      this.points = points;
    }
  }

  /**
   * <h2>SNLLoad</h2>
   * Loads a saved game state for Snakes and Ladders from a CSV file.
   *
   * @param filePath path to the CSV file
   * @return populated {@link GameState} instance
   * @throws IOException if the file can't be read
   */
  public static GameState SNLLoad(String filePath) throws IOException {
    GameState state = new GameState();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean readingPlayers = false;

      while ((line = reader.readLine()) != null) {
        if (line.startsWith("Board,")) {
          state.setBoardFile(line.split(",")[1]);
        } else if (line.startsWith("DiceCount,")) {
          state.diceCount = Integer.parseInt(line.split(",")[1]);
        } else if (line.startsWith("CurrentTurnIndex,")) {
          state.currentTurnIndex = Integer.parseInt(line.split(",")[1]);
        } else if (line.equals("Players:")) {
          readingPlayers = true;
        } else if (readingPlayers && !line.isBlank()) {
          String[] parts = line.split(",");
          state.players.add(new SNLPlayer(parts[0], parts[1], Integer.parseInt(parts[2])));
        }
      }
    }
    return state;
  }

  /**
   * <h2>StarLoad</h2>
   * Loads a saved game state for StarGame from a CSV file.
   *
   * @param filePath path to the CSV file
   * @return populated {@link GameState} instance with points
   * @throws IOException if the file can't be read
   */
  public static GameState StarLoad(String filePath) throws IOException {
    GameState state = new GameState();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean readingPlayers = false;

      while ((line = reader.readLine()) != null) {
        if (line.startsWith("Board,")) {
          state.setBoardFile(line.split(",")[1]);
        } else if (line.startsWith("DiceCount,")) {
          state.diceCount = Integer.parseInt(line.split(",")[1]);
        } else if (line.startsWith("Points")) {
          state.setPoints(Integer.parseInt(line.split(",")[1]));
        } else if (line.startsWith("CurrentTurnIndex,")) {
          state.currentTurnIndex = Integer.parseInt(line.split(",")[1]);
        } else if (line.equals("Players:")) {
          readingPlayers = true;
        } else if (readingPlayers && !line.isBlank()) {
          String[] parts = line.split(",");
          state.players.add(new StarPlayer(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
        }
      }
    }
    return state;
  }
}