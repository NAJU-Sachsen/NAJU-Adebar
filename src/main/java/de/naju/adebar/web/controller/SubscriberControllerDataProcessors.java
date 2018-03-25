package de.naju.adebar.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.naju.adebar.app.newsletter.NewsletterDataProcessor;
import de.naju.adebar.services.conversion.newsletter.PersonToSubscriberConverter;

/**
 * Data processors for the {@link SubscriberController}
 * 
 * @author Rico Bergmann
 */
@Component
class SubscriberControllerDataProcessors {
  public final PersonToSubscriberConverter personToSubscriberConverter;
  public final NewsletterDataProcessor newsletters;

  @Autowired
  public SubscriberControllerDataProcessors(PersonToSubscriberConverter personToSubscriberConverter,
      NewsletterDataProcessor dataProcessor) {
    this.personToSubscriberConverter = personToSubscriberConverter;
    this.newsletters = dataProcessor;
  }

}

