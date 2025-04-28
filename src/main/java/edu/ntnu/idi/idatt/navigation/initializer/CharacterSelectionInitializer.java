package edu.ntnu.idi.idatt.navigation.initializer;

import edu.ntnu.idi.idatt.controller.common.CharacterSelectionHandler;
import edu.ntnu.idi.idatt.model.common.character_selection_screen.CharacterSelectionManager;
import edu.ntnu.idi.idatt.view.common.character_selection_screen.CharacterSelectionScreen;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class CharacterSelectionInitializer {
  public Parent createCharacterSelectionScreen() {
    // Create manager
    CharacterSelectionManager manager = new CharacterSelectionManager();

    // Create view
    CharacterSelectionScreen screen = new CharacterSelectionScreen(manager);

    // Create handler
    CharacterSelectionHandler handler = new CharacterSelectionHandler(manager, screen);

    // Continue button
    Button continueButton = new Button("Continue");
    continueButton.getStyleClass().add("continue-button");
    continueButton.setOnAction(e -> handler.continueToNextScreen());

    // Layout
    VBox layout = new VBox(20);
    layout.getChildren().addAll(screen.getView(), continueButton);
    layout.setAlignment(Pos.CENTER);

    return layout;
  }
}

