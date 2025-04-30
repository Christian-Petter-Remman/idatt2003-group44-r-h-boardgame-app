package edu.ntnu.idi.idatt.view.memorygame;

import edu.ntnu.idi.idatt.model.common.memorygame.*;
import edu.ntnu.idi.idatt.model.common.character_selection.CharacterSelectionData;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class MemoryGameView extends BorderPane implements MemoryGameObserver {
  private final MemoryGame model;
  private final CardView[][] cardGrid;
  private final Label statusLabel = new Label("Click Start to begin");
  private final HBox scoreBox = new HBox(10);
  private final Button startButton = new Button("Start");
  private final GridPane gridPane;

  public MemoryGameView(MemoryGame model) {
    this.model = model;
    MemoryBoard board = model.getBoard();
    int rows = board.getRowCount(), cols = board.getColCount();

    // Build grid pane
    gridPane = new GridPane();
    gridPane.setHgap(5); gridPane.setVgap(5); gridPane.setPadding(new Insets(10));

    cardGrid = new CardView[rows][cols];
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        CardView cv = new CardView();
        cardGrid[r][c] = cv;
        gridPane.add(cv, c, r);

        // User clicks flip
        final int row = r, col = c;
        cv.setOnMouseClicked((MouseEvent e) -> {
          if (model.getState() == GameState.IN_PROGRESS) {
            try { model.flipCard(row, col); }
            catch (IllegalArgumentException ignore) {}
          }
        });
      }
    }
  }

  // Call this to build and show the UI; the controller should use this.
  public void show(Stage stage) {
    // Register self as observer
    model.addObserver(this);

    // Top bar
    startButton.setOnAction(e -> model.start());
    HBox topBar = new HBox(10, statusLabel, startButton);
    topBar.setPadding(new Insets(10));

    // Bottom scores
    scoreBox.setPadding(new Insets(10));

    // Assemble layout
    setTop(topBar);
    setCenter(gridPane);
    setBottom(scoreBox);

    stage.setScene(new Scene(this));
    stage.setTitle("Memory Game");
    stage.show();
  }

  @Override
  public void onCardFlipped(int row, int col, CardState state) {
    MemoryCard card = model.getBoard().getCard(row, col);
    CardView cv = cardGrid[row][col];
    if (state == CardState.FACE_UP || state == CardState.MATCHED) {
      CharacterSelectionData icon = card.getIcon();
      cv.showFace(new Image(icon.getImagePath()));
    } else {
      cv.showBack();

    }
  }

  @Override
  public void onMatchFound(MemoryPlayer player, int newScore) {
    Label lbl = findOrCreateScoreLabel(player.getName());
    lbl.setText(player.getName() + ": " + newScore);
  }

  @Override
  public void onTurnChanged(MemoryPlayer nextPlayer) {
    statusLabel.setText("Turn: " + nextPlayer.getName());
  }

  @Override
  public void onGameStateChanged(GameState newState) {
    if (newState == GameState.IN_PROGRESS) {
      startButton.setDisable(true);
      statusLabel.setText("Game in progress");
    } else if (newState == GameState.FINISHED) {
      startButton.setDisable(false);
    }
  }

  @Override
  public void onGameFinished(List<MemoryPlayer> winners) {
    String names = winners.stream()
        .map(MemoryPlayer::getName)
        .collect(Collectors.joining(", "));
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setHeaderText("Game Over");
    alert.setContentText("Winner(s): " + names);
    alert.showAndWait();
  }

  private Label findOrCreateScoreLabel(String playerName) {
    for (Node node : scoreBox.getChildren()) {
      if (node instanceof Label && ((Label) node).getText().startsWith(playerName + ":")) {
        return (Label) node;
      }
    }
    Label lbl = new Label(playerName + ": 0");
    scoreBox.getChildren().add(lbl);
    return lbl;
  }

  // Simple card UI component with back face and image face.
  private static class CardView extends StackPane {
    private final Rectangle back = new Rectangle(80, 80);
    private final ImageView face = new ImageView();

    public CardView() {
      back.setArcWidth(10);
      back.setArcHeight(10);
      back.setFill(Color.GRAY);
      face.setFitWidth(80);
      face.setFitHeight(80);
      getChildren().addAll(back, face);
      showBack();
    }

    public void showFace(Image img) {
      face.setImage(img);
      back.setVisible(false);
    }

    public void showBack() {
      face.setImage(null);
      back.setVisible(true);
    }
  }
  public Button getStartButton(){
    return startButton;
  }

  public Node[][] getCardSlots(){
    return cardGrid;
  }
}
