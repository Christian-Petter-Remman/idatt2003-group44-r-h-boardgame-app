package edu.ntnu.idi.idatt.filehandling;

public interface FileHandler<T> {

  void saveToFile(T object, String fileName) throws Exception;
  T loadFromFile(String fileName) throws Exception;

}
