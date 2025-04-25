package edu.ntnu.idi.idatt.model.common;

public abstract class BoardGameFactory {



  public abstract String[] getAvailableConfigurations();

  public abstract <T extends BoardGame> T createBoardGameFromConfiguration(String configurationName, Class<T> gameClass);

  public abstract boolean saveBoardGameConfiguration(BoardGame boardGame, String configurationName);
}
