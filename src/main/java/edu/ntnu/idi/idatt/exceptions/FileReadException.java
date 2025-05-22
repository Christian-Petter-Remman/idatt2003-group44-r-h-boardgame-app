package edu.ntnu.idi.idatt.exceptions;

/**
 * <h1>FileReadException</h1>
 * Custom exception class for handling file read errors in the application. This exception is thrown
 * when there is an issue reading a file, such as a missing or inaccessible file.
 *
 * @author Oliver, Christian
 */
public class FileReadException extends Exception {

  /**
   * <h2>Constructor</h2>
   * Initializes a new instance of the FileReadException with a specified error message.
   *
   * @param message the error message describing the issue
   */
  public FileReadException(String message, Throwable cause) {
    super(message, cause);
  }
}