package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.controller.snakesandladders.SalRuleSelectionController;
import edu.ntnu.idi.idatt.exceptions.FileReadException;
import edu.ntnu.idi.idatt.exceptions.JsonParsingException;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.Board;
import edu.ntnu.idi.idatt.model.boardgames.snakesladders.rule_selection.SalRuleSelectionModel;
import edu.ntnu.idi.idatt.view.AbstractView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SalRuleSelectionView extends AbstractView implements SalRuleSelectionModel.Observer {

  private final SalRuleSelectionModel model;
  private final SalRuleSelectionController controller;

  private ToggleGroup difficultyGroup;
  private RadioButton easyRadio, defaultRadio, hardRadio;

  private Label countLabel;
  private Button backBtn, continueBtn, randomBtn;

  public SalRuleSelectionView(SalRuleSelectionModel model, SalRuleSelectionController controller) {
    this.model = model;
    this.controller = controller;
    model.addObserver(this);

    // Load stylesheet
    root.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/css/SalRuleSelectionStyles.css")).toExternalForm()
    );
  }

  @Override
  protected void createUI() {
    // Background: fixed size, faded
    ImageView bg = new ImageView(new Image("/images/snakesbackground.jpg"));
    bg.setFitWidth(800);
    bg.setFitHeight(600);
    bg.setPreserveRatio(true);
    bg.setOpacity(0.3);

    // Card container: centered, limited width, margins
    VBox card = new VBox(20);
    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(30));
    card.setMaxWidth(380);
    card.setBackground(new Background(
        new BackgroundFill(Color.gray(0.2, 0.8), new CornerRadii(12), Insets.EMPTY)
    ));

    // Spacers for vertical centering
    Region topSpacer = new Region();
    Region bottomSpacer = new Region();
    VBox.setVgrow(topSpacer, Priority.ALWAYS);
    VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

    // Title
    Label title = new Label("Rule Selection");
    title.getStyleClass().add("rs-title");

    // Difficulty radios
    difficultyGroup = new ToggleGroup();
    easyRadio    = new RadioButton("Easy");    easyRadio.setUserData("easy.json");
    defaultRadio = new RadioButton("Default"); defaultRadio.setUserData("default.json");
    hardRadio    = new RadioButton("Hard");    hardRadio.setUserData("hard.json");
    easyRadio.getStyleClass().add("rs-diff-rb");
    defaultRadio.getStyleClass().add("rs-diff-rb");
    hardRadio.getStyleClass().add("rs-diff-rb");
    easyRadio.setToggleGroup(difficultyGroup);
    defaultRadio.setToggleGroup(difficultyGroup);
    hardRadio.setToggleGroup(difficultyGroup);
    HBox diffBox = new HBox(10, easyRadio, defaultRadio, hardRadio);
    diffBox.setAlignment(Pos.CENTER);

    // Random button
    randomBtn = new Button("Random");
    randomBtn.getStyleClass().add("rs-random");
    ImageView q = new ImageView(new Image("/images/question_mark_icon.png"));
    q.setFitWidth(18); q.setFitHeight(18);
    randomBtn.setGraphic(q);
    randomBtn.setOnAction(e -> {
      List<String> r = model.getAvailableBoards().stream()
          .filter(f -> f.toLowerCase().startsWith("random"))
          .toList();
      if (!r.isEmpty()) model.setSelectedBoardFile(r.get(new Random().nextInt(r.size())));
    });

    // Game Modifiers label
    Label modTitle = new Label("Game Modifiers");
    modTitle.getStyleClass().add("rs-mod-title");

    // Count label
    countLabel = new Label();
    countLabel.getStyleClass().add("rs-count");

    // Navigation buttons
    backBtn     = new Button("Back");     backBtn.getStyleClass().add("rs-nav");
    continueBtn = new Button("Continue"); continueBtn.getStyleClass().add("rs-nav");
    HBox nav = new HBox();
    Region navSpacer = new Region();
    HBox.setHgrow(navSpacer, Priority.ALWAYS);
    nav.getChildren().addAll(backBtn, navSpacer, continueBtn);
    nav.setAlignment(Pos.CENTER);

    // Assemble card
    card.getChildren().addAll(
        topSpacer,
        title,
        diffBox,
        randomBtn,
        modTitle,
        countLabel,
        nav,
        bottomSpacer
    );

    // Root container
    StackPane container = new StackPane(bg, card);
    StackPane.setAlignment(card, Pos.CENTER);
    StackPane.setMargin(card, new Insets(20));
    root = container;
  }

  @Override
  protected void setupEventHandlers() {
    difficultyGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal != null) {
        model.setSelectedBoardFile(newVal.getUserData().toString());
      }
    });
    backBtn.setOnAction(e -> controller.onBackPressed());
    continueBtn.setOnAction(e -> controller.onContinuePressed());
  }

  @Override
  protected void applyInitialUIState() {
    String sel = model.getSelectedBoardFile();
    if ("easy.json".equals(sel))       easyRadio.setSelected(true);
    else if ("hard.json".equals(sel))  hardRadio.setSelected(true);
    else                                 defaultRadio.setSelected(true);
    onRuleSelectionChanged();
  }

  @Override
  public void onRuleSelectionChanged() {
    try {
      Board b = controller.loadSelectedBoardForGame();
      countLabel.setText(
          "Snakes: " + b.getSnakes().size() +
              "    Ladders: " + b.getLadders().size()
      );
    } catch (FileReadException | JsonParsingException ex) {
      countLabel.setText("Snakes: ?    Ladders: ?");
      logger.error("Failed to load counts", ex);
    }
  }
}
