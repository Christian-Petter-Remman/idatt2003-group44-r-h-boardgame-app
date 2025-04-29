package edu.ntnu.idi.idatt.util;

import edu.ntnu.idi.idatt.filehandling.FileManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BoardSelectionUtil {

  public static List<String> getAvailableBoardNames() {
    File boardsDir = new File(FileManager.SNAKES_LADDERS_BOARDS_DIR);
    List<String> boardNames = new ArrayList<>();
    if (boardsDir.exists() && boardsDir.isDirectory()) {
      for (File file : Objects.requireNonNull(
          boardsDir.listFiles((dir, name) -> name.endsWith(".json")))) {
        String name = file.getName().replace(".json", "");
        if (name.startsWith("random")) {
          boardNames.add("Random " + name.substring(6));
        } else {
          boardNames.add(capitalize(name));
        }
      }
    }
    return boardNames;
  }

  private static String capitalize(String str) {
    if (str == null || str.isEmpty()) return str;
    return str.substring(0,1).toUpperCase() + str.substring(1);
  }

  public static String getFileNameForBoard(String displayName) {
    if (displayName.startsWith("Random ")) {
      return "random" + displayName.substring(7) + ".json";
    }
    return displayName.toLowerCase() + ".json";
  }
}

