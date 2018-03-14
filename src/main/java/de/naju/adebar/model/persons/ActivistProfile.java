package de.naju.adebar.model.persons;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.persons.details.JuleicaCard;
import de.naju.adebar.model.persons.events.PersonDataUpdatedEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.springframework.util.Assert;

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

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "localGroupMembers", //
      joinColumns = @JoinColumn(name = "memberId"), //
      inverseJoinColumns = @JoinColumn(name = "localGroupId"))
  private List<LocalGroup> localGroups;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "projectContributors", //
      joinColumns = @JoinColumn(name = "contributorId"), //
      inverseJoinColumns = @JoinColumn(name = "projectId"))
  private List<Project> projects;

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
  @JpaOnly
  private ActivistProfile() {
  }

  /**
   * @return the ID of the person to whom this profile belongs
   */
  public PersonId getPersonId() {
    return personId;
  }

  /**
   * @param personId the ID of the person to whom this profile belongs.
   */
  @JpaOnly
  private void setPersonId(PersonId personId) {
    this.personId = personId;
  }

  /**
   * @return the activist's Juleica card. May be {@code null} if the person does not have a Juleica
   *     card
   */
  public JuleicaCard getJuleicaCard() {
    return juleicaCard;
  }

  /**
   * @param juleicaCard the activist's Juleica card. May be {@code null} if the person does not
   *     have a Juleica card
   */
  protected void setJuleicaCard(JuleicaCard juleicaCard) {
    this.juleicaCard = juleicaCard;
  }

  /**
   * @return {@code true} if the activist has a Juleica card, or {@code false} otherwise
   */
  public boolean hasJuleica() {
    return juleicaCard != null;
  }

  /**
   * @return {@code true} if the activist's Juleica card is valid (i.e. not expired), {@code false}
   *     otherwise
   */
  public boolean hasValidJuleica() {
    if (!hasJuleica()) {
      return false;
    }
    return juleicaCard.isValid();
  }

  /**
   * @return the local groups the person is active
   */
  public Collection<LocalGroup> getLocalGroups() {
    return Collections.unmodifiableList(localGroups);
  }

  /**
   * @param localGroups the local groups the person is active in
   */
  @JpaOnly
  private void setLocalGroups(List<LocalGroup> localGroups) {
    this.localGroups = localGroups;
  }

  /**
   * @return the projects the person is active in
   */
  public Collection<Project> getProjects() {
    return Collections.unmodifiableList(projects);
  }

  /**
   * @param projects the projects the person is active in
   */
  @JpaOnly
  private void setProjects(List<Project> projects) {
    this.projects = projects;
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
