package edu.ntnu.idi.idatt.view.memorygame;

import edu.ntnu.idi.idatt.controller.memorygame.MemoryRuleSelectionController;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * <h1>MemoryRuleSelectionView.</h1>
 *
 * <p>A JavaFX view for configuring the memory game settings before starting the game.
 * Allows the user to choose board size and input player names.
 * </p>
 */
public class MemoryRuleSelectionView {

  private final MemoryRuleSelectionController controller;
  private Parent root;

  /**
   * <h2>Constructor</h2>
   * Initializes the view with the given controller.
   *
   * @param controller the controller managing interactions
   */
  public MemoryRuleSelectionView(MemoryRuleSelectionController controller) {
    this.controller = controller;
  }

  /**
   * <h2>initializeUI</h2>
   * Sets up the user interface components for rule selection.
   */
  public void initializeUI() {
    ImageView background = new ImageView(new Image("/images/memory_rule_background.png"));
    background.setFitWidth(800);
    background.setFitHeight(600);
    background.setPreserveRatio(true);
    background.setOpacity(0.3);

    VBox card = createCardLayout();
    StackPane container = new StackPane(background, card);
    container.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/css/RuleSelectionStyles.css"))
            .toExternalForm()
    );
    StackPane.setAlignment(card, Pos.CENTER);
    StackPane.setMargin(card, new Insets(20));

    this.root = container;
  }

  /**
   * <h2>createCardLayout</h2>
   * Constructs the inner layout card for settings.
   *
   * @return a VBox containing all the settings inputs
   */
  private VBox createCardLayout() {
    VBox card = new VBox(20);
    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(30));
    card.setMaxWidth(380);
    card.setBackground(new Background(
        new BackgroundFill(Color.gray(0.2, 0.8), new CornerRadii(12), Insets.EMPTY)
    ));

    Label title = new Label("Memory Game Settings");
    title.getStyleClass().add("rs-title");

    HBox difficultyBox = createDifficultySelection();
    Label player1Label = new Label("Player 1 Name:");
    player1Label.getStyleClass().add("rs-mod-title");
    TextField player1Field = new TextField();

    Label player2Label = new Label("Player 2 Name:");
    player2Label.getStyleClass().add("rs-mod-title");
    TextField player2Field = new TextField();

    HBox navigation = createNavigationControls(player1Field, player2Field);

    card.getChildren().addAll(
        title,
        difficultyBox,
        player1Label, player1Field,
        player2Label, player2Field,
        navigation
    );

    return card;
  }

  /**
   * <h2>createDifficultySelection</h2>
   * Builds the board size selection using radio buttons.
   *
   * @return an HBox with difficulty options
   */
  private HBox createDifficultySelection() {
    ToggleGroup sizeGroup = new ToggleGroup();
    RadioButton rb4x4 = createRadioButton("4 × 4", MemoryGameSettings.BoardSize.FOUR_BY_FOUR,
        sizeGroup);
    RadioButton rb4x5 = createRadioButton("4 × 5", MemoryGameSettings.BoardSize.FOUR_BY_FIVE,
        sizeGroup);
    RadioButton rb6x6 = createRadioButton("6 × 6", MemoryGameSettings.BoardSize.SIX_BY_SIX,
        sizeGroup);
    rb4x4.setSelected(true);

    HBox box = new HBox(10, rb4x4, rb4x5, rb6x6);
    box.setAlignment(Pos.CENTER);
    controller.setSizeGroup(sizeGroup);
    return box;
  }

  /**
   * <h2>createRadioButton</h2>
   * Helper to create a styled radio button for board size.
   *
   * @param label the text for the button
   * @param value the associated BoardSize enum
   * @param group the ToggleGroup it belongs to
   * @return a configured RadioButton
   */
  private RadioButton createRadioButton(String label, MemoryGameSettings.BoardSize value,
      ToggleGroup group) {
    RadioButton rb = new RadioButton(label);
    rb.setUserData(value);
    rb.setToggleGroup(group);
    rb.getStyleClass().add("rs-diff-rb");
    return rb;
  }

  /**
   * <h2>createNavigationControls</h2>
   * Creates the back and continue buttons for the UI.
   *
   * @param player1Field reference to player 1 name field
   * @param player2Field reference to player 2 name field
   * @return an HBox containing both navigation buttons
   */
  private HBox createNavigationControls(TextField player1Field, TextField player2Field) {
    Button backBtn = new Button("Back");
    Button continueBtn = new Button("Continue");
    backBtn.getStyleClass().add("rs-nav");
    continueBtn.getStyleClass().add("rs-nav");

    controller.setPlayerFields(player1Field, player2Field);
    controller.setBackButton(backBtn);
    controller.setContinueButton(continueBtn);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    HBox nav = new HBox(backBtn, spacer, continueBtn);
    nav.setAlignment(Pos.CENTER);
    return nav;
  }

  /**
   * <h2>getRoot</h2>
   * Returns the root node of the view.
   *
   * @return the root UI component
   */
  public Parent getRoot() {
    return root;
  }
}