package de.naju.adebar.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.app.newsletter.NewsletterManager;
import de.naju.adebar.app.newsletter.SubscriberManager;
import de.naju.adebar.model.persons.PersonManager;

/**
 * Managers for the {@link SubscriberController}
 * 
 * @author Rico Bergmann
 */
@Component
class SubscriberControllerManagers {
  public final NewsletterManager newsletters;
  public final SubscriberManager subscribers;
  public final PersonManager persons;
  public final LocalGroupManager localGroups;

  @Autowired
  public SubscriberControllerManagers(NewsletterManager newsletterManager,
      SubscriberManager subscriberManager, PersonManager personManager,
      LocalGroupManager localGroupManager) {
    this.newsletters = newsletterManager;
    this.subscribers = subscriberManager;
    this.persons = personManager;
    this.localGroups = localGroupManager;
  }
}
