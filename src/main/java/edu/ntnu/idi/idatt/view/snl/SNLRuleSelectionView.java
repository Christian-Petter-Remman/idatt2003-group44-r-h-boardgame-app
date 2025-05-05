package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Objects;

public class SNLRuleSelectionView implements SNLRuleSelectionModel.Observer {

  private final SNLRuleSelectionModel model;

  private final ToggleGroup difficultyGroup = new ToggleGroup();
  private final RadioButton easyRadio = new RadioButton("Easy");
  private final RadioButton defaultRadio = new RadioButton("Default");
  private final RadioButton hardRadio = new RadioButton("Hard");

  private final Label countLabel = new Label();

  private final Button randomBtn = new Button("Random");

  private final Label diceLabel = new Label("Choose dice amount");
  private final ToggleGroup diceToggle = new ToggleGroup();
  private final RadioButton oneDieRadio = new RadioButton("One Die");
  private final RadioButton twoDiceRadio = new RadioButton("Two Dice");

  private final Button backBtn = new Button("Back");
  private final Button continueBtn = new Button("Continue");

  private final Parent root;

  public SNLRuleSelectionView(SNLRuleSelectionModel model) {
    this.model = model;
    model.addObserver(this);
    root = buildView();
    onRuleSelectionChanged();
  }

  private Parent buildView() {
    ImageView background = new ImageView(new Image("/images/snakesbackground.jpg"));
    background.setFitWidth(800);
    background.setFitHeight(600);
    background.setPreserveRatio(true);
    background.setOpacity(0.3);

    VBox card = new VBox(20,
        createSpacer(),
        createTitle(),
        createDifficultyBox(),
        createRandomButton(),
        createModifierBox(),
        createDiceBox(),
        createNavBox(),
        createSpacer()
    );
    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(30));
    card.setMaxWidth(380);
    card.setBackground(new Background(new BackgroundFill(
        Color.gray(0.2, 0.8), new CornerRadii(12), Insets.EMPTY
    )));

    StackPane container = new StackPane(background, card);
    StackPane.setAlignment(card, Pos.CENTER);
    StackPane.setMargin(card, new Insets(20));
    applyCss(container);
    return container;
  }

  private Region createSpacer() {
    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    return spacer;
  }

  private Label createTitle() {
    Label title = new Label("Rule Selection");
    title.getStyleClass().add("rs-title");
    return title;
  }

  private HBox createDifficultyBox() {
    easyRadio.setUserData("easy.json");
    defaultRadio.setUserData("default.json");
    hardRadio.setUserData("hard.json");
    for (RadioButton rb : new RadioButton[]{easyRadio, defaultRadio, hardRadio}) {
      rb.getStyleClass().add("rs-diff-rb");
      rb.setToggleGroup(difficultyGroup);
    }
    defaultRadio.setSelected(true);

    HBox box = new HBox(20, easyRadio, defaultRadio, hardRadio);
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(5));
    box.setBackground(new Background(new BackgroundFill(
        Color.rgb(106, 13, 173, 0.1), new CornerRadii(5), Insets.EMPTY
    )));
    return box;
  }

  private HBox createRandomButton() {
    randomBtn.getStyleClass().add("rs-random");
    ImageView icon = new ImageView(new Image("/images/question_mark_icon.png"));
    icon.setFitWidth(18);
    icon.setFitHeight(18);
    randomBtn.setGraphic(icon);
    HBox box = new HBox(randomBtn);
    box.setAlignment(Pos.CENTER);
    return box;
  }

  private HBox createModifierBox() {
    Label modTitle = new Label("Game Modifiers");
    modTitle.getStyleClass().add("rs-mod-title");
    countLabel.getStyleClass().add("rs-count");
    VBox content = new VBox(5, modTitle, countLabel);
    content.setAlignment(Pos.CENTER);
    content.setPadding(new Insets(10));
    content.setBackground(new Background(new BackgroundFill(
        Color.rgb(106, 13, 173, 0.2), new CornerRadii(8), Insets.EMPTY
    )));
    HBox box = new HBox(content);
    box.setAlignment(Pos.CENTER);
    return box;
  }

  private HBox createDiceBox() {
    diceLabel.getStyleClass().add("rs-mod-title");

    oneDieRadio.setUserData(1);
    twoDiceRadio.setUserData(2);
    oneDieRadio.getStyleClass().add("rs-diff-rb");
    twoDiceRadio.getStyleClass().add("rs-diff-rb");
    oneDieRadio.setToggleGroup(diceToggle);
    twoDiceRadio.setToggleGroup(diceToggle);

    diceToggle.selectedToggleProperty().addListener((obs, o, n) -> {
      if (n != null) {
        model.setDiceCount((int) n.getUserData());
      }
    });

    HBox radios = new HBox(20, oneDieRadio, twoDiceRadio);
    radios.setAlignment(Pos.CENTER);
    radios.setPadding(new Insets(5));
    radios.setBackground(new Background(new BackgroundFill(
        Color.rgb(106, 13, 173, 0.1), new CornerRadii(5), Insets.EMPTY
    )));

    VBox content = new VBox(5, diceLabel, radios);
    content.setAlignment(Pos.CENTER);
    content.setPadding(new Insets(10));
    content.setBackground(new Background(new BackgroundFill(
        Color.rgb(106, 13, 173, 0.2), new CornerRadii(8), Insets.EMPTY
    )));
    HBox box = new HBox(content);
    box.setAlignment(Pos.CENTER);
    return box;
  }

  private HBox createNavBox() {
    backBtn.getStyleClass().add("rs-nav");
    continueBtn.getStyleClass().add("rs-nav");
    HBox nav = new HBox(10, backBtn, continueBtn);
    nav.setAlignment(Pos.CENTER);
    return nav;
  }

  private void applyCss(Region root) {
    root.getStylesheets().add(
        Objects.requireNonNull(
            getClass().getResource("/css/RuleSelectionStyles.css")
        ).toExternalForm()
    );
  }

  @Override
  public void onRuleSelectionChanged() {
    countLabel.setText(
        "Snakes: " + model.getSnakeCount() +
            " | Ladders: " + model.getLadderCount()
    );

    if (model.getDiceCount() == 2) {
      twoDiceRadio.setSelected(true);
    } else {
      oneDieRadio.setSelected(true);
    }
  }

  public Parent getRoot() {
    return root;
  }

  public ToggleGroup getDifficultyGroup() {
    return difficultyGroup;
  }

  public Button getRandomBtn() {
    return randomBtn;
  }

  public Button getBackBtn() {
    return backBtn;
  }

  public Button getContinueBtn() {
    return continueBtn;
  }
}