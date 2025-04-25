package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.common.Dice;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SNLGame extends BoardGame {

  private SNLBoard board;
  private Dice dice;
  private int currentPlayerIndex;
  private static final Logger logger = LoggerFactory.getLogger(SNLGame.class);

  public SNLGame() {
    super();
    currentPlayerIndex = 0;
  }
  public SNLGame(SNLBoard board) {
    this();
    this.board = board;
  }

  @Override
  public void initialize (StarBoard board) {}

  @Override
  public void initialize (SNLBoard board) {
    this.board = board;
    dice = new Dice(1);
  }

  @Override
  public void makeMove(Player player) {
    if (!(player instanceof SNLPlayer)) {
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

  public SNLBoard getBoard() {
    return board;
  }

  @Override
  public void setBoard(SNLBoard board) {
    this.board = board;
    logger.debug("Set board to {}", board);
  }

  @Override
  public void setBoard(StarBoard board) {
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
    addPlayer(new SNLPlayer(name,character,position));
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

      Player player = new SNLPlayer(name, character,position);
      this.addPlayer(player);
      count++;
    }

    return count;
  }
}
