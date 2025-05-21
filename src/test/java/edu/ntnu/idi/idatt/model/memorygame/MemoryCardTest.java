package edu.ntnu.idi.idatt.model.memorygame;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryCardTest {

  @Test
  void testConstructorAndGetters() {
    MemoryCard card = new MemoryCard("cat", "cat.png");
    assertEquals("cat", card.getId());
    assertEquals("cat.png", card.getImagePath());
    assertFalse(card.isFaceUp());
    assertFalse(card.isMatched());
    assertEquals(-1, card.getMatchedBy());
  }

  @Test
  void testSetFaceUp() {
    MemoryCard card = new MemoryCard("dog", "dog.png");
    assertFalse(card.isFaceUp());
    card.setFaceUp(true);
    assertTrue(card.isFaceUp());
    card.setFaceUp(false);
    assertFalse(card.isFaceUp());
  }

  @Test
  void testSetMatchedAndMatchedBy() {
    MemoryCard card = new MemoryCard("duck", "duck.png");
    assertFalse(card.isMatched());
    assertEquals(-1, card.getMatchedBy());
    card.setMatched(1);
    assertTrue(card.isMatched());
    assertEquals(1, card.getMatchedBy());
  }

  @Test
  void testEqualsAndHashCode() {
    MemoryCard card1 = new MemoryCard("cat", "cat.png");
    MemoryCard card2 = new MemoryCard("cat", "weird_cat.png");
    MemoryCard card3 = new MemoryCard("dog", "dog.png");
    assertEquals(card1, card2);
    assertEquals(card1.hashCode(), card2.hashCode());
    assertNotEquals(card1, card3);
  }

  @Test
  void testEqualsItself() {
    MemoryCard card = new MemoryCard("cat", "cat.png");
    assertEquals(card, card);
  }

  @Test
  void testEqualsNullAndOtherClass() {
    MemoryCard card = new MemoryCard("cat", "cat.png");
    assertNotEquals(null, card);
  }
}
