package edu.ntnu.idi.idatt.view.common.intro.dialogs;

/**
 * <h1>DialogConfig</h1>
 *
 * <p>Represents the configuration for dialogs on the main menu in the application, including its
 * ID, title, body
 * text, fun fact, image path, audio file, call-to-action label, and action.
 */
public class DialogConfig {

  private String id;
  private String title;
  private final String body;
  private final String funFact;
  private final String imagePath;
  private final String audio;
  private final String ctaLabel;
  private final String ctaAction;

  /**
   * <h2>Constructor</h2>
   *
   * <p>Creates a new DialogConfig instance with the specified parameters.
   *
   * @param body      the body text of the dialog
   * @param funFact   a fun fact to be displayed in the dialog
   * @param imagePath the path to the image associated with the dialog
   * @param audio     the audio file associated with the dialog
   * @param ctaLabel  the label for the call-to-action button
   * @param ctaAction the action to be performed when the call-to-action button is clicked
   */
  public DialogConfig(String body, String funFact, String imagePath, String audio, String ctaLabel,
      String ctaAction) {
    this.body = body;
    this.funFact = funFact;
    this.imagePath = imagePath;
    this.audio = audio;
    this.ctaLabel = ctaLabel;
    this.ctaAction = ctaAction;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getBody() {
    return body;
  }

  public String getFunFact() {
    return funFact;
  }

  public String getImagePath() {
    return imagePath;
  }

  public String getAudio() {
    return audio;
  }

  public String getCtaLabel() {
    return ctaLabel;
  }

  public String getCtaAction() {
    return ctaAction;
  }

}