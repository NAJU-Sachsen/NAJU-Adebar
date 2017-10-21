package de.naju.adebar.model.human;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import org.springframework.util.Assert;

/**
 * Referents are persons who can give lectures or host field trips and the like.
 *
 * @author Rico Bergmann
 */
@Entity(name = "referent")
public class ReferentProfile {

  @EmbeddedId
  @Column(name = "personId")
  private PersonId personId;

  @ManyToMany(cascade = CascadeType.ALL)
  private Map<String, Qualification> qualifications;

  // constructors

  /**
   * Each referent profile has to be created based on an existing person.
   *
   * @param person the person to create the profile for
   */
  ReferentProfile(Person person) {
    Assert.notNull(person, "Id may not be null");
    this.personId = person.getId();
    qualifications = new HashMap<>();
  }

  /**
   * Private constructor just for JPA's sake
   */
  @SuppressWarnings("unused")
  private ReferentProfile() {}

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
   * @return all qualifications the person possesses
   */
  public Iterable<Qualification> getQualifications() {
    return qualifications.values();
  }

  /**
   * @param qualifications all qualifications the person possesses
   */
  protected void setQualifications(List<Qualification> qualifications) {
    qualifications.forEach(q -> this.qualifications.put(q.getName(), q));
  }

  // normal methods

  /**
   * @param qualification the qualification to apply to the person
   * @throws IllegalArgumentException if the qualification is {@code null}
   * @throws ExistingQualificationException if the referent already has such a qualification
   */
  public void addQualification(Qualification qualification) {
    Assert.notNull(qualification, "Qualification to add may not be null");
    if (hasQualification(qualification)) {
      throw new ExistingQualificationException("Person is already qualified for " + qualification);
    }
    qualifications.put(qualification.getName(), qualification);
  }

  /**
   * @param qualification the qualification to check
   * @return {@code true} if the referent has such a qualification or {@code false} otherwise
   */
  public boolean hasQualification(Qualification qualification) {
    // a qualification's name is guaranteed to be unique. Therefore we only need to check for the
    // key
    return qualifications.containsKey(qualification.getName());
  }

  /**
   * @param qualification the qualification to withdraw
   * @throws IllegalArgumentException if the referent does not have such a qualification
   */
  public void removeQualification(Qualification qualification) {
    if (!hasQualification(qualification)) {
      throw new IllegalArgumentException("Person has no such qualification: " + qualification);
    }
    qualifications.remove(qualification.getName());
  }
}
