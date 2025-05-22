package edu.ntnu.idi.idatt.filehandling.handlers;

import com.google.gson.*;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.filehandling.FileHandler;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.filehandling.RandomExclusionStrategy;
import edu.ntnu.idi.idatt.model.common.BoardGame;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * # SNLBoardJsonHandler
 *
 * Handles JSON-based loading and saving of Snakes and Ladders boards.
 * Supports parsing of JSON files to construct {@link SNLBoard} objects,
 * as well as creating default board files programmatically.
 */
public class SNLBoardJsonHandler implements FileHandler<SNLBoard> {

  private static final Logger logger = LoggerFactory.getLogger(SNLBoardJsonHandler.class);
  private static final String BOARDS_DIR = FileManager.SNAKES_LADDERS_BOARDS_DIR;
  private final Gson gson;

  /**
   * ## Constructor
   *
   * Initializes the JSON handler with pretty-printing and custom exclusion strategy.
   */
  public SNLBoardJsonHandler() {
    this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .addSerializationExclusionStrategy(new RandomExclusionStrategy())
            .addDeserializationExclusionStrategy(new RandomExclusionStrategy())
            .create();
  }

  /**
   * ## loadBoardFromFile
   *
   * Loads a Snakes and Ladders board from a JSON file.
   *
   * @param filePath the path to the JSON file
   * @return the parsed {@link SNLBoard} object
   * @throws FileReadException if reading the file fails
   * @throws JsonParsingException if JSON parsing fails
   */
  public SNLBoard loadBoardFromFile(String filePath) throws FileReadException, JsonParsingException {
    try (Reader reader = new FileReader(filePath)) {
      JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
      return parseBoard(jsonObject);
    } catch (FileNotFoundException e) {
      throw new FileReadException("SNLBoard file not found: " + filePath, e);
    } catch (IOException e) {
      throw new FileReadException("Error reading board file: " + filePath, e);
    } catch (JsonSyntaxException | IllegalStateException e) {
      throw new JsonParsingException("Invalid JSON structure in board file: " + filePath, e);
    }
  }

  /**
   * ## parseBoard
   *
   * Parses JSON into an {@link SNLBoard}, including ladders and snakes.
   *
   * @param jsonObject the JSON content of the board
   * @return an initialized {@link SNLBoard}
   */
  private SNLBoard parseBoard(JsonObject jsonObject) {
    int size = jsonObject.get("size").getAsInt();
    SNLBoard board = new SNLBoard(size);

    if (jsonObject.has("ladders")) {
      jsonObject.getAsJsonArray("ladders").forEach(e -> {
        JsonObject obj = e.getAsJsonObject();
        board.addLadder(obj.get("start").getAsInt(), obj.get("end").getAsInt());
      });
    }

    if (jsonObject.has("snakes")) {
      jsonObject.getAsJsonArray("snakes").forEach(e -> {
        JsonObject obj = e.getAsJsonObject();
        board.addSnake(obj.get("start").getAsInt(), obj.get("end").getAsInt());
      });
    }

    return board;
  }

  /**
   * ## loadGameFromFile
   *
   * Loads a complete game using the board file and a game creator function.
   *
   * @param filePath the JSON file to load
   * @param gameCreator a function that creates a BoardGame from the board
   * @param <T> the game type
   * @return a constructed game object
   * @throws FileReadException if file reading fails
   * @throws JsonParsingException if JSON parsing fails
   */
  public <T extends BoardGame> T loadGameFromFile(String filePath, Function<SNLBoard, T> gameCreator)
          throws FileReadException, JsonParsingException {
    return gameCreator.apply(loadBoardFromFile(filePath));
  }

  /**
   * ## saveToFile (generic)
   *
   * Saves a generic BoardGame to JSON.
   *
   * @param game the board game to save
   * @param fileName the file path
   * @param <T> the board game type
   * @throws IOException if writing fails
   */
  public <T extends BoardGame> void saveToFile(T game, String fileName) throws IOException {
    try (Writer writer = new FileWriter(fileName)) {
      gson.toJson(game, writer);
      logger.debug("Game saved to file: {}", fileName);
    }
  }

  /**
   * ## saveToFile (SNLBoard)
   *
   * Not implemented — override from {@link FileHandler}.
   *
   * @param object the board object
   * @param fileName destination file
   */
  @Override
  public void saveToFile(SNLBoard object, String fileName) {
    // Intentionally unimplemented
  }

  /**
   * ## loadFromFile
   *
   * Loads a {@link SNLBoard} from JSON using Gson.
   *
   * @param fileName the file path
   * @return parsed board
   * @throws FileReadException if reading fails
   * @throws JsonParsingException if parsing fails
   */
  @Override
  public SNLBoard loadFromFile(String fileName) throws FileReadException, JsonParsingException {
    try (Reader reader = new FileReader(fileName)) {
      logger.info("Loading board from file: {}", fileName);
      return gson.fromJson(reader, SNLBoard.class);
    } catch (FileNotFoundException e) {
      throw new FileReadException("SNLBoard file not found: " + fileName, e);
    } catch (IOException e) {
      throw new FileReadException("Error reading board file: " + fileName, e);
    } catch (JsonSyntaxException | IllegalStateException e) {
      throw new JsonParsingException("Invalid JSON in board file: " + fileName, e);
    }
  }

  /**
   * ## generateBoardFiles
   *
   * Validates board directory and generates all default and random board files.
   *
   * @throws IOException if generation fails or directory is invalid
   */
  public void generateBoardFiles() throws IOException {
    logger.info("Starting board files generation check");

    Path dirPath = Paths.get(BOARDS_DIR);
    if (!Files.exists(dirPath) || !Files.isDirectory(dirPath) || !Files.isWritable(dirPath)) {
      throw new IOException("Invalid or inaccessible board directory: " + BOARDS_DIR);
    }

    String[] requiredBoards = {
            "default.json", "easy.json", "hard.json",
            "random1.json", "random2.json", "random3.json", "random4.json",
            "random5.json", "random6.json", "random7.json", "random8.json"
    };

    boolean missingFiles = false;
    for (String file : requiredBoards) {
      if (!Files.exists(dirPath.resolve(file))) {
        missingFiles = true;
        break;
      }
    }

    if (!missingFiles) {
      logger.info("All board files already exist. No generation needed.");
      return;
    }

    Map<String, String> boards = createBoardsMap();
    for (Map.Entry<String, String> entry : boards.entrySet()) {
      Path path = dirPath.resolve(entry.getKey());
      if (!Files.exists(path)) {
        try (Writer writer = new FileWriter(path.toFile())) {
          writer.write(entry.getValue());
          logger.info("Generated board file: {}", entry.getKey());
        } catch (IOException e) {
          logger.error("Failed to write board file {}: {}", entry.getKey(), e.getMessage());
          throw new IOException("Failed to create: " + entry.getKey(), e);
        }
      }
    }
  }

  /**
   * ## createBoardsMap
   *
   * Constructs a map of filenames to board configurations in JSON format.
   *
   * @return map of filenames and board JSON strings
   */
  private Map<String, String> createBoardsMap() {
    Map<String, String> boards = new HashMap<>();
    boards.put("default.json", generateDefaultBoard());
    boards.put("easy.json", generateEasyBoard());
    boards.put("hard.json", generateHardBoard());
    return boards;
  }

  /**
   * ## generateDefaultBoard
   *
   * @return default board JSON content
   */
  private String generateDefaultBoard() {
    return """
      {
        "size": 100,
        "ladders": [{"start": 1, "end": 38}],
        "snakes": [{"start": 16, "end": 6}]
      }""";
  }

  /**
   * ## generateEasyBoard
   *
   * @return easy board JSON content
   */
  private String generateEasyBoard() {
    return """
      {
        "size": 100,
        "ladders": [{"start": 4, "end": 14}],
        "snakes": [{"start": 17, "end": 7}]
      }""";
  }

  /**
   * ## generateHardBoard
   *
   * @return hard board JSON content
   */
  private String generateHardBoard() {
    return """
      {
        "size": 100,
        "ladders": [{"start": 28, "end": 84}],
        "snakes": [{"start": 99, "end": 29}]
      }""";
  }
}