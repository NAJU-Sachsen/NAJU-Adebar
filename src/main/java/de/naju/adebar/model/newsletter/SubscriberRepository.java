package de.naju.adebar.model.newsletter;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository to access {@link Subscriber}
 * @author Rico Bergmann
 */
@Repository
public interface SubscriberRepository extends CrudRepository<Subscriber, Long> {

    /**
     * @param firstName the first name to query for
     * @return all subscribers with the given first name
     */
	Iterable<Subscriber> findByFirstName(String firstName);

    /**
     * @param lastName the last name to query for
     * @return all subscribers with the given last name
     */
	Iterable<Subscriber> findByLastName(String lastName);

    /**
     * @param firstName the first name to query for
     * @param lastName the first name to query for
     * @return all subscribers with the given name
     */
	Iterable<Subscriber> findByFirstNameAndLastName(String firstName, String lastName);

    /**
     * @param email the email for query for
     * @return the subscriber with the given email if present
     */
	Optional<Subscriber> findByEmail(String email);

    /**
     * @return the first ten subscribers ordered by their email addresses
     */
	Iterable<Subscriber> findFirst10ByOrderByEmail();
}
