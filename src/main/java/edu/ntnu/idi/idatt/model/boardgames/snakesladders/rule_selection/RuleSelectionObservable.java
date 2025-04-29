package edu.ntnu.idi.idatt.model.boardgames.snakesladders.rule_selection;


import edu.ntnu.idi.idatt.view.snakesandladders.SalRuleSelectionViewObserver;

public interface RuleSelectionObservable {
  void addObserver(SalRuleSelectionViewObserver observer);
  void removeObserver(SalRuleSelectionViewObserver observer);
  void notifyObservers();
}

