package edu.ntnu.idi.idatt.filehandling;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * <h1>RandomExclusionStrategy</h1>
 * A custom Gson {@link ExclusionStrategy} used to exclude {@link java.util.Random} fields during
 * serialization and deserialization.
 *
 * <p>AI: used to develop this class</p>
 *
 * <p>This is useful for avoiding issues with serializing non-deterministic fields or fields that
 * are not relevant to the data model (e.g. random number generators).
 */
public class RandomExclusionStrategy implements ExclusionStrategy {

  /**
   * <h2>shouldSkipField</h2>
   * Determines whether the given field should be skipped during (de)serialization.
   *
   * @param f the field to examine
   * @return true if the field is of type {@link java.util.Random}, false otherwise
   */
  @Override
  public boolean shouldSkipField(FieldAttributes f) {
    return f.getDeclaredType() == java.util.Random.class;
  }

  /**
   * <h2>shouldSkipClass</h2>
   * Determines whether the given class should be skipped entirely during (de)serialization.
   *
   * @param clazz the class to examine
   * @return false, indicating no class is excluded
   */
  @Override
  public boolean shouldSkipClass(Class<?> clazz) {
    return false;
  }
}