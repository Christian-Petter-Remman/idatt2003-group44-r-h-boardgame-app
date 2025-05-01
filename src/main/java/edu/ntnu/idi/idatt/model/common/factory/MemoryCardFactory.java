package edu.ntnu.idi.idatt.model.common.factory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.model.common.memorygame.MemoryCard;
import edu.ntnu.idi.idatt.model.common.memorygame.MemoryGameSettings;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MemoryCardFactory {

  private static final Gson GSON = new Gson();
  private static final String MEMORY_DIR = FileManager.DATA_DIR + "/memorygame";

  public static List<MemoryCard> loadAndShuffle(MemoryGameSettings.BoardSize size) {
    String filePath = MEMORY_DIR + "/memory_" + size.name().toLowerCase() + ".json";
    try {
      String json = Files.readString(Path.of(filePath));
      Type listType = new TypeToken<List<Map<String, String>>>() {
      }.getType();
      List<Map<String, String>> items = GSON.fromJson(json, listType);
      List<MemoryCard> cards = new ArrayList<>();
      for (Map<String, String> item : items) {
        String id = item.get("id");
        String imagePath = item.get("imagePath");
        cards.add(new MemoryCard(id, imagePath));
        cards.add(new MemoryCard(id, imagePath));
      }
      Collections.shuffle(cards);
      return cards;
    } catch (IOException e) {
      throw new RuntimeException("Failed to load memory JSON: " + filePath, e);
    }
  }
}
