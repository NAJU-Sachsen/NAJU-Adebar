package de.naju.adebar.web.validation.notifications;

import javax.validation.constraints.NotEmpty;
import de.naju.adebar.app.news.ReleaseNotes;

/**
 * Form describing new {@link ReleaseNotes}
 * 
 * @author Rico Bergmann
 */
public class ReleaseNotesForm {

  private String title;

  @NotEmpty
  private String description;

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "ReleaseNotesForm [title=" + title + ", description=" + description + "]";
  }

}
