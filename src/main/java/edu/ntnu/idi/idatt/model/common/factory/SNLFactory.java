package edu.ntnu.idi.idatt.model.common.factory;

import edu.ntnu.idi.idatt.filehandling.handlers.SNLBoardJsonHandler;
import edu.ntnu.idi.idatt.model.snl.SNLBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * <h1>SNLFactory</h1>
 *
 * Factory class responsible for loading {@link SNLBoard} instances from JSON files
 * using the {@link SNLBoardJsonHandler}.
 */
public class SNLFactory {

  private static final Logger logger = LoggerFactory.getLogger(SNLFactory.class);
  private final SNLBoardJsonHandler boardJsonHandler = new SNLBoardJsonHandler();

  /**
   * <h2>loadBoardFromFile</h2>
   *
   * Loads a Snakes and Ladders board from a given JSON file path.
   *
   * @param fileName The path to the JSON board file.
   * @return A valid {@link SNLBoard} if successful, otherwise {@code null}.
   */
  public SNLBoard loadBoardFromFile(String fileName) {
    File file = new File(fileName);

    if (!file.exists()) {
      logger.debug("SNLBoard file does not exist: {}", fileName);
      return null;
    }

    try {
      SNLBoard board = boardJsonHandler.loadBoardFromFile(fileName);
      logger.info("Successfully loaded board from: {}", fileName);
      return board;
    } catch (Exception e) {
      logger.error("Failed to load board from file: {}", e.getMessage(), e);
      return null;
    }
  }
}