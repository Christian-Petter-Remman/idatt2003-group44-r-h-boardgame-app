package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.Dice;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnakesAndLadders extends BoardGame {

  private SNLBoard board;
  private Dice dice;
  private int currentPlayerIndex;
  private static final Logger logger = LoggerFactory.getLogger(SnakesAndLadders.class);

  public SnakesAndLadders() {
    super();
    currentPlayerIndex = 0;
  }
  public SnakesAndLadders(SNLBoard board) {
    this();
    this.board = board;
  }

  @Override
  public void initialize (SNLBoard board) {
    this.board = board;
    dice = new Dice(1);
  }

  @Override
  public void makeMove(Player player) {
    if (!(player instanceof SnakesAndLaddersPlayer)) {
      throw new IllegalArgumentException("Player must be a SnakesAndLaddersPlayer");
    }
    int roll = dice.roll();
    player.move(roll, board);

    if (player.hasWon()) {
      gameOver = true;
    } else {
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }
  }

  @Override
  public boolean isValidMove(Player player, int roll) {
    return player.getPosition() + roll <= 100;
  }

  @Override
  public boolean checkWinCondition() {
    return players.stream().anyMatch(Player::hasWon);
  }

  @Override
  public SNLBoard getBoard() {
    return board;
  }

  @Override
  public List<Player> getPlayers() {
    return new ArrayList<>(players);
  }

  public void setBoard(SNLBoard board) {
    this.board = board;
    logger.debug("Set board to {}", board);
  }

  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  public List<String> getCharacterNames() {
    List<String> characterNames = new ArrayList<>();
    for (Player player : players) {
      characterNames.add(player.getCharacter());
    }
    return characterNames;
  }

  public int rollDice() {
    return dice.roll();
  }

  public void setDice(Dice dice) {
    this.dice = dice;
  }

  public Player getWinner() {
    return players.stream()
        .filter(Player::hasWon)
        .findFirst()
        .orElse(null);
  }


  public void addPlayer(String name, String character,int position) {
    addPlayer(new SnakesAndLaddersPlayer(name,character,position));
  }

  public void advanceTurn() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  public int loadPlayersFromCsv(String path) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(path));
    int count = 0;

    for (String line : lines) {
      String[] parts = line.split(",");
      if (parts.length != 3) {
        continue; // Skip board name or malformed lines
      }

      String name = parts[0];
      String character = parts[1];
      int position = Integer.parseInt(parts[2]);

      Player player = new SnakesAndLaddersPlayer(name, character,position);
      this.addPlayer(player);
      count++;
    }

    return count;
  }

  public boolean savePlayersToCsv(String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      for (Player player : players) {
        writer.write(player.getName() + ",Default");
        writer.newLine();
      }
      return true;
    } catch (IOException e) {
      logger.error("Error saving players to CSV: {}", e.getMessage());
      return false;
    }
  }

}
