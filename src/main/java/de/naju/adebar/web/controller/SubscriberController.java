package de.naju.adebar.web.controller;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.newsletter.AlreadySubscribedException;
import de.naju.adebar.model.newsletter.ExistingSubscriberException;
import de.naju.adebar.model.newsletter.Newsletter;
import de.naju.adebar.model.newsletter.Subscriber;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.validation.newsletter.AddNewsletterForm;
import java.util.List;
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

  private static final String SUBSCRIBERS = "subscribers";
  private static final String REDIRECT_NEWSLETTERS = "redirect:/newsletters";
  private static final String REDIRECT_NEWSLETTER_DETAILS = "redirect:/newsletters/";

  private final SubscriberControllerRepositories repositories;
  private final SubscriberControllerManagers managers;
  private final SubscriberControllerDataProcessors dataProcessors;

  @Autowired
  public SubscriberController(SubscriberControllerRepositories repositories,
      SubscriberControllerManagers managers, SubscriberControllerDataProcessors dataProcessors) {
    Assert.notNull(repositories, "Repositories may not be null");
    Assert.notNull(managers, "Managers may not be null");
    Assert.notNull(dataProcessors, "Data processors may not be null");
    this.repositories = repositories;
    this.managers = managers;
    this.dataProcessors = dataProcessors;
  }

  /**
   * Displays the subscriber overview with all subscribers
   *
   * @param model model to display the data in
   * @return the newsletters' overview view
   */
  @RequestMapping(value = "newsletters/subscribers/all")
  public String showAllNewsletterSubscribers(Model model) {
    model.addAttribute("newsletters", repositories.newsletters.findAll());
    model.addAttribute(SUBSCRIBERS, repositories.subscribers.findAll());
    model.addAttribute("localGroups", dataProcessors.newsletters.getLocalGroupBelonging());
    model.addAttribute("events", dataProcessors.newsletters.getEventBelonging());
    model.addAttribute("projects", dataProcessors.newsletters.getProjectBelonging());
    model.addAttribute("noBelonging", dataProcessors.newsletters.getNewslettersWithoutBelonging());

    model.addAttribute("allChapters", managers.localGroups.repository().findAll());
    model.addAttribute("addNewsletterForm", new AddNewsletterForm());
    model.addAttribute("tab", SUBSCRIBERS);
    model.addAttribute("subscriberDisplay", "all");

    return "newsletters";
  }

  /**
   * Adds a subscriber to a newsletter
   *
   * @param newsletterId the id (= primary key) of the newsletter
   * @param subscriber subscriber object created by the model
   * @param redirAttr attributes for the view to display some result information
   * @return the newsletter's detail view
   */
  @RequestMapping(value = "/newsletters/{nid}/subscribe", method = RequestMethod.POST)
  public String subscribe(@PathVariable("nid") Long newsletterId,
      @ModelAttribute("newSubscriber") Subscriber subscriber, RedirectAttributes redirAttr) {
    Newsletter newsletter = repositories.newsletters.findById(newsletterId)
        .orElseThrow(() -> new IllegalArgumentException("No newsletter with ID " + newsletterId));

    try {
      subscriber = managers.subscribers.saveSubscriber(subscriber);
      managers.newsletters.subscribe(subscriber, newsletter);
      redirAttr.addFlashAttribute("subscribed", true);
    } catch (ExistingSubscriberException e) {
      redirAttr.addFlashAttribute("emailExists", true);
      redirAttr.addFlashAttribute("existingSubscriber", e.getSubscriber());
    } catch (AlreadySubscribedException e) {
      redirAttr.addAttribute("alreadySubscribed", true);
    }

    return REDIRECT_NEWSLETTER_DETAILS + newsletterId;
  }

  /**
   * Adds an existing person to a newsletter
   *
   * @param newsletterId the id of the newsletter
   * @param personId the id of the person
   * @param redirAttr attributes for the view that should be used after redirection
   * @return the newsletter's detail view
   */
  @RequestMapping(value = "/newsletters/{nid}/subscribe-person")
  public String subscribePerson(@PathVariable("nid") Long newsletterId,
      @RequestParam("person-id") String personId, RedirectAttributes redirAttr) {
    Newsletter newsletter = repositories.newsletters.findById(newsletterId)
        .orElseThrow(() -> new IllegalArgumentException("No newsletter with ID " + newsletterId));
    Person person =
        managers.persons.findPerson(personId).orElseThrow(IllegalArgumentException::new);

    try {
      Subscriber subscriber = managers.subscribers
          .saveSubscriber(dataProcessors.personToSubscriberConverter.convertPerson(person));
      managers.newsletters.subscribe(subscriber, newsletter);
    } catch (AlreadySubscribedException e) {
      redirAttr.addFlashAttribute("alreadySubscribed", true);
    }

    return REDIRECT_NEWSLETTER_DETAILS + newsletterId;
  }

  /**
   * Removes a subscriber form the newsletter
   *
   * @param newsletterId the id (= primary key) of the newsletter
   * @param email the email address (= primary key) of the subscriber to remove
   * @param redirAttr attributes for the view to display some result information
   * @return the newsletter's detail view
   */
  @RequestMapping(value = "/newsletters/{nid}/unsubscribe", method = RequestMethod.POST)
  public String unsubscribe(@PathVariable("nid") Long newsletterId,
      @RequestParam("email") Email email, RedirectAttributes redirAttr) {
    Newsletter newsletter = repositories.newsletters.findById(newsletterId)
        .orElseThrow(() -> new IllegalArgumentException("No newsletter with ID " + newsletterId));

    managers.newsletters.unsubscribe(email, newsletter);

    redirAttr.addFlashAttribute("subscriberRemoved", true);
    return REDIRECT_NEWSLETTER_DETAILS + newsletterId;
  }

  /**
   * Creates a new subscriber and signs it up to the given newsletters
   *
   * @param firstName the subscriber's first name
   * @param lastName the subscriber's last name
   * @param email the subscriber's email
   * @param newsletters the ids of the newsletters to subscribe to
   * @param redirAttr attributes for the view to display some result information
   * @return the newsletter overview view
   */
  @RequestMapping(value = "/newsletters/subscribers/add", method = RequestMethod.POST)
  public String createSubscriber(@RequestParam("firstName") String firstName,
      @RequestParam("lastName") String lastName, @RequestParam("email") Email email,
      @RequestParam("newsletters") List<Long> newsletters, RedirectAttributes redirAttr) {

    try {
      Subscriber subscriber = managers.subscribers.createSubscriber(firstName, lastName, email);
      managers.newsletters.updateSubscriptions(subscriber,
          repositories.newsletters.findAllById(newsletters));
      redirAttr.addFlashAttribute("subscriberCreated", true);
    } catch (ExistingSubscriberException e) {
      redirAttr.addFlashAttribute("emailExists", true);
    }

    redirAttr.addFlashAttribute("tab", SUBSCRIBERS);
    return REDIRECT_NEWSLETTERS;
  }

  /**
   * Updates the information of a subscriber
   *
   * @param id the subscriber's old email
   * @param firstName the subscriber's new first name
   * @param lastName the subscriber's new last name
   * @param email the subscriber's new email
   * @param newsletterIds the newsletters to sign up for
   * @param redirAttr attributes for the view to display some result information
   * @return the newsletter overview view
   */
  @RequestMapping(value = "/newsletters/subscribers/update", method = RequestMethod.POST)
  public String updateSubscriber(@RequestParam("id") Email id,
      @RequestParam("edit-firstName") String firstName,
      @RequestParam("edit-lastName") String lastName, @RequestParam("edit-email") Email email,
      @RequestParam("edit-subscribed") List<Long> newsletterIds, RedirectAttributes redirAttr) {

    repositories.subscribers.findByEmail(id).ifPresent(subscriber -> {
      subscriber = managers.subscribers.updateSubscriberFirstName(subscriber, firstName);
      subscriber = managers.subscribers.updateSubscriberLastName(subscriber, lastName);
      subscriber = managers.subscribers.updateSubscriberEmail(subscriber, email);
      managers.newsletters.updateSubscriptions(subscriber,
          repositories.newsletters.findAllById(newsletterIds));

      redirAttr.addFlashAttribute("tab", SUBSCRIBERS);
      redirAttr.addFlashAttribute("subscriberUpdated", true);
    });

    return REDIRECT_NEWSLETTERS;
  }

  /**
   * Removes a subscriber from the system
   *
   * @param id the subscriber's id
   * @param redirAttr attributes for the view to display some result information
   * @return the newsletter overview view
   */
  @RequestMapping(value = "/newsletters/subscribers/delete", method = RequestMethod.POST)
  public String deleteSubscriber(@RequestParam("id") Email id, RedirectAttributes redirAttr) {
    Iterable<Newsletter> subscribedNewsletters =
        repositories.newsletters.findBySubscribersEmail(id);

    subscribedNewsletters.forEach(newsletter -> managers.newsletters.unsubscribe(id, newsletter));
    managers.subscribers.deleteSubscriber(id);

    redirAttr.addFlashAttribute("tab", SUBSCRIBERS);
    redirAttr.addFlashAttribute("subscriberDeleted", true);
    return REDIRECT_NEWSLETTERS;
  }

}
