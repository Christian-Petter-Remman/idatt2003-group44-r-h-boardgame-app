package edu.ntnu.idi.idatt.filehandling.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.filehandling.FileHandler;
import edu.ntnu.idi.idatt.filehandling.RandomExclusionStrategy;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * SNLBoardJsonHandler
 *
 * <p>Handles JSON-based loading and saving of Snakes and Ladders boards. Supports parsing of JSON
 * files to construct {@link SNLBoard} objects, as well as creating default board files
 * programmatically.
 *
 * <P>AI: active involvement as sparring partner and created underlying frame for development.
 * </P>
 */
public class SNLBoardJsonHandler implements FileHandler<SNLBoard> {

  private static final Logger logger = LoggerFactory.getLogger(SNLBoardJsonHandler.class);
  private final Gson gson;

  /**
   * Constructor
   *
   * <p>Initializes the JSON handler with pretty-printing and custom exclusion strategy.
   */
  public SNLBoardJsonHandler() {
    this.gson = new GsonBuilder()
        .setPrettyPrinting()
        .addSerializationExclusionStrategy(new RandomExclusionStrategy())
        .addDeserializationExclusionStrategy(new RandomExclusionStrategy())
        .create();
  }

  /**
   * loadBoardFromFile
   *
   * <p>Loads a Snakes and Ladders board from a JSON file.
   *
   * @param filePath the path to the JSON file
   * @return the parsed {@link SNLBoard} object
   * @throws FileReadException    if reading the file fails
   * @throws JsonParsingException if JSON parsing fails
   */
  public SNLBoard loadBoardFromFile(String filePath)
      throws FileReadException, JsonParsingException {
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
   * parseBoard
   *
   * <p>Parses JSON into an {@link SNLBoard}, including ladders and snakes.
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
   * loadFromFile
   *
   * <p>Loads a {@link SNLBoard} from JSON using Gson.
   *
   * @param fileName the file path
   * @return parsed board
   * @throws FileReadException    if reading fails
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
}