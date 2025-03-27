package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersPlayer;
import edu.ntnu.idi.idatt.model.common.player.Player;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerCsvHandler implements FileHandler<List<Player>> {

  @Override
  public void saveToFile(List<Player> players, String fileName) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
      for (Player player : players) {
        writer.write(player.getName() + ",Default");
        writer.newLine();
      }
    }
  }

  @Override
  public List<Player> loadFromFile(String fileName) throws IOException {
    List<Player> players = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts.length >= 2) {
          String name = parts[0];
          Player player = new SnakesAndLaddersPlayer(name);
          players.add(player);
        }
      }
    }
    return players;
  }
}