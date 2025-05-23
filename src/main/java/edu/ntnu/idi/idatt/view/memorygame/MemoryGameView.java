package edu.ntnu.idi.idatt.view.memorygame;


import edu.ntnu.idi.idatt.model.memorygame.MemoryBoardGame;
import edu.ntnu.idi.idatt.model.memorygame.MemoryCard;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameObserver;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.model.memorygame.MemoryPlayer;
import edu.ntnu.idi.idatt.view.common.intro.dialogs.WinnerDialogs;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


/**
 * <h1>MemoryGameView</h1>
 *
 * <p>JavaFX view class that renders the memory game board, handles user interaction, displays
 * scores,
 * turn information, and responds to updates in the game model.
 *
 * <P>AI: active involvement as sparring partner and created underlying frame for view development.
 * </P>
 */
public class MemoryGameView implements MemoryGameObserver {

  private BorderPane root;
  private ImageView[] cardViews;
  private boolean[] previousFace;
  private Label turnLabel;
  private Label player1Score;
  private Label player2Score;
  private HBox player1Pairs;
  private HBox player2Pairs;
  private Consumer<Integer> onCardClick;
  private Runnable onQuit;
  private Runnable onRestart;

  /**
   * Initializes the memory game UI using the specified game settings.
   *
   * @param settings the memory game settings
   */
  public void initialize(MemoryGameSettings settings) {
    root = new BorderPane();
    root.getStyleClass().add("memory-root");
    root.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/css/MemoryGameStyleSheet.css"))
            .toExternalForm());

    setupHeader();
    setupGrid(settings);
    setupSidebar(settings);
    setupInstructionsAndQuit();
  }

  /**
   * Returns the root node of this view.
   *
   * @return the root parent node
   */
  public Parent getRoot() {
    return root;
  }

  /**
   * Sets the handler to invoke when a card is clicked.
   *
   * @param handler the click consumer
   */
  public void setOnCardClick(Consumer<Integer> handler) {
    this.onCardClick = handler;
  }

  /**
   * Sets the handler to invoke when the quit button is clicked.
   *
   * @param handler the quit handler
   */
  public void setOnQuit(Runnable handler) {
    this.onQuit = handler;
  }

  /**
   * Sets the handler to invoke when the restart button is clicked.
   *
   * @param handler the restart handler
   */
  public void setOnRestart(Runnable handler) {
    this.onRestart = handler;
  }

  /**
   * Called when the game board is updated.
   *
   * @param board the current game board
   */
  @Override
  public void onBoardUpdated(MemoryBoardGame board) {
    Platform.runLater(() -> render(board));
  }

  /**
   * Called when the game is over.
   *
   * @param winners list of winning players
   */
  @Override
  public void onGameOver(List<MemoryPlayer> winners) {
    new WinnerDialogs().showMemoryWinnerDialog(winners);
  }

  /**
   * Renders the full UI based on the current state of the game board.
   *
   * @param board the memory game board to render
   */
  public void render(MemoryBoardGame board) {
    List<MemoryPlayer> players = board.getPlayers();
    turnLabel.setText("Turn: " + players.get(board.getCurrentPlayerIndex()).getName());
    player1Score.setText("Pairs: " + players.get(0).getScore());
    player2Score.setText("Pairs: " + players.get(1).getScore());

    Map<Integer, Set<String>> matchedBy = board.getCards().stream()
        .filter(MemoryCard::isMatched)
        .collect(Collectors.groupingBy(
            MemoryCard::getMatchedBy,
            Collectors.mapping(MemoryCard::getId, Collectors.toSet())
        ));

    updatePairIcons(player1Pairs, board, matchedBy.getOrDefault(0, Set.of()));
    updatePairIcons(player2Pairs, board, matchedBy.getOrDefault(1, Set.of()));

    List<MemoryCard> cards = board.getCards();
    for (int i = 0; i < cardViews.length; i++) {
      MemoryCard card = cards.get(i);
      boolean showFace = card.isFaceUp() || card.isMatched();
      String path = showFace ? card.getImagePath() : "/images/card_back.png";

      if (previousFace[i] != showFace) {
        animateFlip(i, path, showFace);
      }
    }
  }

  /**
   * Adds the appropriate pair icons to the display box.
   *
   * @param box   the container for pair icons
   * @param board the game board
   * @param ids   the set of matched card IDs
   */
  private void updatePairIcons(HBox box, MemoryBoardGame board, Set<String> ids) {
    box.getChildren().clear();
    ids.stream()
        .map(id -> board.getCards().stream()
            .filter(card -> card.getId().equals(id))
            .findFirst()
            .orElse(null))
        .filter(Objects::nonNull)
        .forEach(card -> {
          ImageView iv = new ImageView(new Image(
              Objects.requireNonNull(getClass().getResourceAsStream(card.getImagePath()))));
          iv.setFitWidth(24);
          iv.setFitHeight(24);
          box.getChildren().add(iv);
        });
  }

  /**
   * Applies a flip animation to a card.
   *
   * @param index        the card index
   * @param imagePath    the new image path
   * @param newFaceState the new face-up state
   */
  private void animateFlip(int index, String imagePath, boolean newFaceState) {
    ImageView iv = cardViews[index];
    iv.setScaleX(1);
    iv.setScaleY(1);

    ScaleTransition hide = new ScaleTransition(Duration.millis(200), iv);
    hide.setFromX(1);
    hide.setToX(0);

    ScaleTransition show = new ScaleTransition(Duration.millis(200), iv);
    show.setFromX(0);
    show.setToX(1);

    hide.setOnFinished(e -> {
      iv.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))));
      show.play();
    });

    show.setOnFinished(e -> previousFace[index] = newFaceState);

    hide.play();
  }

  // -- Helper UI builders --

  private void setupHeader() {
    Label header = new Label("Memory Match!");
    header.getStyleClass().add("header");
    HBox top = new HBox(header);
    top.setAlignment(Pos.CENTER);
    top.setPadding(new Insets(10));
    root.setTop(top);
  }

  private void setupGrid(MemoryGameSettings settings) {
    int rows = settings.getBoardSize().getRows();
    int cols = settings.getBoardSize().getCols();
    GridPane grid = new GridPane();
    grid.getStyleClass().add("grid");
    grid.setHgap(5);
    grid.setVgap(5);
    grid.setAlignment(Pos.CENTER);

    cardViews = new ImageView[rows * cols];
    previousFace = new boolean[rows * cols];

    for (int i = 0; i < rows * cols; i++) {
      ImageView iv = new ImageView(new Image(
          Objects.requireNonNull(getClass().getResourceAsStream("/images/card_back.png"))));
      iv.getStyleClass().add("card");
      iv.setFitWidth(80);
      iv.setFitHeight(80);
      iv.setPreserveRatio(true);
      final int idx = i;
      iv.setOnMouseClicked(e -> {
        if (onCardClick != null) {
          onCardClick.accept(idx);
        }
      });
      cardViews[i] = iv;
      grid.add(iv, i % cols, i / cols);
    }

    root.setCenter(grid);
  }

  private void setupSidebar(MemoryGameSettings settings) {
    turnLabel = new Label("Turn:");
    turnLabel.getStyleClass().add("turn-label");

    player1Score = new Label("Pairs: 0");
    player1Score.getStyleClass().add("sb-score");
    player2Score = new Label("Pairs: 0");
    player2Score.getStyleClass().add("sb-score");

    player1Pairs = new HBox(5);
    player1Pairs.getStyleClass().add("sb-pairs");
    player2Pairs = new HBox(5);
    player2Pairs.getStyleClass().add("sb-pairs");

    VBox p1 = new VBox(4, new Label(settings.getPlayers().getFirst().getName()), player1Score,
        player1Pairs);
    VBox p2 = new VBox(4, new Label(settings.getPlayers().get(1).getName()), player2Score,
        player2Pairs);

    p1.getStyleClass().add("player-box");
    p2.getStyleClass().add("player-box");
    p1.setPadding(new Insets(5));
    p2.setPadding(new Insets(5));

    VBox left = new VBox(10, turnLabel, new Label("Scoreboard"), p1, p2);
    left.getStyleClass().add("scoreboard-box");
    left.setPadding(new Insets(10));
    left.setPrefWidth(250);
    root.setLeft(left);
  }

  private void setupInstructionsAndQuit() {
    Label instrTitle = new Label("How to Play");
    instrTitle.getStyleClass().add("instr-title");

    Label instrText = new Label("""
        • Click a card to flip it
        • Then click a second card
        • If they match, you go again
        • Otherwise turn passes
        • Find all pairs to win!
        """);
    instrText.getStyleClass().add("instr-text");
    instrText.setWrapText(true);

    VBox instr = new VBox(5, instrTitle, instrText);
    instr.getStyleClass().add("instr-box");
    instr.setPadding(new Insets(10));

    Button restartButton = new Button("Restart Game");
    restartButton.getStyleClass().add("restart-button");
    restartButton.setOnAction(e -> {
      if (onRestart != null) {
        onRestart.run();
      }
    });

    Button quitButton = new Button("Quit Game");
    quitButton.getStyleClass().add("quit-button");
    quitButton.setOnAction(e -> {
      if (onQuit != null) {
        onQuit.run();
      }
    });

    VBox right = new VBox(20, instr, restartButton);
    right.setPadding(new Insets(10));
    right.setAlignment(Pos.TOP_CENTER);
    root.setRight(right);

    HBox bottom = new HBox(quitButton);
    bottom.getStyleClass().add("footer-box");
    bottom.setPadding(new Insets(10));
    root.setBottom(bottom);
  }
}