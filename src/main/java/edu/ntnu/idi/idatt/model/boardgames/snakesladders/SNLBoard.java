package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Ladder;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Snake;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SNLBoard {
  private static final Logger logger = LoggerFactory.getLogger(SNLBoard.class);
  private List<Tile> tiles;
  private final int size;
  private final List<Ladder> ladders = new ArrayList<>();
  private final List<Snake> snakes = new ArrayList<>();

  public SNLBoard() {
    this(100);
  }

  public SNLBoard(int size) {
    this.size = size;
    initializeEmptyBoard();
  }

  public List<Tile> getTiles() {
    return new ArrayList<>(tiles);
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
  }

  public void addSnake(int start, int end) {
    if (start <= end || start > size || end < 1) {
      throw new IllegalArgumentException("Invalid snake positions");
    }
    Snake snake = new Snake(start, end);
    snakes.add(snake);
  }

  public Tile getTile(int number) {
    if (number < 1 || number > size) {
      logger.warn("Attempted to get invalid tile number: {}", number);
      throw new IllegalArgumentException("Invalid tile number: " + number);
    }
    return tiles.get(number - 1);
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
}
