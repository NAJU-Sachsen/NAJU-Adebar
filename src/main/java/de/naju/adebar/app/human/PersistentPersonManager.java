package de.naju.adebar.app.human;

import com.google.common.collect.Lists;
import de.naju.adebar.model.human.ActivistProfile;
import de.naju.adebar.model.human.ActivistProfileRepository;
import de.naju.adebar.model.human.NoReferentException;
import de.naju.adebar.model.human.ParticipantProfile;
import de.naju.adebar.model.human.ParticipantProfileRepository;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.PersonFactory;
import de.naju.adebar.model.human.PersonId;
import de.naju.adebar.model.human.PersonRepository;
import de.naju.adebar.model.human.Qualification;
import de.naju.adebar.model.human.QualificationRepository;
import de.naju.adebar.model.human.ReadOnlyPersonRepository;
import de.naju.adebar.model.human.ReferentProfile;
import de.naju.adebar.model.human.ReferentProfileRepository;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * A {@link PersonManager} that persists its data in a database
 *
 * @author Rico Bergmann
 */
@Service
public class PersistentPersonManager implements PersonManager {

  private PersonRepository personRepo;
  private ReadOnlyPersonRepository roRepo;
  private PersonFactory personFactory;
  private ParticipantProfileRepository participantRepo;
  private ActivistProfileRepository activistRepo;
  private ReferentProfileRepository referentRepo;
  private QualificationRepository qualificationRepo;

  @Autowired
  public PersistentPersonManager(PersonRepository personRepo,
      @Qualifier("ro_personRepo") ReadOnlyPersonRepository roRepo, PersonFactory personFactory,
      ParticipantProfileRepository participantRepo, ActivistProfileRepository activistRepo,
      ReferentProfileRepository referentRepo, QualificationRepository qualificationRepo) {
    Object[] params = {personRepo, roRepo, personFactory, participantRepo, activistRepo,
        referentRepo, qualificationRepo};
    Assert.noNullElements(params, "At lest one parameter was null: " + Arrays.toString(params));

    this.personRepo = personRepo;
    this.roRepo = roRepo;
    this.personFactory = personFactory;
    this.participantRepo = participantRepo;
    this.activistRepo = activistRepo;
    this.referentRepo = referentRepo;
    this.qualificationRepo = qualificationRepo;
  }

  @Override
  public Person savePerson(Person person) {
    return personRepo.save(person);
  }

  @Override
  public Person createPerson(String firstName, String lastName, String email) {
    Person p = personFactory.buildNew(firstName, lastName, email).create();
    return savePerson(p);
  }

  @Override
  public Person createPerson(String firstName, String lastName, String email, boolean participant,
      boolean activist, boolean referent) {
    Person p = personFactory.buildNew(firstName, lastName, email).create();
    if (participant) {
      p.makeParticipant();
    }
    if (activist) {
      p.makeActivist();
    }
    if (referent) {
      p.makeReferent();
    }
    return savePerson(p);
  }

  @Override
  public Person updatePerson(PersonId personId, Person newPerson) {
    Person p = personRepo.findOne(personId);
    p = updatePersonData(p, newPerson);

    updateParticipantProfile(p, newPerson);
    updateActivistProfile(p, newPerson);
    updateReferentProfile(p, newPerson);

    return savePerson(p);
  }

  @Override
  public Optional<Person> findPerson(String id) {
    PersonId personId = new PersonId(id);
    Person p = personRepo.findOne(personId);
    return p != null ? Optional.of(p) : Optional.empty();
  }

  @Override
  public void deactivatePerson(Person person) {
    if (person.isActivist() || person.isReferent()) {
      throw new IllegalStateException(
          "Person may not be deactivated as it is a referent or activist: " + person);
    }
    person.setArchived(true);
    updatePerson(person.getId(), person);
  }

  @Override
  public void addQualificationToPerson(Person person, Qualification qualification) {
    if (!person.isReferent()) {
      throw new NoReferentException("Person is no referent: " + person);
    }
    Qualification qualificationToAdd;

    if (qualificationRepo.exists(qualification.getName())) {
      qualificationToAdd = qualificationRepo.findOne(qualification.getName());
    } else {
      qualificationToAdd = qualification;
    }
    person.getReferentProfile().addQualification(qualificationToAdd);
    updatePerson(person.getId(), person);
  }

  @Override
  public ReadOnlyPersonRepository repository() {
    return roRepo;
  }

  /**
   * Replaces the data of a person
   *
   * @param person the person to update
   * @param newData the person with the new data to use
   */
  protected Person updatePersonData(Person person, Person newData) {
    if (person == newData) {
      return person;
    } else if (person.getId().equals(newData.getId())) {
      return newData;
    }
    try {
      Method changeId = Person.class.getDeclaredMethod("setId", PersonId.class);
      changeId.setAccessible(true);
      changeId.invoke(newData, person.getId());
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Error during invocation of reflection", e);
    }
    return newData;
  }

  /**
   * Replaces the participation profile of a person if specified. Otherwise the database will be
   * cleaned.
   *
   * @param person the person to update
   * @param newPerson the new person data
   */
  protected void updateParticipantProfile(Person person, Person newPerson) {
    if (newPerson.isParticipant()) {
      person.setParticipantProfile(
          doUpdateParticipantProfile(person, newPerson.getParticipantProfile()));
    } else {
      person.setParticipantProfile(null);
      if (participantRepo.exists(person.getId())) {
        participantRepo.delete(person.getId());
      }
    }
  }

  /**
   * Replaces the participation profile of a person
   *
   * @param person the person to update
   * @param newProfile the new profile to use
   */
  protected ParticipantProfile doUpdateParticipantProfile(Person person,
      ParticipantProfile newProfile) {
    if (person.getParticipantProfile() == newProfile) {
      return newProfile;
    } else if (person.getId().equals(newProfile.getPersonId())) {
      return newProfile;
    }
    try {
      Method changeId = ParticipantProfile.class.getDeclaredMethod("setPersonId", PersonId.class);
      changeId.setAccessible(true);
      changeId.invoke(newProfile, person.getId());
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Error during invocation of reflection", e);
    }
    return newProfile;
  }

  /**
   * Replaces the activist profile of a person if specified. Otherwise the database will be cleaned.
   *
   * @param person the person to update
   * @param newPerson the new person data
   */
  protected void updateActivistProfile(Person person, Person newPerson) {
    if (newPerson.isActivist()) {
      person.setActivistProfile(doUpdateActivistProfile(person, newPerson.getActivistProfile()));
    } else {
      person.setActivistProfile(null);
      if (activistRepo.exists(person.getId())) {
        activistRepo.delete(person.getId());
      }
    }
  }

  /**
   * Replaces the activist profile of a person
   *
   * @param person the person to update
   * @param newProfile the new profile to use
   */
  protected ActivistProfile doUpdateActivistProfile(Person person, ActivistProfile newProfile) {
    if (person.getActivistProfile() == newProfile) {
      return newProfile;
    } else if (person.getId().equals(newProfile.getPersonId())) {
      return newProfile;
    }
    ActivistProfile profile = person.getActivistProfile();
    profile.setJuleicaCard(newProfile.getJuleicaCard());
    return profile;
  }

  /**
   * Replaces the referent profile of a person if specified. Otherwise the database will be cleaned.
   *
   * @param person the person to update
   * @param newPerson the new person data
   */
  protected void updateReferentProfile(Person person, Person newPerson) {
    if (newPerson.isReferent()) {
      person.setReferentProfile(doUpdateReferentProfile(person, newPerson.getReferentProfile()));
    } else {
      person.setReferentProfile(null);
      if (referentRepo.exists(person.getId())) {
        referentRepo.delete(person.getId());
      }
    }
  }

  /**
   * Replaces the referent profile of a person
   *
   * @param person the person to update
   * @param newProfile the new profile to use
   */
  protected ReferentProfile doUpdateReferentProfile(Person person, ReferentProfile newProfile) {
    if (person.getReferentProfile() == newProfile) {
      return newProfile;
    } else if (person.getId().equals(newProfile.getPersonId())) {
      return newProfile;
    }
    ReferentProfile profile = person.getReferentProfile();
    setQualifications(profile, Lists.newLinkedList(newProfile.getQualifications()));
    return profile;
  }

  /**
   * Sets the qualifications of a referent
   *
   * @param profile the referent to update
   * @param qualifications the referent's qualifications
   */
  protected void setQualifications(ReferentProfile profile, List<Qualification> qualifications) {
    try {
      Method changeQualifications =
          ReferentProfile.class.getDeclaredMethod("setQualifications", List.class);
      changeQualifications.setAccessible(true);
      changeQualifications.invoke(profile, qualifications);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Error during invocation of reflection", e);
    }
  }


}
