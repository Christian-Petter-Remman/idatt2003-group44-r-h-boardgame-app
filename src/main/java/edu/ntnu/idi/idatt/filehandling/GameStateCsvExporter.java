package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionManager;
import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import edu.ntnu.idi.idatt.model.model_observers.CsvExportObserver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GameStateCsvExporter implements CsvExportObserver {

  private final String boardFile;
  private final int diceCount;
  private final List<PlayerData> players;
  private final String savePath;

  public GameStateCsvExporter(String boardFile, int diceCount, List<PlayerData> players, String savePath) {
    this.boardFile = boardFile;
    this.diceCount = diceCount;
    this.players = players;
    this.savePath = savePath;
  }

  @Override
  public void onExportRequested() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(savePath))) {
      writer.write("Board," + boardFile);
      writer.newLine();
      writer.write("DiceCount," + diceCount);
      writer.newLine();
      writer.write("CurrentTurnIndex,0");
      writer.newLine();
      writer.write("Players:");
      writer.newLine();
      for (PlayerData player : players) {
        if (player.isActive() && player.getSelectedCharacter() != null) {
          writer.write(player.getId() + "," +
                  player.getSelectedCharacter().getName() + "," +
                  1); // Start position = 1
          writer.newLine();
        }
      }
    } catch (IOException e) {
      System.err.println("Failed to export game state: " + e.getMessage());
    }
  }
}