package de.naju.adebar.model.persons;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.model.persons.events.PersonDataUpdatedEvent;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import org.springframework.util.Assert;

/**
 * Special information about parents - mainly extra phone numbers - will be stored here.
 *
 * @author Rico Bergmann
 */
@Entity(name = "parent")
public class ParentProfile extends AbstractProfile {

  @EmbeddedId
  private PersonId id;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "value", column = @Column(name = "landlinePhone"))})
  private PhoneNumber landlinePhone;

  @Embedded
  @AttributeOverrides({@AttributeOverride(name = "value", column = @Column(name = "workPhone"))})
  private PhoneNumber workPhone;

  /**
   * Each parent profile has to be created in terms of an existing person.
   * <p>
   * Note that this does not actually check, if the person is parent of somebody.
   *
   * @param person the parent to create the profile for
   */
  ParentProfile(Person person) {
    Assert.notNull(person, "person may not be null");
    this.id = person.getId();
    provideRelatedPerson(person);
  }

  /**
   * Each parent profile has to be created in terms of an existing person.
   * <p>
   * Note that this does not actually check, if the person is parent of somebody.
   *
   * @param person the parent to create the profile for
   * @param landlinePhone the parent's phone number at home
   * @param workPhone the parent's phone number at work
   */
  ParentProfile(Person person, PhoneNumber landlinePhone, PhoneNumber workPhone) {
    Assert.notNull(person, "person may not be null");
    this.id = person.getId();
    this.landlinePhone = landlinePhone;
    this.workPhone = workPhone;
    provideRelatedPerson(person);
  }

  /**
   * Default constructor just for JPA's sake
   */
  @JpaOnly
  private ParentProfile() {}

  /**
   * @return the ID of the person to whom the profile belongs
   */
  public PersonId getId() {
    return id;
  }

  /**
   * @param id the ID of the person this profile belongs to
   */
  @JpaOnly
  private void setId(PersonId id) {
    this.id = id;
  }

  /**
   * @return the parent's phone number at home. If none is specified, {@code null} will be returned.
   */
  public PhoneNumber getLandlinePhone() {
    return landlinePhone;
  }

  /**
   * @param landlinePhone the parent's phone number at home
   */
  protected void setLandlinePhone(PhoneNumber landlinePhone) {
    this.landlinePhone = landlinePhone;
  }

  /**
   * @return the parent's phone number at work. If none is specified, {@code null} will be returned.
   */
  public PhoneNumber getWorkPhone() {
    return workPhone;
  }

  /**
   * @param workPhone the parent's phone number at work
   */
  protected void setWorkPhone(PhoneNumber workPhone) {
    this.workPhone = workPhone;
  }

  /**
   * @return whether the parent has a special phone number at home
   */
  public boolean hasLandlinePhone() {
    return landlinePhone != null;
  }

  /**
   * @return whether the parent has a special phone number at work
   */
  public boolean hasWorkPhone() {
    return workPhone != null;
  }

  /**
   * Replaces the current landline phone number
   *
   * @param landline the new phone number. May also be {@code null} if no number exists.
   * @return the profile for the new phone number
   */
  public ParentProfile updateLandlinePhone(PhoneNumber landline) {
    setLandlinePhone(landline);

    getRelatedPerson().ifPresent( //
        person -> registerEventIfPossible(PersonDataUpdatedEvent.forPerson(person)));

    return this;
  }

  /**
   * Replaces the current work phone number
   *
   * @param workPhone the new phone number. May also be {@code null} if no number exists.
   * @return the profile for the new number
   */
  public ParentProfile updateWorkPhone(PhoneNumber workPhone) {
    setWorkPhone(workPhone);

    getRelatedPerson().ifPresent( //
        person -> registerEventIfPossible(PersonDataUpdatedEvent.forPerson(person)));

    return this;
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
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    ParentProfile other = (ParentProfile) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
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
    return "ParentProfile [id=" + id + ", landlinePhone=" + landlinePhone + ", workPhone="
        + workPhone + "]";
  }

}
