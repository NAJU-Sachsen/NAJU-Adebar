package adebar.model.newsletter;

import org.springframework.stereotype.Service;

import adebar.model.human.Person;

/**
 * Service to take care of {@link Newsletter Newsletters} and more complex management operations
 * @author Rico Bergmann
 *
 */
@Service
public interface NewsletterManager {
	
	Newsletter createNewsletter(String name);
	
	void subscribeToNewsletter(Person person, Newsletter newsletter);
	
	Iterable<Newsletter> getNewslettersForName(String name);
	
	void updateSubscriptions(Subscriber subscriber, Iterable<Newsletter> subscriptions);
}
