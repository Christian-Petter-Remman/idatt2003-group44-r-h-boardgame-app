package edu.ntnu.idi.idatt.filehandling.handlers;

import com.google.gson.*;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.filehandling.FileHandler;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.filehandling.RandomExclusionStrategy;
import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.stargame.StarBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.function.Function;

/**
 * <h1>StarBoardJsonHandler</h1>
 *
 * Handles loading and saving of StarBoard objects using JSON format.
 * Supports both generic and strongly typed board loading, board file generation, and error handling.
 */
public class StarBoardJsonHandler implements FileHandler<StarBoard> {

  private static final Logger logger = LoggerFactory.getLogger(StarBoardJsonHandler.class);
  private final Gson gson;
  private static final String BOARDS_DIR = FileManager.STAR_GAME_DIR;

  /**
   * <h2>Constructor</h2>
   * Initializes a new Gson instance with exclusion strategies for serialization and deserialization.
   */
  public StarBoardJsonHandler() {
    this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .addSerializationExclusionStrategy(new RandomExclusionStrategy())
            .addDeserializationExclusionStrategy(new RandomExclusionStrategy())
            .create();
  }

  /**
   * <h2>loadBoardFromFile</h2>
   * Loads a StarBoard from the specified JSON file.
   *
   * @param filePath Path to the JSON board file.
   * @return Parsed StarBoard.
   * @throws FileReadException    if file is not found or IO error occurs.
   * @throws JsonParsingException if JSON is malformed.
   */
  public StarBoard loadBoardFromFile(String filePath) throws FileReadException, JsonParsingException {
    logger.info("Loading board from file {}", filePath);
    try (Reader reader = new FileReader(filePath)) {
      JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
      return parseBoard(jsonObject);
    } catch (FileNotFoundException e) {
      throw new FileReadException("StarBoard file not found: " + filePath, e);
    } catch (IOException e) {
      throw new FileReadException("Error reading board file: " + filePath, e);
    } catch (JsonSyntaxException | IllegalStateException e) {
      throw new JsonParsingException("Invalid JSON structure in board file: " + filePath, e);
    }
  }

  /**
   * <h2>parseBoard</h2>
   * Parses a StarBoard object from a JsonObject.
   *
   * @param jsonObject JSON object representing the board.
   * @return StarBoard with attributes applied.
   */
  private StarBoard parseBoard(JsonObject jsonObject) {
    int size = jsonObject.get("size").getAsInt();
    StarBoard board = new StarBoard(size);

    if (jsonObject.has("tunnels")) {
      jsonObject.getAsJsonArray("tunnels").forEach(element -> {
        JsonObject obj = element.getAsJsonObject();
        board.addTunnel(obj.get("start").getAsInt(), obj.get("end").getAsInt());
      });
    }

    if (jsonObject.has("bridge")) {
      jsonObject.getAsJsonArray("bridge").forEach(element -> {
        JsonObject obj = element.getAsJsonObject();
        board.addBridge(obj.get("start").getAsInt(), obj.get("end").getAsInt());
      });
    }

    if (jsonObject.has("paths")) {
      jsonObject.getAsJsonArray("paths").forEach(element -> {
        JsonObject obj = element.getAsJsonObject();
        board.addPath(
                obj.get("start").getAsInt(),
                obj.get("dir").getAsString(),
                obj.get("endDyn").getAsInt(),
                obj.get("endStat").getAsInt()
        );
      });
    }

    if (jsonObject.has("jail")) {
      jsonObject.getAsJsonArray("jail").forEach(element ->
              board.addJail(element.getAsJsonObject().get("start").getAsInt())
      );
    }

    if (jsonObject.has("star")) {
      board.addStar();
    }

    return board;
  }

  /**
   * <h2>loadGameFromFile</h2>
   * Loads a board and creates a game instance using the provided creator function.
   *
   * @param filePath    Path to board file.
   * @param gameCreator Function to create a game instance from board.
   * @return The created game instance.
   * @param <T> Type of the BoardGame.
   * @throws FileReadException    if file access fails.
   * @throws JsonParsingException if parsing fails.
   */
  public <T extends BoardGame> T loadGameFromFile(String filePath, Function<StarBoard, T> gameCreator)
          throws FileReadException, JsonParsingException {
    return gameCreator.apply(loadBoardFromFile(filePath));
  }

  /**
   * <h2>saveToFile</h2>
   * Saves a StarBoard to a JSON file.
   *
   * @param object   Board to save.
   * @param fileName Destination file path.
   * @throws Exception if writing fails.
   */
  @Override
  public void saveToFile(StarBoard object, String fileName) throws Exception {
    try (Writer writer = new FileWriter(fileName)) {
      gson.toJson(object, writer);
      logger.debug("Game saved to file: {}", fileName);
    }
  }

  /**
   * <h2>loadFromFile</h2>
   * Loads a StarBoard from file (deserialization).
   *
   * @param fileName File name to read from.
   * @return Loaded StarBoard.
   * @throws FileReadException    on file error.
   * @throws JsonParsingException on JSON parse error.
   */
  @Override
  public StarBoard loadFromFile(String fileName) throws FileReadException, JsonParsingException {
    try (Reader reader = new FileReader(fileName)) {
      return gson.fromJson(reader, StarBoard.class);
    } catch (FileNotFoundException e) {
      throw new FileReadException("StarBoard file not found: " + fileName, e);
    } catch (IOException e) {
      throw new FileReadException("Error reading board file: " + fileName, e);
    } catch (JsonSyntaxException | IllegalStateException e) {
      throw new JsonParsingException("Invalid JSON in board file: " + fileName, e);
    }
  }
}