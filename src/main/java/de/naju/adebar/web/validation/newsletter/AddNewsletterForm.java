package de.naju.adebar.web.validation.newsletter;

import javax.validation.constraints.NotNull;

/**
 * @author Rico Bergmann
 */
public class AddNewsletterForm {

  public enum Belonging {
    NONE, CHAPTER, EVENT, PROJECT
  }

  @NotNull
  private String name;

  @NotNull
  private Belonging belonging;

  private long localGroup;

  public AddNewsletterForm() {
    this.name = "";
    this.belonging = Belonging.NONE;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBelonging() {
    return belonging.toString();
  }

  public void setBelonging(String belonging) {
    this.belonging = Belonging.valueOf(belonging);
  }

  public long getLocalGroup() {
    return localGroup;
  }

  public void setLocalGroup(long localGroup) {
    this.localGroup = localGroup;
  }

  public boolean belongsToChapter() {
    return belonging == Belonging.CHAPTER;
  }
}
