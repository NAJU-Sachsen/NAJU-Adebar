package de.naju.adebar.model.human;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Optional;

/**
 * A {@link PersonManager} that persists its data in a database
 * @author Rico Bergmann
 */
@Service
public class PersistentPersonManager implements PersonManager {
    private PersonRepository personRepo;
    private ReadOnlyPersonRepository roPersonRepo;

    @Autowired
    public PersistentPersonManager(PersonRepository personRepo, ReadOnlyPersonRepository roPersonRepo) {
        Assert.notNull(personRepo, "Person repository may not be null!");
        Assert.notNull(roPersonRepo, "Read only person repository may not be null!");
        this.personRepo = personRepo;
        this.roPersonRepo = roPersonRepo;
    }

    @Override
    public Person savePerson(Person person) {
        if (!person.hasId()) {
            person.setId(new PersonId());
        }
        return personRepo.save(person);
    }

    @Override
    public Person createPerson(String firstName, String lastName, String email, Gender gender, Address address, LocalDate dob) {
        Person person = new Person(new PersonId(), firstName, lastName, email, gender, address, dob);
        return personRepo.save(person);
    }

    @Override
    public Person updatePerson(PersonId personId, Person newPerson) {
        Assert.notNull(personId, "Old person may not be null!");
        Assert.notNull(newPerson, "New person may not be null!");
        newPerson.setId(personId);
        return personRepo.save(newPerson);
    }

    @Override
    public Optional<Person> findPerson(String id) {
        Person person = personRepo.findOneByIdAndActiveIsTrue(new PersonId(id));
        return person == null ? Optional.empty() : Optional.of(person);
    }

    @Override
    public ReadOnlyPersonRepository repository() {
        return roPersonRepo;
    }
}
