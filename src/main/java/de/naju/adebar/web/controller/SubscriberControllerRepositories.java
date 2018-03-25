package de.naju.adebar.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.naju.adebar.model.newsletter.NewsletterRepository;
import de.naju.adebar.model.newsletter.SubscriberRepository;

/**
 * Repositories for the {@link SubscriberController}
 * 
 * @author Rico Bergmann
 */
@Component
class SubscriberControllerRepositories {
  public final NewsletterRepository newsletters;
  public final SubscriberRepository subscribers;

  @Autowired
  public SubscriberControllerRepositories(NewsletterRepository newsletterRepo,
      SubscriberRepository subscriberRepo) {
    this.newsletters = newsletterRepo;
    this.subscribers = subscriberRepo;
  }

}
