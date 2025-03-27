package edu.ntnu.idi.idatt.filehandling;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class BoardJsonHandler implements FileHandler<Board> {
  private final Gson gson;

  public BoardJsonHandler() {
    this.gson = new GsonBuilder().setPrettyPrinting().create();
  }

  @Override
  public void saveToFile(Board board, String fileName) throws IOException {
    try (Writer writer = new FileWriter(fileName)) {
      gson.toJson(board, writer);
    }
  }

  @Override
  public Board loadFromFile(String fileName) throws IOException {
    try (Reader reader = new FileReader(fileName)) {
      return gson.fromJson(reader, Board.class);
    }
  }
}
