package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.LadderTile;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.SnakeTile;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Tile;
import edu.ntnu.idi.idatt.observers.ModelObserver;
import edu.ntnu.idi.idatt.observers.ObservableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Board implements ObservableModel {
  private static final Logger logger = LoggerFactory.getLogger(Board.class);
  private List<Tile> tiles;
  private final int size;
  private final List<Ladder> ladders = new ArrayList<>();
  private final List<Snake> snakes = new ArrayList<>();
  private final List<ModelObserver> observers = new ArrayList<>();

  public Board() {
    this(100);
  }

  public Board(int size) {
    this.size = size;
    initializeEmptyBoard();
  }

  public void initializeEmptyBoard() {
    tiles = new ArrayList<>();
    for (int i = 1; i <= size; i++) {
      tiles.add(new Tile(i));
    }
    ladders.clear();
    snakes.clear();
    logger.debug("Initialized empty board with {} tiles", size);
  }

  public void addFullLadder(int start, int end) {
    if (start >= end || start < 1 || end > size) {
      throw new IllegalArgumentException("Invalid ladder positions");
    }
    Ladder ladder = new Ladder(start, end);
    ladders.add(ladder);
    setTile(start, new LadderTile(start, end));
    notifyObservers("LADDER_ADDED", ladder);
  }

  public void addSnake(int start, int end) {
    if (start <= end || start > size || end < 1) {
      throw new IllegalArgumentException("Invalid snake positions");
    }
    Snake snake = new Snake(start, end);
    snakes.add(snake);
    setTile(start, new SnakeTile(start, end));
    notifyObservers("SNAKE_ADDED", snake);
  }

  public void addDefaultLaddersAndSnakes() {
    addDefaultLadders();
    addDefaultSnakes();
  }

  public void addDefaultLadders() {
    addFullLadder(1, 38);
    addFullLadder(4, 14);
    addFullLadder(9, 31);
    addFullLadder(21, 42);
    addFullLadder(28, 84);
    addFullLadder(51, 67);
    addFullLadder(72, 91);
    addFullLadder(80, 99);
    logger.info("Added default ladders to the board");
  }

  public void addDefaultSnakes() {
    addSnake(17, 7);
    addSnake(54, 34);
    addSnake(62, 19);
    addSnake(64, 60);
    addSnake(87, 36);
    addSnake(93, 73);
    addSnake(95, 75);
    addSnake(99, 78);
    logger.info("Added default snakes to the board");
  }

  public Tile getTile(int number) {
    if (number < 1 || number > size) {
      logger.warn("Attempted to get invalid tile number: {}", number);
      throw new IllegalArgumentException("Invalid tile number: " + number);
    }
    return tiles.get(number - 1);
  }

  public List<Tile> getTiles() {
    return new ArrayList<>(tiles);
  }

  public void setTile(int tileNumber, Tile tile) {
    if (tileNumber < 1 || tileNumber > size) {
      throw new IllegalArgumentException("Invalid tile number: " + tileNumber);
    }
    tiles.set(tileNumber - 1, tile);
  }

  public int getFinalPosition(int position) {
    if (position < 1 || position > size) {
      return position;
    }

    Tile tile = getTile(position);
    if (tile.hasSnakeOrLadder()) {
      logger.debug("Player landed on special tile at {}, moving to {}", position, tile.getDestination());
      return tile.getDestination();
    }
    return position;
  }

  public boolean saveToJson(String filePath) {
    try (Writer writer = new FileWriter(filePath)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(this, writer);
      logger.info("Successfully saved board to JSON: {}", filePath);
      return true;
    } catch (IOException e) {
      logger.error("Error saving board to JSON: {}", e.getMessage());
      return false;
    }
  }

  public int getSize() {
    return size;
  }

  public List<Ladder> getLadders() {
    return new ArrayList<>(ladders);
  }

  public List<Snake> getSnakes() {
    return new ArrayList<>(snakes);
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observers.add(observer);
  }

  @Override
  public void notifyObservers(String eventType, Object data) {
    new ArrayList<>(observers).forEach(obs -> obs.update(eventType, data));
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observers.remove(observer);
  }
}
