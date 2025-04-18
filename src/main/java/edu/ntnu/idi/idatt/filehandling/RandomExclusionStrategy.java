package edu.ntnu.idi.idatt.filehandling;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class RandomExclusionStrategy implements ExclusionStrategy {
  @Override
  public boolean shouldSkipField(FieldAttributes f) {
    return f.getDeclaredType() == java.util.Random.class;
  }

  @Override
  public boolean shouldSkipClass(Class<?> clazz) {
    return false;
  }
}