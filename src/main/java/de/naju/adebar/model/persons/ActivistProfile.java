package de.naju.adebar.model.persons;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.springframework.util.Assert;
import de.naju.adebar.model.persons.details.JuleicaCard;
import de.naju.adebar.model.persons.events.PersonDataUpdatedEvent;

/**
 * Activists are persons who contribute to events, e.g. organize them or work as counselors or
 * 'work' for our society. For now we only need to keep track of Juleica-cards and their expiry
 * dates.
 *
 * @author Rico Bergmann
 */
@Entity(name = "activist")
public class ActivistProfile extends AbstractProfile {

  @EmbeddedId
  @Column(name = "personId")
  private PersonId personId;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "expiryDate", column = @Column(name = "juleicaExpiryDate")),
      @AttributeOverride(name = "level", column = @Column(name = "juleicaLevel"))})
  private JuleicaCard juleicaCard;

  /**
   * Each activist profile has to be created in terms of an existing person.
   *
   * @param person the person to create the profile for
   */
  ActivistProfile(Person person) {
    Assert.notNull(person, "Id may not be null");
    this.personId = person.getId();
    provideRelatedPerson(person);
  }

  /**
   * Convenience constructor to avoid creating a new profile and then setting its Juleica card
   * through a call to {@link #updateJuleicaCard(JuleicaCard)} right after.
   *
   * @param person the person to create the profile for
   * @param juleica the new juleica card
   */
  ActivistProfile(Person person, JuleicaCard juleica) {
    Assert.notNull(person, "Id may not be null");
    this.personId = person.getId();
    this.juleicaCard = juleica;
    provideRelatedPerson(person);
  }

  /**
   * Default constructor just for JPA's sake
   */
  @SuppressWarnings("unused")
  private ActivistProfile() {}

  /**
   * @return the ID of the person to whom this profile belongs
   */
  public PersonId getPersonId() {
    return personId;
  }

  /**
   * @return the activist's Juleica card. May be {@code null} if the person does not have a Juleica
   *         card
   */
  public JuleicaCard getJuleicaCard() {
    return juleicaCard;
  }

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

  /**
   * Replaces the current Juleica card
   *
   * @param juleica the new card
   * @return the profile for the new card
   */
  public ActivistProfile updateJuleicaCard(JuleicaCard juleica) {
    setJuleicaCard(juleica);

    getRelatedPerson().ifPresent( //
        person -> registerEventIfPossible(PersonDataUpdatedEvent.forPerson(person)));

    return this;
  }

  /**
   * @param juleicaCard the activist's Juleica card. May be {@code null} if the person does not have
   *        a Juleica card
   */
  protected void setJuleicaCard(JuleicaCard juleicaCard) {
    this.juleicaCard = juleicaCard;
  }

  /**
   * @param personId the ID of the person to whom this profile belongs.
   */
  @SuppressWarnings("unused")
  private void setPersonId(PersonId personId) {
    this.personId = personId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

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
