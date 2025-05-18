package edu.ntnu.idi.idatt.view.memorygame;

import edu.ntnu.idi.idatt.model.memorygame.MemoryBoardGame;
import edu.ntnu.idi.idatt.model.memorygame.MemoryCard;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameObserver;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.model.memorygame.MemoryPlayer;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import edu.ntnu.idi.idatt.view.common.intro.dialogs.WinnerDialogs;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MemoryGameView implements MemoryGameObserver {
  private BorderPane root;
  private ImageView[] cardViews;
  private boolean[] previousFace;
  private Label turnLabel, player1Score, player2Score;
  private HBox player1Pairs, player2Pairs;
  private Consumer<Integer> onCardClick;
  private Runnable onQuit, onRestart;

  public MemoryGameView() { }

  public void initialize(MemoryGameSettings settings) {
    root = new BorderPane();
    root.getStyleClass().add("memory-root");
    root.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/css/MemoryGameStyleSheet.css")).toExternalForm()
    );

    Label header = new Label("Memory Match!");
    header.getStyleClass().add("header");
    HBox top = new HBox(header);
    top.setAlignment(Pos.CENTER);
    top.setPadding(new Insets(10));
    root.setTop(top);

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
      ImageView iv = new ImageView();
      iv.getStyleClass().add("card");
      iv.setFitWidth(80);
      iv.setFitHeight(80);
      iv.setPreserveRatio(true);
      final int idx = i;
      iv.setOnMouseClicked(e -> {
        if (onCardClick != null) onCardClick.accept(idx);
      });
      cardViews[i] = iv;
      grid.add(iv, i % cols, i / cols);
      previousFace[i] = false;
    }

    Image back = new Image(
        Objects.requireNonNull(getClass().getResourceAsStream("/images/card_back.png"))
    );
    for (ImageView iv : cardViews) {
      iv.setImage(back);
    }

    root.setCenter(grid);

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

    VBox p1 = new VBox(4,
        new Label(settings.getPlayers().getFirst().getName()),
        player1Score, player1Pairs
    );
    p1.getStyleClass().add("player-box");
    p1.setPadding(new Insets(5));

    VBox p2 = new VBox(4,
        new Label(settings.getPlayers().get(1).getName()),
        player2Score, player2Pairs
    );
    p2.getStyleClass().add("player-box");
    p2.setPadding(new Insets(5));

    VBox left = new VBox(10,
        turnLabel, new Label("Scoreboard"), p1, p2
    );
    left.getStyleClass().add("scoreboard-box");
    left.setPadding(new Insets(10));
    left.setPrefWidth(250);
    root.setLeft(left);

    Label instrTitle = new Label("How to Play");
    instrTitle.getStyleClass().add("instr-title");
    Label instrText = new Label(
        """
            • Click a card to flip it
            • Then click a second card
            • If they match, you go again
            • Otherwise turn passes
            • Find all pairs to win!"""
    );
    instrText.getStyleClass().add("instr-text");
    instrText.setWrapText(true);
    VBox instr = new VBox(5, instrTitle, instrText);
    instr.getStyleClass().add("instr-box");
    instr.setPadding(new Insets(10));

    Button restartButton = new Button("Restart Game");
    restartButton.getStyleClass().add("restart-button");
    restartButton.setOnAction(e -> {
      if (onRestart != null) onRestart.run();
    });

    VBox right = new VBox(20, instr, restartButton);
    right.setPadding(new Insets(10));
    right.setAlignment(Pos.TOP_CENTER);
    root.setRight(right);

    Button quitButton = new Button("Quit Game");
    quitButton.getStyleClass().add("quit-button");
    quitButton.setOnAction(e -> {
      if (onQuit != null) onQuit.run();
    });
    HBox bottom = new HBox(quitButton);
    bottom.getStyleClass().add("footer-box");
    bottom.setPadding(new Insets(10));
    root.setBottom(bottom);
  }

  public Parent getRoot() {
    return root;
  }

  public void setOnCardClick(Consumer<Integer> h) {
    this.onCardClick = h;
  }

  public void setOnQuit(Runnable h) {
    this.onQuit = h;
  }

  public void setOnRestart(Runnable h) {
    this.onRestart = h;
  }

  @Override
  public void onBoardUpdated(MemoryBoardGame board) {
    Platform.runLater(() -> render(board));
  }

  @Override
  public void onGameOver(List<MemoryPlayer> winners) {
    WinnerDialogs dialogs = new WinnerDialogs();
    dialogs.showMemoryWinnerDialog(winners);
  }

  public void render(MemoryBoardGame board) {
    List<MemoryPlayer> ps = board.getPlayers();
    turnLabel.setText("Turn: " + ps.get(board.getCurrentPlayerIndex()).getName());
    player1Score.setText("Pairs: " + ps.get(0).getScore());
    player2Score.setText("Pairs: " + ps.get(1).getScore());

    Set<String> p1 = new HashSet<>(), p2 = new HashSet<>();
    for (MemoryCard c : board.getCards()) {
      if (c.isMatched()) {
        if (c.getMatchedBy() == 0) p1.add(c.getId());
        else                       p2.add(c.getId());
      }
    }
    player1Pairs.getChildren().clear();
    player2Pairs.getChildren().clear();
    for (String id : p1) addPairIcon(player1Pairs, board, id);
    for (String id : p2) addPairIcon(player2Pairs, board, id);

    List<MemoryCard> cards = board.getCards();
    for (int i = 0; i < cardViews.length; i++) {
      MemoryCard c = cards.get(i);
      boolean showFace = c.isFaceUp() || c.isMatched();
      String path    = showFace ? c.getImagePath() : "/images/card_back.png";
      if (previousFace[i] != showFace) {
        animateFlip(i, path, showFace);
      }
    }
  }

  private void addPairIcon(HBox box, MemoryBoardGame b, String id) {
    String img = b.getCards().stream()
        .filter(c -> c.getId().equals(id))
        .findFirst()
        .get().getImagePath();
    ImageView iv = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream(img))
    ));
    iv.setFitWidth(24);
    iv.setFitHeight(24);
    box.getChildren().add(iv);
  }

  private void animateFlip(int index, String path, boolean newFaceState) {
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
      iv.setImage(new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(path))
      ));
      show.play();
    });

    show.setOnFinished(e -> previousFace[index] = newFaceState);

    hide.play();
  }
}
