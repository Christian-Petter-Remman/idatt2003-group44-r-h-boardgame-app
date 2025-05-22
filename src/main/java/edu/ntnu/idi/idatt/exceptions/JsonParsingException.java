package edu.ntnu.idi.idatt.exceptions;

/**
 * <h1>JsonParsingException</h1>
 * Custom exception class for handling JSON parsing errors in the application. This exception is
 * thrown when there is an issue parsing a JSON file, such as invalid format or unexpected data.
 *
 * @author Oliver, Christian
 */
public class JsonParsingException extends Exception {

  /**
   * <h2>Constructor</h2>
   * Initializes a new instance of the JsonParsingException with a specified error message.
   *
   * @param message the error message describing the issue
   */
  public JsonParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}
