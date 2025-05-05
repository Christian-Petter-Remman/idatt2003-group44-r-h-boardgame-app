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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StarBoardJsonHandler implements FileHandler<StarBoard> {
  
  private static final Logger logger = LoggerFactory.getLogger(StarBoardJsonHandler.class);
  private final Gson gson;
  private static final String BOARDS_DIR = FileManager.STAR_GAME_DIR;

  public StarBoardJsonHandler() {
    this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .addSerializationExclusionStrategy(new RandomExclusionStrategy())
            .addDeserializationExclusionStrategy(new RandomExclusionStrategy())
            .create();
  }

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


  private StarBoard parseBoard(JsonObject jsonObject) {
    int size = jsonObject.get("size").getAsInt();
    StarBoard board = new StarBoard(size);

    if (jsonObject.has("tunnels")) {
      JsonArray tunnelArray = jsonObject.getAsJsonArray("tunnels");
      for (JsonElement element : tunnelArray) {
        JsonObject obj = element.getAsJsonObject();
        int start = obj.get("start").getAsInt();
        int end = obj.get("end").getAsInt();
        board.addTunnel(start, end);
      }
    }
    if (jsonObject.has("bridge")) {
      JsonArray bridgeArray = jsonObject.getAsJsonArray("bridge");
      for (JsonElement element : bridgeArray) {
        JsonObject obj = element.getAsJsonObject();
        int start = obj.get("start").getAsInt();
        int end = obj.get("end").getAsInt();
        board.addBridge(start, end);
      }
    }

    if (jsonObject.has("paths")) {
      JsonArray pathsArray = jsonObject.getAsJsonArray("paths");
      for (JsonElement element : pathsArray) {
        JsonObject obj = element.getAsJsonObject();
        int start = obj.get("start").getAsInt();
        String direction = obj.get("dir").getAsString();
        int endDynamic = obj.get("endDyn").getAsInt();
        int endStatic = obj.get("endStat").getAsInt();
        board.addPath(start, direction, endDynamic, endStatic);
      }
    }

    if (jsonObject.has("jail")) {
      JsonArray pathArray = jsonObject.getAsJsonArray("jail");
      for (JsonElement element : pathArray) {
        JsonObject obj = element.getAsJsonObject();
        int start = obj.get("start").getAsInt();
        board.addJail(start);
      }
    }

    if (jsonObject.has("star")){
        board.addStar();
      }
    return board;
  }

  public <T extends BoardGame> T loadGameFromFile(String filePath, Function<StarBoard, T> gameCreator)
          throws FileReadException, JsonParsingException {
    StarBoard board = loadBoardFromFile(filePath);
    return gameCreator.apply(board);
  }

  @Override
  public void saveToFile(StarBoard object, String fileName) throws Exception {
    try (Writer writer = new FileWriter(fileName)) {
      gson.toJson(object, writer);
      logger.debug("Game saved to file: {}", fileName);
    }
  }

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

  public void generateBoardFiles() throws IOException {
    logger.info("Starting board files generation check");

    Path dirPath = Paths.get(BOARDS_DIR);

    try {
      if (!Files.exists(dirPath)) {
        logger.error("Boards directory does not exist: {}", BOARDS_DIR);
        throw new IOException("Boards directory does not exist: " + BOARDS_DIR);
      }

      if (!Files.isDirectory(dirPath)) {
        logger.error("Boards path exists but is not a directory: {}", BOARDS_DIR);
        throw new IOException("Boards path exists but is not a directory: " + BOARDS_DIR);
      }

      if (!Files.isWritable(dirPath)) {
        logger.error("Boards directory is not writable: {}", BOARDS_DIR);
        throw new IOException("Boards directory is not writable: " + BOARDS_DIR);
      }
    } catch (SecurityException e) {
      logger.error("Security exception when accessing boards directory: {}", e.getMessage());
      throw new IOException("Security exception when accessing boards directory", e);
    }

    Path defaultBoardPath = dirPath.resolve("default.json");
    if (Files.exists(defaultBoardPath)) {
      logger.info("Default board file already exists, checking if all board files are present");

      boolean allBoardsExist = true;
      String[] requiredBoards = {
              "default.json", "easy.json", "hard.json",
      };

      for (String boardName : requiredBoards) {
        if (!Files.exists(dirPath.resolve(boardName))) {
          logger.warn("StarBoard file missing: {}", boardName);
          allBoardsExist = false;
          break;
        }
      }

      if (allBoardsExist) {
        logger.info("All board files already exist, skipping generation");
        return;
      } else {
        logger.info("Some board files are missing, will generate all boards");
      }
    }

    logger.info("Generating Snakes and Ladders board files...");

    Map<String, String> boards = createBoardsMap();

    int existingFiles = 0;
    int createdFiles = 0;
    int failedFiles = 0;

    for (Map.Entry<String, String> entry : boards.entrySet()) {
      String filename = entry.getKey();
      String content = entry.getValue();

      Path boardFilePath = dirPath.resolve(filename);

      try {
        if (Files.exists(boardFilePath)) {
          logger.debug("StarBoard file already exists, skipping: {}", filename);
          existingFiles++;
          continue;
        }

        try (FileWriter writer = new FileWriter(boardFilePath.toFile())) {
          writer.write(content);
        }

        if (!Files.exists(boardFilePath) || Files.size(boardFilePath) == 0) {
          logger.error("Failed to create board file or file is empty: {}", filename);
          failedFiles++;
          continue;
        }

        logger.info("Successfully generated board file: {}", filename);
        createdFiles++;
      } catch (IOException e) {
        logger.error("Error writing board file {}: {}", filename, e.getMessage());
        failedFiles++;
      } catch (SecurityException e) {
        logger.error("Security exception when writing board file {}: {}", filename, e.getMessage());
        failedFiles++;
      }
    }

    if (failedFiles > 0) {
      logger.warn("StarBoard file generation completed with issues: {} existing, {} created, {} failed",
              existingFiles, createdFiles, failedFiles);
      throw new IOException("Failed to generate " + failedFiles + " board files");
    } else if (createdFiles > 0) {
      logger.info("StarBoard file generation completed successfully: {} existing, {} created",
              existingFiles, createdFiles);
    } else {
      logger.info("All board files already exist. No new files were created.");
    }
  }

  private Map<String, String> createBoardsMap() {
    Map<String, String> boards = new HashMap<>();

    String defaultBoard = """
        {
          "size": 130,
        }""";

    String hardBoard = """
        {
          "size": 130,
    
        }""";

    String easyBoard = """
        {
          "size": 130,
         
        }""";


    boards.put("default.json", defaultBoard);
    boards.put("easy.json", easyBoard);
    boards.put("hard.json", hardBoard);

    return boards;
  }
}
