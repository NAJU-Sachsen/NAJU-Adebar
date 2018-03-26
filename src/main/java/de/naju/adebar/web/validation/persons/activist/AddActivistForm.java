package de.naju.adebar.web.validation.persons.activist;

import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.persons.details.JuleicaCard;
import de.naju.adebar.web.validation.AbstractForm;
import de.naju.adebar.web.validation.persons.AddPersonForm;

/**
 * POJO representation of the activist-related data of the {@link AddPersonForm}
 *
 * @author Rico Bergmann
 * @see AddPersonForm
 */
public class AddActivistForm implements AbstractForm {

  private boolean juleica;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate juleicaExpiryDate;

  private String juleicaLevel;
  private List<LocalGroup> localGroups;

  /**
   * Full constructor
   *
   * @param juleica the activist's juleica card. May be {@code null} if the person has none
   * @param localGroups the local groups the activist is part of
   */
  public AddActivistForm(JuleicaCard juleica, List<LocalGroup> localGroups) {
    this.juleica = juleica != null;

    if (this.juleica) {
      this.juleicaExpiryDate = juleica.getExpiryDate();
      this.juleicaLevel = juleica.getLevel();
    }

    this.localGroups = localGroups;
  }

  /**
   * Default constructor
   */
  public AddActivistForm() {}

  /**
   * @return whether the activist has a Juleica-card
   */
  public boolean isJuleica() {
    return juleica;
  }

  /**
   * @param juleica whether the activist has a Juleica-card
   */
  public void setJuleica(boolean juleica) {
    this.juleica = juleica;
  }

  /**
   * @return the expiry date of the Juleica-card. May be {@code null} if the activist has no juleica
   *         or if its expiry date is unknown.
   */
  public LocalDate getJuleicaExpiryDate() {
    return juleicaExpiryDate;
  }

  /**
   * @param juleicaExpiryDate the expiry date of the Juleica-card. May be {@code null} if the
   *        activist has no juleica or if its expiry date is unknown.
   */
  public void setJuleicaExpiryDate(LocalDate juleicaExpiryDate) {
    this.juleicaExpiryDate = juleicaExpiryDate;
  }

  /**
   * @return the level of the Juleica-card ({@code G} or {@code L}). May be {@code null} if the
   *         activist has no juleica or if its level is unknown
   */
  public String getJuleicaLevel() {
    return juleicaLevel;
  }

  /**
   * @param juleicaLevel the level of the Juleica-card ({@code G} or {@code L}). May be {@code
   *     null} if the activist has no juleica or if its level is unknown
   */
  public void setJuleicaLevel(String juleicaLevel) {
    this.juleicaLevel = juleicaLevel;
  }

  /**
   * @return the local groups the activist is part of
   */
  public List<LocalGroup> getLocalGroups() {
    return localGroups;
  }

  /**
   * @param localGroups the local groups the activist is part of
   */
  public void setLocalGroups(List<LocalGroup> localGroups) {
    this.localGroups = localGroups;
  }

  /**
   * @return whether the activist has a juleica and its expiry date was set
   */
  public boolean hasJuleicaExpiryDate() {
    return juleica && juleicaExpiryDate != null;
  }

  /**
   * @return whether the activist has a juleica and its level was set
   */
  public boolean hasJuleicaLevel() {
    return juleica && juleicaLevel != null && !juleicaLevel.isEmpty();
  }

  /**
   * @return whether the activist is part of some local groups
   */
  public boolean hasLocalGroups() {
    return localGroups != null && !localGroups.isEmpty();
  }

  @Override
  public boolean hasData() {
    return juleica || hasJuleicaExpiryDate() || hasJuleicaLevel() || hasLocalGroups();
  }

  @Override
  public String toString() {
    return "AddActivistForm [" + "juleica=" + juleica + ", juleicaExpiryDate=" + juleicaExpiryDate
        + ", juleicaLevel='" + juleicaLevel + '\'' + ", localGroups=" + localGroups + ']';
  }
}
