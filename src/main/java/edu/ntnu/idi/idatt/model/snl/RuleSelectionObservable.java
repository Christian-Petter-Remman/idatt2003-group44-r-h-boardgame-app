package edu.ntnu.idi.idatt.model.snl;


import edu.ntnu.idi.idatt.view.snl.SNLRuleSelectionViewObserver;

public interface RuleSelectionObservable {
  void addObserver(SNLRuleSelectionViewObserver observer);
  void removeObserver(SNLRuleSelectionViewObserver observer);
  void notifyObservers();
}

