package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import edu.ntnu.idi.idatt.model.common.game.BoardGame;
import edu.ntnu.idi.idatt.model.common.player.Player;
import edu.ntnu.idi.idatt.model.common.dice.Dice;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SnakesAndLadders extends BoardGame {

  private Board board;
  private Dice dice;
  private int currentPlayerIndex;

  public SnakesAndLadders() {
    super();
    currentPlayerIndex = 0;
  }

  @Override
  public void initialize() {
    board = new Board();
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
  public Board getBoard() {
    return board;
  }

  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
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

  public void addPlayer(String name) {
    addPlayer(new SnakesAndLaddersPlayer(name));
  }

  public void advanceTurn() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  public int loadPlayersFromCsv(String filePath) {
    int playersAdded = 0;

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        if ((line = line.trim()).isEmpty()) {
          continue;
        }

        String[] parts = line.split(",");
        if (parts.length >= 1) {
          String name = parts[0].trim();
          addPlayer(new SnakesAndLaddersPlayer(name));
          playersAdded++;
        }
      }
    } catch (IOException e) {
      System.err.println("Error reading from CSV " + e.getMessage());
    }

    return playersAdded;
  }

  public boolean savePlayersToCsv(String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      for (Player player : players) {
        writer.write(player.getName() + ",Default");
        writer.newLine();
      }
      return true;
    } catch (IOException e) {
      System.err.println("Error saving players to CSV: " + e.getMessage());
      return false;
    }
  }

}
