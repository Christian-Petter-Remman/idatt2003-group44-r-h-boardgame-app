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
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MemoryGameView extends BorderPane implements MemoryGameObserver {

  private final GridPane grid;
  private final ImageView[] cardViews;
  private final Label turnLabel;
  private final Label player1Score;
  private final Label player2Score;
  private final HBox player1Pairs;
  private final HBox player2Pairs;
  private Consumer<Integer> onCardClick;
  private Runnable onQuit;

  public MemoryGameView(MemoryGameSettings settings) {
    getStyleClass().add("memory-root");
    getStylesheets().add(
        getClass().getResource("/css/MemoryGameStyleSheet.css").toExternalForm()
    );

    Label title = new Label("Memory Match Game!");
    title.getStyleClass().add("header");
    HBox titleBox = new HBox(title);
    titleBox.setAlignment(Pos.CENTER);
    titleBox.setPadding(new Insets(10));
    setTop(titleBox);

    int rows = settings.getBoardSize().getRows();
    int cols = settings.getBoardSize().getCols();
    grid = new GridPane();
    grid.getStyleClass().add("grid");
    grid.setHgap(5);
    grid.setVgap(5);
    grid.setAlignment(Pos.CENTER);

    cardViews = new ImageView[rows * cols];
    for (int i = 0; i < rows * cols; i++) {
      ImageView iv = new ImageView();
      iv.getStyleClass().add("card");
      iv.setFitWidth(80);
      iv.setFitHeight(80);
      iv.setPreserveRatio(true);
      final int idx = i;
      iv.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
        if (onCardClick != null) {
          onCardClick.accept(idx);
        }
      });
      cardViews[i] = iv;
      grid.add(iv, i % cols, i / cols);
    }
    setCenter(grid);

    turnLabel = new Label("Turn: ");
    turnLabel.getStyleClass().add("turn-label");

    player1Score = new Label("Pairs: 0");
    player1Score.getStyleClass().add("sb-score");
    player2Score = new Label("Pairs: 0");
    player2Score.getStyleClass().add("sb-score");

    player1Pairs = new HBox(5);
    player1Pairs.getStyleClass().add("sb-pairs");
    player2Pairs = new HBox(5);
    player2Pairs.getStyleClass().add("sb-pairs");

    Label sbTitle = new Label("Scoreboard");
    sbTitle.getStyleClass().add("sb-title");

    VBox p1 = new VBox(4,
        new Label(settings.getPlayers().get(0).getName()),
        player1Score, player1Pairs);
    p1.getStyleClass().add("player-box");

    VBox p2 = new VBox(4,
        new Label(settings.getPlayers().get(1).getName()),
        player2Score, player2Pairs);
    p2.getStyleClass().add("player-box");

    VBox scoreboard = new VBox(10, turnLabel, sbTitle, p1, p2);
    scoreboard.getStyleClass().add("scoreboard-box");
    scoreboard.setPadding(new Insets(10));
    setLeft(scoreboard);

    Button quit = new Button("Quit Game");
    quit.getStyleClass().add("quit-button");
    quit.setOnAction(e -> {
      if (onQuit != null) {
        onQuit.run();
      }
    });
    HBox footer = new HBox(quit);
    footer.getStyleClass().add("footer-box");
    footer.setAlignment(Pos.CENTER_LEFT);
    footer.setPadding(new Insets(10));
    setBottom(footer);
  }

  public void setOnCardClick(Consumer<Integer> handler) {
    this.onCardClick = handler;
  }

  public void setOnQuit(Runnable handler) {
    this.onQuit = handler;
  }

  @Override
  public void onBoardUpdated(MemoryBoardGame board) {
    Platform.runLater(() -> render(board));
  }

  @Override
  public void onGameOver(List<MemoryPlayer> winners) {
    Platform.runLater(() -> {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setHeaderText(winners.size() > 1 ? "Winners!" : "Winner!");
      StringBuilder names = new StringBuilder();
      winners.forEach(p -> names.append(p.getName()).append(" "));
      alert.setContentText(names.toString());
      alert.showAndWait();
    });
  }

  public void render(MemoryBoardGame board) {
    List<MemoryPlayer> players = board.getPlayers();
    turnLabel.setText("Turn: " + players.get(board.getCurrentPlayerIndex()).getName());
    player1Score.setText("Pairs: " + players.get(0).getScore());
    player2Score.setText("Pairs: " + players.get(1).getScore());

    Set<String> p1Ids = new HashSet<>(), p2Ids = new HashSet<>();
    for (MemoryCard c : board.getCards()) {
      if (c.isMatched()) {
        if (c.getMatchedBy() == 0) {
          p1Ids.add(c.getId());
        } else {
          p2Ids.add(c.getId());
        }
      }
    }
    player1Pairs.getChildren().clear();
    player2Pairs.getChildren().clear();
    for (String id : p1Ids) {
      ImageView iv = new ImageView(new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(
              board.getCards().stream()
                  .filter(c -> c.getId().equals(id))
                  .findFirst()
                  .get()
                  .getImagePath()
          ))
      ));
      iv.setFitWidth(24);
      iv.setFitHeight(24);
      iv.getStyleClass().add("pair-icon");
      player1Pairs.getChildren().add(iv);
    }
    for (String id : p2Ids) {
      ImageView iv = new ImageView(new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(
              board.getCards().stream()
                  .filter(c -> c.getId().equals(id))
                  .findFirst()
                  .get()
                  .getImagePath()
          ))
      ));
      iv.setFitWidth(24);
      iv.setFitHeight(24);
      iv.getStyleClass().add("pair-icon");
      player2Pairs.getChildren().add(iv);
    }

    for (int i = 0; i < cardViews.length; i++) {
      MemoryCard c = board.getCards().get(i);
      String path = (c.isFaceUp() || c.isMatched())
          ? c.getImagePath()
          : "/images/card_back.png";
      cardViews[i].setImage(new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(path))
      ));
    }
  }

  public Parent getRoot() {
    return this;
  }
}
