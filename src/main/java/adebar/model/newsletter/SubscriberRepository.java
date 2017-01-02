package adebar.model.newsletter;

import org.springframework.data.repository.CrudRepository;

/**
 * 
 * @author Rico Bergmann
 *
 */
public interface SubscriberRepository extends CrudRepository<Subscriber, String> {
	Iterable<Subscriber> findByFirstName(String firstName);
	Iterable<Subscriber> findByLastName(String lastName);
	Iterable<Subscriber> findByFirstNameAndLastName(String firstName, String lastName);
}
