package edu.ntnu.idi.idatt.filehandling.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.ntnu.idi.idatt.filehandling.FileHandler;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.model.memorygame.MemoryCard;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h1>MemoryJsonHandler</h1>
 * Handles loading of memory card configurations from JSON files. This class implements
 * {@link FileHandler} for {@link MemoryCard} objects. Saving functionality is currently not
 * supported.
 *
 * @author Oliver, Christian
 */
public class MemoryJsonHandler implements FileHandler<List<MemoryCard>> {

  private static final Gson GSON = new Gson();
  private static final String MEMORY_DIR = FileManager.MEMORYGAME_DIR;

  /**
   * <h2>loadFromFile</h2>
   * Loads memory cards from a JSON file and duplicates each card for the matching pair.
   * AI: used as helper
   *
   * @param fileName the name of the file to load from
   * @return a list of memory cards with pairs for each entry
   * @throws IOException if the file cannot be read
   */
  @Override
  public List<MemoryCard> loadFromFile(String fileName) throws IOException {
    Path path = Path.of(MEMORY_DIR, fileName);
    String json = Files.readString(path);

    Type listType = new TypeToken<List<Map<String, String>>>() {
    }.getType();
    List<Map<String, String>> entries = GSON.fromJson(json, listType);

    return entries.stream()
        .flatMap(entry -> Stream.of(
            new MemoryCard(entry.get("id"), entry.get("imagePath")),
            new MemoryCard(entry.get("id"), entry.get("imagePath"))
        ))
        .collect(Collectors.toList());
  }
}