package de.naju.adebar.model.human;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;

/**
 * A {@link ActivistManager} that persists the data in a database
 * @author Rico Bergmann
 */
@Service
public class PersistentActivistManager implements ActivistManager {
    private ActivistRepository activistRepo;

    @Autowired
    public PersistentActivistManager(ActivistRepository activistRepo) {
        Assert.notNull(activistRepo, "Activist repository may not be null!");
        this.activistRepo = activistRepo;
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
}
