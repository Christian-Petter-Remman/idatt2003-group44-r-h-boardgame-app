package edu.ntnu.idi.idatt.view.star;

public class BoardCreator {

  /**
   * <h1>BoardCreator</h1>
   *
   * Utility class for mapping tile transitions in the Star game.
   * Given an input tile number, it returns a predefined output tile number
   * based on board logic or redirects (e.g., ladders, warps, penalties).
   */
  public static int StarGameCreator(int imputTile){
    int outputTile = 0;

    if(imputTile == 0){
      outputTile = 1;
    }
    if(imputTile == 1){
      outputTile = 2;
    }
    if(imputTile == 2){
      outputTile = 3;
    }
    if(imputTile == 3){
      outputTile = 4;
    }
    if(imputTile == 4){
      outputTile = 5;
    }
    if(imputTile == 5){
      outputTile = 6;
    }
    if(imputTile == 6){
      outputTile = 0;
    }
    if(imputTile == 7){
      outputTile = 0;
    }
    if(imputTile == 8){
      outputTile = 0;
    }
    if(imputTile >= 9){
      outputTile = 0;
    }
    if(imputTile == 10){
      outputTile = 0;
    }
    if(imputTile == 11){
      outputTile = 0;
    }
    if(imputTile == 12){
      outputTile = 74;
    }
    if(imputTile == 13){
      outputTile = 73;
    }
    if(imputTile == 14){
      outputTile = 72;
    }
    if(imputTile == 15){
      outputTile = 71;
    }
    if(imputTile == 16){
      outputTile = 70;
    }
    if(imputTile == 17){
      outputTile = 69;
    }
    if(imputTile == 18){
      outputTile = 68;
    }
    if(imputTile == 19){
      outputTile = 0;
    }
    if(imputTile == 20){
      outputTile = 7;
    }
    if(imputTile == 21){
      outputTile = 0;
    }
    if(imputTile == 22){
      outputTile = 0;
    }
    if(imputTile == 23){
      outputTile = 0;
    }
    if(imputTile == 24){
      outputTile = 0;
    }
    if(imputTile == 25){
      outputTile = 0;
    }
    if(imputTile == 26){
      outputTile = 17;
    }
    if(imputTile == 27){
      outputTile = 16;
    }
    if(imputTile == 28){
      outputTile = 15;
    }
    if(imputTile == 29){
      outputTile = 0;
    }
    if(imputTile == 30){
      outputTile = 0;
    }
    if(imputTile == 31){
      outputTile = 8;
    }
    if(imputTile == 32){
      outputTile = 0;
    }
    if(imputTile == 33){
      outputTile = 67;
    }
    if(imputTile == 34){
      outputTile = 0;
    }
    if(imputTile == 35){
      outputTile = 0;
    }
    if(imputTile == 36){
      outputTile = 0;
    }
    if(imputTile == 37){
      outputTile = 0;
    }
    if(imputTile == 38){
      outputTile = 0;
    }
    if(imputTile == 39){
      outputTile = 61;
    }
    if(imputTile == 40){
      outputTile = 62;
    }
    if(imputTile == 41){
      outputTile = 63;
    }
    if(imputTile == 42){
      outputTile = 64;
    }
    if(imputTile == 43){
      outputTile = 65;
    }
    if(imputTile == 44){
      outputTile = 66;
    }
    if(imputTile == 45){
      outputTile = 0;
    }
    if(imputTile == 46){
      outputTile = 9;
    }
    if(imputTile == 47){
      outputTile = 0;
    }
    if(imputTile == 48){
      outputTile = 0;
    }
    if(imputTile == 49){
      outputTile = 14;
    }
    if(imputTile == 50){
      outputTile = 0;
    }
    if(imputTile == 51){
      outputTile = 18;
    }
    if(imputTile == 52){
      outputTile = 19;
    }
    if(imputTile == 53){
      outputTile = 100; //Jail
    }
    if(imputTile == 54){
      outputTile = 13;
    }
    if(imputTile == 55){
      outputTile = 12;
    }
    if(imputTile == 56){
      outputTile = 11;
    }
    if(imputTile == 57){
      outputTile = 10;
    }
    if(imputTile == 58){
      outputTile = 0;
    }
    if(imputTile == 59){
      outputTile = 0;
    }
    if(imputTile == 60){
      outputTile = 0;
    }
    if(imputTile == 61){
      outputTile = 0;
    }
    if(imputTile == 62){
      outputTile = 0;
    }
    if(imputTile == 63){
      outputTile = 0;
    }
    if(imputTile == 64){
      outputTile = 60;

    }
    if(imputTile == 65){
      outputTile = 59;
    }
    if(imputTile == 66){
      outputTile = 58;
    }
    if(imputTile == 67){
      outputTile = 57;
    }
    if(imputTile == 68){
      outputTile = 56;

    }
    if(imputTile == 69){
      outputTile = 55;
    }
    if(imputTile == 70){
      outputTile = 54;
    }
    if(imputTile == 71){
      outputTile = 0;
    }
    if(imputTile == 72){
      outputTile = 25;
    }
    if(imputTile == 73){
      outputTile = 24;
    }
    if(imputTile == 74){
      outputTile = 23;
    }
    if(imputTile == 75){
      outputTile = 22;
    }
    if(imputTile == 76){
      outputTile = 21;
    }
    if(imputTile == 77){
      outputTile = 20;
    }
    if(imputTile == 78){
      outputTile = 0;
    }
    if(imputTile == 79){
      outputTile = 0;
    }
    if(imputTile == 80){
      outputTile = 0;
    }
    if(imputTile == 81){
      outputTile = 0;
    }
    if(imputTile == 82){
      outputTile = 0;
    }
    if(imputTile == 83){
      outputTile = 26;
    }
    if(imputTile == 84){
      outputTile = 0;
    }
    if(imputTile == 85){
      outputTile = 53;
    }
    if(imputTile == 86){
      outputTile = 0;
    }
    if(imputTile == 87){
      outputTile = 0;
    }
    if(imputTile == 88){
      outputTile = 0;
    }
    if(imputTile == 89){
      outputTile = 0;
    }
    if(imputTile == 90){
      outputTile = 0;
    }
    if(imputTile == 91){
      outputTile = 47;
    }
    if(imputTile == 92){
      outputTile = 48;
    }
    if(imputTile == 93){

      outputTile = 49;

    }
    if(imputTile == 94){
      outputTile = 50;
    }
    if(imputTile == 95){
      outputTile = 51;
    }
    if(imputTile == 96){
      outputTile = 52;
    }
    if(imputTile == 97){
      outputTile = 0;
    }
    if(imputTile == 98){
      outputTile = 27;
    }
    if(imputTile == 99){
      outputTile = 0;
    }
    if(imputTile == 100){
      outputTile = 39;
    }
    if(imputTile == 101){
      outputTile = 38;
    }
    if(imputTile == 102){
      outputTile = 37;
    }
    if(imputTile == 103){
      outputTile = 36;
    }
    if(imputTile == 104){
      outputTile = 35;
    }
    if(imputTile == 105){
      outputTile = 0;
    }
    if(imputTile == 106){
      outputTile = 0;
    }
    if(imputTile == 107){
      outputTile = 0;
    }
    if(imputTile == 108){
      outputTile = 0;
    }
    if(imputTile == 109){
      outputTile = 28;
    }
    if(imputTile == 110){
      outputTile = 0;
    }
    if(imputTile == 111){
      outputTile = 0;
    }
    if(imputTile == 112){
      outputTile = 0;
    }
    if(imputTile == 113){
      outputTile = 0;
    }

    if(imputTile == 114){
      outputTile = 0;
    }
    if(imputTile == 115){
      outputTile = 0;
    }
    if(imputTile == 116){
      outputTile = 46;
    }
    if(imputTile == 117){
      outputTile = 45;
    }

    if(imputTile == 118){
      outputTile = 44;
    }
    if(imputTile == 119){
      outputTile = 43;
    }
    if(imputTile == 120){
      outputTile = 42;
    }
    if(imputTile == 121){
      outputTile = 41;
    }
    if(imputTile == 122){
      outputTile = 40;
    }
    if(imputTile == 123){
      outputTile = 0;
    }
    if(imputTile == 124){
      outputTile = 29;
    }
    if(imputTile == 125){
      outputTile = 30;
    }
    if(imputTile == 126){
      outputTile = 31;
    }
    if(imputTile == 127){
      outputTile = 32;

    }
    if(imputTile == 128){
      outputTile = 33;
    }
    if(imputTile == 129){
      outputTile = 34;
    }
    return outputTile;
  }
}
