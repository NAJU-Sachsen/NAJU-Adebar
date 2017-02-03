package de.naju.adebar.model.human;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;

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
    private ActivistRepository activistRepo;
    private ReferentRepository referentRepo;


    @Autowired
    public PersistentHumanManager(PersistentPersonManager personManager, PersistentActivistManager activistManager,
                                  PersistentReferentManager referentManager, PersonRepository personRepo,
                                  ActivistRepository activistRepo, ReferentRepository referentRepo) {
        Object[] params = {personManager, activistManager, referentManager, personRepo, activistRepo, referentRepo};
        Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
        this.personManager = personManager;
        this.activistManager = activistManager;
        this.referentManager = referentManager;
        this.personRepo = personRepo;
        this.activistRepo = activistRepo;
        this.referentRepo = referentRepo;
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
    public Referent createReferent(Person person) {
        return referentManager.createReferentForPerson(person);
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
    public Person findPerson(Activist activist) {
        return personRepo.findOne(activist.getAssociatedPerson());
    }

    @Override
    public Person findPerson(Referent referent) {
        return personRepo.findOne(referent.getAssociatedPerson());
    }

    @Override
    public PersonManager getPersonManager() {
        return personManager;
    }

    @Override
    public ActivistManager getActivistManager() {
        return activistManager;
    }

    @Override
    public ReferentManager getReferentManager() {
        return referentManager;
    }
}
