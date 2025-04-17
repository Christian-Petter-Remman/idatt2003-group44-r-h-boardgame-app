package edu.ntnu.idi.idatt.model.common;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLadders;

public abstract class BoardGameFactory {

  public abstract BoardGame createBoardGame();

  public abstract String[] getAvailableConfigurations();

  public abstract <T extends BoardGame> T createBoardGameFromConfiguration(String configurationName, Class<T> gameClass);

  public abstract boolean saveBoardGameConfiguration(BoardGame boardGame, String configurationName);
}
