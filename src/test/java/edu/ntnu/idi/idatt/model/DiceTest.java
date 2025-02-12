     package edu.ntnu.idi.idatt.model;

     import org.junit.jupiter.api.Test;
     import static org.junit.jupiter.api.Assertions.*;

     class DiceTest {

         @Test
         void testRollReturnsValidValue() {
             Dice dice = new Dice(3); // Create a Dice object with 3 dice
             int result = dice.roll();
             assertTrue(result >= 3 && result <= 18, "Roll result is out of range");
         }
     }