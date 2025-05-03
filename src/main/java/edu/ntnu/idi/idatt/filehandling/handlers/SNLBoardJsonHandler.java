package edu.ntnu.idi.idatt.filehandling.handlers;

import com.google.gson.*;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.filehandling.FileHandler;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.filehandling.RandomExclusionStrategy;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import edu.ntnu.idi.idatt.model.common.BoardGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class SNLBoardJsonHandler implements FileHandler<SNLBoard> {
  private static final Logger logger = LoggerFactory.getLogger(SNLBoardJsonHandler.class);
  private final Gson gson;

  private static final String BOARDS_DIR = FileManager.SNAKES_LADDERS_BOARDS_DIR;

  public SNLBoardJsonHandler() {
    this.gson = new GsonBuilder()
            .setPrettyPrinting()
            .addSerializationExclusionStrategy(new RandomExclusionStrategy())
            .addDeserializationExclusionStrategy(new RandomExclusionStrategy())
            .create();
  }

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


  private SNLBoard parseBoard(JsonObject jsonObject) {
    int size = jsonObject.get("size").getAsInt();
    SNLBoard board = new SNLBoard(size);

    if (jsonObject.has("ladders")) {
      JsonArray laddersArray = jsonObject.getAsJsonArray("ladders");
      for (JsonElement element : laddersArray) {
        JsonObject obj = element.getAsJsonObject();
        int start = obj.get("start").getAsInt();
        int end = obj.get("end").getAsInt();
        board.addLadder(start, end);
      }
    }
    if (jsonObject.has("snakes")) {
      JsonArray snakesArray = jsonObject.getAsJsonArray("snakes");
      for (JsonElement element : snakesArray) {
        JsonObject obj = element.getAsJsonObject();
        int start = obj.get("start").getAsInt();
        int end = obj.get("end").getAsInt();
        board.addSnake(start, end);
      }
    }

    return board;
  }

  public <T extends BoardGame> T loadGameFromFile(String filePath, Function<SNLBoard, T> gameCreator)
          throws FileReadException, JsonParsingException {
    SNLBoard board = loadBoardFromFile(filePath);
    return gameCreator.apply(board);
  }

  public <T extends BoardGame> void saveToFile(T game, String fileName) throws IOException {
    try (Writer writer = new FileWriter(fileName)) {
      gson.toJson(game, writer);
      logger.debug("Game saved to file: {}", fileName);
    }
  }

  @Override
  public void saveToFile(SNLBoard object, String fileName) throws Exception {

  }

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
          "random1.json", "random2.json", "random3.json", "random4.json",
          "random5.json", "random6.json", "random7.json", "random8.json"
      };

      for (String boardName : requiredBoards) {
        if (!Files.exists(dirPath.resolve(boardName))) {
          logger.warn("SNLBoard file missing: {}", boardName);
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
          logger.debug("SNLBoard file already exists, skipping: {}", filename);
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
      logger.warn("SNLBoard file generation completed with issues: {} existing, {} created, {} failed",
          existingFiles, createdFiles, failedFiles);
      throw new IOException("Failed to generate " + failedFiles + " board files");
    } else if (createdFiles > 0) {
      logger.info("SNLBoard file generation completed successfully: {} existing, {} created",
          existingFiles, createdFiles);
    } else {
      logger.info("All board files already exist. No new files were created.");
    }
  }

  private Map<String, String> createBoardsMap() {
    Map<String, String> boards = new HashMap<>();

    String defaultBoard = """
        {
          "size": 100,
          "ladders": [
            {"start": 1, "end": 38},
            {"start": 4, "end": 14},
            {"start": 9, "end": 31},
            {"start": 21, "end": 42},
            {"start": 28, "end": 84},
            {"start": 51, "end": 67},
            {"start": 71, "end": 91},
            {"start": 80, "end": 100}
          ],
          "snakes": [
            {"start": 16, "end": 6},
            {"start": 47, "end": 26},
            {"start": 49, "end": 11},
            {"start": 56, "end": 53},
            {"start": 62, "end": 19},
            {"start": 64, "end": 60},
            {"start": 87, "end": 24},
            {"start": 93, "end": 73}
          ]
        }""";

    String hardBoard = """
        {
          "size": 100,
          "ladders": [
            {"start": 4, "end": 14},
            {"start": 21, "end": 42},
            {"start": 28, "end": 84},
            {"start": 51, "end": 67},
            {"start": 80, "end": 100}
          ],
          "snakes": [
            {"start": 16, "end": 6},
            {"start": 33, "end": 3},
            {"start": 47, "end": 26},
            {"start": 49, "end": 11},
            {"start": 56, "end": 27},
            {"start": 62, "end": 19},
            {"start": 87, "end": 24},
            {"start": 93, "end": 73},
            {"start": 95, "end": 75},
            {"start": 99, "end": 29}
          ]
        }""";

    String easyBoard = """
        {
          "size": 100,
          "ladders": [
            {"start": 4, "end": 14},
            {"start": 9, "end": 31},
            {"start": 20, "end": 38},
            {"start": 28, "end": 84},
            {"start": 40, "end": 59},
            {"start": 51, "end": 67},
            {"start": 63, "end": 81},
            {"start": 71, "end": 91},
            {"start": 80, "end": 100},
            {"start": 8, "end": 30}
          ],
          "snakes": [
            {"start": 17, "end": 7},
            {"start": 54, "end": 34},
            {"start": 62, "end": 19},
            {"start": 98, "end": 79}
          ]
        }""";

    String randomBoard1 = """
        {
          "size": 100,
          "ladders": [
              {"start": 3, "end": 51},
              {"start": 15, "end": 67},
              {"start": 29, "end": 74},
              {"start": 43, "end": 92},
              {"start": 64, "end": 85},
              {"start": 71, "end": 91}
              ],
              "snakes": [
              {"start": 99, "end": 41},
              {"start": 87, "end": 36},
              {"start": 75, "end": 28},
              {"start": 56, "end": 8},
              {"start": 48, "end": 16},
              {"start": 25, "end": 2}
              ]
              };
        """;

    String randomBoard2 = """
        {
          "size": 100,
          "ladders": [
            {"start": 4, "end": 68},
            {"start": 12, "end": 50},
            {"start": 21, "end": 82},
            {"start": 35, "end": 57},
            {"start": 45, "end": 86},
            {"start": 71, "end": 94},
            {"start": 77, "end": 98}
          ],
          "snakes": [
            {"start": 99, "end": 38},
            {"start": 89, "end": 53},
            {"start": 76, "end": 24},
            {"start": 66, "end": 47},
            {"start": 54, "end": 19},
            {"start": 43, "end": 17},
            {"start": 32, "end": 6}
          ]
        }""";

    String randomBoard3 = """
        {
          "size": 100,
          "ladders": [
            {"start": 8, "end": 26},
            {"start": 19, "end": 59},
            {"start": 34, "end": 73},
            {"start": 42, "end": 78},
            {"start": 51, "end": 97},
            {"start": 62, "end": 83},
            {"start": 75, "end": 92}
          ],
          "snakes": [
            {"start": 98, "end": 79},
            {"start": 95, "end": 56},
            {"start": 84, "end": 63},
            {"start": 69, "end": 31},
            {"start": 58, "end": 37},
            {"start": 47, "end": 26},
            {"start": 36, "end": 17},
            {"start": 24, "end": 5}
          ]
        }""";

    String randomBoard4 = """
        {
          "size": 100,
          "ladders": [
            {"start": 7, "end": 29},
            {"start": 16, "end": 47},
            {"start": 22, "end": 56},
            {"start": 38, "end": 83},
            {"start": 52, "end": 71},
            {"start": 61, "end": 95},
            {"start": 73, "end": 91},
            {"start": 85, "end": 98}
          ],
          "snakes": [
            {"start": 99, "end": 63},
            {"start": 93, "end": 42},
            {"start": 87, "end": 51},
            {"start": 78, "end": 39},
            {"start": 64, "end": 27},
            {"start": 49, "end": 13}
          ]
        }""";

    String randomBoard5 = """
        {
          "size": 100,
          "ladders": [
            {"start": 5, "end": 23},
            {"start": 14, "end": 76},
            {"start": 31, "end": 67},
            {"start": 45, "end": 84},
            {"start": 57, "end": 96}
          ],
          "snakes": [
            {"start": 97, "end": 65},
            {"start": 88, "end": 49},
            {"start": 79, "end": 41},
            {"start": 62, "end": 18},
            {"start": 53, "end": 11},
            {"start": 33, "end": 9}
          ]
        }""";

    String randomBoard6 = """
        {
          "size": 100,
          "ladders": [
            {"start": 3, "end": 21},
            {"start": 12, "end": 42},
            {"start": 28, "end": 84},
            {"start": 39, "end": 59},
            {"start": 47, "end": 69},
            {"start": 56, "end": 77},
            {"start": 68, "end": 93},
            {"start": 79, "end": 99}
          ],
          "snakes": [
            {"start": 98, "end": 82},
            {"start": 92, "end": 75},
            {"start": 86, "end": 54},
            {"start": 73, "end": 46},
            {"start": 64, "end": 37},
            {"start": 53, "end": 26},
            {"start": 44, "end": 15},
            {"start": 35, "end": 9},
            {"start": 23, "end": 4}
          ]
        }""";

    String randomBoard7 = """
        {
          "size": 100,
          "ladders": [
            {"start": 2, "end": 38},
            {"start": 11, "end": 52},
            {"start": 25, "end": 63},
            {"start": 37, "end": 78},
            {"start": 49, "end": 89},
            {"start": 67, "end": 96},
            {"start": 76, "end": 94},
            {"start": 81, "end": 97}
          ],
          "snakes": [
            {"start": 99, "end": 80},
            {"start": 91, "end": 72},
            {"start": 83, "end": 64},
            {"start": 74, "end": 53},
            {"start": 62, "end": 43},
            {"start": 51, "end": 32}
          ]
        }""";

    String randomBoard8 = """
        {
          "size": 100,
          "ladders": [
            {"start": 6, "end": 24},
            {"start": 13, "end": 46},
            {"start": 26, "end": 53},
            {"start": 33, "end": 71},
            {"start": 41, "end": 79},
            {"start": 58, "end": 85},
            {"start": 69, "end": 87},
            {"start": 72, "end": 93}
          ],
          "snakes": [
            {"start": 98, "end": 61},
            {"start": 95, "end": 57},
            {"start": 89, "end": 44},
            {"start": 82, "end": 39},
            {"start": 65, "end": 27},
            {"start": 54, "end": 18},
            {"start": 36, "end": 8},
            {"start": 23, "end": 5}
          ]
        }""";

    boards.put("default.json", defaultBoard);
    boards.put("easy.json", easyBoard);
    boards.put("hard.json", hardBoard);
    boards.put("random1.json", randomBoard1);
    boards.put("random2.json", randomBoard2);
    boards.put("random3.json", randomBoard3);
    boards.put("random4.json", randomBoard4);
    boards.put("random5.json", randomBoard5);
    boards.put("random6.json", randomBoard6);
    boards.put("random7.json", randomBoard7);
    boards.put("random8.json", randomBoard8);

    return boards;
  }
}
