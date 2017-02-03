package de.naju.adebar.model.human;

import org.springframework.stereotype.Service;

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
    Person savePerson(Person person);
    Activist createActivist(Person person);
    Referent createReferent(Person person);
    Activist findActivist(Person person);
    Referent findReferent(Person person);
    Person findPerson(Activist activist);
    Person findPerson(Referent referent);
    PersonManager getPersonManager();
    ActivistManager getActivistManager();
    ReferentManager getReferentManager();
}
