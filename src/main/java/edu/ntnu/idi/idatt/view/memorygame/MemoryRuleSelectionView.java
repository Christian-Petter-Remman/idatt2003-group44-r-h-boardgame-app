package edu.ntnu.idi.idatt.view.memorygame;

import edu.ntnu.idi.idatt.controller.memorygame.MemoryRuleSelectionController;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MemoryRuleSelectionView {

  private final MemoryRuleSelectionController controller;
  private Parent root;

  public MemoryRuleSelectionView(MemoryRuleSelectionController controller) {
    this.controller = controller;
  }

  public void initializeUI() {
    ImageView bg = new ImageView(new Image("/images/memory_rule_background.png"));
    bg.setFitWidth(800);
    bg.setFitHeight(600);
    bg.setPreserveRatio(true);
    bg.setOpacity(0.3);

    VBox card = new VBox(20);
    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(30));
    card.setMaxWidth(380);
    card.setBackground(new Background(
        new BackgroundFill(Color.gray(0.2, 0.8), new CornerRadii(12), Insets.EMPTY)
    ));

    Label title = new Label("Memory Game Settings");
    title.getStyleClass().add("rs-title");

    ToggleGroup sizeGroup = new ToggleGroup();
    RadioButton rb4x4 = new RadioButton("4 × 4");
    RadioButton rb4x5 = new RadioButton("4 × 5");
    RadioButton rb6x6 = new RadioButton("6 × 6");
    rb4x4.setUserData(MemoryGameSettings.BoardSize.FOUR_BY_FOUR);
    rb4x5.setUserData(MemoryGameSettings.BoardSize.FOUR_BY_FIVE);
    rb6x6.setUserData(MemoryGameSettings.BoardSize.SIX_BY_SIX);
    rb4x4.getStyleClass().add("rs-diff-rb");
    rb4x5.getStyleClass().add("rs-diff-rb");
    rb6x6.getStyleClass().add("rs-diff-rb");
    rb4x4.setToggleGroup(sizeGroup);
    rb4x5.setToggleGroup(sizeGroup);
    rb6x6.setToggleGroup(sizeGroup);
    rb4x4.setSelected(true);
    HBox diffBox = new HBox(10, rb4x4, rb4x5, rb6x6);
    diffBox.setAlignment(Pos.CENTER);

    Label p1Label = new Label("Player 1 Name:");
    p1Label.getStyleClass().add("rs-mod-title");
    TextField player1Field = new TextField();

    Label p2Label = new Label("Player 2 Name:");
    p2Label.getStyleClass().add("rs-mod-title");
    TextField player2Field = new TextField();

    Button backBtn = new Button("Back");
    Button continueBtn = new Button("Continue");
    backBtn.getStyleClass().add("rs-nav");
    continueBtn.getStyleClass().add("rs-nav");
    HBox nav = new HBox();
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    nav.getChildren().addAll(backBtn, spacer, continueBtn);
    nav.setAlignment(Pos.CENTER);

    card.getChildren().addAll(
        title,
        diffBox,
        p1Label, player1Field,
        p2Label, player2Field,
        nav
    );

    StackPane container = new StackPane(bg, card);
    container.getStylesheets().add(
        getClass().getResource("/css/RuleSelectionStyles.css").toExternalForm()
    );
    StackPane.setAlignment(card, Pos.CENTER);
    StackPane.setMargin(card, new Insets(20));

    controller.setSizeGroup(sizeGroup);
    controller.setPlayerFields(player1Field, player2Field);
    controller.setBackButton(backBtn);
    controller.setContinueButton(continueBtn);

    this.root = container;
  }

  public Parent getRoot() {
    return root;
  }
}
