package edu.ntnu.idi.idatt.controller;

import org.slf4j.Logger;

public abstract class AbstractController {
  protected final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

  protected abstract void navigateTo(String viewName);
  protected abstract void navigateBack();

  protected abstract void initialize();

}
