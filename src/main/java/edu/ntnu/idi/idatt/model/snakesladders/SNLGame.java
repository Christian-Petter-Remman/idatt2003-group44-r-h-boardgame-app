package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.common.Dice;
import edu.ntnu.idi.idatt.model.common.Player;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SNLGame extends BoardGame {

  private static final Logger logger = LoggerFactory.getLogger(SNLGame.class.getName());

  public SNLGame(SNLBoard board) {
    super(board);
    logger.info("StarGame created with board size {}", board.getSize());
  }

  public void initialize(SNLBoard board) {
    this.board = board;
    this.dice = new Dice(2);
    this.currentPlayerIndex = 0;
    logger.info("Game initialized with board and dice");
  }

  public void initializePlayer(List<Player> players) {
    super.initializePlayer(players);
    logger.info("{} players initialized", players.size());
  }

  public void playTurn() {
    Player currentPlayer = getCurrentPlayer();
    int roll = dice.roll();
    currentPlayer.move(roll, board);
    notifyMoveObservers(currentPlayer, roll);
    nextTurn();
  }

}