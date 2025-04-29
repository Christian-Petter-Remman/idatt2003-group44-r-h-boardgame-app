package edu.ntnu.idi.idatt.filehandling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaveFileNameGenerator {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

  /**
   * Generates a unique filename for saving game states.
   * @return a filename like snl_save_20250429_171532.csv
   */
  public static String generateSaveFileName() {
    String timestamp = LocalDateTime.now().format(formatter);
    return "snl_save_" + timestamp + ".csv";
  }
}