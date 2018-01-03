package de.naju.adebar.app.news;

import org.springframework.util.Assert;

/**
 * Event indicating that some new release notes are available
 * 
 * @author Rico Bergmann
 */
public class ReleaseNotesPublishedEvent {

  private final ReleaseNotes notes;

  /**
   * Creates a new event
   * 
   * @param notes the new notes. May not be {@code null}
   * @return the event
   */
  public static ReleaseNotesPublishedEvent of(ReleaseNotes notes) {
    return new ReleaseNotesPublishedEvent(notes);
  }

  /**
   * @param notes the notes
   * @throws IllegalArgumentException if the notes are {@code null}
   */
  private ReleaseNotesPublishedEvent(ReleaseNotes notes) {
    Assert.notNull(notes, "Release notes may not be null");
    this.notes = notes;
  }

  /**
   * @return the notes that are attached to this event
   */
  public final ReleaseNotes getNotes() {
    return notes;
  }

  @Override
  public String toString() {
    return "ReleaseNotesPublishedEvent for" + notes;
  }

}
