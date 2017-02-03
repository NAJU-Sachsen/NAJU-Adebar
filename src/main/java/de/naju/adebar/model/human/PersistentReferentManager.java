package de.naju.adebar.model.human;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * A {@link ReferentManager} that persists its data in a database
 * @author Rico Bergmann
 */
@Service
public class PersistentReferentManager implements ReferentManager {
    private ReferentRepository referentRepo;

    @Autowired
    public PersistentReferentManager(ReferentRepository referentRepo) {
        Assert.notNull(referentRepo, "Activist repository may not be null!");
        this.referentRepo = referentRepo;
    }

    @Override
    public Referent saveReferent(Referent referent) {
        return referentRepo.save(referent);
    }

    @Override
    public Referent createReferentForPerson(Person person, Qualification... qualifications) {
        Referent referent = new Referent(person.getId());
        for (Qualification q : qualifications) {
            referent.addQualification(q);
        }
        return referentRepo.save(referent);
    }

    @Override
    public Referent updateReferent(PersonId referentId, Referent newReferent) {
        newReferent.setAssociatedPerson(referentId);
        return referentRepo.save(newReferent);
    }

    @Override
    public Referent findReferentByPerson(Person person) {
        Referent referent = referentRepo.findOne(person.getId());
        if (referent == null) {
            throw new NoReferentException("Person is no referent: " + person);
        }
        return referent;
    }

    @Override
    public boolean isReferent(Person person) {
        return referentRepo.findOne(person.getId()) != null;
    }

    @Override
    public Iterable<Qualification> getQualificationsForPerson(Person person) {
        if (!isReferent(person)) {
            throw new NoReferentException("Person is no referent: " + person);
        }
        return findReferentByPerson(person).getQualifications();
    }
}
