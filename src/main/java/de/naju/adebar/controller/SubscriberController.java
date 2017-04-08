package de.naju.adebar.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import de.naju.adebar.model.human.HumanManager;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.newsletter.*;
import de.naju.adebar.util.conversion.newsletter.PersonToSubscriberConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Subscriber related controller mappings
 *
 * @author Rico Bergmann
 * @see Newsletter
 * @see Subscriber
 */
@Controller
public class SubscriberController {
    private NewsletterRepository newsletterRepo;
    private SubscriberRepository subscriberRepo;
    private NewsletterManager newsletterManager;
    private SubscriberManager subscriberManager;
    private HumanManager humanManager;
    private PersonToSubscriberConverter personToSubscriberConverter;

    @Autowired
    public SubscriberController(NewsletterRepository newsletterRepo, SubscriberRepository newsletterSubscriberRepo, NewsletterManager newsletterManager, SubscriberManager subscriberManager, HumanManager humanManager, PersonToSubscriberConverter personToSubscriberConverter) {
        Object[] params = {newsletterRepo, newsletterSubscriberRepo, newsletterManager, subscriberManager, humanManager, personToSubscriberConverter};
        Assert.notNull(params, "At least one parameter was null: " + Arrays.toString(params));
        this.newsletterRepo = newsletterRepo;
        this.subscriberRepo = newsletterSubscriberRepo;
        this.newsletterManager = newsletterManager;
        this.subscriberManager = subscriberManager;
        this.humanManager = humanManager;
        this.personToSubscriberConverter = personToSubscriberConverter;
    }

    /**
     * Displays the subscriber overview with all subscribers
     * @param model model to display the data in
     * @return the newsletters' overview view
     */
    @RequestMapping(value="newsletters/subscribers/all")
    public String showAllNewsletterSubscribers(Model model) {
        model.addAttribute("newsletters", newsletterRepo.findAll());
        model.addAttribute("subscribers", subscriberRepo.findAll());
        model.addAttribute("tab", "subscribers");
        model.addAttribute("subscriberDisplay", "all");
        return "newsletters";
    }

    /**
     * Adds a subscriber to a newsletter
     * @param newsletterId the id (= primary key) of the newsletter
     * @param subscriber subscriber object created by the model
     * @param redirAttr attributes for the view to display some result information
     * @return the newsletter's detail view
     */
    @RequestMapping(value = "/newsletters/{nid}/subscribe", method = RequestMethod.POST)
    public String subscribe(@PathVariable("nid") Long newsletterId, @ModelAttribute("newSubscriber") Subscriber subscriber, RedirectAttributes redirAttr) {
        Newsletter newsletter = newsletterRepo.findOne(newsletterId);
        
        try {
        	subscriber = subscriberManager.saveSubscriber(subscriber);
        	newsletterManager.subscribe(subscriber, newsletter);
        	redirAttr.addFlashAttribute("subscribed", true);
        } catch (ExistingSubscriberException e) {
        	redirAttr.addFlashAttribute("emailExists", true);
        	redirAttr.addFlashAttribute("existingSubscriber", e.getExistingSubscriber());
        } catch (AlreadySubscribedException e ) {
        	redirAttr.addAttribute("alreadySubscribed", true);
        }
        
        return "redirect:/newsletters/" + newsletterId;
    }

    /**
     * Adds an existing person to a newsletter
     * @param newsletterId the id of the newsletter
     * @param personId the id of the person
     * @param redirAttr attributes for the view that should be used after redirection
     * @return the newsletter's detail view
     */
    @RequestMapping(value = "/newsletters/{nid}/subscribe-person")
    public String subscribePerson(@PathVariable("nid") Long newsletterId, @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
        Newsletter newsletter = newsletterRepo.findOne(newsletterId);
        Person person = humanManager.findPerson(personId).orElseThrow(IllegalArgumentException::new);

        Subscriber subscriber = subscriberManager.saveSubscriber(personToSubscriberConverter.convertPerson(person));
        newsletterManager.subscribe(subscriber, newsletter);

        return "redirect:/newsletters/" + newsletterId;
    }

    /**
     * Removes a subscriber form the newsletter
     * @param newsletterId the id (= primary key) of the newsletter
     * @param email the email address (= primary key) of the subscriber to remove
     * @param redirAttr attributes for the view to display some result information
     * @return the newsletter's detail view
     */
    @RequestMapping(value = "/newsletters/{nid}/unsubscribe", method = RequestMethod.POST)
    public String unsubscribe(@PathVariable("nid") Long newsletterId, @RequestParam("email") String email, RedirectAttributes redirAttr) {
        Newsletter newsletter = newsletterRepo.findOne(newsletterId);

        newsletterManager.unsubscribe(email, newsletter);

        redirAttr.addFlashAttribute("subscriberRemoved", true);
        return "redirect:/newsletters/" + newsletterId;
    }

    /**
     * Creates a new subscriber and signs it up to the given newsletters
     * @param firstName the subscriber's first name
     * @param lastName the subscriber's last name
     * @param email the subscriber's email
     * @param newsletters the ids of the newsletters to subscribe to
     * @param redirAttr attributes for the view to display some result information
     * @return the newsletter overview view
     */
    @RequestMapping(value = "/newsletters/subscribers/add", method = RequestMethod.POST)
    public String createSubscriber(@RequestParam("firstName") String firstName,
                                   @RequestParam("lastName") String lastName, @RequestParam("email") String email,
                                   @RequestParam("newsletters") List<Long> newsletters, RedirectAttributes redirAttr) {
        
        try {
        	Subscriber subscriber = subscriberManager.createSubscriber(firstName, lastName, email);
        	newsletterManager.updateSubscriptions(subscriber, newsletterRepo.findAll(newsletters));
        	redirAttr.addFlashAttribute("subscriberCreated", true);
        } catch (ExistingSubscriberException e) {
        	redirAttr.addFlashAttribute("emailExists", true);
        }
        
        redirAttr.addFlashAttribute("tab", "subscribers");
        return "redirect:/newsletters";
    }

    /**
     * Updates the information of a subscriber
     * @param id the subscriber's old email
     * @param firstName the subscriber's new first name
     * @param lastName the subscriber's new last name
     * @param email the subscriber's new email
     * @param newsletterIds the newsletters to sign up for
     * @param redirAttr attributes for the view to display some result information
     * @return the newsletter overview view
     */
	@RequestMapping(value = "/newsletters/subscribers/update", method = RequestMethod.POST)
	public String updateSubscriber(@RequestParam("id") String id, @RequestParam("edit-firstName") String firstName,
			@RequestParam("edit-lastName") String lastName, @RequestParam("edit-email") String email,
			@RequestParam("edit-subscribed") List<Long> newsletterIds, RedirectAttributes redirAttr) {

		subscriberRepo.findByEmail(id).ifPresent(subscriber -> {
            subscriber = subscriberManager.updateSubscriberFirstName(subscriber, firstName);
            subscriber = subscriberManager.updateSubscriberLastName(subscriber, lastName);
            subscriber = subscriberManager.updateSubscriberEmail(subscriber, email);
            newsletterManager.updateSubscriptions(subscriber, newsletterRepo.findAll(newsletterIds));

            redirAttr.addFlashAttribute("tab", "subscribers");
            redirAttr.addFlashAttribute("subscriberUpdated", true);
        });

		return "redirect:/newsletters";
	}

    /**
     * Removes a subscriber from the system
     * @param id the subscriber's id
     * @param redirAttr attributes for the view to display some result information
     * @return the newsletter overview view
     */
    @RequestMapping(value = "/newsletters/subscribers/delete", method = RequestMethod.POST)
    public String deleteSubscriber(@RequestParam("id") String id, RedirectAttributes redirAttr) {
        Iterable<Newsletter> subscribedNewsletters = newsletterRepo.findBySubscribersEmail(id);

        subscribedNewsletters.forEach(newsletter -> newsletterManager.unsubscribe(id, newsletter));
        subscriberManager.deleteSubscriber(id);

        redirAttr.addFlashAttribute("tab", "subscribers");
        redirAttr.addFlashAttribute("subscriberDeleted", true);
        return "redirect:/newsletters";
    }
}

