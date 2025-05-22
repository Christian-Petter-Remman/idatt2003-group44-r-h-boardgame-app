package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.model.common.characterselection.PlayerData;
import edu.ntnu.idi.idatt.model.modelobservers.CsvExportObserver;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h1>SNLGameStateCsvExporter</h1>
 * Handles the export of the current Snakes and Ladders game setup to a CSV file. Used to persist
 * the initial game state (board, dice, players) before starting.
 */
public class SNLGameStateCsvExporter implements CsvExportObserver {

  private static final Logger logger = LoggerFactory.getLogger(SNLGameStateCsvExporter.class);

  private final SNLRuleSelectionModel model;
  private final List<PlayerData> players;
  private final String savePath;

  /**
   * <h2>Constructor</h2>
   * Initializes the exporter with model, players and save path.
   *
   * @param model    the rule selection model holding game configuration.
   * @param players  list of player data to be saved.
   * @param savePath full file path for the CSV output.
   */
  public SNLGameStateCsvExporter(SNLRuleSelectionModel model, List<PlayerData> players,
      String savePath) {
    this.model = model;
    this.players = players;
    this.savePath = savePath;
  }

  /**
   * <h2>onExportRequested</h2>
   * Triggers the export of game state to CSV when notified by the observer pattern. Writes board
   * file, dice count, and player information to the output file.
   */
  @Override
  public void onExportRequested() {
    try {
      File file = new File(savePath);
      File parentDir = file.getParentFile();
      if (parentDir != null && !parentDir.exists()) {
        boolean created = parentDir.mkdirs();
        if (!created) {
          logger.warn("Could not create directory for save path: {}", parentDir.getAbsolutePath());
        }
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

        players.stream()
            .filter(p -> p.isActive() && p.getSelectedCharacter() != null)
            .forEach(player -> {
              try {
                writer.write(String.format("%s,%s,1",
                    player.getName(),
                    player.getSelectedCharacter().getName()));
                writer.newLine();
              } catch (IOException e) {
                logger.error("Failed to write player data for: {}", player.getName(), e);
              }
            });

        logger.info("Game state exported to {}", savePath);
      }

    } catch (IOException e) {
      logger.error("Failed to export game state to CSV: {}", savePath, e);
    }
  }
}