package edu.ntnu.idi.idatt.model.boardgames.snakesladders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.LadderTile;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.SnakeTile;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.tile.Tile;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Board {

  private List<Tile> tiles;

  public Board() {
    initializeBoard();
  }

  public Board(boolean initialize) {
    if (initialize) {
      initializeBoard();
    } else {
      tiles = new ArrayList<>();
    }
  }

  private void initializeBoard() {
    tiles = new ArrayList<>();
    for (int i = 1; i <= 100; i++) {
      tiles.add(new Tile(i));
    }

    addLadders();
    addSnakes();
  }

  private void addLadders() {
    setSpecialTile(new LadderTile(1, 21));
    setSpecialTile(new LadderTile(2, 22));
    setSpecialTile(new LadderTile(3, 23));
    setSpecialTile(new LadderTile(4, 24));
    setSpecialTile(new LadderTile(5, 25));
    setSpecialTile(new LadderTile(6, 16));
  }

  private void addSnakes() {
    setSpecialTile(new SnakeTile(99, 78));
    setSpecialTile(new SnakeTile(95, 75));
    setSpecialTile(new SnakeTile(92, 73));
    setSpecialTile(new SnakeTile(89, 67));
    setSpecialTile(new SnakeTile(64, 60));
    setSpecialTile(new SnakeTile(62, 19));
  }

  private void setSpecialTile(Tile tile) {
    tiles.set(tile.getNumberOfTile() - 1, tile);
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

