package edu.ntnu.idi.idatt.exceptions;

public class JsonParsingException extends Exception {
  public JsonParsingException(String message) {
    super(message);
  }

  public JsonParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}
