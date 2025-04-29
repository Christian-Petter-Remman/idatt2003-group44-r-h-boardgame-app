package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SNLGameTUI {
  private SNLGame game;
  private final Scanner scanner;

  public SNLGameTUI() {
    scanner = new Scanner(System.in);
    setupGame();
    startGameLoop();
  }

  private void setupGame() {
    SNLBoard board = new SNLBoard(100);
    board.addSnake(21,8);
    board.addLadder(5,14);
    board.addLadder(6,14);
    board.addLadder(7,14);
    board.addLadder(8,14);
    board.addLadder(9,14);

    game = new SNLGame(board);
    game.initialize(board);

    List<Player> players = new ArrayList<>();
    players.add(new SNLPlayer("Alice", "cow", 1 ));
    players.add(new SNLPlayer("Bob", "pig", 1));

    game.initializePlayer(players);
  }

  private void startGameLoop() {
    boolean gameRunning = true;

    while (gameRunning) {
      System.out.println("\nCurrent Player: " + game.getCurrentPlayer().getName());
      System.out.println("Press ENTER to roll the dice...");
      scanner.nextLine();
      game.playTurn();

      for (Player player : game.getPlayers()) {
        System.out.println(player.getName() + " -> Position: " + player.getPosition());

        if (((SNLPlayer) player).hasWon()) {
          System.out.println("\n🏆 " + player.getName() + " has won the game!");
          gameRunning = false;
          break;
        }
      }
    }
  }

  public static void main(String[] args) {
    new SNLGameTUI();
  }
}

