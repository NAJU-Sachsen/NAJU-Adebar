package adebar.model.newsletter;

import org.springframework.data.repository.CrudRepository;

/**
 * 
 * @author Rico Bergmann
 *
 */
public interface NewsletterRepository extends CrudRepository<Newsletter, Long> {
	Iterable<Newsletter> findByName(String name);
	Iterable<Newsletter> findBySubscribersContains(Subscriber subscriber);
	Iterable<Newsletter> findBySubscribersEmail(String email);
}
