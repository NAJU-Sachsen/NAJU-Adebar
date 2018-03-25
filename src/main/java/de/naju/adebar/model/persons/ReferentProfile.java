package de.naju.adebar.model.persons;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import org.springframework.util.Assert;
import de.naju.adebar.model.persons.events.PersonDataUpdatedEvent;
import de.naju.adebar.model.persons.qualifications.ExistingQualificationException;
import de.naju.adebar.model.persons.qualifications.Qualification;
import de.naju.adebar.util.Maps;

/**
 * Referents are persons who can give lectures or host field trips and the like.
 *
 * @author Rico Bergmann
 */
@Entity(name = "referent")
public class ReferentProfile extends AbstractProfile {

  @EmbeddedId
  @Column(name = "personId")
  private PersonId personId;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @MapKey
  private Map<String, Qualification> qualifications;

  /**
   * Each referent profile has to be created based on an existing person.
   *
   * @param person the person to create the profile for
   */
  ReferentProfile(Person person) {
    Assert.notNull(person, "Id may not be null");
    this.personId = person.getId();
    this.qualifications = new HashMap<>();
    provideRelatedPerson(person);
  }

  /**
   * Convenience constructor to create a referent profile and initialize it right away
   *
   * @param person the person to create the profile for
   * @param qualifications the person's qualifications
   */
  ReferentProfile(Person person, Collection<Qualification> qualifications) {
    Assert.notNull(person, "Id may not be null");
    this.personId = person.getId();
    this.qualifications = Maps.fromCollection(qualifications, Qualification::getName);
    provideRelatedPerson(person);
  }

  /**
   * Private constructor just for JPA's sake
   */
  @SuppressWarnings("unused")
  private ReferentProfile() {}

  /**
   * @return the ID of the person to whom this profile belongs
   */
  public PersonId getPersonId() {
    return personId;
  }

  /**
   * @return all qualifications the person possesses
   */
  public Iterable<Qualification> getQualifications() {
    return qualifications.values();
  }

  /**
   * @param qualification the qualification to check
   * @return {@code true} if the referent has such a qualification or {@code false} otherwise
   */
  public boolean hasQualification(Qualification qualification) {
    // a qualification's name is guaranteed to be unique. Therefore we only
    // need to check for the key
    return qualifications.containsKey(qualification.getName());
  }

  /**
   * @param qualification the qualification to apply to the person
   * @return the updated profile
   * @throws IllegalArgumentException if the qualification is {@code null}
   * @throws ExistingQualificationException if the referent already has such a qualification
   */
  public ReferentProfile addQualification(Qualification qualification) {
    Assert.notNull(qualification, "Qualification to add may not be null");
    if (hasQualification(qualification)) {
      throw new ExistingQualificationException("Person is already qualified for " + qualification);
    }
    qualifications.put(qualification.getName(), qualification);

    getRelatedPerson().ifPresent( //
        person -> registerEventIfPossible(PersonDataUpdatedEvent.forPerson(person)));

    return this;
  }

  /**
   * @param qualification the qualification to withdraw
   * @return the updated profile
   * @throws IllegalArgumentException if the referent does not have such a qualification
   */
  public ReferentProfile removeQualification(Qualification qualification) {
    if (!hasQualification(qualification)) {
      throw new IllegalArgumentException("Person has no such qualification: " + qualification);
    }
    qualifications.remove(qualification.getName());

    getRelatedPerson().ifPresent( //
        person -> registerEventIfPossible(PersonDataUpdatedEvent.forPerson(person)));

    return this;
  }

  /**
   * @param qualifications all qualifications the person possesses
   */
  protected void setQualifications(List<Qualification> qualifications) {
    qualifications.forEach(q -> this.qualifications.put(q.getName(), q));
  }

  /**
   * @param personId the ID of the person to whom this profile belongs.
   */
  @SuppressWarnings("unused")
  private void setPersonId(PersonId personId) {
    this.personId = personId;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(Object)
   */
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    ReferentProfile profile = (ReferentProfile) o;

    return personId.equals(profile.personId);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return personId.hashCode();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ReferentProfile [personId=" + personId + ", qualifications=" + qualifications + "]";
  }

}
