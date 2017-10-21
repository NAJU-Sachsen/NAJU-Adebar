package de.naju.adebar.model.human;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.springframework.util.Assert;

/**
 * Activists are persons who contribute to events, e.g. organize them or work as counselors or
 * 'work' for our society. For now we only need to keep track of Juleica-cards and their expiry
 * dates.
 *
 * @author Rico Bergmann
 */
@Entity(name = "activist")
public class ActivistProfile implements Cloneable {

  @EmbeddedId
  @Column(name = "personId")
  private PersonId personId;

  @Embedded
  @Column(name = "juleica")
  private JuleicaCard juleicaCard;

  // constructors

  /**
   * Each activist profile has to be created in terms of an existing person.
   *
   * @param person the person to create the profile for
   */
  ActivistProfile(Person person) {
    Assert.notNull(person, "Id may not be null");
    this.personId = person.getId();
  }

  private ActivistProfile(PersonId id) {
    this.personId = id;
  }

  /**
   * Default constructor just for JPA's sake
   */
  @SuppressWarnings("unused")
  private ActivistProfile() {}

  // getter and setter

  /**
   * @return the ID of the person to whom this profile belongs
   */
  public PersonId getPersonId() {
    return personId;
  }

  /**
   * @param personId the ID of the person to whom this profile belongs.
   */
  @SuppressWarnings("unused")
  private void setPersonId(PersonId personId) {
    this.personId = personId;
  }

  /**
   * @return the activist's Juleica card. May be {@code null} if the person does not have a Juleica
   *         card
   */
  public JuleicaCard getJuleicaCard() {
    return juleicaCard;
  }

  /**
   * @param juleicaCard the activist's Juleica card. May be {@code null} if the person does not have
   *        a Juleica card
   */
  public void setJuleicaCard(JuleicaCard juleicaCard) {
    this.juleicaCard = juleicaCard;
  }

  // normal methods

  /**
   * @return {@code true} if the activist has a Juleica card, or {@code false} otherwise
   */
  public boolean hasJuleica() {
    return juleicaCard != null;
  }

  /**
   * @return {@code true} if the activist's Juleica card is valid (i.e. not expired), {@code false}
   *         otherwise
   */
  public boolean hasValidJuleica() {
    if (!hasJuleica()) {
      return false;
    }
    return juleicaCard.isValid();
  }

  // "implementation" of Cloneable

  @Override
  public ActivistProfile clone() {
    ActivistProfile clonedProfile = new ActivistProfile(personId);

    if (hasJuleica()) {
      clonedProfile.setJuleicaCard(juleicaCard.clone());
    }

    return clonedProfile;
  }

  // overridden from Object

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ActivistProfile profile = (ActivistProfile) o;

    return personId.equals(profile.personId);
  }

  @Override
  public int hashCode() {
    return personId.hashCode();
  }

  @Override
  public String toString() {
    return "ActivistProfile{" + "personId=" + personId + ", juleicaCard=" + juleicaCard + '}';
  }
}
