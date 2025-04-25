package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.model.snakesladders.SnakesAndLaddersPlayer;
import edu.ntnu.idi.idatt.model.common.Player;
import edu.ntnu.idi.idatt.model.stargame.StarPlayer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerCsvHandler implements FileHandler<List<Player>> {

  @Override
  public void saveToFile(List<Player> players, String fileName) throws IOException {
    String directory = "data/user-data/player-files/";
    new File(directory).mkdirs();

    String fullPath = fileName;

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
      for (Player player : players) {
        writer.write(player.getName() + "," + player.getCharacter()+","+player.getStartPosition());
        writer.newLine();
      }
    }
  }

  public void saveStarPlayerToFile(List<StarPlayer> players, String fileName) throws IOException {
    String directory = "data/user-data/star-players/";
    new File(directory).mkdirs();

    String fullPath = fileName;

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
      for (StarPlayer player : players) {
        writer.write(player.getName() + "," + player.getCharacter()+","+player.getStartPosition()+","+player.getScore());
        writer.newLine();
      }
    }
  }

  @Override
  public List<Player> loadFromFile(String fileName) throws FileReadException, CsvFormatException {
    List<Player> players = new ArrayList<>();
    String basePath = "data/user-data/player-files/";
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      int lineNumber = 0;

      while ((line = reader.readLine()) != null) {
        lineNumber++;
        String[] parts = line.split(",");
        if (parts.length < 2 || parts[0].isBlank()) {
          throw new CsvFormatException("Invalid format on line " + lineNumber + ": " + line);
        }

        String name = parts[0].trim();
        String character = parts[1].trim();
        int position = Integer.parseInt(parts[2].trim());
        players.add(new SnakesAndLaddersPlayer(name, character, position));
      }
    } catch (FileNotFoundException e) {
      throw new FileReadException("Player CSV file not found: " + fileName, e);
    } catch (IOException e) {
      throw new FileReadException("Error reading player CSV file: " + fileName, e);
    }

    return players;
  }
}