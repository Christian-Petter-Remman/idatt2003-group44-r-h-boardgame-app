package edu.ntnu.idi.idatt.filehandling.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.ntnu.idi.idatt.filehandling.FileHandler;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.model.common.memorygame.MemoryCard;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemoryJsonHandler implements FileHandler<List<MemoryCard>> {
  private static final Gson GSON = new Gson();
  private static final String MEMORY_DIR = FileManager.DATA_DIR + "/memorygame";

  @Override
  public void saveToFile(List<MemoryCard> cards, String fileName) {
    throw new UnsupportedOperationException("Saving memory boards not supported");
  }

  @Override
  public List<MemoryCard> loadFromFile(String fileName) throws IOException {
    String path = MEMORY_DIR + "/" + fileName;
    String json = Files.readString(Path.of(path));
    Type listType = new TypeToken<List<Map<String, String>>>(){}.getType();
    List<Map<String, String>> entries = GSON.fromJson(json, listType);
    List<MemoryCard> cards = new ArrayList<>();
    for (Map<String, String> e : entries) {
      String id = e.get("id");
      String imagePath = e.get("imagePath");
      cards.add(new MemoryCard(id, imagePath));
      cards.add(new MemoryCard(id, imagePath));
    }
    return cards;
  }
}

