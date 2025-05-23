package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.controller.snl.SNLRuleSelectionController;
import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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
 * <h1>SNLRuleSelectionView</h1>
 * Snakes and Ladders Rule Selection View.
 *
 * <p>JavaFX view for selecting rules in a Snakes and Ladders game. Allows players to choose
 * difficulty
 * level, number of dice, and shows modifiers. Updates dynamically based on
 * {@link SNLRuleSelectionModel} state.
 *
 * <P>AI: active involvement as sparring partner and created underlying frame for view development.
 * </P>
 * </p>
 */
public class SNLRuleSelectionView implements SNLRuleSelectionModel.Observer {

  private final SNLRuleSelectionModel model;
  private final SNLRuleSelectionController controller;

  private ToggleGroup difficultyGroup;
  private ToggleGroup diceGroup;

  private RadioButton easyRadio;
  private RadioButton defaultRadio;
  private RadioButton hardRadio;
  private RadioButton oneDiceRadio;
  private RadioButton twoDiceRadio;

  private Label modifiersLabel;
  private Label countLabel;

  private Button backBtn;
  private Button continueBtn;
  private Button randomBtn;

  private StackPane root;

  /**
   * Constructs the view with associated model and controller.
   *
   * @param model      the rule selection model
   * @param controller the controller managing user interaction
   */
  public SNLRuleSelectionView(SNLRuleSelectionModel model, SNLRuleSelectionController controller) {
    this.model = model;
    this.controller = controller;
    model.addObserver(this);
  }

  /**
   * <h2>initializeUI</h2>
   * Initializes all UI components and event bindings.
   */
  public void initializeUI() {
    createUI();
    root.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/css/RuleSelectionStyles.css"))
            .toExternalForm()
    );
    setupEventHandlers();
    applyInitialUIState();
  }

  /**
   * <h2>createUI</h2>
   * Constructs the layout and UI elements of the rule selection screen.
   */
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

    Label title = new Label("Rule Selection");
    title.getStyleClass().add("rs-title");

    difficultyGroup = new ToggleGroup();
    easyRadio = createRadioButton("Easy", "easy.json", difficultyGroup);
    defaultRadio = createRadioButton("Default", "default.json", difficultyGroup);
    hardRadio = createRadioButton("Hard", "hard.json", difficultyGroup);
    HBox diffBox = new HBox(10, easyRadio, defaultRadio, hardRadio);
    diffBox.setAlignment(Pos.CENTER);

    diceGroup = new ToggleGroup();
    oneDiceRadio = createRadioButton("1 Die", 1, diceGroup);
    twoDiceRadio = createRadioButton("2 Dice", 2, diceGroup);
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

    modifiersLabel = new Label();
    modifiersLabel.getStyleClass().add("rs-modifiers");

    countLabel = new Label();
    countLabel.getStyleClass().add("rs-count");

    Label modTitle = new Label("Game Modifiers");
    modTitle.getStyleClass().add("rs-mod-title");

    backBtn = new Button("Back");
    backBtn.getStyleClass().add("rs-nav");

    continueBtn = new Button("Continue");
    continueBtn.getStyleClass().add("rs-nav");

    HBox nav = new HBox(10, backBtn, continueBtn);
    nav.setAlignment(Pos.CENTER);

    Region topSpacer = new Region();
    Region bottomSpacer = new Region();
    VBox.setVgrow(topSpacer, Priority.ALWAYS);
    VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

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

  /**
   * <h2>setupEventHandlers</h2>
   * Wires UI components to controller actions.
   */
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

  /**
   * <h2>applyInitialUIState</h2>
   * Applies the model state to the UI on first load.
   */
  private void applyInitialUIState() {
    switch (model.getSelectedBoardFile()) {
      case "easy.json" -> easyRadio.setSelected(true);
      case "hard.json" -> hardRadio.setSelected(true);
      default -> defaultRadio.setSelected(true);
    }

    if (model.getDiceCount() == 2) {
      twoDiceRadio.setSelected(true);
    } else {
      oneDiceRadio.setSelected(true);
    }

    onRuleSelectionChanged();
  }

  /**
   * <h2>onRuleSelectionChanged</h2>
   * Updates UI when rule selections change in the model.
   */
  @Override
  public void onRuleSelectionChanged() {
    int ladders = model.getLadderCountFromJSON();
    int snakes = model.getSnakeCountFromJSON();
    int dice = model.getDiceCount();
    modifiersLabel.setText(String.format("Ladders: %d   Snakes: %d", ladders, snakes));
    countLabel.setText("Dice count: " + dice);
  }

  /**
   * <h2>getRoot</h2>
   * Returns the root node of the UI for embedding in a scene.
   *
   * @return the root node of the UI
   */
  public Parent getRoot() {
    return root;
  }

  /**
   * <h2>createRadioButton</h2>
   * Utility method for radio button creation.
   *
   * @param text     label text
   * @param userData user data to associate
   * @param group    ToggleGroup to assign the button to
   * @return configured RadioButton
   */
  private RadioButton createRadioButton(String text, Object userData, ToggleGroup group) {
    RadioButton rb = new RadioButton(text);
    rb.setUserData(userData);
    rb.setToggleGroup(group);
    rb.getStyleClass().add("rs-diff-rb");
    return rb;
  }
}