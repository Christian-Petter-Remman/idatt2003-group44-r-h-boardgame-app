package edu.ntnu.idi.idatt.model.common.character_selection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PlayerCsvExporter {
  public static void exportPlayers(List<PlayerData> players, String filePath) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      for (PlayerData player : players) {
        if (player.isActive() && player.getSelectedCharacter() != null) {
          writer.write(player.getId() + "," + player.getSelectedCharacter().getName());
          writer.newLine();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}