package de.naju.adebar.model.human;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Optional;

/**
 * A {@link HumanManager} that persists its data in a database
 * @author Rico Bergmann
 */
@Service
public class PersistentHumanManager implements HumanManager {
    private PersonManager personManager;
    private ActivistManager activistManager;
    private ReferentManager referentManager;
    private PersonRepository personRepo;


    @Autowired
    public PersistentHumanManager(PersistentPersonManager personManager, PersistentActivistManager activistManager,
                                  PersistentReferentManager referentManager, PersonRepository personRepo) {
        Object[] params = {personManager, activistManager, referentManager, personRepo};
        Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
        this.personManager = personManager;
        this.activistManager = activistManager;
        this.referentManager = referentManager;
        this.personRepo = personRepo;
    }

    @Override
    public Person savePerson(Person person) {
        return personManager.savePerson(person);
    }

    @Override
    public Activist createActivist(Person person) {
        return activistManager.createActivistForPerson(person);
    }

    @Override
    public Referent createReferent(Person person, Qualification... qualifications) {
        return referentManager.createReferentForPerson(person, qualifications);
    }

    @Override
    public Activist findActivist(Person person) {
        return activistManager.findActivistByPerson(person);
    }

    @Override
    public Referent findReferent(Person person) {
        return referentManager.findReferentByPerson(person);
    }

    @Override
    public Optional<Person> findPerson(String id) {
        return personManager.findPerson(id);
    }

    @Override
    public Optional<Person> findPerson(Activist activist) {
        return personManager.findPerson(activist.getAssociatedPerson().getId());
    }

    @Override
    public Optional<Person> findPerson(Referent referent) {
        return personManager.findPerson(referent.getAssociatedPerson().getId());
    }

    @Override
    public PersonManager personManager() {
        return personManager;
    }

    @Override
    public ActivistManager activistManager() {
        return activistManager;
    }

    @Override
    public ReferentManager referentManager() {
        return referentManager;
    }
}
