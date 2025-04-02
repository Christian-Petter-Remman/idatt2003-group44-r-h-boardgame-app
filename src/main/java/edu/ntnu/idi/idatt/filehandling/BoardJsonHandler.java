package edu.ntnu.idi.idatt.filehandling;

import com.google.gson.*;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;

import java.io.*;

public class BoardJsonHandler implements FileHandler<Board> {
  private final Gson gson;


  public Board loadBoardFromFile(String filePath) throws FileReadException, JsonParsingException {
    try (Reader reader = new FileReader(filePath)) {
      JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
      return parseBoard(jsonObject);
    } catch (FileNotFoundException e) {
      throw new FileReadException("Board file not found: " + filePath, e);
    } catch (IOException e) {
      throw new FileReadException("Error reading board file: " + filePath, e);
    } catch (JsonSyntaxException | IllegalStateException e) {
      throw new JsonParsingException("Invalid JSON structure in board file: " + filePath, e);
    }
  }

  private Board parseBoard(JsonObject jsonObject) {
    return new Board(); // placeholder
  }

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
  public Board loadFromFile(String fileName) throws FileReadException, JsonParsingException {
    try (Reader reader = new FileReader(fileName)) {
      return gson.fromJson(reader, Board.class);
    } catch (FileNotFoundException e) {
      throw new FileReadException("Board file not found: " + fileName, e);
    } catch (IOException e) {
      throw new FileReadException("Error reading board file: " + fileName, e);
    } catch (JsonSyntaxException | IllegalStateException e) {
      throw new JsonParsingException("Invalid JSON in board file: " + fileName, e);
    }
  }
}
