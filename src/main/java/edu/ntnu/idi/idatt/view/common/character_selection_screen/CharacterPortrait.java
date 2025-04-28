package edu.ntnu.idi.idatt.view.common.character_selection_screen;

import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionData;
import java.io.InputStream;
import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;

public class CharacterPortrait {
  private final CharacterSelectionData character;
  private StackPane container;
  private ImageView imageView;

  public CharacterPortrait(CharacterSelectionData character) {
    this.character = character;
    createView();
  }

  private void createView() {
    container = new StackPane();
    container.getStyleClass().add("css/CharacterSelectionStyleSheet.css/character-container");

    InputStream iconStream = getClass().getResourceAsStream(character.getImagePath());
    if (iconStream == null) {
      throw new IllegalArgumentException("Image not found: " + character.getImagePath());
    }
    imageView = new ImageView(new Image(iconStream));
    imageView.setFitWidth(80);
    imageView.setFitHeight(80);
    imageView.setPickOnBounds(true);

    Objects.requireNonNull(container).getChildren().add(imageView);
  }

  public void refresh() {
    if (character.isSelected()) {
      imageView.setOpacity(0.5);
    } else {
      imageView.setOpacity(1.0);
    }
  }

  public CharacterSelectionData getCharacter() {
    return character;
  }

  public ImageView getImageView() {
    return imageView;
  }

  public Parent getView() {
    return container;
  }

  public void setSelected(boolean selected, boolean isCurrentPlayer) {
    if (selected && isCurrentPlayer) {
      container.setStyle("-fx-border-color: blue; -fx-border-width: 3;");
    } else {
      container.setStyle("");
    }
  }
}
