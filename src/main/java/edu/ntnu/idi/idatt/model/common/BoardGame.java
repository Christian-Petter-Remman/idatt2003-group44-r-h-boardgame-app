package edu.ntnu.idi.idatt.model.common;

import edu.ntnu.idi.idatt.model.modelobservers.BoardObserver;
import edu.ntnu.idi.idatt.model.modelobservers.GameObserver;
import edu.ntnu.idi.idatt.model.modelobservers.GameScreenObserver;
import edu.ntnu.idi.idatt.model.modelobservers.PathDecisionObserver;
import edu.ntnu.idi.idatt.model.stargame.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>BoardGame</h1>
 *
 * <p>Abstract class representing a board game with players, turn handling, dice rolling, and
 * observer
 * patterns for handling events like player movement, scoring, and winning.
 */
public abstract class BoardGame {

  protected List<Player> players;
  protected AbstractBoard board;
  protected Dice dice;
  protected int currentPlayerIndex;

  protected final List<GameObserver> observers = new ArrayList<>();
  protected final List<PathDecisionObserver> pathDecisionObservers = new ArrayList<>();
  protected final List<GameScreenObserver> turnObservers = new ArrayList<>();
  protected final List<GameScreenObserver> moveObservers = new ArrayList<>();
  protected final List<GameScreenObserver> winnerObservers = new ArrayList<>();
  protected final List<BoardObserver> boardObservers = new ArrayList<>();

  /**
   * <h2>Constructor</h2>
   *
   * <p>Initializes a new board game with the given board and an empty player list.
   *
   * @param board The board to be used in the game.
   */
  public BoardGame(AbstractBoard board) {
    this.board = board;
    this.players = new ArrayList<>();
    this.dice = new Dice(1);
    this.currentPlayerIndex = 0;
  }

  /**
   * <h2>getWinner</h2>
   *
   * <p>Returns the player who has won the game, if any.
   *
   * @return The winning player, or {@code null} if no player has won.
   */
  public Player getWinner() {
    return players.stream()
        .filter(Player::hasWon)
        .findFirst()
        .orElse(null);
  }

  /**
   * <h2>initializePlayer</h2>
   *
   * <p>Sets the player list and resets the current player index.
   *
   * @param players The list of players to participate in the game.
   */
  public void initializePlayer(List<Player> players) {
    this.players = players;
    this.currentPlayerIndex = 0;
  }

  /**
   * <h2>addPlayer</h2>
   *
   * <p>Adds a player to the game.
   *
   * @param player The player object to add.
   */
  public void addPlayer(Player player) {
    this.players.add(player);
  }

  /**
   * <h2>getPlayers</h2>
   *
   * <p>Returns the list of all players in the game.
   *
   * @return The list of players.
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * <h2>getCurrentPlayer</h2>
   *
   * <p>Returns the player whose turn it currently is.
   *
   * @return The current player.
   */
  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  /**
   * <h2>getDiceCount</h2>
   *
   * <p>Returns the number of dice used in the game.
   *
   * @return The number of dice.
   */
  public int getDiceCount() {
    return dice.getDiceCount();
  }

  /**
   * <h2>getCurrentPlayerIndex</h2>
   *
   * <p>Returns the index of the current player.
   *
   * @return The index of the player whose turn it is.
   */
  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  /**
   * <h2>nextTurn</h2>
   *
   * <p>Advances to the next player's turn.
   */
  public void nextTurn() {
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  /**
   * <h2>getBoard</h2>
   *
   * <p>Returns the board used in this game.
   *
   * @return The game board.
   */
  public AbstractBoard getBoard() {
    return board;
  }

  /**
   * <h2>addTurnObserver</h2>
   *
   * <p>Registers an observer for turn-based events.
   *
   * @param observer The observer to register.
   */
  public void addTurnObserver(GameScreenObserver observer) {
    turnObservers.add(observer);
  }

  /**
   * <h2>addMoveObserver</h2>
   *
   * <p>Registers an observer for move-based events.
   *
   * @param observer The observer to register.
   */
  public void addMoveObserver(GameScreenObserver observer) {
    moveObservers.add(observer);
  }

  /**
   * <h2>addWinnerObserver</h2>
   *
   * <p>Registers an observer for winner events.
   *
   * @param observer The observer to register.
   */
  public void addWinnerObserver(GameScreenObserver observer) {
    winnerObservers.add(observer);
  }

  /**
   * <h2>notifyMoveObservers</h2>
   *
   * <p>Notifies move and board observers about a player's move.
   *
   * @param player The player who moved.
   * @param roll   The dice result.
   */
  public void notifyMoveObservers(Player player, int roll) {
    moveObservers.forEach(observer -> {
      observer.onDiceRolled(roll);
      observer.onPlayerPositionChanged(player, -1, player.getPosition());
      observer.onPlayerTurnChanged(player);
    });

    boardObservers.forEach(observer ->
        observer.onPlayerMoved(player, -1, player.getPosition())
    );
  }

  /**
   * <h2>notifyWinnerObservers</h2>
   *
   * <p>Notifies all registered observers that a player has won.
   *
   * @param player The winning player.
   */
  public void notifyWinnerObservers(Player player) {
    winnerObservers.forEach(observer -> observer.onGameOver(player));
  }

  /**
   * <h2>notifyPathDecisionRequested</h2>
   *
   * <p>Notifies relevant observers that a path decision is required by a player.
   *
   * @param player The player who needs to make a decision.
   * @param path   The current path requiring a decision.
   */
  public void notifyPathDecisionRequested(Player player, Path path) {
    observers.forEach(o -> o.onPathDecisionRequested(player, path));
    pathDecisionObservers.forEach(o -> o.onPathDecisionRequested(player, path));
  }

  /**
   * <h2>notifyStarObservers</h2>
   *
   * <p>Notifies observers when a star is respawned on the board.
   *
   * @param player  The player triggering the star event.
   * @param newTile The tile number where the star respawned.
   */
  public void notifyStarObservers(Player player, int newTile) {
    observers.forEach(o -> o.onStarRespawned(player, newTile));
  }

  /**
   * <h2>notifyScoreObservers</h2>
   *
   * <p>Notifies observers that a player's score has changed.
   *
   * @param player The player whose score changed.
   * @param score  The new score.
   */
  public void notifyScoreObservers(Player player, int score) {
    observers.forEach(o -> o.onScoreChanged(player, score));
  }
}