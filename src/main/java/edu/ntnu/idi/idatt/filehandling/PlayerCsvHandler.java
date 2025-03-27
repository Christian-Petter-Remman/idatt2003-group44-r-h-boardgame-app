package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersPlayer;
import edu.ntnu.idi.idatt.model.common.player.Player;

import java.io.*;
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
  public List<Player> loadFromFile(String fileName) throws FileReadException, CsvFormatException {
    List<Player> players = new ArrayList<>();

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
        // parts[1] = piece type (ignored for now, hardcoded as "Default")
        players.add(new SnakesAndLaddersPlayer(name));
      }
    } catch (FileNotFoundException e) {
      throw new FileReadException("Player CSV file not found: " + fileName, e);
    } catch (IOException e) {
      throw new FileReadException("Error reading player CSV file: " + fileName, e);
    }

    return players;
  }
}