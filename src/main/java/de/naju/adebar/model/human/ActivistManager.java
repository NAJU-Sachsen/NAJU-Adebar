package de.naju.adebar.model.human;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

/**
 * Service to take care of {@link Activist Activists}
 * @author Rico Bergmann
 * @see Activist
 * @see Person
 */
@Service
public interface ActivistManager {

    /**
     * Saves a given activist
     * @param activist the activist to save
     * @return the saved activist. As its internal state may differ after the save, this instance should be used
     * for future operations
     */
    Activist saveActivist(Activist activist);

    /**
     * Turns a person into an activist
     * @param person the person
     * @return the freshly created activist instance
     */
    Activist createActivistForPerson(Person person);

    /**
     * Changes the state of a saved activist
     * @param activistId the activist to update
     * @param newActivist the new activist data
     * @return the updated (and saved) activist
     */
    Activist updateActivist(PersonId activistId, Activist newActivist);

    /**
     * If the activist does not exists it will be created, otherwise if it is not active any more it will be made active
     * again and the existing activist will be returned.
     * @param person the associated person
     * @return the activist
     */
    Activist createActivistIfNotExists(Person person);

    /**
     * @param person the person to query for
     * @return the associated activist
     * @throws NoActivistException if the person is not an activist
     */
    Activist findActivistByPerson(Person person);

    /**
     * @param person the person to query for
     * @return {@code true} if an activist is associated to that person, {@code false} otherwise
     */
    boolean isActivist(Person person);

    /**
     * @param person the person to check
     * @return {@code true} if the person is an active activst, or {@code false} if any of the conditions are violated
     */
    boolean activistIsActive(Person person);

    /**
     * It the person is an activist, it will be deactivated
     * @param person the associated person
     */
    void deactivateActivistIfExists(Person person);

    /**
     * @param person the person to query for
     * @return the expiry date of the person's JuLeiCa card
     * @throws NoActivistException if the person is not an activist
     */
    LocalDate getJuleicaExpiryDateForPerson(Person person);

    /**
     * @return a map featuring the juleica expiry date of each person that has one
     */
    Map<Person, LocalDate> getJuleicaExpiryDates();

    /**
     * Provides access to the underlying data
     * @return a read only repository instance
     */
    ReadOnlyActivistRepository repository();

}
