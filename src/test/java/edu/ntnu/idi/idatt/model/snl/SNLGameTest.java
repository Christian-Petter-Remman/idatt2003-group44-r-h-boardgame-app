//package edu.ntnu.idi.idatt.model.snl;
//
//import edu.ntnu.idi.idatt.model.common.Player;
//import edu.ntnu.idi.idatt.testutil.FixedDice;
//import org.junit.jupiter.api.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SNLGameTest {
//
//  SNLGame game;
//
//  @BeforeEach
////  void setUp() {
////    game = new SNLGame();
////    game.addPlayer("A", "toad",0);
////    game.addPlayer("B", "peach",0);
////    game.initialize(new SNLBoard();
////  }
//
//  @Test
//  void testAddPlayers() {
//    assertEquals(2, game.getPlayers().size());
//    assertEquals("A", game.getPlayers().get(0).getName());
//  }
//
//  @Test
//  void testGetCurrentPlayer() {
//    Player current = game.getCurrentPlayer();
//    assertEquals("A", current.getName());
//  }
//
//  @Test
//  void testMakeMoveChangesPlayerTurn() {
//    Player first = game.getCurrentPlayer();
//    Player second = game.getCurrentPlayer();
//    assertNotEquals(first, second, "Player turn should change after move");
//  }
//
//
//}