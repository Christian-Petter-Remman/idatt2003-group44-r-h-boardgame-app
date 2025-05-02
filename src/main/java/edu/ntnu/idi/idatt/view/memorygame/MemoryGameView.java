package edu.ntnu.idi.idatt.view.memorygame;

import java.util.Objects;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import edu.ntnu.idi.idatt.model.memorygame.MemoryBoardGame;
import edu.ntnu.idi.idatt.model.memorygame.MemoryCard;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameObserver;
import edu.ntnu.idi.idatt.model.memorygame.MemoryGameSettings;
import edu.ntnu.idi.idatt.model.memorygame.MemoryPlayer;
import java.util.List;
import java.util.function.Consumer;

public class MemoryGameView extends BorderPane implements MemoryGameObserver {

  private final Label player1Label;
  private final Label player2Label;
  private final ImageView[] cardViews;
  private Consumer<Integer> onCardClick;

  public MemoryGameView(MemoryGameSettings settings) {
    int rows = settings.getBoardSize().getRows();
    int cols = settings.getBoardSize().getCols();
    this.cardViews = new ImageView[rows * cols];

    // Scoreboard
    player1Label = new Label();
    player2Label = new Label();
    HBox scoreboard = new HBox(20, player1Label, player2Label);
    scoreboard.setPadding(new Insets(10));
    setTop(scoreboard);

    // Grid of cards
    GridPane grid = new GridPane();
    grid.setHgap(5);
    grid.setVgap(5);
    grid.setPadding(new Insets(10));
    for (int i = 0; i < rows * cols; i++) {
      ImageView iv = new ImageView();
      iv.setFitWidth(80);
      iv.setFitHeight(80);
      final int idx = i;
      iv.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
        if (onCardClick != null) onCardClick.accept(idx);
      });
      cardViews[i] = iv;
      grid.add(iv, i % cols, i / cols);
    }
    setCenter(grid);
  }

  public void setOnCardClick(Consumer<Integer> handler) {
    this.onCardClick = handler;
  }

  @Override
  public void onBoardUpdated(MemoryBoardGame board) {
    Platform.runLater(() -> render(board));
  }

  @Override
  public void onGameOver(List<MemoryPlayer> winners) {
    Platform.runLater(() -> {
      Alert a = new Alert(Alert.AlertType.INFORMATION);
      a.setHeaderText("Game Over");
      StringBuilder sb = new StringBuilder("Winner");
      if (winners.size() > 1) sb.append("s");
      sb.append(": ");
      winners.forEach(p -> sb.append(p.getName()).append(" "));
      a.setContentText(sb.toString());
      a.showAndWait();
    });
  }

  public void render(MemoryBoardGame board) {
    List<MemoryCard> cards = board.getCards();
    for (int i = 0; i < cards.size(); i++) {
      MemoryCard c = cards.get(i);
      String imagePath = (c.isFaceUp() || c.isMatched())
          ? c.getImagePath()
          : "/images/card_back.png";
      cardViews[i].setImage(new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(imagePath))
      ));
    }
    List<MemoryPlayer> players = board.getPlayers();
    player1Label.setText(players.get(0).getName() + ": " + players.get(0).getScore());
    player2Label.setText(players.get(1).getName() + ": " + players.get(1).getScore());
  }


  public Parent getRoot() {
    return this;
  }
}
