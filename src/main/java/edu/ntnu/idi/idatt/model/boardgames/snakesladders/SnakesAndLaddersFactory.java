package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import edu.ntnu.idi.idatt.model.common.BoardGameFactory;
import edu.ntnu.idi.idatt.model.common.game.BoardGame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SnakesAndLaddersFactory extends BoardGameFactory {

  private static final String BOARD_DIRECTORY = "data/custom_boards/snakes_and_ladders/";

  @Override
  public BoardGame createBoardGame() {
    SnakesAndLadders game = new SnakesAndLadders();
    // Create set board method
    game.initialize();
    return game;
  }

  @Override
  public String[] getAvailableConfigurations() {
    File directory = new File(BOARD_DIRECTORY);
    List<String> boardFiles = new ArrayList<>();

    boardFiles.add("Default board");
    boardFiles.add("Easy board");
    boardFiles.add("Hard board");

    if (directory.exists() && directory.isDirectory()) {
      File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
      if (files != null) {
        for (File file : files) {
          String configName = file.getName().replace(".json", "");
          if (!boardFiles.contains(configName)) {
            boardFiles.add(configName);
          }
        }
      }
    }
    return boardFiles.toArray(new String[0]);
  }

  @Override
  public BoardGame createBoardGameFromConfiguration(String configurationName) {
    return null;
  }

  @Override
  public boolean saveBoardGameConfiguration(BoardGame boardGame, String configurationName) {
    return false;
  }
}
