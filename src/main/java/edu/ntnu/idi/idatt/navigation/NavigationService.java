package edu.ntnu.idi.idatt.navigation;

import edu.ntnu.idi.idatt.navigation.NavigationManager.NavigationTarget;
import java.util.Map;

public interface NavigationService {
  void navigateTo(NavigationTarget target, Object... params);
  void navigateTo(NavigationTarget target, Map<String, Object> parameters);
  void navigateBack();
}
