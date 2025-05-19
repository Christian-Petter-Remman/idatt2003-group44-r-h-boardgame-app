package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLRuleSelectionController;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Random;

public class SNLRuleSelectionView implements SNLRuleSelectionModel.Observer {

  private final SNLRuleSelectionModel model;
  private final SNLRuleSelectionController controller;

  private ToggleGroup difficultyGroup;
  private RadioButton easyRadio, defaultRadio, hardRadio;

  private ToggleGroup diceGroup;
  private RadioButton oneDiceRadio, twoDiceRadio;

  private Label modifiersLabel;
  private Label countLabel;
  private Button backBtn, continueBtn, randomBtn;

  private StackPane root;  // use StackPane for CSS

  public SNLRuleSelectionView(SNLRuleSelectionModel model, SNLRuleSelectionController controller) {
    this.model = model;
    this.controller = controller;
    model.addObserver(this);
  }

  public void initializeUI() {
    createUI();
    // Load our CSS
    root.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/css/RuleSelectionStyles.css"))
            .toExternalForm()
    );
    setupEventHandlers();
    applyInitialUIState();
  }

  private void createUI() {
    ImageView bg = new ImageView(new Image("/images/snakesbackground.jpg"));
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

    Region topSpacer = new Region();
    Region bottomSpacer = new Region();
    VBox.setVgrow(topSpacer, Priority.ALWAYS);
    VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

    Label title = new Label("Rule Selection");
    title.getStyleClass().add("rs-title");

    difficultyGroup = new ToggleGroup();
    easyRadio = new RadioButton("Easy");
    easyRadio.setUserData("easy.json");
    defaultRadio = new RadioButton("Default");
    defaultRadio.setUserData("default.json");
    hardRadio = new RadioButton("Hard");
    hardRadio.setUserData("hard.json");
    for (RadioButton rb : List.of(easyRadio, defaultRadio, hardRadio)) {
      rb.getStyleClass().add("rs-diff-rb");
      rb.setToggleGroup(difficultyGroup);
    }
    HBox diffBox = new HBox(10, easyRadio, defaultRadio, hardRadio);
    diffBox.setAlignment(Pos.CENTER);

    diceGroup = new ToggleGroup();
    oneDiceRadio = new RadioButton("1 Die");
    oneDiceRadio.setUserData(1);
    twoDiceRadio = new RadioButton("2 Dice");
    twoDiceRadio.setUserData(2);
    oneDiceRadio.getStyleClass().add("rs-dice-rb");
    twoDiceRadio.getStyleClass().add("rs-dice-rb");
    oneDiceRadio.setToggleGroup(diceGroup);
    twoDiceRadio.setToggleGroup(diceGroup);
    Label diceLabel = new Label("Number of Dice:");
    diceLabel.getStyleClass().add("rs-dice-label");
    HBox diceBox = new HBox(10, oneDiceRadio, twoDiceRadio);
    diceBox.setAlignment(Pos.CENTER);
    VBox diceContainer = new VBox(5, diceLabel, diceBox);
    diceContainer.setAlignment(Pos.CENTER);

    randomBtn = new Button("Random");
    randomBtn.getStyleClass().add("rs-random");
    ImageView q = new ImageView(new Image("/images/question_mark_icon.png"));
    q.setFitWidth(18);
    q.setFitHeight(18);
    randomBtn.setGraphic(q);

    Label modTitle = new Label("Game Modifiers");
    modTitle.getStyleClass().add("rs-mod-title");

    modifiersLabel = new Label();
    modifiersLabel.getStyleClass().add("rs-modifiers");

    countLabel = new Label();
    countLabel.getStyleClass().add("rs-count");

    backBtn = new Button("Back");
    backBtn.getStyleClass().add("rs-nav");
    continueBtn = new Button("Continue");
    continueBtn.getStyleClass().add("rs-nav");
    HBox nav = new HBox(10, backBtn, continueBtn);
    nav.setAlignment(Pos.CENTER);

    card.getChildren().addAll(
        topSpacer,
        title,
        diffBox,
        diceContainer,
        randomBtn,
        modTitle,
        modifiersLabel,
        countLabel,
        nav,
        bottomSpacer
    );

    root = new StackPane(bg, card);
    StackPane.setAlignment(card, Pos.CENTER);
  }

  private void setupEventHandlers() {
    difficultyGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
      if (newT != null) {
        controller.setSelectedBoardFile(newT.getUserData().toString());
      }
    });
    diceGroup.selectedToggleProperty().addListener((obs, oldT, newT) -> {
      if (newT != null) {
        controller.setDiceCount((int) newT.getUserData());
      }
    });
    randomBtn.setOnAction(e -> {
      List<String> r = model.getAvailableBoards().stream()
          .filter(f -> f.toLowerCase().startsWith("random"))
          .toList();
      if (!r.isEmpty()) {
        controller.setSelectedBoardFile(r.get(new Random().nextInt(r.size())));
      }
    });
    backBtn.setOnAction(e -> controller.onBackPressed());
    continueBtn.setOnAction(e -> controller.onContinuePressed());
  }

  private void applyInitialUIState() {
    String sel = model.getSelectedBoardFile();
    if ("easy.json".equals(sel)) {
      easyRadio.setSelected(true);
    } else if ("hard.json".equals(sel)) {
      hardRadio.setSelected(true);
    } else {
      defaultRadio.setSelected(true);
    }

    if (model.getDiceCount() == 2) {
      twoDiceRadio.setSelected(true);
    } else {
      oneDiceRadio.setSelected(true);
    }

    onRuleSelectionChanged();
  }

  @Override
  public void onRuleSelectionChanged() {
    int ladders = model.getLadderCountFromJSON();
    int snakes = model.getSnakeCountFromJSON();
    int dice = model.getDiceCount();
    modifiersLabel.setText(
        String.format("Ladders: %d   Snakes: %d", ladders, snakes)
    );
    countLabel.setText("Dice count: " + dice);
  }

  public Parent getRoot() {
    return root;
  }
}
