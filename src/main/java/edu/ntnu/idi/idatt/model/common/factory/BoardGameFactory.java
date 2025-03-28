package edu.ntnu.idi.idatt.model.common.factory;

import edu.ntnu.idi.idatt.model.common.game.BoardGame;

public abstract class BoardGameFactory {

  public abstract BoardGame createBoardGame();

  public abstract String[] getAvailableConfigurations();

  public abstract BoardGame createBoardGameFromConfiguration(String configurationName);

  public abstract boolean saveBoardGameConfiguration(BoardGame boardGame, String configurationName);
}
