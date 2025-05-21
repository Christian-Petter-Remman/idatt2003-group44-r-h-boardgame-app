package edu.ntnu.idi.idatt.filehandling.handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.ntnu.idi.idatt.filehandling.FileManager;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.DialogConfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <h1>DialogJsonHandler</h1>
 * Utility class responsible for loading dialog configurations from a JSON file into a map.
 * Each dialog is uniquely identified by an ID.
 */
public class DialogJsonHandler {

  private static final String DIALOGS_PATH = FileManager.DATA_DIR + "/dialogs/dialogs.json";

  /**
   * <h2>loadDialogs</h2>
   * Loads a list of {@link DialogConfig} objects from the JSON file located at {@code DIALOGS_PATH}
   * and maps them by their unique ID.
   *
   * @return a map of dialog configurations keyed by ID
   * @throws RuntimeException if reading or parsing the file fails
   */
  public static Map<String, DialogConfig> loadDialogs() {
    try {
      String json = Files.readString(Path.of(DIALOGS_PATH));
      Type listType = new TypeToken<List<DialogConfig>>() {}.getType();
      List<DialogConfig> dialogList = new Gson().fromJson(json, listType);

      return dialogList.stream()
              .collect(Collectors.toMap(DialogConfig::getId, cfg -> cfg));

    } catch (IOException e) {
      throw new RuntimeException("Failed to load dialogs.json", e);
    }
  }
}