package edu.ntnu.idi.idatt.exceptions;

public class FileReadException extends Exception {
  public FileReadException(String message) {
    super(message);
  }

  public FileReadException(String message, Throwable cause) {
    super(message, cause);
  }
}