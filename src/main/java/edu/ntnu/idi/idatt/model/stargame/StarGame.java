package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.snakesladders.SNLBoard;
import edu.ntnu.idi.idatt.model.snakesladders.SnakesAndLaddersPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StarGame extends BoardGame {

  private StarBoard board;
  private Dice dice;
  private int currentPlayerIndex;
  private static final Logger logger = LoggerFactory.getLogger(StarGame.class);

  public StarGame() {
    super();
    currentPlayerIndex = 0;
  }

  public StarGame(StarBoard board) {
    this();
    this.board = board;
  }

  @Override
  public void initialize(StarBoard board) {
    this.board = board;
    dice = new Dice(1);
  }

  public void setBoard(StarBoard board) {
    this.board = board;
    logger.debug("Set board to {}", board);
  }

  @Override
  public void makeMove(Player player) {{
      if (player == null) {
        throw new IllegalArgumentException("Player must be a Player");
      }
      int roll = dice.roll();
      player.move(roll, board);

      if (player.hasWon()) {
        gameOver = true;
      } else {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
      }
    }
  }

  @Override
  public boolean isValidMove(Player player, int move) {
    return player.getPosition() + move <= 130;
  }

  @Override
  public boolean checkWinCondition() {
    return players.stream().anyMatch(Player::hasWon);
  }

  @Override
  public void initialize(SNLBoard board) {
  }

  @Override
  public void setBoard(SNLBoard board) {
  }

  public StarBoard getBoard() {
    return board;
  }

  public List<String> getCharacterNames() {
    List<String> characterNames = new ArrayList<>();
    for (Player player : players) {
      characterNames.add(player.getCharacter());
    }
    return characterNames;
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
        continue;
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



}







