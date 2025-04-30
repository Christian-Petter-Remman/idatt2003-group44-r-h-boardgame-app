package edu.ntnu.idi.idatt.model.snl;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;
import edu.ntnu.idi.idatt.model.common.factory.SNLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SNLBoard extends AbstractBoard {

  private static final Logger logger = LoggerFactory.getLogger(SNLBoard.class);

  private final List<Ladder> ladders = new ArrayList<>();
  private final List<Snake> snakes = new ArrayList<>();
  SNLFactory snlFactory = new SNLFactory();
  String filename;

  public SNLBoard(int size) {
    super(size);
  }


  public void addLadder(int start, int end) {
    Ladder ladder = new Ladder(start, end);
    ladders.add(ladder);
    tiles.get(start).addAttribute(ladder);
    logger.info("Added Ladder from {} to {} ", start, end);
  }

  public void addSnake(int start, int end) {
    Snake snake = new Snake(start, end);
    snakes.add(snake);
    tiles.get(start).addAttribute(snake);
    logger.info("Added Snake from {} to {} ", start, end);
  }

  public Integer getLadderEnd(int tileNumber) {
    for (Ladder ladder : ladders) {
      if (ladder.getStart() == tileNumber) {
        return ladder.getEnd();
      }
    }
    return null;
  }

  public Integer getSnakeEnd(int tileNumber) {
    for (Snake snake : snakes) {
      if (snake.getStart() == tileNumber) {
        return snake.getEnd();
      }
    }
    return null;
  }

  public SNLBoard initializeBoardFromFile(String initFilename) {
    SNLBoard board = snlFactory.loadBoardFromFile(initFilename);
    logger.info("Board initialized from {} ", initFilename);
    return board;
  }


  public List<Ladder> getLadders() {
    return new ArrayList<>(ladders);
  }

  public List<Snake> getSnakes() {
    return new ArrayList<>(snakes);
  }

  @Override
  public int getSize() {
    return tiles.size();
  }
}