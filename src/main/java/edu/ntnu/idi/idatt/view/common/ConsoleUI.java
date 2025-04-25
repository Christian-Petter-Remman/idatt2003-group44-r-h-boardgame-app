package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.model.snakesladders.SnakesAndLadders;
import edu.ntnu.idi.idatt.model.common.Player;

import java.util.Scanner;

public class ConsoleUI {
  private final Scanner scanner;
  private final SnakesAndLadders game;

  public ConsoleUI(SnakesAndLadders game) {
    this.scanner = new Scanner(System.in);
    this.game = game;
  }

  public void start() {
    System.out.println("Welcome to Snakes and Ladders!");
    System.out.println("First to reach tile 100 wins!");

    while (!game.isGameOver()) {
      Player currentPlayer = game.getCurrentPlayer();
      System.out.println("\n" + currentPlayer.getName() + "'s turn. Press Enter to roll the dice.");
      scanner.nextLine();

      int roll = game.rollDice();
      System.out.println(currentPlayer.getName() + " rolled a " + roll);

      game.makeMove(currentPlayer);
      System.out.println(currentPlayer.getName() + " moved to tile " + currentPlayer.getPosition());

      for (Player player : game.getPlayers()) {
        System.out.println(player.getName() + ": " + player.getPosition());
      }
    }

    Player winner = game.getWinner();
    if (winner != null) {
      System.out.println("\nCongratulations, " + winner.getName() + " wins!");
    } else {
      System.out.println("\nGame over with no winner!");
    }

    scanner.close();
  }
}
