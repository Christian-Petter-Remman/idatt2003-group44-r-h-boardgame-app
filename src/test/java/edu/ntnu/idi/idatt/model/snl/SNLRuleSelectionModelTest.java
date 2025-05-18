package edu.ntnu.idi.idatt.model.snl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import static org.junit.jupiter.api.Assertions.*;


class SNLRuleSelectionModelTest {

  private SNLRuleSelectionModel model;

  @BeforeEach
  void setUp() {
    // Temporarily redirect the file loading location for test isolation
    new File("src/test/resources/boards").mkdirs();
    model = new SNLRuleSelectionModel();
  }

  @Test
  void testDiceCountBoundaries() {
    model.setDiceCount(0);
    assertEquals(1, model.getDiceCount());

    model.setDiceCount(3);
    assertEquals(2, model.getDiceCount());

    model.setDiceCount(1);
    assertEquals(1, model.getDiceCount());
  }

  @Test
  void testSetAndGetSelectedBoardFile() {
    model.setSelectedBoardFile("easy.json");
    assertEquals("easy.json", model.getSelectedBoardFile());
  }

  @Test
  void testSetAndGetSavePath() {
    model.setSavePath("saves/test.csv");
    assertEquals("saves/test.csv", model.getSavePath());
  }


  @Test
  void testGetDisplayNameForStandardFiles() {
    assertEquals("Default", SNLRuleSelectionModel.getDisplayName("default.json"));
    assertEquals("Easy", SNLRuleSelectionModel.getDisplayName("easy.json"));
    assertEquals("Hard", SNLRuleSelectionModel.getDisplayName("hard.json"));
    assertEquals("Random 12345", SNLRuleSelectionModel.getDisplayName("random_12345.json"));
    assertEquals("customboard", SNLRuleSelectionModel.getDisplayName("customboard.json"));
    assertEquals("", SNLRuleSelectionModel.getDisplayName(null));
  }

}