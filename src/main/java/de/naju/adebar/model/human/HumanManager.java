package de.naju.adebar.model.human;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * A high-level service mainly for convenience reasons.
 *
 * <p>
 * It basically is just a kind of facade for more specific "human" services, namely {@link PersonManager},
 * {@link ActivistManager} and {@link ReferentManager}. Many important management operations will likely be
 * executable by just using this service. No need to create instances of three different ones. However if a more
 * dedicated service is needed, it may easily be queried.
 * </p>
 *
 * @author Rico Bergmann
 * @see PersonManager
 * @see ActivistManager
 * @see ReferentManager
 * @see <a href="https://en.wikipedia.org/wiki/Facade_pattern">Facade pattern</a>
 */
@Service
public interface HumanManager {

    /**
     * Saves a given person. It may or may not be saved already. If it has no ID specified, one will automatically
     * be generated
     * @param person the person to save
     * @return the saved person. As its internal state may differ after the save, this instance should be used
     * for future operations
     */
    Person savePerson(Person person);

    /**
     * Turns a person into an activist
     * @param person the person
     * @return the freshly created activist instance
     */
    Activist createActivist(Person person);

    /**
     * Turns a person into a referent
     * @param person the person
     * @return the freshly created referent instance
     */
    Referent createReferent(Person person, Qualification... qualifications);

    /**
     * @param person the person to query for
     * @return the associated activist
     * @throws NoActivistException if the person is not an activist
     */
    Activist findActivist(Person person);

    /**
     * @param person the person to query for
     * @return the associated referent
     * @throws NoReferentException if the person is not a referent
     */
    Referent findReferent(Person person);

    /**
     * Queries for a specific person
     * @param id the person's id
     * @return the person
     */
    Optional<Person> findPerson(String id);

    /**
     * @param activist the activist to query for
     * @return the associated person
     */
    Optional<Person> findPerson(Activist activist);

    /**
     * @param referent the referent to query for
     * @return the associated person
     */
    Optional<Person> findPerson(Referent referent);

    /**
     * Disables a person. It may/should not be available as a potential camp participant, etc. any more afterwards.
     * To keep statistics correct, persons should not be deleted but instead only disabled (and anonymized)
     * @param person the person to disable
     * @throws IllegalStateException if the person may not be deactivated (e.g. because it is an activist or referent)
     */
    void deactivatePerson(Person person);

    /**
     * @return an instance of a person manager for more specific queries
     */
    PersonManager personManager();

    /**
     * @return an instance of an activist mananager for more specific queries
     */
    ActivistManager activistManager();

    /**
     * @return an instance of a referent manager for more specific queries
     */
    ReferentManager referentManager();
}
