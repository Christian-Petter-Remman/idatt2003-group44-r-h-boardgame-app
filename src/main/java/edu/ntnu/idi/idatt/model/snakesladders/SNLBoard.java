package edu.ntnu.idi.idatt.model.snakesladders;

import edu.ntnu.idi.idatt.model.common.AbstractBoard;

import java.util.ArrayList;
import java.util.List;

public class SNLBoard extends AbstractBoard {
  private final List<Ladder> ladders = new ArrayList<>();
  private final List<Snake> snakes = new ArrayList<>();

  public SNLBoard(int size) {
    super(size);
  }

  public void addFullLadder(int start, int end) {
    Ladder ladder = new Ladder(start, end);
    ladders.add(ladder);
    tiles.get(start).addAttribute(ladder);
  }

  public void addSnake(int start, int end) {
    Snake snake = new Snake(start, end);
    snakes.add(snake);
    tiles.get(start).addAttribute(snake);
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