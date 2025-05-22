package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.model.common.character_selection.PlayerData;
import edu.ntnu.idi.idatt.model.model_observers.CsvExportObserver;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>StarGameStateExporter</h1>
 *
 * <p>Exports the current StarGame configuration to a CSV file. Used to save initial game setup
 * such
 * as board, dice count, and player information.
 */
public class StarGameStateExporter implements CsvExportObserver {

  private static final Logger logger = LoggerFactory.getLogger(StarGameStateExporter.class);

  private final List<PlayerData> players;
  private final String savePath;

  /**
   * <h2>Constructor.</h2>
   *
   * @param players  List of players to export.
   * @param savePath File path to save the CSV file to.
   */
  public StarGameStateExporter(List<PlayerData> players, String savePath) {
    this.players = players;
    this.savePath = savePath;
  }

  /**
   * <h2>onExportRequested</h2>
   *
   * <p>Called when the observer is triggered to export the game state to CSV. Includes board
   * filename,
   * dice count, current turn, and player data.
   */
  @Override
  public void onExportRequested() {
    try {
      File file = new File(savePath);
      File parentDir = file.getParentFile();
      if (parentDir != null && !parentDir.exists()) {
        boolean created = parentDir.mkdirs();
        if (!created) {
          logger.warn("Could not create directory: {}", parentDir.getAbsolutePath());
        }
      }

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
        writer.write("Board,default.json");
        writer.newLine();
        writer.write("DiceCount,2");
        writer.newLine();
        writer.write("CurrentTurnIndex,0");
        writer.newLine();
        writer.write("Players:");
        writer.newLine();

        players.stream()
            .filter(p -> p.isActive() && p.getSelectedCharacter() != null)
            .forEach(player -> {
              try {
                writer.write(String.format("%s,%s,1,%d",
                    player.getName(),
                    player.getSelectedCharacter().getName(),
                    player.getPoints()));
                writer.newLine();
              } catch (IOException e) {
                logger.error("Error writing player data for {}: {}", player.getName(),
                    e.getMessage());
              }
            });

        logger.info("StarGame state exported to {}", savePath);
      }

    } catch (IOException e) {
      logger.error("Failed to export StarGame state to CSV", e);
    }
  }
}