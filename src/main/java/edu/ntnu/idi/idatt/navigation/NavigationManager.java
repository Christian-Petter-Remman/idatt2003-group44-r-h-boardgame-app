package edu.ntnu.idi.idatt.navigation;

import edu.ntnu.idi.idatt.model.common.screens.CharacterController;
import edu.ntnu.idi.idatt.model.common.screens.CharacterSelectionController;
import edu.ntnu.idi.idatt.model.common.screens.CharacterSelectionModel;
import edu.ntnu.idi.idatt.view.snakesandladders.SalCharacterSelectionView;
import java.util.Stack;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NavigationManager {
  private static NavigationManager instance;
  private Stage primaryStage;
  private final Stack<Parent> screenStack = new Stack<>();

  // Private constructor to prevent direct instantiation
  private NavigationManager() {}

  public static void initialize(Stage primaryStage) {
    instance = new NavigationManager();
    instance.primaryStage = primaryStage;
  }

  public static NavigationManager getInstance() {
    return instance;
  }

  public void setRoot(Parent root) {
    if (primaryStage.getScene() == null) {
      primaryStage.setScene(new Scene(root));
    } else {
      screenStack.push(primaryStage.getScene().getRoot());
      primaryStage.getScene().setRoot(root);
    }
  }

  public void navigateBack() {
    if (!screenStack.isEmpty()) {
      primaryStage.getScene().setRoot(screenStack.pop());
    }
  }

  // In NavigationManager:
  public void navigateToCharacterSelection() {
    CharacterSelectionModel model = new CharacterSelectionModel();
    CharacterController controller = new CharacterSelectionController(model);
    SalCharacterSelectionView view = new SalCharacterSelectionView(controller, model);
    setRoot(view.getRoot());
  }

}
