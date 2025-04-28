package edu.ntnu.idi.idatt.view.snakesandladders;

import edu.ntnu.idi.idatt.model.boardgames.snakesladders.rule_selection.SalRuleSelectionModel;
import edu.ntnu.idi.idatt.controller.snakesandladders.SalRuleSelectionController;
import edu.ntnu.idi.idatt.view.AbstractView;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.HashMap;
import java.util.Map;

public class SalRuleSelectionView extends AbstractView implements SalRuleSelectionModel.Observer {

  private final SalRuleSelectionModel model;
  private final SalRuleSelectionController controller;
  private Parent root;

  private final ToggleGroup boardToggleGroup = new ToggleGroup();
  private final ToggleGroup diceToggleGroup = new ToggleGroup();
  private final Map<String, RadioButton> boardRadioButtons = new HashMap<>();

  private final RadioButton oneDieRadio = new RadioButton("1 Die");
  private final RadioButton twoDiceRadio = new RadioButton("2 Dice");

  public SalRuleSelectionView(SalRuleSelectionModel model, SalRuleSelectionController controller) {
    this.model = model;
    this.controller = controller;
    model.addObserver(this);

    VBox layout = new VBox();
    layout.setSpacing(24);
    layout.setPadding(new Insets(32, 32, 32, 32));

    // Board selection
    Label boardLabel = new Label("Select Board:");
    VBox boardBox = new VBox(8);

    for (String boardFile : model.getAvailableBoards()) {
      String displayName = SalRuleSelectionModel.getDisplayName(boardFile);
      RadioButton rb = new RadioButton(displayName);
      rb.setToggleGroup(boardToggleGroup);
      rb.setUserData(boardFile);
      boardBox.getChildren().add(rb);
      boardRadioButtons.put(boardFile, rb);
    }

    // Dice selection
    Label diceLabel = new Label("Select Number of Dice:");
    oneDieRadio.setToggleGroup(diceToggleGroup);
    twoDiceRadio.setToggleGroup(diceToggleGroup);

    HBox diceBox = new HBox(16, oneDieRadio, twoDiceRadio);

    layout.getChildren().addAll(boardLabel, boardBox, diceLabel, diceBox);

    this.root = layout;

    // Initial selection
    updateBoardSelection();
    updateDiceSelection();

    // UI listeners using controller now
    boardToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
      if (newToggle != null) {
        String selectedFile = (String) newToggle.getUserData();
        if (!selectedFile.equals(model.getSelectedBoardFile())) {
          controller.onBoardSelected(selectedFile);
        }
      }
    });

    diceToggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
      if (newToggle != null) {
        int dice = newToggle == twoDiceRadio ? 2 : 1;
        if (dice != model.getDiceCount()) {
          controller.onDiceCountSelected(dice);
        }
      }
    });
  }

  @Override
  public void onRuleSelectionChanged() {
    updateBoardSelection();
    updateDiceSelection();
  }

  private void updateBoardSelection() {
    String selected = model.getSelectedBoardFile();
    if (selected != null && boardRadioButtons.containsKey(selected)) {
      boardRadioButtons.get(selected).setSelected(true);
    }
  }

  private void updateDiceSelection() {
    if (model.getDiceCount() == 2) {
      twoDiceRadio.setSelected(true);
    } else {
      oneDieRadio.setSelected(true);
    }
  }

  public ToggleGroup getBoardToggleGroup() {
    return boardToggleGroup;
  }

  public ToggleGroup getDiceToggleGroup() {
    return diceToggleGroup;
  }

  @Override
  protected void createUI() { }

  @Override
  protected void setupEventHandlers() { }

  @Override
  protected void applyInitialUIState() { }

  @Override
  public Parent getRoot() {
    return root;
  }
}
