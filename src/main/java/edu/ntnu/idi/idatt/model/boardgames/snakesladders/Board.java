package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.LadderTile;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.SnakeTile;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Tile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Board {

  private List<Tile> tiles;

  public Board() {
    initializeDefaultTiles();
  }

  private void initializeDefaultTiles() {
    tiles = new ArrayList<>();
    for (int i = 1; i <= 100; i++) {
      tiles.add(new Tile(i));
    }
  }

  public void addDefaultLaddersAndSnakes() {
    addLadders();
    addSnakes();
  }

  private void addLadders() {
    setTile(1, new LadderTile(1, 21));
    setTile(2, new LadderTile(2, 22));
    setTile(3, new LadderTile(3, 23));
    setTile(4, new LadderTile(4, 24));
    setTile(5, new LadderTile(5, 25));
    setTile(6, new LadderTile(6, 16));
  }

  private void addSnakes() {
    setTile(99, new SnakeTile(99, 78));
    setTile(95, new SnakeTile(95, 75));
    setTile(92, new SnakeTile(92, 73));
    setTile(89, new SnakeTile(89, 67));
    setTile(64, new SnakeTile(64, 60));
    setTile(62, new SnakeTile(62, 19));
  }

  public Tile getTile(int number) {
    return tiles.get(number - 1);
  }

  public List<Tile> getTiles() {
    return tiles;
  }

  public void setTiles(List<Tile> tiles) {
    this.tiles = tiles;
  }

  public void setTile(int tileNumber, Tile tile) {
    if (tileNumber < 1 || tileNumber > tiles.size()) {
      throw new IllegalArgumentException("Invalid tile number: " + tileNumber);
    }
    tiles.set(tileNumber - 1, tile);
  }

  public boolean saveToJson(String filePath) {
    try (Writer writer = new FileWriter(filePath)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(this, writer);
      return true;
    } catch (IOException e) {
      System.err.println("Error saving to JSON " + e.getMessage());
      return false;
    }
  }

  public static Board loadFromJson(String filePath) {
    try (Reader reader = new FileReader(filePath)) {
      Gson gson = new GsonBuilder().create();
      return gson.fromJson(reader, Board.class);
    } catch (IOException e) {
      System.err.println("Error loading from JSON: " + e.getMessage());
      return null;
    }
  }
}