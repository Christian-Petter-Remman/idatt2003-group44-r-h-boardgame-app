package edu.ntnu.idi.idatt.view.snl;

import edu.ntnu.idi.idatt.model.snl.SNLRuleSelectionModel;
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

public class SNLRuleSelectionView implements SNLRuleSelectionModel.Observer {
  private final SNLRuleSelectionModel model;
  private final ToggleGroup difficultyGroup;
  private final Label countLabel;
  private final Button backBtn;
  private final Button continueBtn;
  private final Button randomBtn;
  private final Parent root;

  public SNLRuleSelectionView(SNLRuleSelectionModel model) {
    this.model = model;
    model.addObserver(this);

    ImageView bg = new ImageView(new Image("/images/snakesbackground.jpg"));
    bg.setFitWidth(800);
    bg.setFitHeight(600);
    bg.setPreserveRatio(true);
    bg.setOpacity(0.3);

    VBox card = new VBox(20);
    card.setAlignment(Pos.CENTER);
    card.setPadding(new Insets(30));
    card.setMaxWidth(380);
    card.setBackground(new Background(new BackgroundFill(Color.gray(0.2, 0.8), new CornerRadii(12), Insets.EMPTY)));

    Label title = new Label("Rule Selection");
    title.getStyleClass().add("rs-title");

    difficultyGroup = new ToggleGroup();
    RadioButton easyRadio = new RadioButton("Easy");
    easyRadio.setUserData("easy.json");
    RadioButton defaultRadio = new RadioButton("Default");
    defaultRadio.setUserData("default.json");
    RadioButton hardRadio = new RadioButton("Hard");
    hardRadio.setUserData("hard.json");
    easyRadio.getStyleClass().add("rs-diff-rb");
    defaultRadio.getStyleClass().add("rs-diff-rb");
    hardRadio.getStyleClass().add("rs-diff-rb");
    easyRadio.setToggleGroup(difficultyGroup);
    defaultRadio.setToggleGroup(difficultyGroup);
    hardRadio.setToggleGroup(difficultyGroup);
    HBox diffBox = new HBox(10, easyRadio, defaultRadio, hardRadio);
    diffBox.setAlignment(Pos.CENTER);

    randomBtn = new Button("Random");
    randomBtn.getStyleClass().add("rs-random");
    ImageView q = new ImageView(new Image("/images/question_mark_icon.png"));
    q.setFitWidth(18);
    q.setFitHeight(18);
    randomBtn.setGraphic(q);

    Label modTitle = new Label("Game Modifiers");
    modTitle.getStyleClass().add("rs-mod-title");

    countLabel = new Label();
    countLabel.getStyleClass().add("rs-count");

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

    card.getChildren().addAll(topSpacer, title, diffBox, randomBtn, modTitle, countLabel, nav, bottomSpacer);
    StackPane container = new StackPane(bg, card);
    StackPane.setAlignment(card, Pos.CENTER);
    StackPane.setMargin(card, new Insets(20));
    root = container;
  }

  @Override
  public void onRuleSelectionChanged() {
    countLabel.setText("Snakes: " + model.getSnakeCount() + " | Ladders: " + model.getLadderCount());
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