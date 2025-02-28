package edu.ntnu.idi.idatt.mainapp;

import edu.ntnu.idi.idatt.model.Board;
import edu.ntnu.idi.idatt.model.Dice;
import edu.ntnu.idi.idatt.model.Player;
import edu.ntnu.idi.idatt.model.Tile.LadderTile;
import edu.ntnu.idi.idatt.model.Tile.SnakeTile;

import java.util.Scanner;

public class Run {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    Player player1 = new Player("Oliver");
    Player player2 = new Player("Christian");
    Dice dice = new Dice(1);
    Board board = new Board();




    System.out.println("Welcome to Snakes and Ladders");
    System.out.println("Players in this game: " + player1.getName() + " and " + player2.getName() + ".");
    System.out.println("Press enter to roll dice");
    boolean gameRunning = true;
    while (gameRunning) {

      System.out.println(player1.getName() + "'s turn");
      scanner.nextLine();
      int roll = dice.roll();
      System.out.println(player1.getName() + " rolled a " + roll);
      player1.move(roll, board);
      if (player1.hasWon()) {
        System.out.println("Congratulations, " + player1.getName() + " wins!");
      }

      System.out.println(player2.getName() + "'s turn");
      scanner.nextLine();
      roll = dice.roll();
      System.out.println(player2.getName() + " rolled a " + roll);
      player2.move(roll, board);
      if (player2.hasWon()) {
        System.out.println("Congratulations, " + player2.getName() + " wins!");
      }

      System.out.println(player1.getName() + ": " + player1.getPosition() + "/ " + player2.getName() + " : " + player2.getPosition());


    }
    scanner.close();
  }

}
