package edu.ntnu.idi.idatt.filehandling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <h1>SaveFileNameGenerator</h1>
 * Utility class responsible for generating unique, timestamped filenames for saving game state
 * files for both Snakes and Ladders and StarGame.
 */
public class SaveFileNameGenerator {

  /**
   * <h2>formatter</h2>
   * Formatter used to convert the current date and time into a filename-friendly format. Pattern:
   * yyyyMMdd_HHmmss
   */
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

  /**
   * <h2>snlGenerateSaveFileName</h2>
   * Generates a unique filename for a Snakes and Ladders game save.
   *
   * @return A string filename such as "snl_save_20250429_171532.csv".
   */
  public static String snlGenerateSaveFileName() {
    String timestamp = LocalDateTime.now().format(formatter);
    return "snl_save_" + timestamp + ".csv";
  }

  /**
   * <h2>starGenerateSaveFileName</h2>
   * Generates a unique filename for a StarGame save.
   *
   * @return A string filename such as "star_save_20250429_171532.csv".
   */
  public static String starGenerateSaveFileName() {
    String timestamp = LocalDateTime.now().format(formatter);
    return "star_save_" + timestamp + ".csv";
  }
}