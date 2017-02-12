package de.naju.adebar.model.human;

import java.util.List;
import java.util.Map;

/**
 * Service to take care of {@link Referent Referents}
 * @author Rico Bergmann
 * @see Referent
 * @see Person
 */
public interface ReferentManager {

    /**
     * Saves a given referent
     * @param referent the referent to save
     * @return the saved referent. As its internal state may differ after the save, this instance should be used
     * for future operations
     */
    Referent saveReferent(Referent referent);

    /**
     * Turns a person into a referent
     * @param person the person
     * @return the freshly created referent instance
     */
    Referent createReferentForPerson(Person person, Qualification... qualifications);

    /**
     * Changes the state of a saved referent
     * @param referentId the referent to update
     * @param newReferent the new referent data
     * @return the updated (and saved) referent
     */
    Referent updateReferent(PersonId referentId, Referent newReferent);

    /**
     * @param person the person to query for
     * @return the associated referent
     * @throws NoReferentException if the person is not a referent
     */
    Referent findReferentByPerson(Person person);

    /**
     * @param person the person to query for
     * @return {@code true} if a referent is associated to that person, {@code false} otherwise
     */
    boolean isReferent(Person person);

    /**
     * @param person the person to query for
     * @return the qualifications of that person
     * @throws NoReferentException if the person is not a referent
     * @see Qualification
     */
    Iterable<Qualification> getQualificationsForPerson(Person person);

    /**
     * @return a map featuring the qualifications of each person that is a referent
     */
    Map<Person, Iterable<Qualification>> getQualifications();

    /**
     * Provides access to the underlying data
     * @return a read only repository instance
     */
    ReadOnlyReferentRepository repository();
}
