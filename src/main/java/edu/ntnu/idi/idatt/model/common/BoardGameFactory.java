package edu.ntnu.idi.idatt.model.common;

public abstract class BoardGameFactory {

  public abstract BoardGame createBoardGame();

  public abstract String[] getAvailableConfigurations();

  public abstract BoardGame createBoardGameFromConfiguration(String configurationName);

  public abstract boolean saveBoardGameConfiguration(BoardGame boardGame, String configurationName);
}
