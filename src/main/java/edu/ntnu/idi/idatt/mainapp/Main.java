package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Dice;

import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Initialize game elements
    Board board = new Board();
    Player player1 = new Player("Player 1");
    Player player2 = new Player("Player 2");
    Dice dice = new Dice(1);

    System.out.println("Welcome to Snakes and Ladders!");
    System.out.println("First to reach tile 100 wins!");

    boolean gameRunning = true;

    // Main game loop
    while (gameRunning) {
      // Player 1's turn
      System.out.println("\n" + player1.getName() + "'s turn. Press Enter to roll the dice.");
      scanner.nextLine();
      int roll = dice.roll();
      System.out.println(player1.getName() + " rolled a " + roll);
      player1.move(roll, board);
      if (player1.hasWon()) {
        System.out.println("\nCongratulations, " + player1.getName() + " wins!");
        break;
      }

      // Player 2's turn
      System.out.println("\n" + player2.getName() + "'s turn. Press Enter to roll the dice.");
      scanner.nextLine();
      roll = dice.roll();
      System.out.println(player2.getName() + " rolled a " + roll);
      player2.move(roll, board);
      if (player2.hasWon()) {
        System.out.println("\nCongratulations, " + player2.getName() + " wins!");
        break;
      }
    }

    scanner.close();
  }
}