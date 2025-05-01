package edu.ntnu.idi.idatt.filehandling;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.DialogConfig;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DialogJsonHandler {
  private static final String DIALOGS_PATH =
      FileManager.DATA_DIR + "/dialogs/dialogs.json";

  public static Map<String, DialogConfig> loadDialogs() {
    try {
      String json = Files.readString(Path.of(DIALOGS_PATH));
      Type listType = new TypeToken<List<DialogConfig>>() {}.getType();
      List<DialogConfig> list = new Gson().fromJson(json, listType);
      Map<String, DialogConfig> map = new HashMap<>();
      for (DialogConfig cfg : list) {
        map.put(cfg.getId(), cfg);
      }
      return map;
    } catch (IOException e) {
      throw new RuntimeException("Failed to SNLLoad dialogs.json", e);
    }
  }
}
