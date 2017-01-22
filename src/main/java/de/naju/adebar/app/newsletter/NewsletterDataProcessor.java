package de.naju.adebar.app.newsletter;

import de.naju.adebar.model.newsletter.Newsletter;
import de.naju.adebar.model.newsletter.Subscriber;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Service to easily access data from a newsletter.
 * <p>
 * As this application was designed with the principles of
 * <a href="https://en.wikipedia.org/wiki/Domain-driven_design">Domain-driven design</a> in mind, this functionality
 * has been outsourced from the {@link Newsletter} class itself. The class is stateless following DDD once more.
 * </p>
 * @author Rico Bergmann
 *
 */
@Service
public class NewsletterDataProcessor {
	/**
	 * Many email addresses will be separated by this token
	 */
	private final static String EMAIL_DELIMITER = ";";
	
	/**
	 * default email address for newsletters. All instances of sent newsletters will have this address as sender.
	 */
	private final static String NEWSLETTER_EMAIL = "newsletter@naju-sachsen.de";
	
	/**
	 * Concatenates all subscribers of the newsletters given and returns them as one large {@code String}.
	 * This may be especially useful, when used as recipients of emails.
	 * @param newsletters the newsletters to examine
	 * @return concatenation of all subscribers, separated by the {@literal EMAIL_DELIMITER} 
	 */
	public String getSubscriberEmails(Newsletter... newsletters) {
		Assert.noNullElements(newsletters, "Newsletter may not be null!");
		StringBuilder emailBuilder = new StringBuilder();
		for (Newsletter newsletter : newsletters) {
			Iterable<Subscriber> subscribers = newsletter.getSubscribers();
			subscribers.forEach(s -> {
				if (emailBuilder.indexOf(s.getEmail()) == -1)
					emailBuilder.append(s.getEmail()).append(EMAIL_DELIMITER);
			});
		}
		if (emailBuilder.lastIndexOf(EMAIL_DELIMITER) != -1) {
			emailBuilder.deleteCharAt(emailBuilder.lastIndexOf(EMAIL_DELIMITER));
		}
		return emailBuilder.toString();
	}
	
	/**
	 * @return the default address of newsletters.
	 */
	public String getNewsletterEmail() {
		return NEWSLETTER_EMAIL;
	}
	
}
