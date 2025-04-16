package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.LadderTile;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.SnakeTile;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Tile;
import java.io.*;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Board {
  private static final Logger logger = LoggerFactory.getLogger(Board.class);
  private static final int MAX_LADDERS = 15;
  private static final int MAX_SNAKES = 15;
  private static final int MAX_ATTEMPTS_PER_PLACEMENT = 50;

  private List<Tile> tiles;
  private final int size;
  private final List<Ladder> ladders = new ArrayList<>();
  private final List<Snake> snakes = new ArrayList<>();

  public Board() {
    this(100);
  }

  public Board(int size) {
    this.size = size;
    initializeEmptyBoard();
  }

  public void initializeEmptyBoard() {
    tiles = new ArrayList<>();
    for (int i = 1; i <= size; i++) tiles.add(new Tile(i));
    ladders.clear();
    snakes.clear();
  }

  public void addFullLadder(int start, int end) {
    if (isValidLadderPlacement(start, end)) {
      ladders.add(new Ladder(start, end));
      setTile(start, new LadderTile(start, end));
    }
  }

  public void addSnake(int start, int end) {
    if (isValidSnakePlacement(start, end)) {
      snakes.add(new Snake(start, end));
      setTile(start, new SnakeTile(start, end));
    }
  }

  public void addRandomLadders(int count) {
    int added = 0;
    for (int i = 0; i < count && ladders.size() < MAX_LADDERS; i++) {
      if (tryAddRandomLadder()) added++;
    }
    logger.info("Added {} of {} requested ladders", added, count);
  }

  public void addRandomSnakes(int count) {
    int added = 0;
    for (int i = 0; i < count && snakes.size() < MAX_SNAKES; i++) {
      if (tryAddRandomSnake()) added++;
    }
    logger.info("Added {} of {} requested snakes", added, count);
  }

  private boolean tryAddRandomLadder() {
    Random random = new Random();
    for (int attempt = 0; attempt < MAX_ATTEMPTS_PER_PLACEMENT; attempt++) {
      int start = random.nextInt(size - 1) + 1;
      int startRow = getRow(start);
      int end = findValidLadderEnd(start, startRow, random);
      if (end != -1 && isValidLadderPlacement(start, end)) {
        addFullLadder(start, end);
        return true;
      }
    }
    return false;
  }

  private boolean tryAddRandomSnake() {
    Random random = new Random();
    for (int attempt = 0; attempt < MAX_ATTEMPTS_PER_PLACEMENT; attempt++) {
      int start = random.nextInt(size - 1) + 2;
      int startRow = getRow(start);
      int end = findValidSnakeEnd(start, startRow, random);
      if (end != -1 && isValidSnakePlacement(start, end)) {
        addSnake(start, end);
        return true;
      }
    }
    return false;
  }

  private int findValidLadderEnd(int start, int startRow, Random random) {
    if (startRow <= 0) {
      return -1;
    }
    for (int attempt = 0; attempt < 100; attempt++) {
      int endRow = random.nextInt(startRow);
      int end = randomInRow(endRow, random);
      if (end > start && getRow(end) < startRow) return end;
    }
    return -1;
  }

  private int findValidSnakeEnd(int start, int startRow, Random random) {
    if (startRow >= 9) {
      return -1;
    }
    for (int attempt = 0; attempt < 100; attempt++) {
      int endRow = startRow + 1 + random.nextInt(9 - startRow);
      int end = randomInRow(endRow, random);
      if (end < start && getRow(end) > startRow) return end;
    }
    return -1;
  }

  private int getRow(int position) {
    return (position - 1) / 10;
  }

  private int randomInRow(int row, Random random) {
    int min = row * 10 + 1;
    int max = (row + 1) * 10;
    return min + random.nextInt(max - min + 1);
  }

  private boolean isValidLadderPlacement(int start, int end) {
    return start < end &&
        getRow(start) != getRow(end) &&
        hasExistingConnection(start, end);
  }

  private boolean isValidSnakePlacement(int start, int end) {
    return start > end &&
        getRow(start) != getRow(end) &&
        hasExistingConnection(start, end);
  }

  private boolean hasExistingConnection(int start, int end) {
    return ladders.stream().noneMatch(l -> l.start() == start || l.end() == end) &&
        snakes.stream().noneMatch(s -> s.start() == start || s.end() == end);
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

  public void addDefaultLaddersAndSnakes() {
    addDefaultLadders();
    addDefaultSnakes();
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
      logger.debug("Player landed on special tile at {}, moving to {}",
          position, tile.getDestination());
      return tile.getDestination();
    }
    return position;
  }

  public boolean saveToJson(String filePath) {
    try (Writer writer = new FileWriter(filePath)) {
      Gson gson = new GsonBuilder()
          .setPrettyPrinting()
          .create();
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
