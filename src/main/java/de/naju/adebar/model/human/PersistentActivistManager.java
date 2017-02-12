package de.naju.adebar.model.human;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link ActivistManager} that persists the data in a database
 * @author Rico Bergmann
 */
@Service
public class PersistentActivistManager implements ActivistManager {
    private PersonRepository personRepo;
    private ActivistRepository activistRepo;
    private ReadOnlyActivistRepository roRepo;

    @Autowired
    public PersistentActivistManager(PersonRepository personRepo, ActivistRepository activistRepo, ReadOnlyActivistRepository roRepo) {
        Assert.notNull(personRepo, "Person repository may not be null!");
        Assert.notNull(activistRepo, "Activist repository may not be null!");
        Assert.notNull(roRepo, "Read only activist repository may not be null!");
        this.personRepo = personRepo;
        this.activistRepo = activistRepo;
        this.roRepo = roRepo;
    }

    @Override
    public Activist saveActivist(Activist activist) {
        return activistRepo.save(activist);
    }

    @Override
    public Activist createActivistForPerson(Person person) {
        Activist activist = new Activist(person.getId(), null);
        return activistRepo.save(activist);
    }

    @Override
    public Activist updateActivist(PersonId activistId, Activist newActivist) {
        newActivist.setAssociatedPerson(activistId);
        return activistRepo.save(newActivist);
    }

    @Override
    public Activist findActivistByPerson(Person person) {
        Activist activist = activistRepo.findOne(person.getId());
        if (activist == null) {
            throw new NoActivistException("Person is no activist: " + person);
        }
        return activist;
    }

    @Override
    public boolean isActivist(Person person) {
        return activistRepo.findOne(person.getId()) != null;
    }

    @Override
    public LocalDate getJuleicaExpiryDateForPerson(Person person) {
        if (!isActivist(person)) {
            throw new NoActivistException("Person is no activist: " + person);
        }
        return findActivistByPerson(person).getJuleicaExpiryDate();
    }

    @Override
    public Map<Person, LocalDate> getJuleicaExpiryDates() {
        Map<Person, LocalDate> activistsMap = new HashMap<>();
        for (Activist activist : activistRepo.findByJuleicaExpiryDateIsNotNull()) {
            activistsMap.put(personRepo.findOne(activist.getAssociatedPerson()), activist.getJuleicaExpiryDate());
        }
        return activistsMap;
    }

    @Override
    public ReadOnlyActivistRepository repository() {
        return roRepo;
    }
}
