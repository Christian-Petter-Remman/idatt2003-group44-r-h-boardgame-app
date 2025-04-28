package edu.ntnu.idi.idatt.model.stargame;

import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import edu.ntnu.idi.idatt.model.stargame.StarGame;
import edu.ntnu.idi.idatt.model.stargame.StarPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class
StarGameTUI {
  private StarGame game;
  private Scanner scanner;

  public StarGameTUI() {
    scanner = new Scanner(System.in);
    setupGame();
    startGameLoop();
  }

  private void setupGame() {
    StarBoard board = new StarBoard(130);
    board.addBridge(10, 20);
    board.addTunnel(30, 5);
    board.addPath(40, "RIGHT", 41, 80);
    board.addJail(4);
    board.addStar();

    game = new StarGame(board);
    game.initialize(board);

    List<Player> players = new ArrayList<>();
    players.add(new StarPlayer("Alice", "cow", 1, 0));
    players.add(new StarPlayer("Bob", "pig", 1, 0));

    game.initializePlayer(players);
  }

  private void startGameLoop() {
    while (!game.isGameOver()) {
      System.out.println("\nCurrent Player: " + game.getCurrentPlayer().getName());

      if (game.isAwaitingPathDecision()) {
        handlePathDecision();
      } else {
        System.out.println("Press ENTER to roll the dice...");
        scanner.nextLine();
        game.playTurn();
      }

      for (StarPlayer player : game.getStarPlayers()) {
        System.out.println(player.getName() + " -> Position: " + player.getPosition() + ", Score: " + player.getPoints());
      }
    }

    System.out.println("\n🏆 Winner: " + game.getWinner().getName() + "!");
  }

  private void handlePathDecision() {
    System.out.println("You encountered a path! Do you want to take it? (Y/N): ");
    String input = scanner.nextLine().trim().toUpperCase();
    boolean usePath = input.equals("Y") || input.equals("YES");

    game.continuePathDecision(usePath);
  }

  public static void main(String[] args) {
    new StarGameTUI();
  }
}