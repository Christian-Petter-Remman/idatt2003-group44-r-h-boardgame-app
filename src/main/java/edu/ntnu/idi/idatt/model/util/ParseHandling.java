package edu.ntnu.idi.idatt.model.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.idi.idatt.model.snakesladders.SNLBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class ParseHandling {
  private static final Logger logger = LoggerFactory.getLogger(SNLBoard.class);

  public boolean saveToJson(String filePath) {
    try (Writer writer = new FileWriter(filePath)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(this, writer);
      logger.info("Successfully saved board to JSON: {}", filePath);
      return true;
    } catch (IOException e) {
      logger.error("Error saving board to JSON: {}", e.getMessage());
      return false;
    }
  }
}
