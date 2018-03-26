package de.naju.adebar.app.news;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.util.Assert;
import de.naju.adebar.documentation.infrastructure.JpaOnly;

/**
 * Abstraction of release notes. A release note consists of a description of what was changed and
 * may optionally also feature some cool title of the release. Instances are immutable.
 *
 * @author Rico Bergmann
 */
@Entity(name = "releaseNews")
public class ReleaseNotes extends AbstractAggregateRoot<ReleaseNotes> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "title")
  private String title;

  @Column(name = "description", length = 1023)
  private String description;

  @Column(name = "date")
  private LocalDateTime date;

  @Column(name = "active")
  private boolean active;

  /**
   * Creates a new set of release notes with title only.
   *
   * @param description
   */
  public ReleaseNotes(String description) {
    this("", description);
  }

  /**
   * Creates a new set of release notes with title and description.
   *
   * @param title the release notes' title. May be {@code null}
   * @param description
   */
  public ReleaseNotes(String title, String description) {
    Assert.hasText(description, "Description must be given");

    this.title = title != null //
        ? title //
        : "";
    this.description = description;
    this.date = LocalDateTime.now();
    this.active = true;
    registerEvent(ReleaseNotesPublishedEvent.of(this));
  }

  /**
   * Default constructor just for JPA
   */
  @JpaOnly
  private ReleaseNotes() {
  }

  /**
   * @return the release title. May be empty but never {@code null}
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the notes' title
   */
  protected void setTitle(String title) {
    this.title = title != null //
        ? title //
        : "";
  }

  /**
   * @return the change notes. Will always contain text.
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description the notes' description
   */
  protected void setDescription(String description) {
    Assert.hasText(description, "Description may not be empty");
    this.description = description;
  }

  /**
   * @return the date the release (or at least the release notes) where published
   */
  public LocalDateTime getDate() {
    return date;
  }

  /**
   * @param date the release notes' date of publication
   */
  protected void setDate(LocalDateTime date) {
    this.date = date;
  }

  /**
   * @return whether the notes describe the latest release or if they are outdated (because there
   *     was a newer release or it has been a long time since the last release and the notes are
   *     therefore not really relevant anymore)
   */
  public boolean isActive() {
    return active;
  }

  /**
   * @param active whether the release notes are still relevant
   */
  protected void setActive(boolean active) {
    this.active = active;
  }

  /**
   * Marks the release notes as outdated
   */
  public void archive() {
    this.active = false;
  }

  /**
   * @return the ID. Just for JPA's sake
   */
  @JpaOnly
  private long getId() {
    return id;
  }

  /**
   * @param id the ID. Just for JPA's sake
   */
  @JpaOnly
  private void setId(long id) {
    this.id = id;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ReleaseNotes other = (ReleaseNotes) obj;
    if (date == null) {
      if (other.date != null) {
        return false;
      }
    } else if (!date.equals(other.date)) {
      return false;
    }
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else if (!description.equals(other.description)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return String.format("Release notes (%s): %s from %tc", title, description, date);
  }

}
