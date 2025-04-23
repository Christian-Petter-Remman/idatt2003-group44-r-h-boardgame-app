package edu.ntnu.idi.idatt.view.common;

import edu.ntnu.idi.idatt.navigation.NavigationHandler;
import edu.ntnu.idi.idatt.navigation.NavigationManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public abstract class AbstractRuleSelectionView implements NavigationHandler {
  private static final Logger logger = LoggerFactory.getLogger(AbstractRuleSelectionView.class);

  protected final BorderPane root;
  protected GridPane layout;
  protected Label titleLabel;
  protected Label descriptionLabel;
  protected Button startGameButton;
  protected Button backButton;

  protected static final String SECONDARY_COLOR = "#2c3e50";
  protected static final String BACKGROUND_COLOR = "#ecf0f1";
  protected static final String START_BUTTON_COLOR = "#27ae60";
  protected static final String BACK_BUTTON_COLOR = "#bdc3c7";

  protected boolean uiInitialized = false;

  public AbstractRuleSelectionView() {
    this.root = new BorderPane();
  }

  public void show() {
    initializeLayout();
    setupBackgroundImage();
    setupTitleAndDescription();
    setupGameSpecificControls();
    setupButtons();
    layoutComponents();
    setupEventHandlers();

    uiInitialized = true;

    initializeDefaultSettings();

    NavigationManager.getInstance().setRoot(root);
  }


  protected void initializeLayout() {
    layout = new GridPane();
    layout.setHgap(20);
    layout.setVgap(20);
    layout.setPadding(new Insets(30, 40, 40, 40));
    layout.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
    root.setCenter(layout);
  }

  protected void setupBackgroundImage() {
    try {
      Image backgroundImage = new Image(Objects.requireNonNull(
          getClass().getResourceAsStream(getBackgroundImagePath())));

      BackgroundImage background = new BackgroundImage(
          backgroundImage,
          BackgroundRepeat.NO_REPEAT,
          BackgroundRepeat.NO_REPEAT,
          BackgroundPosition.CENTER,
          new BackgroundSize(1.0, 1.0, true, true, false, false)
      );

      StackPane backgroundPane = new StackPane();
      backgroundPane.setBackground(new Background(background));
      backgroundPane.setOpacity(0.3);

      layout.getChildren().addFirst(backgroundPane);
      GridPane.setRowSpan(backgroundPane, Integer.MAX_VALUE);
      GridPane.setColumnSpan(backgroundPane, Integer.MAX_VALUE);

      logger.info("Background image loaded successfully");
    } catch (Exception e) {
      logger.error("Failed to load background image: {}", e.getMessage());
    }
  }

  protected void setupTitleAndDescription() {
    titleLabel = new Label(getViewTitle());
    titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
    titleLabel.setTextFill(Color.web(SECONDARY_COLOR));

    descriptionLabel = new Label(getViewDescription());
    descriptionLabel.setFont(Font.font("System", FontWeight.NORMAL, 16));
    descriptionLabel.setTextFill(Color.web(SECONDARY_COLOR));
  }

  protected void setupButtons() {
    startGameButton = createNavigationButton("Start Game", START_BUTTON_COLOR, "white");
    backButton = createNavigationButton("Back", BACK_BUTTON_COLOR, SECONDARY_COLOR);
  }

  protected Button createNavigationButton(String text, String bgColor, String textColor) {
    Button btn = new Button(text);
    btn.setStyle("-fx-font-size: 14px; " +
        "-fx-padding: 10 20; " +
        "-fx-min-width: 120px; " +
        "-fx-background-color: " + bgColor + "; " +
        "-fx-text-fill: " + textColor + "; " +
        "-fx-background-radius: 5px;");
    return btn;
  }

  protected void layoutComponents() {
    BorderPane mainContainer = new BorderPane();
    mainContainer.setPadding(new Insets(20));

    VBox contentBox = new VBox(20);
    contentBox.setMaxWidth(600);
    contentBox.setAlignment(Pos.CENTER);
    contentBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.9); " +
        "-fx-background-radius: 10; " +
        "-fx-padding: 20;");

    DropShadow shadow = new DropShadow();
    shadow.setColor(Color.color(0, 0, 0, 0.3));
    shadow.setRadius(10);
    shadow.setOffsetX(0);
    shadow.setOffsetY(2);
    contentBox.setEffect(shadow);

    VBox gameSettingsSection = createGameSettingsSection();
    HBox buttonBox = createNavigationButtonBox();

    contentBox.getChildren().addAll(gameSettingsSection, buttonBox);

    mainContainer.setCenter(contentBox);

    layout.add(mainContainer, 0, 2);
    GridPane.setHgrow(mainContainer, Priority.ALWAYS);
    GridPane.setVgrow(mainContainer, Priority.ALWAYS);

    centerTitleAndDescription();
  }

  protected void centerTitleAndDescription() {
    StackPane titlePane = new StackPane(titleLabel);
    titlePane.setAlignment(Pos.CENTER);
    layout.add(titlePane, 0, 0);
    GridPane.setHgrow(titlePane, Priority.ALWAYS);

    StackPane descPane = new StackPane(descriptionLabel);
    descPane.setAlignment(Pos.CENTER);
    layout.add(descPane, 0, 1);
    GridPane.setHgrow(descPane, Priority.ALWAYS);
  }

  protected HBox createNavigationButtonBox() {
    HBox buttonBox = new HBox(20, backButton, startGameButton);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.setPadding(new Insets(20, 0, 0, 0));
    return buttonBox;
  }

  @Override
  public void navigateBack() {
    NavigationManager.getInstance().navigateBack();
    logger.info("Navigated back");
  }

  public BorderPane getRoot() {
    return root;
  }

  protected abstract String getBackgroundImagePath();

  protected abstract String getViewTitle();

  protected abstract String getViewDescription();

  protected abstract void setupGameSpecificControls();

  protected abstract VBox createGameSettingsSection();

  protected abstract void setupEventHandlers();

  protected abstract void initializeDefaultSettings();
}
