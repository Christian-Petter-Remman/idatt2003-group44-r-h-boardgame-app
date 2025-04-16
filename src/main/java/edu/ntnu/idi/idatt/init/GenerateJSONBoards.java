package edu.ntnu.idi.idatt.init;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateJSONBoards {

  private static final Logger logger = LoggerFactory.getLogger(GenerateJSONBoards.class);

  public static void generateJsonBoards() {

    File dir = new File("data/custom_boards/snakes_and_ladders");
    if (!dir.exists()) {
      boolean created = dir.mkdirs();
      if (!created) {
        logger.error("Failed to create directory: {}", dir.getAbsolutePath());
        return;
      }
    }

    // Generate default board
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

    // Generate hard board
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

    // Generate easy board
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

    // Generate random boards
    String randomBoard1 = """
        {
          "size": 100,
          "ladders": [
            {"start": 24, "end": 90},
            {"start": 72, "end": 91},
            {"start": 51, "end": 63},
            {"start": 64, "end": 96},
            {"start": 10, "end": 41},
            {"start": 67, "end": 80}
          ],
          "snakes": [
            {"start": 49, "end": 18},
            {"start": 48, "end": 20},
            {"start": 88, "end": 2},
            {"start": 30, "end": 12},
            {"start": 47, "end": 35},
            {"start": 69, "end": 22}
          ]
        }""";

    String randomBoard2 = """
        {
          "size": 100,
          "ladders": [
            {"start": 29, "end": 43},
            {"start": 50, "end": 94},
            {"start": 3, "end": 31},
            {"start": 34, "end": 62},
            {"start": 49, "end": 57},
            {"start": 61, "end": 83},
            {"start": 28, "end": 88}
          ],
          "snakes": [
            {"start": 26, "end": 12},
            {"start": 75, "end": 19},
            {"start": 55, "end": 23},
            {"start": 17, "end": 9},
            {"start": 60, "end": 2},
            {"start": 65, "end": 22},
            {"start": 13, "end": 7},
            {"start": 35, "end": 21},
            {"start": 59, "end": 42}
          ]
        }""";

    String randomBoard3 = """
        {
          "size": 100,
          "ladders": [
            {"start": 36, "end": 42},
            {"start": 8, "end": 31},
            {"start": 39, "end": 81},
            {"start": 14, "end": 58},
            {"start": 73, "end": 87},
            {"start": 41, "end": 59},
            {"start": 57, "end": 70}
          ],
          "snakes": [
            {"start": 71, "end": 10},
            {"start": 26, "end": 19},
            {"start": 27, "end": 2},
            {"start": 23, "end": 15},
            {"start": 69, "end": 52},
            {"start": 64, "end": 32},
            {"start": 49, "end": 38},
            {"start": 86, "end": 18}
          ]
        }""";

    String randomBoard4 = """
        {
          "size": 100,
          "ladders": [
            {"start": 32, "end": 58},
            {"start": 27, "end": 34},
            {"start": 2, "end": 48},
            {"start": 75, "end": 86},
            {"start": 41, "end": 54},
            {"start": 38, "end": 43},
            {"start": 25, "end": 40},
            {"start": 5, "end": 29}
          ],
          "snakes": [
            {"start": 63, "end": 37},
            {"start": 47, "end": 22},
            {"start": 42, "end": 36},
            {"start": 95, "end": 57},
            {"start": 90, "end": 18},
            {"start": 97, "end": 12}
          ]
        }""";

    String randomBoard5 = """
        {
          "size": 100,
          "ladders": [
            {"start": 42, "end": 87},
            {"start": 32, "end": 62},
            {"start": 74, "end": 95},
            {"start": 6, "end": 34},
            {"start": 53, "end": 66}
          ],
          "snakes": [
            {"start": 58, "end": 39},
            {"start": 92, "end": 31},
            {"start": 55, "end": 3},
            {"start": 91, "end": 15},
            {"start": 17, "end": 2},
            {"start": 73, "end": 64}
          ]
        }""";

    String randomBoard6 = """
        {
          "size": 100,
          "ladders": [
            {"start": 52, "end": 80},
            {"start": 31, "end": 96},
            {"start": 66, "end": 84},
            {"start": 35, "end": 83},
            {"start": 24, "end": 89},
            {"start": 58, "end": 97},
            {"start": 54, "end": 61},
            {"start": 53, "end": 70},
            {"start": 34, "end": 81}
          ],
          "snakes": [
            {"start": 73, "end": 47},
            {"start": 62, "end": 12},
            {"start": 76, "end": 57},
            {"start": 42, "end": 5},
            {"start": 94, "end": 16},
            {"start": 18, "end": 7},
            {"start": 37, "end": 19},
            {"start": 39, "end": 21},
            {"start": 29, "end": 15}
          ]
        }""";

    String randomBoard7 = """
        {
          "size": 100,
          "ladders": [
            {"start": 50, "end": 62},
            {"start": 17, "end": 35},
            {"start": 64, "end": 99},
            {"start": 32, "end": 63},
            {"start": 68, "end": 93},
            {"start": 4, "end": 86},
            {"start": 16, "end": 48},
            {"start": 55, "end": 67},
            {"start": 8, "end": 74}
          ],
          "snakes": [
            {"start": 33, "end": 15},
            {"start": 46, "end": 7},
            {"start": 56, "end": 6},
            {"start": 13, "end": 10},
            {"start": 88, "end": 49},
            {"start": 85, "end": 5}
          ]
        }""";

    String randomBoard8 = """
        {
          "size": 100,
          "ladders": [
            {"start": 72, "end": 83},
            {"start": 12, "end": 21},
            {"start": 30, "end": 54},
            {"start": 33, "end": 84},
            {"start": 6, "end": 88},
            {"start": 59, "end": 63},
            {"start": 49, "end": 75},
            {"start": 10, "end": 39}
          ],
          "snakes": [
            {"start": 89, "end": 44},
            {"start": 96, "end": 70},
            {"start": 29, "end": 20},
            {"start": 91, "end": 22},
            {"start": 51, "end": 8},
            {"start": 61, "end": 48},
            {"start": 37, "end": 28},
            {"start": 62, "end": 16}
          ]
        }""";

    // Map of filenames to board content
    Map<String, String> boards = new HashMap<>();
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

    // Write all boards to files
    for (Map.Entry<String, String> entry : boards.entrySet()) {
      String filename = entry.getKey();
      String content = entry.getValue();

      try (FileWriter writer = new FileWriter(new File(dir, filename))) {
        writer.write(content);
        logger.error("Generated board file: {}", filename);
      } catch (IOException e) {
        logger.error("Error writing board file {}: {}", filename, e.getMessage());
      }
    }

    logger.info("All board files generated successfully.");

  }
}
