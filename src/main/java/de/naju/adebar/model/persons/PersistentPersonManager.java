package de.naju.adebar.model.persons;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.exceptions.NoReferentException;
import de.naju.adebar.model.persons.qualifications.Qualification;
import de.naju.adebar.model.persons.qualifications.QualificationRepository;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * A {@link PersonManager} that persists its data in a database
 *
 * @author Rico Bergmann
 */
@Service
@Transactional
public class PersistentPersonManager implements PersonManager {

  private PersonRepository personRepo;
  private ReadOnlyPersonRepository roRepo;
  private PersonFactory personFactory;
  private QualificationRepository qualificationRepo;

  @Autowired
  public PersistentPersonManager(PersonRepository personRepo,
      @Qualifier("ro_personRepo") ReadOnlyPersonRepository roRepo, PersonFactory personFactory,
      QualificationRepository qualificationRepo) {
    Object[] params = {personRepo, roRepo, personFactory, qualificationRepo};
    Assert.noNullElements(params, "At lest one parameter was null: " + Arrays.toString(params));

    this.personRepo = personRepo;
    this.roRepo = roRepo;
    this.personFactory = personFactory;
    this.qualificationRepo = qualificationRepo;
  }

  @Override
  public Person savePerson(Person person) {
    return personRepo.save(person);
  }

  @Override
  public Person createPerson(String firstName, String lastName, Email email) {
    Person p = personFactory.buildNew(firstName, lastName, email).create();
    return savePerson(p);
  }

  @Override
  public Person createPerson(String firstName, String lastName, Email email, boolean participant,
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
  public Person updatePerson(Person person) {
    if (!personRepo.existsById(person.getId())) {
      throw new IllegalArgumentException("Person was not persisted before " + person);
    }

    return savePerson(person);
  }

  @Override
  public Optional<Person> findPerson(String id) {
    PersonId personId = new PersonId(id);
    Person p = personRepo.findOne(personId);
    return p != null //
        ? Optional.of(p) //
        : Optional.empty();
  }

  @Override
  public void deactivatePerson(Person person) {
    if (person.isActivist() || person.isReferent()) {
      throw new IllegalStateException(
          "Person may not be deactivated as it is a referent or activist: " + person);
    }
    updatePerson(person.archive());
  }

  @Override
  public void addQualificationToPerson(Person person, Qualification qualification) {
    if (!person.isReferent()) {
      throw new NoReferentException("Person is no referent: " + person);
    }
    Qualification qualificationToAdd;

    qualificationToAdd = qualificationRepo.findById(qualification.getName()).orElse(qualification);

    person.getReferentProfile().addQualification(qualificationToAdd);
    updatePerson(person);
  }

  @Override
  public ReadOnlyPersonRepository repository() {
    return roRepo;
  }

}
