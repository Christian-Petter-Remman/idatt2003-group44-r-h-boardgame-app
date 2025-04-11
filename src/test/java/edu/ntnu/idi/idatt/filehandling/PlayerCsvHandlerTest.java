package edu.ntnu.idi.idatt.filehandling;

import edu.ntnu.idi.idatt.exceptions.CsvFormatException;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.SnakesAndLaddersPlayer;
import edu.ntnu.idi.idatt.model.common.Player;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerCsvHandlerTest {

  private static final String TEST_FILE = "test_players.csv";
  private PlayerCsvHandler handler;

  @BeforeEach
  void setUp() {
    handler = new PlayerCsvHandler();
  }

  @AfterEach
  void tearDown() {
    new File(TEST_FILE).delete();
  }

  @Test
  void testSaveAndLoadPlayers() throws IOException, FileReadException, CsvFormatException {
    List<Player> players = new ArrayList<>();
    players.add(new SnakesAndLaddersPlayer("Anna", "bowser"));
    players.add(new SnakesAndLaddersPlayer("Bob", "toad"));

    handler.saveToFile(players, TEST_FILE);
    List<Player> loaded = handler.loadFromFile(TEST_FILE);

    assertEquals(2, loaded.size());
    assertEquals("Anna", loaded.get(0).getName());
    assertEquals("Bob", loaded.get(1).getName());
  }

  @Test
  void testLoadFromInvalidFile() {
    assertThrows(FileReadException.class, () -> {
      handler.loadFromFile("non_existent_file.csv");
    });
  }

  @Test
  void testLoadFromMalformedCsv() throws IOException {
    try (FileWriter writer = new FileWriter(TEST_FILE)) {
      writer.write("only_one_column\n");
    }

    assertThrows(CsvFormatException.class, () -> {
      handler.loadFromFile(TEST_FILE);
    });
  }

  @Test
  void testLoadPlayersFromResourceFile() throws Exception {
    // Last inn filbanen fra resources
    String path = getClass().getClassLoader()
            .getResource("PlayerFiles/test_players.csv")
            .toURI()
            .getPath();

    List<Player> players = handler.loadFromFile(path);

    assertEquals(2, players.size(), "Should load 2 players from CSV");
    assertEquals("Anna", players.get(0).getName());
    assertEquals("Bob", players.get(1).getName());
  }
}