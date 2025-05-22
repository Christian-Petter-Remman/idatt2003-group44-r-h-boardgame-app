package edu.ntnu.idi.idatt.filehandling;

/**
 * <h1>FileHandler</h1>
 *
 * <p>A generic interface for handling file operations such as saving and loading objects of a given
 * type.
 *
 * @param <T> The type of object to be saved or loaded.
 */
public interface FileHandler<T> {

  /**
   * <h2>loadFromFile</h2>
   *
   * <p>Loads an object of type T from the specified file.
   *
   * @param fileName The path or name of the file to load from.
   * @return The loaded object.
   * @throws Exception If an error occurs during the load operation.
   */
  T loadFromFile(String fileName) throws Exception;
}