package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import edu.ntnu.idi.idatt.model.model_observers.CsvExportObserver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GameStateCsvExporter implements CsvExportObserver {

  private final SNLRuleSelectionModel model;
  private final List<PlayerData> players;
  private final String savePath;

  public GameStateCsvExporter(SNLRuleSelectionModel model, List<PlayerData> players, String savePath) {
    this.model = model;
    this.players = players;
    this.savePath = savePath;
  }

  @Override
  public void onExportRequested() {
    try {
      File file = new File(savePath);
      File parentDir = file.getParentFile();
      if (parentDir != null && !parentDir.exists()) {
        parentDir.mkdirs();
      }

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        writer.write("Board," + model.getSelectedBoardFile());
        writer.newLine();
        writer.write("DiceCount," + model.getDiceCount());
        writer.newLine();
        writer.write("CurrentTurnIndex,0");
        writer.newLine();
        writer.write("Players:");
        writer.newLine();

        for (PlayerData player : players) {
          if (player.isActive() && player.getSelectedCharacter() != null) {
            writer.write(player.getName() + "," +
                    player.getSelectedCharacter().getName() + "," +
                    1); // Start pos
            writer.newLine();
          }
        }
      }

    } catch (IOException e) {
      System.err.println("Failed to export game state: " + e.getMessage());
    }
  }
}