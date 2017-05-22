package de.naju.adebar.app.newsletter;

import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.newsletter.Newsletter;
import de.naju.adebar.model.newsletter.NewsletterRepository;
import de.naju.adebar.model.newsletter.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

// TODO many Map methods are mostly stubs as Projects and Events cannot be associated to a newsletter yet

/**
 * Service to easily access data from a newsletter.
 * <p>
 * As this application was designed with the principles of
 * <a href="https://en.wikipedia.org/wiki/Domain-driven_design">Domain-driven design</a> in mind, this functionality
 * has been outsourced from the {@link Newsletter} class itself. The class is stateless following DDD once more.
 * (More or less... at least it is inspired by DDD)
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

	private NewsletterManager newsletterManager;
	private NewsletterRepository newsletterRepo;
    private LocalGroupManager localGroupManager;

    @Autowired
    public NewsletterDataProcessor(NewsletterManager newsletterManager, NewsletterRepository newsletterRepo, LocalGroupManager localGroupManager) {
        Object[] params = {newsletterManager, newsletterRepo, localGroupManager};
        Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
        this.newsletterManager = newsletterManager;
        this.newsletterRepo = newsletterRepo;
        this.localGroupManager = localGroupManager;
    }

    /**
	 * Concatenates all subscribers of the newsletters given and returns them as one large {@code String}.
	 * This may be especially useful, when used as recipients of emails.
	 * @param newsletters the newsletters to examine
	 * @return concatenation of all subscribers, separated by the {@code EMAIL_DELIMITER}
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

    /**
     * @return a map containing all newsletters that belong to a local group, as well as this very local group
     */
	public Map<Newsletter, LocalGroup> getLocalGroupBelonging() {
	    HashMap<Newsletter, LocalGroup> belonging = new HashMap<>();

	    for (Newsletter newsletter : newsletterRepo.findAll()) {
	        if (belongsToLocalGroup(newsletter)) {
	            belonging.put(newsletter, localGroupManager.repository().findByNewslettersContains(newsletter).orElse(null));
            }
        }

	    return Collections.unmodifiableMap(belonging);
    }

    /**
     * @return a map containing all newsletters that belong to a project, as well as this very project
     */
    public Map<Newsletter, Project> getProjectBelonging() {
	    return Collections.unmodifiableMap(new HashMap<>());
    }

    /**
     * @return a map containing all newsletters that belong to an event, as well as this very event
     */
    public Map<Newsletter, LocalGroup> getEventBelonging() {
        return Collections.unmodifiableMap(new HashMap<>());
    }

    /**
     * @return all newsletters that do not belong to anything else
     */
    public List<Newsletter> getNewslettersWithoutBelonging() {
        List<Newsletter> noBelonging = new LinkedList<>();
        for (Newsletter newsletter : newsletterRepo.findAll()) {
            if (!belongsToLocalGroup(newsletter) && !belongsToProject(newsletter) && !belongsToEvent(newsletter)) {
                noBelonging.add(newsletter);
            }
        }
        return noBelonging;
    }

    /**
     * @param newsletter the newsletter to check
     * @return {@code true} if the newsletter belongs to a local group, or {@code false} otherwise
     */
    private boolean belongsToLocalGroup(Newsletter newsletter) {
	    return localGroupManager.repository().findByNewslettersContains(newsletter).isPresent();
    }

    /**
     * @param newsletter the newsletter to check
     * @return {@code true} if the newsletter belongs to a project, or {@code false} otherwise
     */
    private boolean belongsToProject(Newsletter newsletter) {
	    return false;
    }

    /**
     * @param newsletter the newsletter to check
     * @return {@code true} if the newsletter belongs to an event, or {@code false} otherwise
     */
    private boolean belongsToEvent(Newsletter newsletter) {
	    return false;
    }

}
