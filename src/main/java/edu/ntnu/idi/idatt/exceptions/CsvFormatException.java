package edu.ntnu.idi.idatt.exceptions;

public class CsvFormatException extends Exception {
  public CsvFormatException(String message) {
    super(message);
  }

  public CsvFormatException(String message, Throwable cause) {
    super(message, cause);
  }
}