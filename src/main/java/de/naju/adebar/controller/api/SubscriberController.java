package de.naju.adebar.controller.api;

import java.util.LinkedList;
import java.util.Optional;
import de.naju.adebar.model.newsletter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import de.naju.adebar.api.data.TechnicalSubscriberJSON;

/**
 * REST controller to access subscriber data
 * 
 * @author Rico Bergmann
 * @see <a href= "https://en.wikipedia.org/wiki/Representational_State_Transfer">REST Services</a>
 */
@RestController("api_subscriberController")
@RequestMapping("/api/newsletter")
public class SubscriberController {
  private NewsletterRepository newsletterRepo;
  private SubscriberRepository subscriberRepo;

  @Autowired
  public SubscriberController(NewsletterRepository newsletterRepo,
      SubscriberRepository subscriberRepo) {
    this.newsletterRepo = newsletterRepo;
    this.subscriberRepo = subscriberRepo;
  }

  /**
   * @param email the email of the subscriber to query for
   * @return a {@link TechnicalSubscriberJSON} object providing the requested data
   */
  @RequestMapping("/subscriberDetails")
  public TechnicalSubscriberJSON sendTechnicalSubscriberDetails(
      @RequestParam("email") String email) {
    LinkedList<Long> subscribedNewsletters = new LinkedList<>();
    for (Newsletter newsletter : newsletterRepo.findBySubscribersEmail(email)) {
      subscribedNewsletters.add(newsletter.getId());
    }
    Optional<Subscriber> subscriber = subscriberRepo.findByEmail(email);
    return subscriber.map(sub -> new TechnicalSubscriberJSON(sub, subscribedNewsletters))
        .orElse(null);
  }

}
